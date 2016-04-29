package edu.kit.trufflehog.view.jung.visualization;

import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

class RectangleSelectionController {

    private Rectangle rectangle;
    private Parent root;
    private double nodeX;
    private double nodeY;
    private double firstX;
    private double firstY;
    private double secondX;
    private double secondY;
    private EventHandler<MouseEvent> mouseDraggedEventHandler;
    private EventHandler<MouseEvent> mousePressedHandler;
    private EventHandler<MouseEvent> mouseReleasedHandler;

    public RectangleSelectionController() {
        //
    }

/*    public void apply(Parent root,
            Rectangle rect,
            EventHandlerGroup<MouseEvent> draggedEvtHandler,
            EventHandlerGroup<MouseEvent> pressedEvtHandler,
            EventHandlerGroup<MouseEvent> releasedEvtHandler) {
        init(root, rect);
        draggedEvtHandler.addHandler(mouseDraggedEventHandler);
        pressedEvtHandler.addHandler(mousePressedHandler);
        releasedEvtHandler.addHandler(mouseReleasedHandler);
    }*/

    private void init(final Parent root, final Rectangle rect) {

        this.rectangle = rect;

        this.root = root;

 /*       root.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                WindowUtil.getDefaultClipboard().unselectAll();
            }
        });*/

        mouseDraggedEventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                performDrag(root, event);
                event.consume();
            }
        };

        mousePressedHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                performDragBegin(root, event);
                event.consume();
            }
        };

        mouseReleasedHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                performDragEnd(root, event);
                event.consume();
            }
        };
    }

    public void performDrag(
            Parent root, MouseEvent event) {

        final double parentScaleX = root.
                localToSceneTransformProperty().getValue().getMxx();
        final double parentScaleY = root.
                localToSceneTransformProperty().getValue().getMyy();

        secondX = event.getSceneX();
        secondY = event.getSceneY();

        firstX = Math.max(firstX, 0);
        firstY = Math.max(firstY, 0);

        secondX = Math.max(secondX, 0);
        secondY = Math.max(secondY, 0);

        double x = Math.min(firstX, secondX);
        double y = Math.min(firstY, secondY);

        double width = Math.abs(secondX - firstX);
        double height = Math.abs(secondY - firstY);

        rectangle.setX(x / parentScaleX);
        rectangle.setY(y / parentScaleY);
        rectangle.setWidth(width / parentScaleX);
        rectangle.setHeight(height / parentScaleY);
    }

    public void performDragBegin(
            Parent root, MouseEvent event) {

        if (rectangle.getParent() != null) {
            return;
        }

        // record the current mouse X and Y position on Node
        firstX = event.getSceneX();
        firstY = event.getSceneY();

       // NodeUtil.addToParent(root, rectangle);

        rectangle.setWidth(0);
        rectangle.setHeight(0);

        rectangle.setX(firstX);
        rectangle.setY(firstY);

        rectangle.toFront();

    }

    public void performDragEnd(
            Parent root, MouseEvent event) {

/*
        NodeUtil.removeFromParent(rectangle);

        for (Node n : root.getChildrenUnmodifiable()) {
            if (rectangle.intersects(n.getBoundsInParent()) && n instanceof SelectableNode) {
                WindowUtil.getDefaultClipboard().select((SelectableNode) n, true);
            }
        }
*/

    }
}