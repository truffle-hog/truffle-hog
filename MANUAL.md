# TruffleHog Manual
The manual is loosely seperated into three sections. *Setup* describes the initial process of starting the programm. *Basics* covers basic usage such as navigating on the network graph and understanding the different statistic overlays. *Filters* more deeply explains how the filter menu works and what kind of filters you can apply to the graph how. 

## Setup
1. Start the customized version of Snort you can obtain [here](https://github.com/404). It contains a PROFINET-preprocessor as well as the necessary classes for IPC with TruffleHog. 
2. Start TruffleHog as you would start any other Java-program by either running `java -jar TruffleHog.jar` if you downloaded the Jar or `java Main` if you cloned the source code. Consider that you might need to have *Java by Oracle*, not *OpenJDK* installed. 
3. Click *Connect* in the lower left corner of the main TruffleHog window. 
4. You are good to go. As soon as Snort intercepts packages TruffleHog will start building a graph of the PROFINET-devices and their connections.

## Basics

### Things you see

- In the **Center** you can see the graph with nodes representing network participants, edges representing established connections between the participants and MAC-addresses (as names) associated to one node.
- In the **Lower left corner** there is a toolbar providing the most basic functionality of the program. It includes (left to right) starting inter-process communication with Snort (a.k.a. connect) and the filter menu (see section below).
- In the **lower right corner** there is an overlay showing pretty self-explanatory basic information about the current network graph.
- In the **upper right corner (as long at least one node is selected)** one finds another overlay containing information on the *currently selected* nodes. Attention! This information is far more advanced and aimed at professionals looking for a more in-depth view on what is going on the ether.

### Navigation
Very easy! Just drag and drop nodes and use your mouse wheel to move around the whole graph.

### Key combinations
- `CTRL+X` algorithmically positions the nodes of the graph according to the current zoom level to *look nice*. If it does not, try to zoom out a bit, there might not be enough place on your current zoom level to work on.
- `CTRL+A` selects all nodes (see section Filters below).
- `CTRL+Q` closes the program properly just like clicking the `X` on the window decoration would.

## Filters
