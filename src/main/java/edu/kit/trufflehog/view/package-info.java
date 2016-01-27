/**
 * <p>
 *     Provides the classes necessary to create a GUI for the TruffleHog
 *     application.
 * </p>
 * <p>
 *     The view framework provides two abstractions:
 *     <ul>
 *     <li>
 *         The windows that will be placed on the screen. Every window class
 *         implements the
 *         {@link edu.kit.trufflehog.view.IWindowController} interface.
 *     </li>
 *     <li>
 *         The views that are places into the windows. Views are showing
 *         buttons, menus, toolbars etc. as well as the graph visualization.
 *         Every view controller class implements the
 *         {@link edu.kit.trufflehog.view.IViewController}
 *         interface. {@code IViewControllers}
 *     </li>
 *     </ul>
 * </p>
 * <p>
 *     As this application is developed with the javafx framework, every view
 *     implementation derives from a basic abstraction of javafx Components
 *     that can be places in javafx scenes.
 * </p>
 */
package edu.kit.trufflehog.view;
