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
	- ACCESS_FINE_LOCATION		#be requried for wifi ssid scan, check permission on first-time run
	- ACCESS_COARSE_LOCATION	#be requried for wifi ssid scan
	- INTERNET			#be required for speed test
## Test Cases:
- Test on:  RedMI Note8 Pro. 
Case1: First run permission request</br>
![img](https://github.com/Lyon-CHEN/NetworkAnalysis/blob/main/Images/screenshot-7.png) </br>
Case2: Wifi scan, refresh every 20 seconds </br>
![img](https://github.com/Lyon-CHEN/NetworkAnalysis/blob/main/Images/screenshot-1s.png) </br>
Case3: LAN device scan displays IP, MAC and device manufacturer, scan might be slow and you can pause by pressing the button</br>
![img](https://github.com/Lyon-CHEN/NetworkAnalysis/blob/main/Images/screenshot-2.png) </br>
Case4: Test download and upload speed seperately and show in Kb/s</br>
![img](https://github.com/Lyon-CHEN/NetworkAnalysis/blob/main/Images/screenshot-3.png) </br>
Case5: Diagnose network connection</br>
![img](https://github.com/Lyon-CHEN/NetworkAnalysis/blob/main/Images/screenshot-4.png) </br>
Case6: Theme color switching, two are provided by default</br>
![img](https://github.com/Lyon-CHEN/NetworkAnalysis/blob/main/Images/screenshot-5.png) </br>
Case7: Language switching, now supports Chinese and English</br>
![img](https://github.com/Lyon-CHEN/NetworkAnalysis/blob/main/Images/screenshot-6.png) </br>
***
## Key Technology
- WiFi scanning: 
	- Using MVVM model，Fragment(UI) refresh list when monitoring Wifi list
	- Evaluate wifi strength using different icons.
	- Register for the android broadcast in model, and listen to the Wifi scan result refresh a livedata list every 20 seconds.        
	- Architecture
	![img](https://github.com/Lyon-CHEN/NetworkAnalysis/blob/main/Images/wifi-fragment.png)
- Device scanning: 
	- Using MVVM and Room frame
	- Use multithread to scan devices in network, ping operation from system command is used
	- Animation are showing while scanning
	- Fetch arp table by ip neighbour and find device ststus with Mac address
	- Download IEEE official Oui device file in database
	- Find manufacturer by search Oui database via Mac address
	- Using Room frame on Database
	![img](https://github.com/Lyon-CHEN/NetworkAnalysis/blob/main/Images/scan-fragment.png)
- Speed test:
	- Using MVVM model	
	- Using JSpeedTest third-party lib which is compatible with speedtest
	- Use signal to control Download and Upload Tests
- Network diagnosis:
	- Detect Wifi and Data status via system services
	- Check gateway and DNS connection by looking at Pings.
	- By analyzing google.com, to judged if the DNS  is normal
- Setting
	- Using MVVM model, monitoring scope change and value picked by user
	- Setup 2 layer of list menus, developer can easily add and delete different double-level menus by munipulating arrays
	- Theme switch are modulized, developer can add theme quick by adding new style under theme.xml
	- listview are refreshing between different datas, take less time and save more space.(manually delay can solve the animation effect display issue)
***
## Todo list
- [ ] Add WiFi security detection function, such as encryption method, weak password detection, etc.
- [ ] Add port scanning, and identify the port service protol, like Http/Https, SSH, Bonjour, Samba...
- [ ] Through the speed test site selection, the list of nearby SpeedTest servers has been obtained through API
- [ ] Improving UI

***
## Bug/Issue
The DNS resolution check function is compatible with some computers, the no class def found error of Landroid / net / Dns Resolver will occur.
Temporary solution: annotation checkResolvingName() function in FixerViewModel.java

The French server is used for speed measurement by default, and the data is not accurate due to that.
## Changlog
***
## External libraries
[JSpeedTest:] (https://github.com/bertrandmartel/speed-test-lib)</br>
[WiFiDetective:] (https://github.com/gpfduoduo/WiFiDetective) </br>
[RadarView]

