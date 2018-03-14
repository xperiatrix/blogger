# ThinkPad FanControl (ThinkFan) on Fedora27

&nbsp;&nbsp;&nbsp;&nbsp;I did upgrade my old ThinkPad laptop to Fedora27 yesterday afternoon. And it worked as well as I had expected, <b>except the fan speed of the CPU</b>. It runs loud and I find that the max fan-speed is up to <b>3,900+ RPM</b>. So I have to work it out for controlling the fan-speed. That's ThinkFan and it is quite simple to make it.  

+ Step 1: You need to install thinkfan.  

```javascript
	thinkpad@fedora27 =>  su   		// Type the password of the root
	root@fedora27 =>  dnf install -y thinkfan  // Same with 'yum install thinkfan'
```  

+ Step 2: Check the fan level parameters for your ThinkPad model on [thinkwiki](http://www.thinkwiki.org/wiki/How_to_control_fan_speed). My Laptop is ThinkPad T400 and it can work quietly under the param with fan-level 1.

```javascript
	ThinkPad T400 (fan levels RPM: 1 = ~1886, 2 = ~2800, 3-5 = ~3000, 6-7 = ~3500; disengaged mode works at ~5100)
```


+ Step 3: Prepare the config file for thinkfan.  
```javascript
	root@fedora27 => touch /etc/modules-load.d/options  // Create config file for thinkfan.
	root@fedora27 => vi /etc/modules-load.d/options		// Edit config file.

	options thinkpad_acpi fan_control=1		// Paste this line into the file, then save and exit.

	root@fedora27 => cat /etc/modules-load.d/options >> /etc/modules-load.d/thinkpad_acpi.conf  
	// Create the same config file named thinkpad_acpi.conf
	
	root@fedora27 => reboot									// Reboot the system and make it work 
```  


+ Step 4: Check or Adjust fan-level. My laptop works quietly again.  
