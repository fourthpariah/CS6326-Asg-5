/* This java file controls the shape superclass used by the game activity
and view. It holds all the common variables to both circles and squares
and also (is meant to) implements the intersection methods for both
shape types to check for collision

Written by Joseth Holman (jdh160630) at University of Texas at Dallas
starting April 2nd, 2020
 */
package com.example.jdh160630asg5;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Shape {

    // this helps identify whether a shape is a circle or a square
    int type;
    static int CIRCLE = 0;
    static int SQUARE = 1;


    // all shapes must have these variables
    int X, Y;
    int color;
    int lifetime; // this is how long the shape has existed
    int lifespan; // a shape cannot exist longer than this
    Date createDate, destroyDate;

    // this method takes a shape, determines the positions of both incoming shape s and the current
    // shape and calls the specific intersection method for each shape type
    public boolean intersects(Shape s) {

        double distance = 0;

        if (s.Y > this.Y || s.X > this.X) {
            distance = Math.sqrt((s.Y - this.Y)*(s.Y - this.Y) + (s.X - this.X)*(s.X - this.X));
        }
        else if (this.Y > s.Y || this.X > s.X) {
            distance = Math.sqrt((this.Y - s.Y)*(this.Y - s.Y) + (this.X - s.X)*(this.X - s.X));
        }

        if (this.type == CIRCLE) {
            ((ShapeCircle)this).circleIntersects(s, distance);

        } else if (this.type == SQUARE) {
            ((ShapeSquare)this).squareIntersects(s, distance);
        }

        return true;
    }


}
