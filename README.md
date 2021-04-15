# NetworkAnalysis
 
## Function & Features
- WiFi scanning: 
	- Get the WiFi SSID access points around the android device
	- Collect information: SSID name, signal strength and frequency channel.
	- Key Point
		- Using MVVM model 
		- Evaluate wifi strength then applying different icons.
		- Register for the android broadcast, and monitoring and refreshing Wifi scan result every 20 seconds.    
	- Architecture</br>
	![img](https://github.com/Lyon-CHEN/NetworkAnalysis/blob/main/Images/wifi-fragment.png)
- Device scanning: 
	- Scan all alive devices which under the wifi your android device is located and connected. 
	- Identify the device's manufacturer base on Mac address.
	- Key Point
		- Using MVVM and Room frame
		- Animation are showing while scanning
		- Use multithread pool to scan devices in network, the 'ping' system command is used
		- By comparing the arp table in the android system to identify the status of device, by 'ip neighbour' command
		- Find manufacturer in Oui database via Mac address
		- Import IEEE official Oui database
		- Using Room frame on Database</br>
	-  Architecture</br>
	![img](https://github.com/Lyon-CHEN/NetworkAnalysis/blob/main/Images/scan-fragment.png) 
- Speed test:
	- Test the upload and download speed through JSpeedTest Lib, compatible with https://www.speedtest.net/
	- Key Point
		- Using MVVM model	
		- Using JSpeedTest third-party lib which is compatible with speedtest
		- Use semaphore to control Download and Upload Tests</br>
	-  Architecture</br>
	![img](https://github.com/Lyon-CHEN/NetworkAnalysis/blob/main/Images/test-fragment.png)
- Network diagnosis
	- Connectivity to gateway
	- Connectivity to DNS Server
	- Resovling Domain name(default:www.google.com)	
	- Key Point
		-  Detect Wifi and Data status via system services
		- Check gateway and DNS connection by Ping command.
		- By test resolving dns name of 'www.google.com', to judged if the DNS  is normal
- Setting
	- Language support: English, Chinese
	- Theme
	- Key Point
		- Using MVVM model, monitoring scope change and value picked by user
		- Setup 2 layer of list menus, developer can easily add and delete different double-level menus by munipulating arrays
		- Theme switch are modulized, developer can add theme quick by adding new style under theme.xml
		- listview are refreshing between different datas, take less time and save more space.(manually delay can solve the animation effect display issue)
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
![img](https://github.com/Lyon-CHEN/NetworkAnalysis/blob/main/Images/screenshot-1.png) </br>
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
## Todo list
- [ ] Add WiFi security detection function, such as encryption method, weak password detection, etc.
- [ ] Add port scanning, and identify the port service protol, like Http/Https, SSH, Bonjour, Samba...
- [ ] Some Apple devices block the real Mac address by a Private Address, which makes it hard to obtain manufacturer information through mac address. </br>
Resolve solution: Determined by device communication protocol or port protocol</br>
- [ ] Add Speed Test Location Selector, So you can choose the nearest SpeedTest Servers or the server which your want to run speed test. The server list can obtail according to geographic informationhas.
- [ ] Improving UI

***
## Bug/Issue
The DNS resolution check function is compatible with some computers, the no class def found error of Landroid / net / Dns Resolver will occur.</br>
Temporary solution: annotation checkResolvingName() function in FixerViewModel.java</br>

When Using Jspeedtest to test Upload , API will automatically run test twices.

The French server is used for speed measurement by default, and the data is not accurate due to that.
## Changlog
***
## References/External libraries
[JSpeedTest:] (https://github.com/bertrandmartel/speed-test-lib)</br>
[WiFiDetective:] (https://github.com/gpfduoduo/WiFiDetective) </br>
