package edu.kit.trufflehog;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.RectangleBuilder;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextBuilder;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class Demo extends Application {

    @Override
    public void start(Stage primaryStage) {
        WebView webView = new WebView();
        webView.getEngine().load("http://www.google.com");
        StackPane root = new StackPane();
        root.getChildren().addAll(webView, getOverlay());
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }

    private Pane getOverlay() {
        StackPane p = new StackPane();
        Rectangle r = RectangleBuilder.create()
                .height(100).width(100)
                .arcHeight(40).arcWidth(40)
                .stroke(Color.RED)
                .fill(Color.web("red", 0.1))
                .build();

        Text txt=TextBuilder.create().text("Overlay")
                .font(Font.font("Arial", FontWeight.BOLD, 18))
                .fill(Color.BLUE)
                .build();
        p.getChildren().addAll(r, txt);
        return p;
    }

    public static void main(String[] args) {
        launch(args);
    }
}