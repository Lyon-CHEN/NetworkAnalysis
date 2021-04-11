package ca.unb.mobiledev.networkanalysis.network.devicescan;

import ca.unb.mobiledev.networkanalysis.network.Device;

public interface DeviceScanResult
{
    public void deviceScanResult(Device device);
    public void updateProgress(Integer progress);
}

