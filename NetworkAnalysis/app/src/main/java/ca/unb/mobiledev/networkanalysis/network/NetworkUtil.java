package ca.unb.mobiledev.networkanalysis.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.DnsResolver;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteOrder;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NetworkUtil {
    private final static String TAG = "NetworkUtil";

    private static final String tag = NetworkUtil.class.getSimpleName();
    private static final String NEIGHBOR_FAILED = "FAILED";
    private static final String NEIGHBOR_INCOMPLETE="INCOMPLETE";
    private static final String NEIGHBOR_REACHABLE="REACHABLE";



    public static boolean isWifiConnected(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if (wifiNetworkInfo.isConnected()) {
            return true;
        }

        return false;
    }

    public static boolean lookupName(String hostname, Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);

        return  true;
    }

    public static boolean getDataEnabled(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            Method getMobileDataEnabledMethod = tm.getClass().getDeclaredMethod("getDataEnabled");
            if (null != getMobileDataEnabledMethod) {
                return (boolean) getMobileDataEnabledMethod.invoke(tm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }



    public static WifiInfo getWifiInfo(Context context) {
        WifiManager wifiManager = getWifiManager(context);
        if (wifiManager != null) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            return wifiInfo;
        }
        else {
            return null;
        }
    }


    public static WifiManager getWifiManager(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(
                Context.WIFI_SERVICE);
        return wifiManager;
    }


    public static DhcpInfo getDhcpInfo(Context context) {
        WifiManager wifiManager = getWifiManager(context);
        if (wifiManager != null) {
            DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();
            return dhcpInfo;
        }
        else {
            return null;
        }
    }


    public static String getCurrentSsid(Context context) {
        WifiInfo wifiInfo = getWifiInfo(context);
        if (wifiInfo != null) {
            return wifiInfo.getSSID();
        }
        else {
            return null;
        }
    }


    public static String getGateWayIp(Context context) {
        String gatewayIp = null;
        DhcpInfo dhcpInfo = getDhcpInfo(context);
        if (dhcpInfo != null) {
            gatewayIp = Int2String(dhcpInfo.gateway);
        }

        Log.d(tag, "gate way ip = " + gatewayIp);
        return gatewayIp;
    }

    public static List<String> getDNSIp(Context context) {
        List dnsIp = new ArrayList();
        DhcpInfo dhcpInfo = getDhcpInfo(context);
        if (dhcpInfo != null) {
            dnsIp.add(Int2String(dhcpInfo.dns1));
            if(dhcpInfo.dns2>0)
                dnsIp.add(Int2String(dhcpInfo.dns2));
        }

        //Log.d(tag, "gate way ip = " + dnsIp.toString());
        return dnsIp;
    }

    /**
     * local macAddress
     */
    public static String getLocalMac(Context context) {
        String localMac = null;
        WifiInfo wifiInfo = getWifiInfo(context);
        if (wifiInfo != null) {
            localMac = wifiInfo.getMacAddress();
        }

        Log.d(tag, "local mac = " + localMac);
        return localMac;
    }


    /**
     *
     *
     * @throws UnknownHostException
     */
    public static InetAddress getBroadcastAddress(Context context)
            throws UnknownHostException {
        DhcpInfo dhcpInfo = getDhcpInfo(context);
        if (dhcpInfo == null) {
            return InetAddress.getByName("255.255.255.255");
        }
        int broadcast = (dhcpInfo.ipAddress & dhcpInfo.netmask) |
                ~dhcpInfo.netmask;
        byte[] quads = new byte[4];
        for (int k = 0; k < 4; k++) {
            quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
        }

        return InetAddress.getByAddress(quads);
    }


    /**
     *
     */
    public static String getLocalIp() {
        String localIp = null;

        try {
            Enumeration<NetworkInterface> en
                    = NetworkInterface.getNetworkInterfaces();
            while (en.hasMoreElements()) {
                NetworkInterface networkInterface = en.nextElement();
                Enumeration<InetAddress> inetAddresses
                        = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    if (!inetAddress.isLoopbackAddress() &&
                            inetAddress instanceof Inet4Address) {
                        localIp = inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        Log.d(tag, "local ip = " + localIp);
        return localIp;
    }
    /**
     * 从proc/net/arp中读取ip_mac对
     */
    public static List<Device> getDeviceListFromArp()
    {
        List<Device> result = new ArrayList<Device>();
        String mac="";

        String regExp = "((([0-9,A-F,a-f]{1,2}" + ":" + "){1,5})[0-9,A-F,a-f]{1,2})";
        Pattern pattern;
        Matcher matcher;
        BufferedReader br = null;
        try {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                Process ipProc = Runtime.getRuntime().exec("ip neighbor");
                ipProc.waitFor();
                if (ipProc.exitValue() != 0) {
                    throw new Exception("Unable to access ARP entries");
                }

                br = new BufferedReader(new InputStreamReader(ipProc.getInputStream(), StandardCharsets.UTF_8));
                String line;
                while ((line = br.readLine()) != null) {
                    String[] neighborLine = line.split("\\s+");
                    if (neighborLine.length <= 4) {
                        continue;
                    }
                    String ip = neighborLine[0];
                    InetAddress addr = InetAddress.getByName(ip);
                    if (addr.isLinkLocalAddress() || addr.isLoopbackAddress()) {
                        continue;
                    }
                    String macAddress = neighborLine[4];
                    String state = neighborLine[neighborLine.length - 1];

                    if (!NEIGHBOR_REACHABLE.equals(state) && !NEIGHBOR_INCOMPLETE.equals(state)) {
                        /*
                        boolean isReachable = false;
                        try {
                            isReachable = InetAddress.getByName(ip).isReachable(5000);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (isReachable) {
                        */
                            result.add(new Device(ip, macAddress, ""));
                        //}
                    }
                }
            } else {
                br = new BufferedReader(new FileReader("/proc/net/arp"));
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(" +");
                    if (parts.length < 6) {
                        continue;
                    }
                    String ip = parts[0];
                    String macAddress = parts[3];
                    if (!ip.equalsIgnoreCase("IP")) {
                        boolean isReachable = false;
                        try {
                            isReachable = InetAddress.getByName(ip).isReachable(5000);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (isReachable) {
                            result.add(new Device(ip, macAddress,""));
                        }
                    }

                }
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
            }
        }
        return result;
    }

    public static List<String> getSubnet(String localIP, boolean localIncluded){
        String netIp = localIP.substring(0, localIP.lastIndexOf(".") + 1);
        List<String> ipList = new ArrayList<String>();

        for (int i = 1; i < Constant.IP_COUNT; i++)
        {
            ipList.add(netIp + i);
        }

        if(!localIncluded)
            ipList.remove(getLocalIp());
        return ipList;

    }



    public final static String Int2String(int IP) {
        String ipStr = "";

        if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
            ipStr += String.valueOf(0xFF & IP);
            ipStr += ".";
            ipStr += String.valueOf(0xFF & IP >> 8);
            ipStr += ".";
            ipStr += String.valueOf(0xFF & IP >> 16);
            ipStr += ".";
            ipStr += String.valueOf(0xFF & IP >> 24);
        }
        else {

            ipStr += String.valueOf(0xFF & IP >> 24);
            ipStr += ".";
            ipStr += String.valueOf(0xFF & IP >> 16);
            ipStr += ".";
            ipStr += String.valueOf(0xFF & IP >> 8);
            ipStr += ".";
            ipStr += String.valueOf(0xFF & IP);
        }

        return ipStr;
    }


    public static boolean is24GHz(int frequency) {
        return frequency> 2400 && frequency < 2500;
    }

    public static boolean is5GHz(int frequency) {
        return frequency > 4900 && frequency < 5900;
    }



    /**
     *
     */
    public static boolean isPingOk(String ip) {
        Log.d(tag, "ping ip = " + ip);
        try {
            Process p = Runtime.getRuntime()
                    .exec("/system/bin/ping -c 10 -w 4 " + ip);
            if (p == null) {
                return false;
            }

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                if (line.contains("bytes from")) {
                    //Log.d(tag, "ping read line = " + line);
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }


    public static String ping(String ip, String count) {
        String result = "";
        try {
            Process p = Runtime.getRuntime()
                    .exec("/system/bin/ping -c " + count + " -w 4 " +
                            ip);
            if (p == null) {
                return result;
            }

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                if (line.contains("bytes from")) {
                    Log.d(tag, "ping result = " + line);
                    result += line + "\n";
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }


    /**
     * 该ip的端口某些端口是否打开

     * see link{https://support.apple.com/zh-cn/HT202944}
     */
    public static boolean isAnyPortOk(String ip) {
        int portArray[] = { 22, 80, 135, 137, 139, 445, 3389, 4253, 1034, 1900,
                993, 5353, 5351, 62078 };
        Selector selector;
        for (int i = 0; i < portArray.length; i++) {
            try {
                Log.d(tag, "is any port ok ? ip = " + ip + " port =" +
                        portArray[i]);
                //tcp port detection
                selector = Selector.open();
                SocketChannel channel = SocketChannel.open();
                SocketAddress address = new InetSocketAddress(ip, portArray[i]);
                channel.configureBlocking(false);
                channel.connect(address);
                channel.register(selector, SelectionKey.OP_CONNECT, address);
                if (selector.select(500) != 0) {
                    Log.d(tag, ip + "is any port ok port ? " + portArray[i] +
                            " tcp is ok");
                    selector.close();
                    return true;
                }
                else {
                    selector.close();
                    //Socket socket = new Socket();
                    //SocketAddress socketAddress = new InetSocketAddress(ip,
                    //        portArray[i]);
                    //socket.connect(socketAddress, 500);
                    //Log.d(tag, ip + "is any port ok ? port " + portArray[i] +
                    //        " tcp is ok");
                    //socket.close();
                    continue;
                }
            } catch (IOException e) {
                Log.e(tag, e.getMessage().toString());
            }
        }
        return false;
    }
}