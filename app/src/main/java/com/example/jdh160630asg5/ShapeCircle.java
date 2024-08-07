/* This java file controls the circle subclass for the shape superclass.
it holds the size for each circle object (as it's radius) and implements
the intersection method for circles
Written by Joseth Holman (jdh160630) at University of Texas at Dallas
starting April 2nd, 2020
 */
package com.example.jdh160630asg5;

public class ShapeCircle extends Shape {

    int radius;

    ShapeCircle(int r) {
        this.type = CIRCLE;
        radius = r;
    }

    // this checks if a shape at a certain distance from the
    // current shape intersects with it
    public boolean circleIntersects(Shape s, double dist) {


        // if both shapes are circles, then the sum of both radii
        // cannot be greater than the distance between them
        if (s.type == CIRCLE) {
            if (dist < this.radius + ((ShapeCircle) s).radius) {
                return true;
            }
        }
        else {
            // if the income shape is a square, then the center point of the circle
            // should be farther than the top-left corner of the rectangle
            if (dist > this.X || dist > this.Y) {
                return true;
            }

        }

        return false;
    }

}
