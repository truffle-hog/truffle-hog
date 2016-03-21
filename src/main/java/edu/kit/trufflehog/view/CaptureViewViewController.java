package edu.kit.trufflehog.view;

import edu.kit.trufflehog.command.usercommand.IUserCommand;
import edu.kit.trufflehog.model.configdata.ConfigData;
import edu.kit.trufflehog.model.filter.FilterInput;
import edu.kit.trufflehog.model.network.INetwork;
import edu.kit.trufflehog.model.network.INetworkViewPort;
import edu.kit.trufflehog.model.network.recording.INetworkDevice;
import edu.kit.trufflehog.util.IListener;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

/**
 * <p>
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
public class CaptureViewViewController extends LiveViewController {

    public CaptureViewViewController(String fxml,
                                     ConfigData configData,
                                     MultiViewManager multiViewManager,
                                     StackPane stackPane,
                                     INetworkViewPort viewPort,
                                     Scene scene,
                                     IUserCommand<FilterInput> updateFilterCommand,
                                     IListener<IUserCommand> userCommandIListener,
                                     INetworkDevice networkDevice,
                                     INetwork liveNetwork) {

        super(fxml, configData, multiViewManager, stackPane, viewPort, scene, updateFilterCommand, userCommandIListener,
                networkDevice, liveNetwork);
    }

    @Override
    protected AnchorPane addCaptureOverlay(INetworkDevice networkDevice, INetwork liveNetwork) {
        final AnchorPane captureView = new PlaybackMenuViewController("playback_menu_overlay.fxml");
        AnchorPane.setBottomAnchor(captureView, 60d);
        AnchorPane.setLeftAnchor(captureView, 18d);
        captureView.setVisible(false);
        return captureView;
    }

    @Override
    public CaptureViewViewController clone() {
        return new CaptureViewViewController("live_view.fxml", configData, multiViewManager, stackPane,
                viewPort, scene, updateFilterCommand, userCommandListener, networkDevice, liveNetwork);
    }
}
