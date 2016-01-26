package edu.kit.ipd.trufflehog.model.graph;

import jung.algorithms.layout.Layout;

/**<p>
 * Interface to exchange graph drawing algorithms.
 * </p>
 */
public interface INetworkGraphLayout extends Layout {
	private GraphProxy graphProxy;
}
