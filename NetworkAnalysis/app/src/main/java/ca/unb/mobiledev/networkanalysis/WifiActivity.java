package ca.unb.mobiledev.networkanalysis;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class WifiActivity extends AppCompatActivity {
    private final static String TAG = "WifiActivity";

    private WifiManager wifiManager;
    List<ScanResult> listWifiScan;

    private final int SAMPLE_RATE = 20000;

    private ItemWifiListAdapter listAdapter;


    private BroadcastReceiver wifiScanResultReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "wifi Scan");
            Boolean wifiScanResult = intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
            Boolean networkIDSChanged = intent.getAction().equals(WifiManager.NETWORK_IDS_CHANGED_ACTION);
            Boolean networkStateChanged = intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION);
            Boolean RSSIChanged = intent.getAction().equals(WifiManager.RSSI_CHANGED_ACTION);

            if(networkStateChanged || networkIDSChanged) {
                updateCurrentWifiStatus();
            }

            if(wifiScanResult) {
                listWifiScan = wifiManager.getScanResults();
                listAdapter = new ItemWifiListAdapter(context, listWifiScan);
                ListView listView = findViewById(R.id.wifiListView);
                listView.setAdapter(listAdapter);
                listAdapter.notifyDataSetChanged();
            }
        }
    };


    protected  void updateCurrentWifiStatus () {
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        StringBuffer mCurrConnStr = new StringBuffer();
        mCurrConnStr.append("SSID: ").append(wifiInfo.getSSID()).append("\n");
        mCurrConnStr.append("MAC Address: ").append(wifiInfo.getBSSID()).append("\n");
        mCurrConnStr.append("Signal Strength(dBm): ").append(wifiInfo.getRssi()).append("\n");
        mCurrConnStr.append("speed: ").append(wifiInfo.getLinkSpeed()).append(" ").append(WifiInfo.LINK_SPEED_UNITS);

        TextView connected_wifi_info = findViewById(R.id.connected_wifi_info);
        connected_wifi_info.setText(mCurrConnStr);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate()");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);

        Context context = getApplicationContext();

        wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {  //turn-on wifi
            wifiManager.setWifiEnabled(true);
        }
        listWifiScan = wifiManager.getScanResults();

        updateCurrentWifiStatus();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        intentFilter.addAction(WifiManager.RSSI_CHANGED_ACTION);
        registerReceiver(wifiScanResultReceiver, intentFilter);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Log.i(TAG, "start scan()");
                wifiManager.startScan();
            }
        },0,SAMPLE_RATE);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(wifiScanResultReceiver);
    }
}