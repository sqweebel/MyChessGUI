package com.chess.GUI;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ChessGame extends Application {

    private ChessGUI gui;

    @Override
    public void start(Stage primaryStage) throws Exception {

        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(50,100,50,50));
        gui = new ChessGUI();
        pane.setCenter(gui.boardPane);
        pane.setRight(gui.gameHistoryPane);
        Scene scene = new Scene(pane);

        primaryStage.setTitle("Chess GUI");
        primaryStage.setScene(scene);

        primaryStage.show();

    }
}
