package com.chess.GUI;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static com.chess.GUI.ChessGUI.*;

public class GameHistoryPane extends ScrollPane {

    final MoveLog moveLog = new MoveLog();
    private final ArrayList<HBox> moveBoxes;
    private final VBox moveGUI;
    ChessGUI gui;
    int currentMoveIndex;
    int numberOfMoves;

    public GameHistoryPane(VBox verticalBox, ChessGUI GUI) {

        moveBoxes = new ArrayList<>();
        moveGUI = verticalBox;
        this.gui = GUI;
        this.currentMoveIndex = 0;
        this.numberOfMoves = 0;
    }

    public static class MoveLog {

        private final ArrayList<Move> moves;

        MoveLog() {
            this.moves = new ArrayList<>();
        }

        public ArrayList<Move> getMoves() {
            return this.moves;
        }

        void addMove(final Move move) {
            this.moves.add(move);
        }

        public int size() {
            return this.moves.size();
        }

        public void clear() {
            this.moves.clear();
        }

    }

    public void addLastToGUI() {

        int lastIndex = moveLog.moves.size() - 1;
        this.currentMoveIndex = lastIndex;
        this.numberOfMoves = lastIndex;
        Label moveLabel;
        String moveString = moveLog.moves.get(lastIndex).toString() + calculateCheckAndCheckMateHash(this.gui.boardPane.chessBoard);
        if (lastIndex % 2 == 0) {
            HBox moveBox = new HBox();

            String moveNum = String.valueOf(moveBoxes.size() + 1);

            moveLabel = new Label(moveNum + ". " + moveString);
            moveLabel.setFont(new Font("Arial", 15));
            moveLabel.setPadding(new Insets(0,0,0,10));
            moveLabel.setMinSize(45,30);

            moveBox.getChildren().addAll(moveLabel);
            moveGUI.getChildren().add(moveBox);
            moveBoxes.add(moveBox);
        }
        else {
            moveLabel = new Label(moveString);
            moveLabel.setFont(new Font("Arial", 15));
            moveLabel.setPadding(new Insets(0,0,0,10));
            moveLabel.setMinSize(45,30);

            moveBoxes.get(moveBoxes.size()-1).getChildren().add(moveLabel);
        }
        moveLabel.setOnMouseClicked(event ->
        {
            changeHighlightedMove(lastIndex);
            gui.boardPane.drawBoardUpToSpecificMove(lastIndex);
        });


    }

    private void changeHighlightedMove(int newMoveIndex) {

        int currentBoxIndex;
        int newBoxIndex;

        if (newMoveIndex % 2 == 0) {
            newBoxIndex = newMoveIndex / 2;
            moveBoxes.get(newBoxIndex).getChildren().get(0).setStyle("-fx-background-color: green;");

        }

        else {
            newBoxIndex = (newMoveIndex) / 2;
            moveBoxes.get(newBoxIndex).getChildren().get(1).setStyle("-fx-background-color: green;");
        }

        if (this.currentMoveIndex % 2 == 0) {
            currentBoxIndex = this.currentMoveIndex / 2;
            moveBoxes.get(currentBoxIndex).getChildren().get(0).setStyle(null);
        }
        else {
            currentBoxIndex = this.currentMoveIndex / 2;
            moveBoxes.get(currentBoxIndex).getChildren().get(1).setStyle(null);
        }
        this.currentMoveIndex = newMoveIndex;

    }

    private String calculateCheckAndCheckMateHash(final Board board) {
        if (board.currentPlayer().isInCheckMate()) {
            return "#";
        }
        else if (board.currentPlayer().isInCheck()) {
            return "+";
        }
        return "";
    }

}
