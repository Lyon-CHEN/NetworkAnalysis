package ca.unb.mobiledev.networkanalysis;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ca.unb.mobiledev.networkanalysis.adapter.SearchDeviceListAdapter;
import ca.unb.mobiledev.networkanalysis.network.Constant;
import ca.unb.mobiledev.networkanalysis.network.Device;
import ca.unb.mobiledev.networkanalysis.network.NetworkUtil;
import ca.unb.mobiledev.networkanalysis.network.devicescan.DeviceScanManager;
import ca.unb.mobiledev.networkanalysis.network.devicescan.DeviceScanResult;
import ca.unb.mobiledev.networkanalysis.radarview.RadarView;


public class SearchFragment extends Fragment {
    private final static String TAG = "SearchFragment";

    private View vSearchFragmentView;
    private Context mContext;
    private RecyclerView mDeviceListView;
    private SearchDeviceListAdapter mDeviceListAdapter;
    private ProgressBar mProgressBar;
    private TextView tLocalIP;

    private SearchViewModel mSearchViewModel;
    private RadarView vRadarView;
    private ToggleButton vSerachBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vSearchFragmentView = inflater.inflate(R.layout.fragment_search, container, false);

        vRadarView = vSearchFragmentView.findViewById(R.id.search_radar_view);
        vRadarView.setDirection(RadarView.ANTI_CLOCK_WISE);

        mDeviceListView = vSearchFragmentView.findViewById(R.id.device_list);
        mProgressBar = vSearchFragmentView.findViewById(R.id.searchProgressBar);

        tLocalIP = vSearchFragmentView.findViewById(R.id.localIp);
        mContext = container.getContext();

        StringBuffer mCurrConnStr = new StringBuffer();
        mCurrConnStr.append("Local IP: ").append( NetworkUtil.getLocalIp()).append("\n");
        mCurrConnStr.append("GateWay IP: ").append(NetworkUtil.getGateWayIp(mContext));

        tLocalIP.setText(mCurrConnStr);
        mProgressBar.setMax(Constant.IP_COUNT);
        mProgressBar.setProgress(0);

        if (!NetworkUtil.isWifiConnected(mContext)) {
            Toast.makeText(mContext,
                    R.string.connect_wifi_please,Toast.LENGTH_SHORT).show();

        }

        mSearchViewModel = new ViewModelProvider(requireActivity()).get(SearchViewModel.class);
        mSearchViewModel.getScanResult().observe(getViewLifecycleOwner(), deviceLiveData -> {
            if(deviceLiveData !=null){
                mDeviceListAdapter = new SearchDeviceListAdapter(mContext,deviceLiveData);
                mDeviceListView.setAdapter(mDeviceListAdapter);
            }
            mDeviceListAdapter.notifyDataSetChanged();
        });

        mSearchViewModel.getProgress().observe(getViewLifecycleOwner(), progressValue -> {
            if(progressValue !=null){
                mProgressBar.setProgress(progressValue);
            }

        });

        vSerachBtn =vSearchFragmentView.findViewById(R.id.search_ToggleButton);

        vSerachBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mSearchViewModel.stopScan();
                    vRadarView.stop();
                } else {
                    mSearchViewModel.startScan();
                    vRadarView.start();
                }
            }
        });

        return vSearchFragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mSearchViewModel.startScan();
        vRadarView.start();

    }

    @Override
    public void onPause() {
        super.onPause();
        mSearchViewModel.stopScan();
        vRadarView.stop();
    }
}