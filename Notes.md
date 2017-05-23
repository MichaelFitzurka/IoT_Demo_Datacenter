# Installation Instructions

## Software Needed:
* RHEL: [https://access.redhat.com/downloads/content/69/ver=/rhel---7/7.3/x86_64/product-software](https://access.redhat.com/downloads/content/69/ver=/rhel---7/7.3/x86_64/product-software)
* VBoxAdditons: http://download.virtualbox.org/virtualbox/5.1.22/VBoxGuestAdditions_5.1.22.iso

## Oracle VM Virtual Box
* New
	* Name: `IoT All-in-One`
	* Type: `Linux`
	* Version: `Red Hat (64-bit)`
* Next
	* Memory size: `10192 MB`
* Next
* Create
* Next
* Next
	* Size: `40 GB`
* Create
---
* Settings
	* System - Processor - Processors: `2 CPUs`
	* Storage - Controller: IDE - Choose Virtual Optical Disk File.. - `rhel-server-7.3-x86_64-dvd.iso`
	* Network - Adapter 1 - Advanced - Port Forwarding - Add Host & Guest Port: `61616`
	* Shared Folders - Add - Folder Path: `~/Projects/IoT_Demo` - Auto-mount: `Check` - Ok
* Ok
---
* Start

## On VM: Install RHEL
See https://github.com/PatrickSteiner/IoT_Summit_Lab/blob/master/BuildVM.adoc

* Select `Test this media and install Red Hat Enterprise Linux 7.3`
* Continue
---
* Software Selection: Server with GUI: `Check`
* Done
---
* Installation Destination: `(Nothing)`
* Done
---
* Network & Host Name: `On`
* Done
---
* Begin Installation
---
* Root Password:
	* Root Password: `change12_me`
* Done
---
* User Creation:
	* Full Name: `demo`
	* Username: `demo-user`
	* Make this user administrator: `Check`
	* Password: `change12_me`
* Done
---
* Wait ...
---
* Reboot
---
* License Information
	* I accept the license agreement: `Check`
* Done
---
* Finish Configuration

## On VM: Inital Setup

### Close Introduction
* Next
* Next
* Skip
* Start using Red Hat Enterprise Linux Server

### Register
```sh
$ sudo subscription-manager register --username <username> --password <password> --auto-attach
$ sudo subscription-manager repos --enable=rhel-7-server-optional-rpms
$ sudo yum-config-manager --save --setopt=rhel-7-server-rt-beta-rpms.skip_if_unavailable=true

$ sudo yum upgrade -y

$ sudo yum install -y kernel-devel gcc
```

### Power Off VM

## Oracle VM Virtual Box
* Settings
	* Storage - Controller: IDE - Choose Virtual Optical Disk File.. - `VBoxGuestAdditions.iso`
* OK

## On VM: Install Virual Box Additions
```sh
$ echo export KERN_DIR=/usr/src/kernels/`uname -r` >> ~/.bashrc
$ . ~/.bashrc
```
#### To Verify
```sh
$ ls $KERN_DIR
```

* Click `VBOXADDITIONS_5.1.22_115126` icon
* Run Software
* Run
* Return

```sh
$ sudo usermod -aG vboxsf demo-user
```

### Restart VM

## In VirtualBox Menu Bar
* Devices - Shared Clipboard - Bidirectional
* Devices - Drag and Drop - Bidirectional

## On VM: Install Software

### Install GIT & Maven
```sh
$ sudo yum install -y git maven
```

### Install Ansible
```sh
$ sudo rpm -Uvh https://dl.fedoraproject.org/pub/epel/epel-release-latest-7.noarch.rpm
$ sudo yum install -y ansible
```

### Install Docker
```sh
$ curl -fsSL https://get.docker.com/ | sh
$ sudo chkconfig docker on
$ sudo usermod -aG docker demo-user
```

### Restart VM

### Back On VM

### Install Docker Compose
```sh
$ sudo -i
$ curl -L https://github.com/docker/compose/releases/download/1.13.0/docker-compose-`uname -s`-`uname -m` > /usr/local/bin/docker-compose
$ chmod +x /usr/local/bin/docker-compose
$ exit
```

### Restart VM

### Back On VM

#### To Verify
```sh
$ java -version
$ git --version
$ mvn --version
$ ansible --version
$ docker version
$ docker-compse version
```

### Get Code
```sh
$ cd ~
$ git clone https://github.com/MichaelFitzurka/IoT_Demo_AllInOne.git
```

### Copy over software from Laptop to `~/IoT_Demo_AllInOne/software`
* [JBoss BPM Suite v6.4.0](https://access.redhat.com/jbossnetwork/restricted/softwareDownload.html?softwareId=48441)
* [JBoss DataGrid v7.0.0 Server](https://access.redhat.com/jbossnetwork/restricted/softwareDownload.html?softwareId=45511)
* [JBoss Data Grid v7.0.0 Library Module for JBoss EAP v7](https://access.redhat.com/jbossnetwork/restricted/softwareDownload.html?softwareId=45561)
* [JBoss Developer Studio v10.4.0.GA Standalone](https://tools.jboss.org/downloads/devstudio/neon/10.4.0.GA.html)
* [JBoss EAP v7.0.0](https://access.redhat.com/jbossnetwork/restricted/softwareDownload.html?softwareId=43891)
* [JBoss EAP v7.0.3 Patch](https://access.redhat.com/jbossnetwork/restricted/softwareDownload.html?softwareId=47721)
* [JBoss Fuse on Karaf v6.3.0](https://access.redhat.com/jbossnetwork/restricted/softwareDownload.html?softwareId=46901)

### (Optional) Install DevStudio
```sh
$ cd ~/IoT_Demo_AllInOne/software
$ java -jar devstudio-10.4.0.GA-installer-standalone.jar
```
Install DevStudio with Fuse and BP&R Development Plugins

### Build with Ansible
```sh
$ cd ~/IoT_Demo_AllInOne/Ansible
$ ansible-playbook demo_root.yml --ask-become-pass
```
Wait ... Wait ... Wait ...

### (Optional) DevStudio
* Import Git Project after 4 git clones in Ansible.

# To Run

## VM: Terminal
```sh
$ cd ~/IoT_Demo_AllInOne
$ docker-compose up -d
$ docker-compose logs -f
```

## VM: Firefox
* Open BPM Suite:
	* http://localhost:8080/business-central/kie-wb.jsp
	* UserName: psteiner
	* Password: change12_me
* Open Fuse (Data Center):
	* http://localhost:8181/hawtio/login
	* UserName: admin
	* Password: change12_me

### Configure BPM Suite
* Authoring
* Artifact Repository
	* Upload `~IoT_Demo_AllInOne/IoT_Demo_Datacenter/bpm/LightWorkItemHandler/target/lightWorkItemHandler-1.0.0-SNAPSHOT.jar`
---
* Authoring
* Project Authoring
* Business Processes
* IoTEvent
	* Remove second process
	* Link 1st Process to 3rd 
	* Save 
	* Add "comment"
---
* Authoring
* Project Authoring
* Open Project Editor
	* Group Artifact Version
	* Set Version to 1.0 (not 1.6)
	* Save
	* Add "comment"
	* Save
	* Override (if needed)
	---
	* Build
	* Build & Deploy
	* Override (if needed)

## Pi: 1st Terminal
```sh
$ top
```

## Pi: 2nd Terminal
```sh
$ cd ~/IoT_Demo_AllInOne
$ docker-compose up -d
$ docker-compose logs -f
```

## Pi: 1st Terminal
* Wait until top show low waiting.

## VM: Firefox
* Open Fuse (Smart Gateway):
	* http://10.42.0.2:8181/hawtio/login
	* UserName: admin
	* Password: change12_me

## Sensor(s)
* Plugin ESP8622 chip to power.

## Laptop
To show IPs connected to WiFi:
```sh
$ arp
```
wl3ps0 connections that are not the SmartGateway (10.42.0.2) are the ESP8266 WiFi chips.

## On VM: Firefox
* Open ESP8622 Status Report:
	* http://10.42.0.37/  (based on arp identified IP addresses from the arp command above)

# To Shutdown

## Sensor(s)
* Disconnect power.

## Pi
```sh
$ docker-compose down
$ docker system prune
```
## VM
```sh
$ docker-compose down
$ docker system prune
```
