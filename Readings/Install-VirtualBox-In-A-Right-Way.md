#  Install VirtualBox in the right way. (Kernel driver not installed rc=-1908)

&nbsp;&nbsp;&nbsp;&nbsp; I did upgrade to the latest linux kernel on Fedora 27 tonight, but I cannot start any virtual machine instance after the reboot. So I google for the error message like this  

<p align="center"> <img src="./AAA-resource/rc=-1908.png"> </p> 



&nbsp;&nbsp;&nbsp;&nbsp; I used to fix this issue before, but this time I'll write down the solutions for the people who need help.  
&nbsp;&nbsp;&nbsp;&nbsp; Here is the solution: <B>Install development kernel and dkms</B>

	```  
		dnf update
		
		dnf install -y kernel-devel-$(uname -r)
		
		dnf install -y kernel-headers-$(uname -r)

		dnf install -y dkms

		// Apply for the configuration
		sudo /sbin/vboxconfig  

		sudo reboot
	```  

&nbsp;&nbsp;&nbsp;&nbsp; You can start any virtual instance over again after the next time you enter to desktop.  
