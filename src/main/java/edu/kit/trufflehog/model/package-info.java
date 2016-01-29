/**
 * <p>
 *     Provides structures to contain and manage the data of TruffleHog, such as graphs, settings and filters.
 *     Inspired by the model-view-presenter pattern, this data is manipulated by the service and observed by the view.
 *     E.g. TruffleReceiver gets a Truffle, sets up a command which is carried out by the CommandExecutor (member of
 *     the service) adding data to the graph thus changing its appearance. The graph notifies the view about the
 *     changes and the view redraws the graph.
 * </p>
 */
package edu.kit.trufflehog.model;