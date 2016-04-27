# TruffleHog Manual
The manual is loosely seperated into three sections. *Setup* describes the initial process of starting the programm. *Basics* covers basic usage such as navigating on the network graph and understanding the different statistic overlays. *Filters* more deeply explains how the filter menu works and what kind of filters you can apply to the graph how. 

## Setup
1. Start the customized version of Snort you can obtain [here](https://github.com/404). It contains a PROFINET-preprocessor as well as the necessary classes for IPC with TruffleHog. 
2. Start TruffleHog as you would start any other Java-program by either running `java -jar TruffleHog.jar` if you downloaded the Jar or `java Main` if you cloned the source code. 
3. Click *Connect* in the lower left corner of the main TruffleHog window. 
4. You are good to go. As soon as Snort intercepts packages TruffleHog will start building a graph of the PROFINET-devices and their connections.

## Basics

## Filters
