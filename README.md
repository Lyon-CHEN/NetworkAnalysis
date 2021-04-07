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

***
## Key Technology
- WiFi scanning: 
	- Fragment(UI) 观察Model里的数据变化，来更新Wifi列表显示
	- 根据wifi强度，list列可以显示不同的图标
	- Model 里Register for the android broadcast, and listen to the Wifi scan result refresh a livedata list every 20 seconds.        
	- Architecture
![img](https://github.com/Lyon-CHEN/NetworkAnalysis/blob/main/Images/wifi-fragment.png)
- Device scanning: 
- Speed test:
- Network diagnosis:
***
## Todo list
- [ ] Add WiFi security detection function, such as encryption method, weak password detection, etc.
- [ ] Add port scanning, and identify the port service protol, like Http/Https, SSH, Bonjour, Samba...
- [ ] Improving UI

***
## Bug/Issue
The DNS resolution check function is compatible with some computers, the no class def found error of Landroid / net / Dns Resolver will occur.
Temporary solution: annotation checkResolvingName() function in FixerViewModel.java
## Changlog
***
## External libraries
[JSpeedTest:] (https://github.com/bertrandmartel/speed-test-lib)</br>
[WiFiDetective:] (https://github.com/gpfduoduo/WiFiDetective)

