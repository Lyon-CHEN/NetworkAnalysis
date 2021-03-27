package ca.unb.mobiledev.networkanalysis.network;

public class Constant
{
    public static final int IP_COUNT = 255;

    public static final int SCAN_COUNT = 10;

    public static final int UPD_TIMEOUT = 500;
    public static final int PROGRESS_MAX = 100;
    public static final int SSH = 22; //linux ssh端口
    public static final int HTTP = 80;//和8081 http 端口
    public static final int TELENT= 135; //远程打开对方的telnet服务器
    public static final int NETBIOS= 137; //在局域网中提供计算机的名字或OP地址查询服务，一般安装了NetBIOS协议后，就会自动开放
    public static final int SAMBA =139; //Windows获得NetBIOS/SMB服务
    public static final int PRINT =445; //局域网中文件的共享端口
    public static final int Remote =3389; //远程桌面服务端口
    public static final int SSDP =1900; //ssdp协议
    public static final int AppleBonjour =5351; //AppleBonjour
    public static final int AppleService =5353; //Apple Bonjour、AirPlay、家庭共享、打印机查找、回到我的 Mac
    public static final int Apple = 62078; //Apple的一个端口


    public interface MSG
    {
        public static final int START = 0;
        public static final int STOP = -1;
        public static final int SCAN_ONE = 1;
        public static final int SCAN_OVER = 2;

        public static final int WIFI_SCAN_RESULT = 10;

    }

}

