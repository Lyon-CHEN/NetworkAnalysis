package ca.unb.mobiledev.networkanalysis;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

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
    private WifiViewModel mWifiViewModel;
    private Context mContext;

    private ItemWifiListAdapter listAdapter;

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
        mWifiViewModel = new ViewModelProvider(requireActivity()).get(WifiViewModel.class);
        mWifiViewModel.getWifiScanResult().observe(getViewLifecycleOwner(),ScanResult->{
            listAdapter = new ItemWifiListAdapter(mContext, ScanResult);
            ListView listView = vWifiFragment.findViewById(R.id.wifiListView);

            listView.setAdapter(listAdapter);
            listView.setOnItemClickListener(listAdapter);

            listAdapter.notifyDataSetChanged();
        });
        updateCurrentWifiStatus();
        return vWifiFragment;
    }



    @Override
    public void onResume() {
        super.onResume();
        if (!NetworkUtil.isWifiConnected(mContext)) {
            Toast.makeText(mContext,
                    R.string.connect_wifi_please,Toast.LENGTH_SHORT).show();
        }else {
            mWifiViewModel.startTimer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mWifiViewModel.stopTimer();
    }
}