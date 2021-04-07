# NetworkAnalysis
 
## Function & Features
- WiFi scanning: 
	- Get the WiFi SSID access points around the android device
	- Collect information: SSID, WiFi signal strength, channel and encryption method.
- Device scanning: 
	- Scan all alive devices which under the wifi your android device is located and connected. 
	- Identify the device's manufacturer base on Mac address.
- Speed test:
	- Test the upload and download speed under the current network connection through JSpeedTest Lib.
- Network diagnosis
	- Connectivity to gateway
	- Connectivity to DNS Server
	- Resovling Domain name(default:www.google.com)
	
- Setting
	- Language support:
	- Theme
***
## Compatibility
- Platform:
	Compile with: Android Studio 4.1.1 & Java 1.8
		
- Supported API levels:
	**Android 10, API Level 29**
- Permission requirement:
	- ACCESS_NETWORK_STATE
	- ACCESS_WIFI_STATE
	- CHANGE_WIFI_STATE
	- ACCESS_FINE_LOCATION		#be requried for wifi ssid scan, 第一次运行时会检查授权
	- ACCESS_COARSE_LOCATION	#be requried for wifi ssid scan
	- INTERNET			#be required for speed test
## Test Cases:
- Test on:  RedMI Note8 Pro. 
Case1: 首次运行权限申请
![img](https://github.com/Lyon-CHEN/NetworkAnalysis/blob/main/Images/screenshot-7.png) </br>
Case2: Wifi 扫描，每20秒刷新 </br>
![img](https://github.com/Lyon-CHEN/NetworkAnalysis/blob/main/Images/screenshot-1.png) </br>
Case3: 局域网设备扫描，可能显示IP,MAC和设备厂商，扫描比较慢，可以通过按键暂停</br>
![img](https://github.com/Lyon-CHEN/NetworkAnalysis/blob/main/Images/screenshot-2.png) </br>
Case4: 分别测试上传和下载速度，通过KB/s显示</br>
![img](https://github.com/Lyon-CHEN/NetworkAnalysis/blob/main/Images/screenshot-3.png) </br>
Case5: 诊断网络连接</br>
![img](https://github.com/Lyon-CHEN/NetworkAnalysis/blob/main/Images/screenshot-4.png) </br>
Case6: 主题切换，默认提供两个</br>
![img](https://github.com/Lyon-CHEN/NetworkAnalysis/blob/main/Images/screenshot-5.png) </br>
Case7: 语言切换，现在支持中文和English</br>
![img](https://github.com/Lyon-CHEN/NetworkAnalysis/blob/main/Images/screenshot-6.png) </br>
***
## Key Technology
- WiFi scanning: 
	- MVVM结构，Fragment(UI) 观察Model里的数据变化，来更新Wifi列表显示
	- 根据wifi强度，list列可以显示不同的图标
	- Model 里Register for the android broadcast, and listen to the Wifi scan result refresh a livedata list every 20 seconds.        
	- Architecture
	![img](https://github.com/Lyon-CHEN/NetworkAnalysis/blob/main/Images/wifi-fragment.png)
- Device scanning: 
	- MVVM和Room框架， 
	- 调用系统命令进行ping操作，扫描网络中设备，采用多线程方式
	- 扫描过程中显示动画
	- 通过ip neighbor读取arp表，并提取Mac address, 用来判断设备是否在线
	- 下载IEEE官方组织的Oui设备文件，导入数据库。
	- 通过Mac address在Oui数据库中找到厂商信息
	- 数据库使用Room框架
	![img](https://github.com/Lyon-CHEN/NetworkAnalysis/blob/main/Images/scan-fragment.png)
- Speed test:
	- MVVM框架	
	- 使用JSpeedTest的三方库，兼容speedtest测速
	- 下载和上传操作互斥，使用信号量控制
- Network diagnosis:
	- 通过系统服务判断wifi和数据连接
	- 通过Ping判断Gateway和DNS连接是否正常
	- 通过对google.com解析，判断DNS解析正常
- Setting
	- 通过...实现theme切换
***
## Todo list
- [ ] Add WiFi security detection function, such as encryption method, weak password detection, etc.
- [ ] Add port scanning, and identify the port service protol, like Http/Https, SSH, Bonjour, Samba...
- [ ] 通过测速站点选择，已通过API实现获取附近SpeedTest服务器列表。
- [ ] Improving UI

***
## Bug/Issue
The DNS resolution check function is compatible with some computers, the no class def found error of Landroid / net / Dns Resolver will occur.
Temporary solution: annotation checkResolvingName() function in FixerViewModel.java

默认使用法国服务器测速，数据不精确
## Changlog
***
## External libraries
[JSpeedTest:] (https://github.com/bertrandmartel/speed-test-lib)</br>
[WiFiDetective:] (https://github.com/gpfduoduo/WiFiDetective) </br>
[RadarView]

