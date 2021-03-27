package ca.unb.mobiledev.networkanalysis;

import androidx.fragment.app.Fragment;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import ca.unb.mobiledev.networkanalysis.adapter.ItemWifiListAdapter;
import ca.unb.mobiledev.networkanalysis.network.NetworkUtil;

public class WifiFragment extends Fragment {
    private final static String TAG = "WifiFragment";
    private View vWifiFragment;
    WifiManager mWifiManager;
    List<ScanResult> listWifiScan;
    private Context mContext;

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
                listWifiScan = mWifiManager.getScanResults();
                listAdapter = new ItemWifiListAdapter(context, listWifiScan);
                ListView listView = vWifiFragment.findViewById(R.id.wifiListView);

                listView.setAdapter(listAdapter);
                listView.setOnItemClickListener(listAdapter);

                listAdapter.notifyDataSetChanged();
            }
        }
    };

    protected  void updateCurrentWifiStatus () {
        WifiInfo wifiInfo = NetworkUtil.getWifiInfo(mContext);
        StringBuffer mCurrConnStr = new StringBuffer();
        mCurrConnStr.append("SSID: ").append(wifiInfo.getSSID()).append("\n");
        mCurrConnStr.append("MAC Address: ").append(wifiInfo.getBSSID()).append("\n");
        mCurrConnStr.append("Signal Strength(dBm): ").append(wifiInfo.getRssi()).append("\n");
        mCurrConnStr.append("speed: ").append(wifiInfo.getLinkSpeed()).append(" ").append(WifiInfo.LINK_SPEED_UNITS);

        TextView tConnected_WiFI = vWifiFragment.findViewById(R.id.connected_wifi_info);
        tConnected_WiFI.setText(mCurrConnStr);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vWifiFragment = inflater.inflate(R.layout.fragment_wifi, container, false);
        mContext = container.getContext();

        if (!NetworkUtil.isWifiConnected(mContext)) {
            Toast.makeText(mContext,
                    R.string.connect_wifi_please,Toast.LENGTH_SHORT).show();
        }else {
            mWifiManager = NetworkUtil.getWifiManager(mContext);
            listWifiScan = mWifiManager.getScanResults();

            updateCurrentWifiStatus();

            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
            intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
            intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
            intentFilter.addAction(WifiManager.RSSI_CHANGED_ACTION);
            mContext.registerReceiver(wifiScanResultReceiver, intentFilter);

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    Log.i(TAG, "start scan()");
                    mWifiManager.startScan();
                }
            }, 0, SAMPLE_RATE);
        }
        return vWifiFragment;
    }
}