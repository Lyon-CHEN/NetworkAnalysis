package ca.unb.mobiledev.networkanalysis;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.sql.Time;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import ca.unb.mobiledev.networkanalysis.adapter.ItemWifiListAdapter;
import ca.unb.mobiledev.networkanalysis.network.Constant;
import ca.unb.mobiledev.networkanalysis.network.NetworkUtil;

public class WifiViewModel extends AndroidViewModel {
    private final static String TAG = "WifiViewMode";
    private Timer mTimer;
    private TimerTask mTimerTask;
    private final int SAMPLE_RATE = 20000;
    private WifiManager mWifiManager;
    List<ScanResult> listWifiScan;
    MutableLiveData<List<ScanResult>> mWifiScanResult;
    MutableLiveData<Boolean> mRefresh;

    Context mContext;

    public MutableLiveData<List<ScanResult>> getWifiScanResult(){
        return mWifiScanResult;
    }

    public WifiViewModel(@NonNull Application application) {
        super(application);

        mWifiScanResult = new MutableLiveData<List<ScanResult>>();
        mRefresh =new MutableLiveData<Boolean>();

        mContext = application.getApplicationContext();
        mWifiManager = NetworkUtil.getWifiManager(mContext);
        listWifiScan = mWifiManager.getScanResults();
        mWifiScanResult.setValue(listWifiScan);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        intentFilter.addAction(WifiManager.RSSI_CHANGED_ACTION);
        mContext.registerReceiver(wifiScanResultReceiver, intentFilter);

        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                mWifiManager.startScan();
            }
        };
    }

    private BroadcastReceiver wifiScanResultReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "wifi Scan");
            Boolean wifiScanResult = intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
            Boolean networkIDSChanged = intent.getAction().equals(WifiManager.NETWORK_IDS_CHANGED_ACTION);
            Boolean networkStateChanged = intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION);
            Boolean RSSIChanged = intent.getAction().equals(WifiManager.RSSI_CHANGED_ACTION);

            if(networkStateChanged || networkIDSChanged) {

            }

            if(wifiScanResult) {
                listWifiScan = mWifiManager.getScanResults();
                mWifiScanResult.setValue(listWifiScan);
            }
        }
    };

    public void startTimer(){
        if(mTimer ==null) {
            mTimer = new Timer();
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    mWifiManager.startScan();
                }
            };
            mTimer.schedule(mTimerTask, 0, SAMPLE_RATE);
        }
    }

    public  void stopTimer() {
        Log.i(TAG, "wifi Timer Stop");
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
    }
}
