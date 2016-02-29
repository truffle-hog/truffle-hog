package edu.kit.trufflehog.model.network.recording;

import edu.kit.trufflehog.model.network.INetworkViewPort;

/**
 * This interface inhabits the functionality for switching the currently active viewport. It work with delegation.
 * A NetworkView will get this PortSwitch as a Viewport, and can operate on it but has the capability to switch witout
 * noticing of the view.
 */
public interface INetworkViewPortSwitch extends INetworkViewPort {

    /**
     * Sets the active ViewPort of this viewport switch.
     * @param viewPort the view port to be set active
     */
    void setActiveViewPort(INetworkViewPort viewPort);

    /**
     * Returns the NetworkViewPort that is currently active.
     * @return The NetworkViewPort currently activated
     */
    INetworkViewPort getActiveViewPort();
}
