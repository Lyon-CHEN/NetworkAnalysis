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
- Test on:  
	RedMI Note8 Pro. 
***
## Key Technology
- WiFi scanning: 
	- Register for the android broadcast, and listen to the Wifi scan result refresh every 20 seconds.
	- Architecture
 
- Device scanning: 
- Speed test:
- Network diagnosis:
***
## Todo list
- [ ] TODO

***
## Changlog
## Bug/Issue
The DNS resolution check function is compatible with some computers, the no class def found error of Landroid / net / Dns Resolver will occur.
Temporary solution: annotation checkResolvingName() function in FixerViewModel.java

***
## External libraries
[JSpeedTest:] (https://github.com/bertrandmartel/speed-test-lib)</br>
[WiFiDetective:] (https://github.com/gpfduoduo/WiFiDetective)

