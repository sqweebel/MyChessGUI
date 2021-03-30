package com.chess.engine;

import com.chess.engine.board.Move;

import java.util.ArrayList;

public class Game {

    private final ArrayList<Move> moveList;


    public Game() {

        this.moveList = new ArrayList<>();

    }

    public void addMove(Move newMove) {
        this.moveList.add(newMove);

    }


    public ArrayList<Move> getMoves()  {
        return this.moveList;
    }
}
