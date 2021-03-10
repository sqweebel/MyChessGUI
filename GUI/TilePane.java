package com.chess.GUI;


import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.io.InputStream;
import java.net.URL;

public class TilePane extends Pane {

   private static String defaultPieceImagesPath = "C:\\Users\\colin\\Documents\\BlackWidow-Chess-master\\art\\chess.com\\";

    final int tileId;
    BoardPane boardPane;

    TilePane(final int tileId, BoardPane boardPane) {
        this.tileId = tileId;
        this.boardPane = boardPane;
        setPrefSize(75,75);

    }

    public void drawTile(final Board board) {
        assignSquareColor();
        assignTilePieceIcon(board);

    }

    public void assignTilePieceIcon(final Board board) {
        getChildren().clear();
        if (board.getTile(this.tileId).isTileOccupied()) {

            final Image image = new Image("/com/chess/resources/" + board.getTile(this.tileId).getPiece().getPieceAlliance().toString().substring(0, 1) +
                    board.getTile(this.tileId).getPiece().toString() + ".png");

            image.errorProperty().addListener(
                    (obs,ov,nv) -> System.out.println("error=" + nv)
            );

            getChildren().add(new ImageView(image));

        }
    }

    public void assignSquareColor() {


        if (BoardUtils.EIGHTH_RANK[this.tileId] ||
                BoardUtils.SIXTH_RANK[this.tileId] ||
                BoardUtils.FOURTH_RANK[this.tileId] ||
                BoardUtils.SECOND_RANK[this.tileId]) {
            if (this.tileId % 2 == 0) {
                setBackground(new Background(new BackgroundFill(Color.PEACHPUFF, CornerRadii.EMPTY, Insets.EMPTY)));
            } else {
                setBackground(new Background(new BackgroundFill(Color.SADDLEBROWN, CornerRadii.EMPTY, Insets.EMPTY)));
            }
        } else if (BoardUtils.SEVENTH_RANK[this.tileId] ||
                BoardUtils.FIFTH_RANK[this.tileId] ||
                BoardUtils.THIRD_RANK[this.tileId] ||
                BoardUtils.FIRST_RANK[this.tileId]) {
            if (this.tileId % 2 != 0) {
                setBackground(new Background(new BackgroundFill(Color.PEACHPUFF, CornerRadii.EMPTY, Insets.EMPTY)));
            } else {
                setBackground(new Background(new BackgroundFill(Color.SADDLEBROWN, CornerRadii.EMPTY, Insets.EMPTY)));
            }
        }
    }

    public void highlightSquare() {

        setBackground(new Background(new BackgroundFill(Color.SEAGREEN, CornerRadii.EMPTY, Insets.EMPTY)));

    }



}
