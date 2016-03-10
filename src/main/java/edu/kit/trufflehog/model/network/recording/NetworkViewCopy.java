package edu.kit.trufflehog.model.network.recording;

import edu.kit.trufflehog.model.network.IAddress;

import java.awt.geom.Point2D;
import java.util.Map;

/**
 * Created by jan on 07.03.16.
 */
public class NetworkViewCopy {
    private final long viewTime;
    private final int maxThroughput;
    private final int maxConnectionSize;
    private final Map<IAddress,Point2D> locationMap;

    public NetworkViewCopy(Map<IAddress, Point2D> locationMap, int maxConnectionSize,
                           int maxThroughput, long viewTime) {

        this.viewTime = viewTime;
        this.locationMap = locationMap;
        this.maxConnectionSize = maxConnectionSize;
        this.maxThroughput = maxThroughput;
    }

    public Map<IAddress,Point2D> getLocationMap() {
        return locationMap;
    }

    public int getMaxConnectionSize() {
        return maxConnectionSize;
    }

    public int getMaxThroughput() {
        return maxThroughput;
    }

    public long getViewTime() {
        return viewTime;
        
    }
}
