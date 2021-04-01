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
import java.util.concurrent.LinkedBlockingQueue;
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
    private ThreadPoolExecutor  mExecutor;
    private Context mContext;
    volatile int mRun = 0;  //0==stop,1==run,2==final
    private static int count=0;

    public DeviceScanManager(Context context, DeviceScanResult scanResult) {
        mScanResult = scanResult;
        mContext = context;
        mProgress = 0;

        if(Looper.myLooper() ==null) {
            Looper.prepare();
            Looper.myLooper();
        }

        String localIp = NetworkUtil.getLocalIp();
        String routerIp = NetworkUtil.getGateWayIp(mContext);
        if (TextUtils.isEmpty(localIp) || TextUtils.isEmpty(routerIp))
            return;
        mIpInLan.addAll(NetworkUtil.getSubnet(localIp,true));
        //init a thread pool
        mExecutor=  new ThreadPoolExecutor(Constant.SCAN_COUNT, Constant.SCAN_COUNT, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());
    }



    @Override
    public void run() {



        //waiting for all task completed


        while (!mExecutor.isTerminated()) {
            try {
                if(mRun ==1){
                    String mIP = (String) mIpInLan.get(count);

                    if(!mExecutor.isShutdown()) {
                        ScanTasks mScanTask = new ScanTasks(mIP,mDeviceScanManagerHandler);
                        mExecutor.submit(mScanTask);
                        count++;
                    }

                } else if(mRun ==2){
                    count=0;
                    mIpInLan.clear();
                    String localIp = NetworkUtil.getLocalIp();
                    mIpInLan.addAll(NetworkUtil.getSubnet(localIp,true));
                    mRun =0;
                }
                Thread.sleep(1000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        mProgress = Constant.PROGRESS_MAX;
    }

    public void startScan(){
        mDeviceScanManagerHandler.sendMessage(
                mDeviceScanManagerHandler.obtainMessage(
                        Constant.MSG.START) );
    }


    public void stopScan()
    {
        mDeviceScanManagerHandler.sendMessage(
                mDeviceScanManagerHandler.obtainMessage(
                        Constant.MSG.STOP) );
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
                    mDeviceScanManagerHandler.sendMessage(
                            mDeviceScanManagerHandler.obtainMessage(
                                    Constant.MSG.SCAN_OVER) );
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
                    manager.mProgress++;
                    break;
                case Constant.MSG.START :
                    manager.mRun = 1;
                    break;
                case Constant.MSG.STOP :
                    manager.mRun =2;
                    break;
            }
        }
    }
}
