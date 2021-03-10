package com.chess.GUI;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.chess.engine.player.MoveTransition;
import com.google.common.collect.Lists;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class ChessGUI {

    public final BoardPane boardPane;
    public GameHistoryPane gameHistoryPane;

    ChessGUI() {

        this.boardPane = new BoardPane(this);
        this.boardPane.drawBoard();
        VBox vertical = new VBox();
        this.gameHistoryPane = new GameHistoryPane(vertical, this);
        this.gameHistoryPane.setMinSize(120, 500);
        this.gameHistoryPane.setContent(vertical);

    }

    public enum BoardDirection {
        NORMAL {
            @Override
            List<TilePane> traverse(final List<TilePane> boardTiles) {
                return boardTiles;
            }
            @Override
            BoardDirection opposite() {
                return FLIPPED;
            }
        },
        FLIPPED {
            @Override
            List<TilePane> traverse(final List<TilePane> boardTiles) {
                return Lists.reverse(boardTiles);
            }
            @Override
            BoardDirection opposite() {
                return NORMAL;
            }
        };

        abstract List<TilePane> traverse(final List<TilePane> boardTiles);
        abstract BoardDirection opposite();

    }

}
