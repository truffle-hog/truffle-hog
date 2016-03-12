package edu.kit.trufflehog.presenter.viewbuilders;

import edu.kit.trufflehog.view.AnchorPaneViewController;
import javafx.scene.layout.AnchorPane;

/**
 * <p>
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
class StartViewBuilder implements IViewBuilder {
    public StartViewBuilder() {
    }

    @Override
    public AnchorPane buildView() {
        AnchorPane startView = new AnchorPaneViewController("start_view.fxml");

        AnchorPane.setBottomAnchor(startView, 0d);
        AnchorPane.setTopAnchor(startView, 0d);
        AnchorPane.setLeftAnchor(startView, 0d);
        AnchorPane.setRightAnchor(startView, 0d);

        return startView;
    }
}
