package com.chess.GUI;

import com.chess.engine.Game;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.chess.engine.player.MoveTransition;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;

public class GameDisplay extends HBox {

    Game game;
    BoardPane boardPane;
    GameHistoryPane gameHistoryPane;

    public GameDisplay(Game game) {
        this.game = game;
        this.boardPane = new BoardPane();
        this.gameHistoryPane = new GameHistoryPane();
        this.getChildren().add(boardPane);
        this.getChildren().add(gameHistoryPane);
    }

    public class BoardPane extends GridPane {

        private Tile destinationTile;
        private Piece humanMovedPiece;
        public Board chessBoard;
        private Tile selectedTile;

        private final List<TilePane> boardTilePanes;

        public BoardPane() {
            this.chessBoard = Board.createStandardBoard();
            this.boardTilePanes = new ArrayList<>();
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {

                    final TilePane tilePane = new TilePane(row*8 + col, this);
                    GridPane.setConstraints(tilePane, col, row);
                    this.boardTilePanes.add(tilePane);

                    tilePane.setOnMouseClicked(event ->
                    {
                        tilePaneClickedFunction(event, tilePane);

                    });
                    getChildren().add(tilePane);
                }
            }
        }

        private void tilePaneClickedFunction(MouseEvent event, TilePane tilePane) {
            if (event.getButton() == MouseButton.PRIMARY) {
                if (selectedTile == null) {
                    selectedTile = chessBoard.getTile(tilePane.tileId);
                    humanMovedPiece = selectedTile.getPiece();
                    if (humanMovedPiece == null)
                    {
                        selectedTile = null;
                    }
                }
                else
                {
                    destinationTile = chessBoard.getTile(tilePane.tileId);
                    final Move move = Move.MoveFactory.createMove(chessBoard, selectedTile.getTileCoordinate(), destinationTile.getTileCoordinate());
                    final MoveTransition transition = chessBoard.currentPlayer().makeMove(move);
                    if (transition.getMoveStatus().isDone()) {
                        chessBoard = transition.getTransitionBoard();
                        selectedTile = null;
                        if (game != null) {
                            game.addMove(move);
                            gameHistoryPane.addLastToGUI();
                        }
                    }
                    else {
                        selectedTile = chessBoard.getTile(tilePane.tileId);
                    }
                    destinationTile = null;
                    humanMovedPiece = null;

                }
                drawBoard();
            }
        }


        public void drawBoardUpToSpecificMove(int moveIndex) {
            System.out.println("Drawing board up to move " + moveIndex);

            this.chessBoard = game.getMoves().get(moveIndex).getBoard();

            drawBoard();

        }


        public void drawBoard() {
            getChildren().clear();
            for (final TilePane tilePane : boardTilePanes) {
                tilePane.drawTile(chessBoard);
                getChildren().add(tilePane);
            }
            if (selectedTile != null)
                this.boardTilePanes.get(selectedTile.getTileCoordinate()).highlightSquare();
        }


    }



    public class GameHistoryPane extends ScrollPane {

        private final ArrayList<HBox> moveBoxes;
        int currentMoveIndex;
        int numberOfMoves;
        private final VBox moveDisplay;

        public GameHistoryPane() {
            moveBoxes = new ArrayList<>();
            this.currentMoveIndex = 0;
            this.numberOfMoves = 0;
            this.moveDisplay = new VBox();
            this.setMinSize(120, 500);
            this.setContent(this.moveDisplay);

        }

        public void addLastToGUI() {

            int lastIndex = game.getMoves().size() - 1;
            this.currentMoveIndex = lastIndex;
            this.numberOfMoves = lastIndex;
            Label moveLabel;
            String moveString = game.getMoves().get(lastIndex).toString() + calculateCheckAndCheckMateHash(boardPane.chessBoard);
            if (lastIndex % 2 == 0) {
                HBox moveBox = new HBox();

                String moveNum = String.valueOf(moveBoxes.size() + 1);

                moveLabel = new Label(moveNum + ". " + moveString);
                moveLabel.setFont(new Font("Arial", 15));
                moveLabel.setPadding(new Insets(0,0,0,10));
                moveLabel.setMinSize(45,30);

                moveBox.getChildren().addAll(moveLabel);
                moveDisplay.getChildren().add(moveBox);
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
                boardPane.drawBoardUpToSpecificMove(lastIndex);
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





}
