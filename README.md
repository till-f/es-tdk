Introduction
============

The Embedded Software Test Development Kit (see www.es-tdk.org) eases the implementation of test for
embedded software. It focuses on C-code and can be used like a unit test framework, but also supports
advanced test scenarios for embedded systems including timing analysis and Hardware-in-the-Loop (HiL) 
testing. The ES TDK comes with a new test programming language called ETSpec providing a consistent
view on heterogeneous test environments.

The tested software is executed on the real device. Test execution is controlled and the behavior 
is observed using the debug and trace interfaces of the processor hardware. In principle any 
microcontroller is supported (especially multicore controllers). Aggregation of data obtained from 
different targets and other sources (communication networks, analog signals, simulated environment) 
is also supported. The ES TDK is independent of any operating system and does not require any 
software support on the target device.

The first version of the language was called "PTSpec" and has been developed in the research project
"MoTTeM" (www.mottem.fzi.de) funded by the BMWI (German Federal Ministry for Economic Affairs). This
explains the occurence of the phrases "pts", "ptspec" and "mottem" in the source code.


Main Folders
------------

* workspace_main
  * The main development workspace for the ES TDK.
* workspace_libs
  * The development workspace for drivers / JNI adapters of access hardware.
* workspace_ptspec
  * The workspace to for debugging/testing of the ES TDK.
  * Contains several examples.


Primary Requirements
--------------------

* Java Development Kit (JDK) 1.8
* Eclipse Neon (4.6.0) with the following plugins
  
| Eclipse Plugin                          | Version |
|-----------------------------------------|---------|
| Eclipse Plug-in Development Environment | 3.12.0  |
| Eclipse Modeling Framework Xcore        | 1.4.0   |
| Xtext SDK                               | 2.10.0  |
| Graphiti SDK                            | 0.13.0  |
| C/C++ Development Tools SDK             | 9.1.0   |


Configure build environment
---------------------------

* Set `JAVA_HOME` environment variable to point to your JDK installation
  * e.g. C:\Program Files\Java\jdk1.8.0_25

* Start Eclipse and select "workspace_main" as workspace.
  * Do not use another folder, i.e. do not try to copy the projects into another folder.
  * You may want to create a shortcut for Eclipse that always opens the specific workspace.
    This is possible using the "-data" argument for eclipse, e.g.
    C:\eclipse\eclipse.exe -data "C:\es-tdk-repository\workspace_main"

* Ensure that installed JREs and default JRE are set correctly (version 1.8)
  * Main Menu -> Window -> Preferences -> Java -> Installed JREs

* Ensure that JDK Compliance is set correctly (version 1.8)
  * Main Menu -> Window -> Preferences -> Java -> Compiler
  
* Import projects
  * Right-click in Project/Package Explorer -> Import -> General -> Existing projects into Workspace
    -> "Next" -> "Browse..." -> "OK" -> (all projects should be selected) -> "Finish"
  * After loading you will find a large number of error in the code, this is expected. We have to
    trigger code generation first (see next step).


Build and run the ES TDK
------------------------

* Generate code for fzi.mottem.model
  * Right-click "fzi.mottem.model/Generate Model Code.launch" -> Run As -> Generate Model Code.
  * A shortcut can be found under "External Tools" in the Eclipse toolbar after the first run.

* Generate code for fzi.mottem.ptspec.dsl
  * Right-click "fzi.mottem.ptspec.dsl/Generate PTSpec.launch" -> Run As -> Generate PTSpec.
  * A shortcut can be found under "Run Configurations" in the Eclipse toolbar after the first run.
  
* DONE! Try out the plugin:
  * Right-click "fzi.mottem.model/MoTTeM Plugin.launch" -> Run As -> MoTTeM Plugin.


Example Applications
====================

The source code for two example applications (target code) is included in this project.
To build and run these examples specific hardware and software is required.

* The "raupe" example consists of two STM32F4 micro controllers communicating over CAN. They are
  assembled on a crawler vehicle. One STM32F4 controls the speed, the other measures the distance
  to an obstacle ahead of the vehicle using an IR sensor.
* The "ecumotor" example consists of MPC5643L micro controller with two processor cores. One core
  contains a realtime critical control algorithm for a brushless DC motor (reading hall sensors,
  commutation), the other core is responsible for CAN communication.

Even if the hardware is not available the source code of these example applications can be used to
get an idea of the ES TDK.


Requirements to build example applications
------------------------------------------

* For "raupe" example
  * Cygwin with specific packages (there is a minimal package available on the erika website)
  * GNUARM GCC
  * RT-Druid Eclipse Plugin
  * ERIKA RTOS build environment (RT-Druid) must be configured correctly (see online documentation)
* For "ecumotor" example
  * Cygwin with "make" und "binutils"
  * PowerPC GCC
  * Set environment variable `MOTTEM_PPC_GCC` to your PowerPC GCC installation using cygwin path style:
    e.g. /cygdrive/c/gcc/ppc/bin
* For building native API adapters (based on JNI)
  * Visual Studio 2015


Requirements to run example applications
----------------------------------------

* iSYSTEM WinIDEA 2012
  * isystem.connect API
* Vector Informatik CANoe
  * VXL API
* Keysight API
  * IOLibrary Suite
  * IVI Shared Components
  * AGInfiniiVision IVI + Matlab Driver
* GNUARM Eclipse Plugin and STLink tools (for Flashing/Debugging on ST Discovery Board)
