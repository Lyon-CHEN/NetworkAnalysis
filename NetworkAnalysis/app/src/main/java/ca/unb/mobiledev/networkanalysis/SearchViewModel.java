package ca.unb.mobiledev.networkanalysis;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ca.unb.mobiledev.networkanalysis.network.Device;
import ca.unb.mobiledev.networkanalysis.network.devicescan.DeviceScanManager;
import ca.unb.mobiledev.networkanalysis.network.devicescan.DeviceScanResult;

public class SearchViewModel extends AndroidViewModel {
    private DeviceScanManager mManager;
    private MutableLiveData<List<Device>> mDeviceListLiveData ;
    private List<Device> mDeviceList;
    private ExecutorService threadPool;
    private MutableLiveData<Integer> mScanProgress;

    public SearchViewModel(@NonNull Application application){
        super(application);
        mDeviceList = new ArrayList<Device>();
        threadPool = Executors.newSingleThreadExecutor();
        mManager = new DeviceScanManager(application.getApplicationContext(), new DeviceScanResult() {
            @Override
            public void deviceScanResult(Device device, Integer progress) {
                if (!mDeviceList.contains(device)) {
                    mDeviceList.add(device);
                    mDeviceListLiveData.postValue(mDeviceList);
                }
                mScanProgress.setValue(progress);
            }
        });
        threadPool.execute(mManager);
    }

    public void stopScan(){
        mManager.stopScan();
    }

    public void startScan(){
        mDeviceList.clear();
        mManager.startScan();
    }

    LiveData<Integer> getProgress () {
        if (mScanProgress==null){
            mScanProgress = new MutableLiveData<Integer>();
        }
        return mScanProgress;
    }

    LiveData<List<Device>> getScanResult () {
        if (mDeviceListLiveData==null){
            mDeviceListLiveData = new MutableLiveData<List<Device>>();
        }
        return mDeviceListLiveData;
    }



}
