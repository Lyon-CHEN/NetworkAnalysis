package ca.unb.mobiledev.networkanalysis.network.devicescan;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.ContactsContract;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.unb.mobiledev.networkanalysis.R;
import ca.unb.mobiledev.networkanalysis.network.Constant;
import ca.unb.mobiledev.networkanalysis.network.Device;
import ca.unb.mobiledev.networkanalysis.network.NetworkUtil;

public class DeviceScanManager implements Runnable
{
    private final static String TAG = "DeviceScanManager";

    private final DeviceScanManagerHandler mDeviceScanManagerHandler = new DeviceScanManagerHandler(this);
    private static List<String> mIpInLan = Collections.synchronizedList(new ArrayList());
    private static Iterator mIpScanIterator = mIpInLan.iterator();
    private DeviceScanResult mScanResult;
    private Integer mProgress;
    private ExecutorService mExecutor;
    private Context mContext;

    public static final int  MESSAGE_SCAN_ONE = 200001;

    public DeviceScanManager(Context context, DeviceScanResult scanResult) {
        mScanResult = scanResult;
        mContext = context;
        mProgress = 0;
    }


    @Override
    public void run() {
        Looper.prepare();
        Looper.myLooper();
        startScan();
    }

    public void startScan()
    {
        List<Device> deviceList;
        String localIp = NetworkUtil.getLocalIp();
        String routerIp = NetworkUtil.getGateWayIp(mContext);
        if (TextUtils.isEmpty(localIp) || TextUtils.isEmpty(routerIp))
            return;
        mIpInLan.addAll(NetworkUtil.getSubnet(localIp,true));

        //init a thread pool
        mExecutor= Executors.newFixedThreadPool(Constant.SCAN_COUNT);

        for (String mIP : mIpInLan) {
            ScanTasks mScanTask = new ScanTasks(mIP,mDeviceScanManagerHandler);
            mExecutor.submit(mScanTask);
        }
        //waiting for all task completed
        mExecutor.shutdown();

        while (!mExecutor.isTerminated()) {
            try {
                Thread.sleep(1000);
                mProgress++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        mProgress = Constant.PROGRESS_MAX;
    }

    public void stopScan()
    {
        if (mExecutor != null)
        {
            mExecutor.shutdownNow();
        }
    }

    private String parseHostInfo(String mac) {
        return Manufacture.getInstance()
                .getManufacture(mac, mContext);
    }

    class ScanTasks implements Callable {
        private String mIpAddr;
        private DeviceScanManagerHandler mHandler;

        public ScanTasks(String ipAddr, DeviceScanManagerHandler deviceScanManagerHandler) {
            mIpAddr = ipAddr;
            mHandler = deviceScanManagerHandler;
        }

        @Override
        public Device call() {

            NetworkUtil.isPingOk(mIpAddr);

            Device mDevice = null;
            BufferedReader br = null;
            try {

                Process ipProc = Runtime.getRuntime().exec("ip neighbor show " + mIpAddr);
                ipProc.waitFor();
                if (ipProc.exitValue() != 0) {
                    throw new Exception("Unable to access ARP entries");
                }

                br = new BufferedReader(new InputStreamReader(ipProc.getInputStream(), StandardCharsets.UTF_8));
                String line;
                while ((line = br.readLine()) != null) {
                    String[] neighborLine = line.split("\\s+");
                    if (neighborLine.length <= 4) {
                        continue;
                    }
                    String ip = neighborLine[0];
                    InetAddress addr = InetAddress.getByName(ip);
                    if (addr.isLinkLocalAddress() || addr.isLoopbackAddress()) {
                        continue;
                    }
                    String macAddress = neighborLine[4];
                    String state = neighborLine[neighborLine.length - 1];

                    if (!state.equals(R.string.NEIGHBOR_REACHABLE) && !state.equals(R.string.NEIGHBOR_INCOMPLETE)) {
                        String vendor = parseHostInfo(macAddress);
                        if(TextUtils.isEmpty(vendor)){
                            vendor ="UNKNOWN";
                        }else{
                            vendor = vendor.substring(0,10);
                        }
                        mDevice = new Device(ip, macAddress, vendor);
                        mDeviceScanManagerHandler.sendMessage(
                                mDeviceScanManagerHandler.obtainMessage(
                                        Constant.MSG.SCAN_ONE, mDevice));
                    }
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (br != null) {
                        br.close();
                    }
                } catch (IOException e) {
                }
            }
            return mDevice;
        }

    }

     class DeviceScanManagerHandler extends Handler
    {
        private WeakReference<DeviceScanManager> weakReference;

        public DeviceScanManagerHandler(DeviceScanManager manager)
        {
            weakReference = new WeakReference<>(manager);
        }

        @Override
        public void handleMessage(Message msg)
        {
            DeviceScanManager manager = weakReference.get();
            if (manager == null)
                return;
            switch (msg.what)
            {
                case Constant.MSG.SCAN_ONE :
                    if (manager.mScanResult != null)
                    {
                        Device ip_mac = (Device) msg.obj;
                        if (ip_mac != null)
                            manager.mScanResult.deviceScanResult(ip_mac,mProgress);
                    }
                    break;
                case Constant.MSG.SCAN_OVER :
                    break;
            }
        }
    }
}