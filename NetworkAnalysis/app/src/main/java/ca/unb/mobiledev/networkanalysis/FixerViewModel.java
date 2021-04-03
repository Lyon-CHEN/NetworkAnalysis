package ca.unb.mobiledev.networkanalysis;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DnsResolver;
import android.net.Network;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ca.unb.mobiledev.networkanalysis.network.NetworkUtil;

public class FixerViewModel extends AndroidViewModel {
    private MutableLiveData<String> mConnectingGateway;
    private MutableLiveData<String> mConnectingDNS;
    private MutableLiveData<String> mResolvingName;
    private MutableLiveData<String> mConnectingWifi;
    private MutableLiveData<String> mConnectingData;

    private Context mContext;
    private ExecutorService threadPool;

    public MutableLiveData<String> getConnectingWifi() {
        return mConnectingWifi;
    }

    public MutableLiveData<String> getConnectingData() {
        return mConnectingData;
    }

    public MutableLiveData<String> getResolvingName() {
        return mResolvingName;
    }

    public MutableLiveData<String> getConnectingDNS() {
        return mConnectingDNS;
    }

    public MutableLiveData<String> getConnectingGateway() {
        return mConnectingGateway;
    }

    public FixerViewModel(@NonNull Application application) {
        super(application);

        mConnectingGateway = new MutableLiveData<String>();
        mConnectingGateway.setValue("Connecting Gateway...");

        mConnectingDNS = new MutableLiveData<String>();
        mResolvingName = new MutableLiveData<String>();
        mConnectingData = new MutableLiveData<String>();
        mConnectingWifi = new MutableLiveData<String>();

        mContext = application.getApplicationContext();
        threadPool = Executors.newSingleThreadExecutor();
    }

    public void checkConnectGateway (){
        String connect = " ";
        connect = NetworkUtil.getGateWayIp(mContext);
        if(!TextUtils.isEmpty(connect))
            if(NetworkUtil.isPingOk(connect))
                connect += " is Connected. OK!";
            else
                connect += " is not Connected. Failed!";
        mConnectingGateway.setValue(connect);
    }

    public void checkConnectDNS (){
        String connect =" ";
        List<String> dns = NetworkUtil.getDNSIp(mContext);
        for (String dnsStr : dns){
            if(!TextUtils.isEmpty(dnsStr)){
                connect += "DNS:" + dnsStr;
                if(NetworkUtil.isPingOk(dnsStr))
                    connect += " is Connected. OK!\n";
                else
                    connect += " is not Connected. Failed!\n";
            }
        }

        mConnectingDNS.setValue(connect);
    }

    public void checkResolvingName(){
        String  connect =" ";
        List<InetAddress> result = new ArrayList<InetAddress>();
        DnsResolver dnsResolver = DnsResolver.getInstance();
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mContext.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        Network net = connectivityManager.getActiveNetwork();
        String domain = "www.google.com";
        int flags = dnsResolver.FLAG_NO_CACHE_LOOKUP;
        dnsResolver.query(net, domain, flags, threadPool, null, new DnsResolver.Callback(){

            @Override
            public void onAnswer(@NonNull Object answer, int rcode) {
                List<InetAddress> querySet = (List<InetAddress>)answer;
                String ans = querySet.get(0).getHostAddress();
                ans += " OK!";
                mResolvingName.postValue(ans);

            }

            @Override
            public void onError(@NonNull DnsResolver.DnsException error) {
                mResolvingName.postValue(" Failed!");
            }
        });

    }

    public void checkConnectWifi(){
        String connectWifi = NetworkUtil.getCurrentSsid(mContext);
        if(!TextUtils.isEmpty(connectWifi))
            connectWifi += " connected!";
        else
            connectWifi = " No wifi connected!";
        mConnectingWifi.setValue(connectWifi);
    }

    public void checkConnectDataName(){
        String connect = " ";
        if (NetworkUtil.getDataEnabled(mContext))
            connect += " Mobile Data is enabled. OK!";
        else
            connect += " Mobile Data is disable. Failed!";
        mConnectingData.setValue(connect);
    }

    public void startCheck() {
        checkConnectGateway();
        checkConnectDNS();
        //checkResolvingName();
        checkConnectWifi();
        checkConnectDataName();
    }
}
