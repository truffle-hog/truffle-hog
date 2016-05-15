# TruffleHog Manual
The manual is loosely seperated into three sections. *Setup* describes the initial process of starting the programm. *Basics* covers basic usage such as navigating on the network graph and understanding the different statistic overlays. *Filters* more deeply explains how the filter menu works and what kind of filters you can apply to the graph how. 

## Setup
1. Start the customized version of Snort you can obtain [here](https://github.com/404). It contains a PROFINET-preprocessor as well as the necessary classes for IPC with TruffleHog. 
2. Start TruffleHog as you would start any other Java-program by either running `java -jar TruffleHog.jar` if you downloaded the Jar or `java Main` if you cloned the source code. Consider that you might need to have *Java by Oracle*, not *OpenJDK* installed. 
3. Click *Connect* in the lower left corner of the main TruffleHog window. If no IPC server is found, the program will show artificial demo traffic.
4. You are good to go. As soon as Snort intercepts packages TruffleHog will start building a graph of the PROFINET-devices and their connections.

## Basics

### Things you see

- In the **center** you can see the graph with nodes representing network participants, edges representing established connections between the participants and MAC-addresses or hostnames associated to one node.
- In the **lower left corner** there is a toolbar providing the most basic functionality of the program. It includes (left to right) starting inter-process communication with Snort (a.k.a. connect) and the filter menu (see section below).
- In the **lower right corner** there is an overlay showing pretty self-explanatory basic information about the current network graph.
- In the **upper right corner (as long at least one node is selected)** one finds another overlay containing information on the *currently selected* nodes. Attention! This information is far more advanced and aimed at professionals looking for a more in-depth view on what is going on on the ether.

### Navigation
Very easy! Just drag and drop nodes and use your mouse wheel to move around the whole graph. Alternatively, use the right mouse button to move the view around.

### Key combinations
- `CTRL+X` algorithmically positions the nodes of the graph according to the current zoom level to *look nice*. If it does not, try to zoom out a bit, there might not be enough place on your current zoom level to work on.
- `CTRL+A` selects all nodes (see section Filters below).
- `CTRL+Q` closes the program properly just like clicking the `X` on the window decoration would.

## Filters

Filters define a set of nodes either by MAC-address, IP-address or Regex (applied to the name of nodes). Selected nodes can then be colored and/or marked as "authorized". Filters are also assigned a priority. The filter with the highest priority gets executed last and therefor mask ones with lower priority.

The list of filters is pretty self-explanatory. `+` to create a new one, `-` to delete the highlighted filter and then pen to edit it. The button on the very left adds a new filter matching only the currently selected nodes.

The easiest way to create a filter to a visible set of nodes is selecting them in the graph, then opening the filters menu and clicking the button showing little nodes on the very left. A new filter is opened in the filter creation window then and the selected nodes are defined by MAC-address. Assign priority, color and if they should be marked as authorized and you are good to go.

To create more complex filters you can also define nodes by regex (e.g. station-* to match to all clients with a name starting with "station-") or by fixed IP-address or subnet (192.168.1.1/24).

Marking nodes as authorized with filters serves mainly organisational purposes. It helps to also color them green or any other color that symbolizes "ok" for you. Contrary, clients from foreign subnets could be colored red.