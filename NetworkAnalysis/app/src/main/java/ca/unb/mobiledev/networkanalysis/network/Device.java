package ca.unb.mobiledev.networkanalysis.network;

public class Device {
    public String Ip;
    public String macAddress;
    public String vendor;
    public String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIp() {
        return Ip;
    }

    public void setIp(String ip) {
        Ip = ip;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public Device(String Ip, String macAddress)
    {
        this.Ip = Ip;
        this.macAddress = macAddress;
    }

    public Device(String Ip, String macAddress, String vendor, String address)
    {
        this.Ip = Ip;
        this.macAddress = macAddress;
        this.vendor = vendor;
        this.address = address;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Device device = (Device) o;

        return Ip.equals(device.Ip) && macAddress.equals(device.macAddress);
    }

}
