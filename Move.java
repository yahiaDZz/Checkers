/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkers;

import static checkers.Checkers.moves;
import static checkers.Checkers.pieces;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import static checkers.Checkers.root;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 *
 * @author DELL
 */
public class Move {

    private Piece pieceToMove;
    private List<Piece> piecesToCapture = new ArrayList<>();
    private Button moveButton;
    private int offset = Checkers.squareLength / 2;

    public Move() {

    }

    public Move(double x, double y, Piece pieceToMove) {
        this.pieceToMove = pieceToMove;
        moveButton = new Button();
        moveButton.setStyle(Checkers.greenStyle);
        moveButton.setTranslateX(x + offset);
        moveButton.setTranslateY(y + offset);
        moveButton.setScaleX(Piece.size / 2);
        moveButton.setScaleY(Piece.size / 2);
        moveButton.setOpacity(.3d);
        moveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                pieceToMove.setI(Checkers.pxToI(moveButton.getTranslateY()));
                pieceToMove.setJ(Checkers.pxToJ(moveButton.getTranslateX()));
            if (pieceToMove.getI() == 0 && pieceToMove.getTeam().equals(Team.BLACK) || pieceToMove.getI() == 7 && pieceToMove.getTeam().equals(Team.RED)) {
              //  System.out.println(pieceToMove.getI()+" "+pieceToMove.getJ());
                pieceToMove.isQueened = true;
            }
                Timeline tl = new Timeline();
                tl.setAutoReverse(false);
                tl.getKeyFrames().add(new KeyFrame(Duration.millis(1000),
                        new KeyValue(pieceToMove.getButton().translateXProperty(), moveButton.getTranslateX()),
                        new KeyValue(pieceToMove.getButton().translateYProperty(), moveButton.getTranslateY())));
                for (Piece p : piecesToCapture) {
                    tl.getKeyFrames().add(new KeyFrame(Duration.millis(1000),
                            new KeyValue(p.getButton().opacityProperty(), 0)));
                    PauseTransition pt = new PauseTransition(Duration.millis(1000));
                    pt.setOnFinished(ee
                            -> {
                        root.getChildren().remove(p.getButton());
                        root.getChildren().remove(moveButton);
                    });
                    pt.play();
                    pieces.remove(p);
                }
                tl.play();
                tl.setOnFinished(x->{
                Checkers.SetupScore(Checkers.root);
                });
                for (Move m : moves) {
                    root.getChildren().remove(m.getButton());
                    m.clearPiecesToCapture();
                }
                for (Piece pi : pieces) {
                    pi.clearMoves();
                }
                moves.clear();
                Checkers.stack.clear();
                Checkers.updateSquares();
                Checkers.blackturn = ! Checkers.blackturn;
            }
        });
    }

    public Button getButton() {
        return this.moveButton;
    }

    public void clearPiecesToCapture() {
        piecesToCapture.clear();
    }

    public void addPieceToCapture(Piece p) {
        piecesToCapture.add(p);
    }

    public void setPiecesToCapture(List<Piece> list) {
        this.piecesToCapture = list;
    }

    public List<Piece> getPiecesToCapture() {
        return this.piecesToCapture;
    }

    public void setPieceToMove(Piece p) {
        this.pieceToMove = p;
    }

    public Piece getPieceToMove() {
        return this.pieceToMove;
    }
}
