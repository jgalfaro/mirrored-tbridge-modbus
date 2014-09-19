# README #

"tollbridge" is a test bed for modbus (SCADA Network Protocol).
It implements :
* a control center,
* a Lego EV3 bridge,
* a Lego EV3 toll,
* a Simulated toll.

### How do I get set up? ###

help for installation of Lejos environnement on : http://thinkbricks.net/?p=826
Some other help to configure : http://hirt.se/blog/?p=489

A. Configuration of Home Computer
	1. Configure Eclipse as mentionned by Lejos
	2. Git clone project
	3. Open project in eclipse

B. Configuration of EV3 :
	1. Follow instructions to start Lejos :
		a. prepare SD card (sd500.img)
		b. copy Lejos files
		c. copy Embedded JRE
		d. Start EV3 with Micro SD
		e. Proceed to configuration (see FAQ)
	2. Configure Lejos
		a. connect to Lejos (USB/Bluetooth/WIFI)
			after having plugged the EV3 :
			ssh root@10.0.1.1
		b. configure correct IP
	3. Configure Toll/Bridge Device
		a. with Eclipse, compile and upload 
		b. edit Device.ini to satisfy parameters
		c. launch tollbridge.jar (by EV3 or by Eclipse)

C. Configuration of Control Center :
	1. with Eclipse, compile and launch "ControlCenter"


FAQ :
* How to change IP address/range

	1. Login on EV3 :
		# ssh root@10.0.1.1
	2. edit /home/root/lejos/bin/netaddress
	    # change IP address
	3. edit /home/root/lejos/bin/udhcpd.conf
	    # change IP range fo the DHCP server
	4. restart EV3
		# reboot


