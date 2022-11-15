/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkers;

import java.util.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author DELL
 */
public class Checkers extends Application {

    static int frameWidth = 700, frameHeight = 700;
    static int squareLength = 70;
    static Color board_color_1 = Color.rgb(192, 192, 192);
    static Color board_color_2 = Color.rgb(0, 0, 0);
    static Color backgroundColor = Color.BROWN;
    static double board_pos_x = 50, board_pos_y = 50;
    static double board_background_offset = 40;
    static Square[][] squares = new Square[8][8];
    static List<Piece> pieces = new ArrayList<>();
    static List<Move> moves = new ArrayList<>();
    Rectangle prev;
    String blackStyle = "-fx-background-radius: 5px; "
            + "-fx-background-color: #844747;"
            + "-fx-min-width: 5px; "
            + "-fx-border-radius:50%;"
            + "-fx-min-height: 5px; "
            + "-fx-max-width: 5px; "
            + "-fx-max-height: 5px;";
    String redStyle
            = "-fx-background-radius: 5em; "
            + "-fx-background-color: #5c9eff;"
            + "-fx-min-width: 5px; "
            + "-fx-border-radius:50%;"
            + "-fx-min-height:5px; "
            + "-fx-max-width: 5px; "
            + "-fx-max-height: 5px;";
    static String greenStyle
            = "-fx-background-radius: 5em; "
            + "-fx-background-color: #00E705;"
            + "-fx-min-width: 5px; "
            + "-fx-border-radius:50%;"
            + "-fx-min-height:5px; "
            + "-fx-max-width: 5px; "
            + "-fx-max-height: 5px;";
    static Group root = new Group();
    static Stack<Piece> stack = new Stack<>();
    static boolean blackturn = true;
    static Text text = new Text();
    @Override
    public void start(Stage primaryStage) {
        SetupCheckerBoard(root);
        SetupPieces(root);
        SetupScore(root);
        root.getChildren().add(text);
        for (Button btn : Piece.PIECESBUTTONS) {
            btn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {SetupScore(root);
                    for (Move m : moves) {
                        root.getChildren().remove(m.getButton());
                        m.clearPiecesToCapture();
                    }
                    for (Piece p : pieces) {
                        p.clearMoves();
                    }
                    moves.clear();
                    Piece piece = getPieceThatHasButton(btn);
                    Rectangle rect = new Rectangle(jToPx(piece.getJ()), iToPx(piece.getI()), squareLength, squareLength);
                    rect.setFill(Color.YELLOW);
                    rect.setOpacity(.5d);
                    if (prev != null) {
                        root.getChildren().remove(prev);
                    }
                    if (!(blackturn ^ piece.getTeam() == Team.BLACK)) {
                        ApplyLogic(piece, root);
                    }
                    root.getChildren().add(rect);
                    prev = rect;
                }
            });
        }
        Scene scene = new Scene(root, frameWidth, frameHeight);
        primaryStage.setTitle("Sexy Checkers");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    static void SetupScore(Group root) {
        int b = 0, r = 0;
        for (Piece p : pieces) {
            if (p.getTeam() == Team.BLACK) {
                b++;
            } else {
                r++;
            }
        }
        Font f = new Font("Arial", 25);
        text.setText((blackturn ? "Black to play\n" : "Blue to play\n") + "Black : " + b + " Blue :" + r);
        text.setFont(f);
        text.setTranslateX(frameWidth/2);
        text.setTranslateY(frameHeight - 40);
        text.setFill(Color.WHITE);
    }

    void ApplyLogic(Piece p, Group root) {
        Recursive(p, p.getTeam(), squares[p.getI()][p.getJ()], root);
    }

    void Recursive(Piece p, Team t, Square current, Group root) {
        if (current == null) {
            return;
        }
        Square fg = FG(t, current);
        Piece g = getPieceInSquare(fg);
        Square fd = FD(t, current);
        Piece d = getPieceInSquare(fd);

        if (g != null) {
            if (g.getTeam() != t && fg != null && !fg.isEmpty && FG(t, fg) != null && FG(t, fg).isEmpty && !(current.isEmpty && fg.isEmpty)) {
                stack.add(g);
                CreateCapture(p, t, current, fg, 'G', root);
                Recursive(p, t, FG(t, fg), root);
            }
        }
        if (d != null) {
            if (d.getTeam() != t && fd != null && !fd.isEmpty && FD(t, fd) != null && FD(t, fd).isEmpty && !(current.isEmpty && fd.isEmpty)) {
                stack.add(d);
                CreateCapture(p, t, current, fd, 'D', root);
                Recursive(p, t, FD(t, fd), root);
            }
        }
        if (p.isQueened) {
            Team t2 = t.equals(Team.BLACK) ? Team.RED : Team.BLACK;
            Square fg2 = FG(t2, current);
            Piece g2 = getPieceInSquare(fg2);
            Square fd2 = FD(t2, current);
            Piece d2 = getPieceInSquare(fd2);
            if (g2 != null) {
                if (g2.getTeam() != t && fg2 != null && !fg2.isEmpty && FG(t2, fg2) != null && FG(t2, fg2).isEmpty && !(current.isEmpty && fg2.isEmpty)) {
                    stack.add(g2);
                    CreateCapture(p, t2, current, fg2, 'G', root);
                    Recursive(p, t2, FG(t2, fg2), root);
                }
            }
            if (d2 != null) {
                if (d2.getTeam() != t && fd2 != null && !fd2.isEmpty && FD(t2, fd2) != null && FD(t2, fd2).isEmpty && !(current.isEmpty && fd2.isEmpty)) {
                    stack.add(d2);
                    CreateCapture(p, t2, current, fd2, 'D', root);
                    Recursive(p, t2, FD(t2, fd2), root);
                }
            }
            if (!current.isEmpty && fg2 != null && fg2.isEmpty) {
                CreateMove(p, t2, current, 'G', root);
            }
            if (!current.isEmpty && fd2 != null && fd2.isEmpty) {
                CreateMove(p, t2, current, 'D', root);
            }
        }
        if (!stack.isEmpty()) {
            stack.pop();
        }
        if (!current.isEmpty && fg != null && fg.isEmpty) {
            CreateMove(p, t, current, 'G', root);
        }
        if (!current.isEmpty && fd != null && fd.isEmpty) {
            CreateMove(p, t, current, 'D', root);
        }
    }

    Piece getPieceInSquare(Square s) {
        if (s != null) {
            for (Piece p : pieces) {
                if (p.getX() == s.getX() && p.getY() == s.getY()) {
                    return p;
                }
            }
        }
        return null;
    }

    void CreateMove(Piece p, Team t, Square c, char dir, Group root) {
        Move move;
        if (t.equals(Team.BLACK)) {
            if (dir == 'G') {
                move = new Move(jToPx(pxToJ(c.getX()) - 1), iToPx(pxToI(c.getY()) - 1), p);
            } else {
                move = new Move(jToPx(pxToJ(c.getX()) + 1), iToPx(pxToI(c.getY()) - 1), p);
            }
        } else {
            if (dir == 'D') {
                move = new Move(jToPx(pxToJ(c.getX()) + 1), iToPx(pxToI(c.getY()) + 1), p);
            } else {
                move = new Move(jToPx(pxToJ(c.getX()) - 1), iToPx(pxToI(c.getY()) + 1), p);
            }
        }
        p.addMove(move);
        moves.add(move);
        root.getChildren().add(move.getButton());
    }

    void CreateCapture(Piece pi, Team t, Square p, Square c, char dir, Group root) {
        Move move;
        if (t.equals(Team.BLACK)) {
            if (dir == 'D') {
                move = new Move(jToPx(pxToJ(c.getX()) + 1), iToPx(pxToI(c.getY()) - 1), pi);

            } else {
                move = new Move(jToPx(pxToJ(c.getX()) - 1), iToPx(pxToI(c.getY()) - 1), pi);

            }
        } else {
            if (dir == 'D') {
                move = new Move(jToPx(pxToJ(c.getX()) + 1), iToPx(pxToI(c.getY()) + 1), pi);

            } else {
                move = new Move(jToPx(pxToJ(c.getX()) - 1), iToPx(pxToI(c.getY()) + 1), pi);
            }
        }
        for (Piece piece : stack) {
            move.addPieceToCapture(piece);
        }
        pi.addMove(move);
        moves.add(move);
        root.getChildren().add(move.getButton());
    }

    Square FG(Team t, Square s) {
        try {
            if (t.equals(Team.BLACK)) {
                return squares[pxToI(s.getY()) - 1][pxToJ(s.getX()) - 1];
            } else {
                return squares[pxToI(s.getY()) + 1][pxToJ(s.getX()) - 1];
            }
        } catch (Exception ex) {
            return null;
        }

    }

    Square FD(Team t, Square s) {
        try {
            if (t.equals(Team.BLACK)) {
                return squares[pxToI(s.getY()) - 1][pxToJ(s.getX()) + 1];
            } else {
                return squares[pxToI(s.getY()) + 1][pxToJ(s.getX()) + 1];
            }
        } catch (Exception ex) {
            return null;
        }

    }

    Piece getPieceWithCoordinates(int i, int j) {
        for (Piece p : pieces) {
            if (p.getI() == i && p.getJ() == j) {
                return p;
            }
        }
        return null;
    }

    static void debugSquares() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print((squares[i][j].isEmpty ? 0 : 1) + " ");
            }
            System.out.println();
        }
    }

    Piece getPieceThatHasButton(Button btn
    ) {
        for (Piece p : pieces) {
            if (p.getButton().equals(btn)) {
                return p;
            }
        }
        return null;
    }

    void SetupPieces(Group root) {
        for (int i = 0; i < 8; i++) {
            if (i % 2 == 0) {
                Piece black1 = new Piece(Team.BLACK, 5, i);
                Piece black2 = new Piece(Team.BLACK, 7, i);
                Piece red = new Piece(Team.RED, 1, i);
                pieces.add(black1);
                pieces.add(black2);
                pieces.add(red);
            } else {
                Piece red1 = new Piece(Team.RED, 0, i);
                Piece red2 = new Piece(Team.RED, 2, i);
                Piece black = new Piece(Team.BLACK, 6, i);
                pieces.add(red1);
                pieces.add(red2);
                pieces.add(black);
            }
        }
        updateSquares();
        for (int i = 0; i < pieces.size(); i++) {
            Button pieceButton = new Button();
            pieceButton.setTranslateX(pieces.get(i).getX() + squareLength / 2 - 1);
            pieceButton.setTranslateY(pieces.get(i).getY() + squareLength / 2 - 1);
            if (pieces.get(i).getTeam() == Team.BLACK) {
                pieceButton.setStyle(blackStyle);

            } else {
                pieceButton.setStyle(redStyle);
            }
            Image img;
            if (pieces.get(i).getTeam() == Team.BLACK) {
                img = new Image(getClass().getResource("brown.png").toString(), true);
            } else {
                img = new Image(getClass().getResource("blue.png").toString(), true);
            }
            ImageView view = new ImageView(img);
            view.setFitHeight(5);
            view.setPreserveRatio(true);
            pieceButton.setGraphic(view);
            pieceButton.setScaleX(10);
            pieceButton.setScaleY(10);
            pieces.get(i).setButton(pieceButton);
            root.getChildren().add(pieceButton);
        }
    }

    static void updateSquares() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                squares[i][j].isEmpty = true;
            }
        }
        for (Piece p : pieces) {
            squares[p.getI()][p.getJ()].isEmpty = false;
        }
    }

    void SetupCheckerBoard(Group root) {
        Rectangle background = new Rectangle(0, 0, frameWidth, frameHeight);
        background.setFill(backgroundColor);
        root.getChildren().add(background);
        Rectangle board_background = new Rectangle(board_pos_x - board_background_offset / 2, board_pos_y - board_background_offset / 2, squareLength * 8 + board_background_offset, squareLength * 8 + board_background_offset);
        board_background.setFill(Color.BLACK);
        root.getChildren().add(board_background);
        Square.setLength(squareLength);
        Color w = board_color_1;
        Color b = board_color_2;
        boolean white = true;
        for (double i = board_pos_x; i < board_pos_x + Square.LENGTH * 8; i += Square.LENGTH) {
            for (double j = board_pos_y; j < board_pos_y + 8 * Square.LENGTH; j += Square.LENGTH) {
                Square sq = new Square(i, j, white ? w : b);
                Rectangle rect = new Rectangle(i, j, Square.LENGTH, Square.LENGTH);
                rect.setFill(white ? w : b);
                root.getChildren().add(rect);
                white = !white;
                squares[pxToJ(j)][pxToI(i)] = sq;
            }
            white = !white;
        }
    }

    static double iToPx(int i) {
        return board_pos_y + i * squareLength;
    }

    static double jToPx(int j) {
        return board_pos_x + j * squareLength;
    }

    static int pxToI(double px) {
        return (int) ((px - board_pos_y) / squareLength);
    }

    static int pxToJ(double px) {
        return (int) ((px - board_pos_x) / squareLength);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
