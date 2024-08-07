/* This java file controls the square subclass for the shape superclass.
it holds the size for each square object (as the length of its sides)
and implements the intersection method for squares

Written by Joseth Holman (jdh160630) at University of Texas at Dallas
starting April 2nd, 2020
 */
package com.example.jdh160630asg5;

public class ShapeSquare extends Shape {

    int sideLength;

    ShapeSquare(int sl) {
        this.type = SQUARE;
        sideLength = sl;
    }

    // this checks if a shape at a certain distance from the
    // current shape intersects with it
    public boolean squareIntersects(Shape s, double dist) {


        // if the incoming shape is also a square
        // then there should be intersection if the distance
        // is greater than either the X or Y position of the
        // top-left vertex
        if (s.type == SQUARE) {
            if (this.X > dist || this.Y > dist) {
                return true;
            }

        }
        else {
            // if it is a circle
            // then the distance should be smaller than the sum of the circle's radius
            // and the X or Y positions of the square's top-left vertex
            if (dist < this.X + ((ShapeCircle)s).radius || dist < this.Y + ((ShapeCircle)s).radius) {
                return true;
            }
        }


        return false;
    }
}
