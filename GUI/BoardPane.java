package com.chess.GUI;


import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.chess.engine.player.MoveTransition;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;

public class BoardPane extends GridPane {

    private Tile destinationTile;
    private Piece humanMovedPiece;
    public Board chessBoard;
    ChessGUI gui;
    private Tile selectedTile;
    public int currentMoveIndex;

    final List<TilePane> boardTilePanes;

    public BoardPane(ChessGUI GUI) {

        this.chessBoard = Board.createStandardBoard();
        this.currentMoveIndex = 0;
        this.boardTilePanes = new ArrayList<>();
        this.gui = GUI;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {

                final TilePane tilePane = new TilePane(row*8 + col, this);
                GridPane.setConstraints(tilePane, col, row);
                this.boardTilePanes.add(tilePane);

                tilePane.setOnMouseClicked(event ->
                {
                    if (event.getButton() == MouseButton.PRIMARY && this.gui.gameHistoryPane.currentMoveIndex == this.gui.gameHistoryPane.numberOfMoves) {

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
                                this.gui.gameHistoryPane.moveLog.addMove(move);
                                this.gui.gameHistoryPane.addLastToGUI();
                                this.currentMoveIndex = this.gui.gameHistoryPane.moveLog.getMoves().size();
                            }
                            else {
                                selectedTile = chessBoard.getTile(tilePane.tileId);
                            }
                            destinationTile = null;
                            humanMovedPiece = null;

                        }
                        drawBoard();
                    }
                });

                getChildren().add(tilePane);
            }
        }
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

    public void drawBoardUpToSpecificMove(int moveIndex) {
        System.out.println("Drawing board up to move " + moveIndex);

        this.chessBoard = Board.createBoardFromMoves(this.gui.gameHistoryPane.moveLog.getMoves(), moveIndex);

        drawBoard();

    }

}