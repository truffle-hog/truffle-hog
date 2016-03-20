package edu.kit.trufflehog.interaction;

/**
 * <p>
 *     Interactions that can be performed on the main toolbar.
 * </p>
 */
public enum ProtocolControlInteraction implements IInteraction {

	/** Try to connect to a running Snort process. **/
	CONNECT,
	/** Disconnect from the Snort process connected to. **/
	DISCONNECT,
	/** Refresh the graph layout. **/
	REFRESH_LAYOUT,
	/** Start networkreplay the currently monitored network communication. **/
	RECORD_START,
	/** Stop networkreplay the currently monitored network communication. **/
	RECORD_STOP;
}
