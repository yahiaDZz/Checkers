/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkers;

import javafx.scene.paint.Color;

/**
 *
 * @author DELL
 */
public class Square {

    static int LENGTH;
    private double x, y;
    private Color color;
    public boolean isEmpty = true;

    public Square() {

    }

    public Square(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Square(double x, double y, Color color) {
        this(x,y);
        this.color = color;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public static int getLength() {
        return LENGTH;
    }

    public static void setLength(int LENGTH) {
        Square.LENGTH = LENGTH;
    }


}
