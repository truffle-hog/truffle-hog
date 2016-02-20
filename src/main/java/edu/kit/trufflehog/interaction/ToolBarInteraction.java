package edu.kit.trufflehog.interaction;

/**
 * <p>
 *     Interactions that can be performed on the main toolbar.
 * </p>
 */
public enum ToolBarInteraction implements IInteraction {

	/** Try to connect to a running Snort process. **/
	CONNECT_SNORT,
	/** Disconnect from the Snort process connected to. **/
	DISCONNECT_SNORT,
	/** Refresh the graph layout. **/
	REFRESH_LAYOUT,
	/** Start networkreplay the currently monitored network communication. **/
	RECORD_START,
	/** Stop networkreplay the currently monitored network communication. **/
	RECORD_STOP;
}
