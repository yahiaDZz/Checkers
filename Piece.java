/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkers;

import static checkers.Checkers.board_pos_x;
import static checkers.Checkers.board_pos_y;
import static checkers.Checkers.squareLength;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Button;

/**
 *
 * @author DELL
 */
enum Team {
    BLACK, RED
}

public class Piece extends Square {

    private Team team; // 0 for black and 1 for red 
    private int i, j;
    private Button btn;
    static List<Button> PIECESBUTTONS = new ArrayList<>();
    private List<Move> moves = new ArrayList<>();
    public  boolean isQueened = false;

    public List<Move> getMoves() {
        return moves;
    }

    public void setMoves(List<Move> moves) {
        this.moves = moves;
    }

    public void clearMoves() {
        moves.clear();
    }

    public void addMove(Move m) {
        moves.add(m);
    }

    public void removeMove(Move m) {
        moves.remove(m);
    }
    public static int size = 10;

    public Piece() {

    }

    public Piece(int i, int j) {
        this.i = i;
        this.j = j;
        this.setY(board_pos_y + i * squareLength);
        this.setX(board_pos_x + j * squareLength);
    }

    public Piece(Team team, int i, int j) {
        this(i, j);
        this.team = team;
    }

    public Team getTeam() {
        return team;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
        this.setY(board_pos_y + i * squareLength);
    }

    public int getJ() {
        return j;
    }

    public void setJ(int j) {
        this.j = j;
        this.setX(board_pos_x + j * squareLength);
    }

    public void setButton(Button btn) {
        this.btn = btn;
        PIECESBUTTONS.add(btn);
    }

    public Button getButton() {
        return this.btn;
    }
}
