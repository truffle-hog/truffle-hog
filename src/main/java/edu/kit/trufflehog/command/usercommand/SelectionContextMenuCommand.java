package edu.kit.trufflehog.command.usercommand;

import edu.kit.trufflehog.model.network.graph.IConnection;
import edu.kit.trufflehog.model.network.graph.INode;
import edu.kit.trufflehog.view.jung.visualization.FXVisualizationViewer;
import javafx.application.Platform;
import javafx.scene.control.ContextMenu;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Hoehler on 28.04.2016.
 */
public class SelectionContextMenuCommand implements IUserCommand<Pair<Set<INode>, Set<IConnection>>> {
    private static final Logger logger = LogManager.getLogger();
    private Pair<Set<INode>, Set<IConnection>> selected = new ImmutablePair<>(new HashSet<>(), new HashSet<>());
    private final ContextMenu contextMenu;
    private final FXVisualizationViewer viewer;
    private double posX, posY;

    public SelectionContextMenuCommand(FXVisualizationViewer visualizationViewer, ContextMenu contextMenu) {
        if (contextMenu == null) throw new NullPointerException("ContextMenu must not be null!");
        if (visualizationViewer == null) throw new NullPointerException("VisualizationViewer must not be null!");
        this.contextMenu = contextMenu;
        this.viewer = visualizationViewer;
        this.posX = 0;
        this.posY = 0;
    }

    @Override
    synchronized
    public <S extends Pair<Set<INode>, Set<IConnection>>> void setSelection(S selection) {
        if (selection == null) throw new NullPointerException("Selection must not be null!");
        selected = selection;
        logger.debug(selected);
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    @Override
    public void execute() {
        if (selected.getLeft().isEmpty() && selected.getRight().isEmpty()) {
            logger.debug("nothing selected");
            Platform.runLater(() -> contextMenu.hide());
        } else if (selected.getLeft().isEmpty() && !selected.getRight().isEmpty()) {
            logger.debug("only edges selected");
        } else if (!selected.getLeft().isEmpty() && selected.getRight().isEmpty()) {
            logger.debug("only nodes selected");
            Platform.runLater(() -> contextMenu.show(viewer, posX, posY));
        } else if (!selected.getLeft().isEmpty() && !selected.getRight().isEmpty()) {
            logger.debug("nodes and edges selected");
        }
    }
}
