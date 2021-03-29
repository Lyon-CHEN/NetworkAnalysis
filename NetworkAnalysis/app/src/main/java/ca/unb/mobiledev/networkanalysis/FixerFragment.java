package ca.unb.mobiledev.networkanalysis;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import ca.unb.mobiledev.networkanalysis.adapter.SearchDeviceListAdapter;

public class FixerFragment extends Fragment {
    private final static String TAG = "FixerFragment";

    private View FixerFragmentView;
    private Context mContext;
    private RecyclerView mDeviceListView;
    private SearchDeviceListAdapter mDeviceListAdapter;
    private ProgressBar mProgressBar;
    private TextView tCheckWifi;
    private TextView tCheckData;
    private TextView tCheckDns;
    private TextView tCheckGateway;
    private TextView tCheckResolvingDNS;

    private FixerViewModel mFixerViewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FixerFragmentView = inflater.inflate(R.layout.fragment_fixer, container, false);

        mProgressBar = FixerFragmentView.findViewById(R.id.checkProgressBar);
        mContext = container.getContext();
        tCheckWifi = FixerFragmentView.findViewById(R.id.connecting_wifi);
        tCheckData = FixerFragmentView.findViewById(R.id.connecting_data);
        tCheckGateway = FixerFragmentView.findViewById(R.id.connecting_Gateway);
        tCheckDns = FixerFragmentView.findViewById(R.id.connecting_DNS);
        tCheckResolvingDNS = FixerFragmentView.findViewById(R.id.connection_ResolvingDNS);

        mFixerViewModel = new ViewModelProvider(requireActivity()).get(FixerViewModel.class);
        mFixerViewModel.getConnectingWifi().observe(getViewLifecycleOwner(),  isWifiConnected-> {

            tCheckWifi.setText(isWifiConnected);
       });
        mFixerViewModel.getConnectingData().observe(getViewLifecycleOwner(),  isDataConnected-> {

            tCheckData.setText(isDataConnected);
        });
        mFixerViewModel.getConnectingDNS().observe(getViewLifecycleOwner(),  isDNSConnected-> {

            tCheckDns.setText(isDNSConnected);
        });
        mFixerViewModel.getConnectingGateway().observe(getViewLifecycleOwner(),  isGatewayConnected-> {

            tCheckGateway.setText(isGatewayConnected);
        });
        mFixerViewModel.getResolvingName().observe(getViewLifecycleOwner(),  isResolvingConnected-> {

            tCheckResolvingDNS.setText(isResolvingConnected);
        });

        mFixerViewModel.startCheck();

        return FixerFragmentView;
    }
}