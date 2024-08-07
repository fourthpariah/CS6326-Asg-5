/* This java file controls the custom view used to play the game. This
is where all shapes are created, drawn, destroyed or removed. This view
uses many variables inside it's owning GameActivity class. When enough shapes
are pressed, this class will call the method in GameActivity to end the game.
Written by Joseth Holman (jdh160630) at University of Texas at Dallas
starting April 3rd, 2020
 */
package com.example.jdh160630asg5;

import android.app.Activity;
import android.content.Context;
import android.graphics.*;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class GameView extends View {

    Paint[] gameColors = new Paint[7];

    Handler h = new Handler();
    int frameRate = 1000;

    public GameView(Context context) {
        super(context);
    }

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public GameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    // this method is called inside GameActivity to provide the game view with it's
    // array of paint colors
    void setColors() {
        GameActivity theGame = (GameActivity)getContext();
        for (int colorIndex = 0; colorIndex < theGame.colors.length; colorIndex++) {
            gameColors[colorIndex] = new Paint(Color.parseColor(theGame.colors[colorIndex]));
            gameColors[colorIndex].setColor(Color.parseColor(theGame.colors[colorIndex]));
        }
    }

    // if this runs at the beginning of the game and no shapes exist yet,
    // onDraw() will create all of them
    // after that, this method will draw all "living" shapes
    @Override
    protected void onDraw(Canvas canvas) {

        GameActivity theGame = (GameActivity)getContext();

        Random randNum = new Random();
        int viewWidth = this.getWidth();
        int viewHeight = this.getHeight();

        if (theGame.shapes.size() == 0) {
            for (int shapeIndex = 0; shapeIndex < theGame.numShapes; shapeIndex++) {
                newShape();
            }
        }

        if (theGame.shapes.size() > 0) {

            //this for loop (re)draws all currently existing shapes in the arraylist.
            for (int shapeIndex = 0; shapeIndex < theGame.shapes.size(); shapeIndex++) {
                int shapeX, shapeY, shapeColor, shapeSize, shapeLifeTime;
                shapeX = theGame.shapes.get(shapeIndex).X;
                shapeY = theGame.shapes.get(shapeIndex).Y;
                shapeColor = theGame.shapes.get(shapeIndex).color;
                shapeLifeTime = theGame.shapes.get(shapeIndex).lifetime;

                Paint shapePaint = gameColors[shapeColor];

                Shape temp = theGame.shapes.get(shapeIndex);
                if (theGame.shapes.get(shapeIndex).type == 0) {
                    temp = theGame.shapes.get(shapeIndex);
                    ShapeCircle circle = (ShapeCircle)temp;

                    shapeSize = circle.radius;
                    canvas.drawCircle(shapeX, shapeY, shapeSize, shapePaint);
                }
                else if (theGame.shapes.get(shapeIndex).type == 1) {
                    temp = theGame.shapes.get(shapeIndex);
                    ShapeSquare square = (ShapeSquare)temp;

                    shapeSize = square.sideLength;
                    canvas.drawRect(shapeX, shapeY, shapeX+shapeSize, shapeY+shapeSize, shapePaint);
                }
            }
        }

        h.postDelayed(r, frameRate);
    }

    // this method creates a single new shape with a random color, position, size, and lifespan
    public void newShape() {
        GameActivity theGame = (GameActivity) getContext();
        Random randNum = new Random();

        int viewWidth = this.getWidth();
        int viewHeight = this.getHeight();

        int randColor = randNum.nextInt(7);
        int randShape = randNum.nextInt(2);

        // the lifespan of any shape should be within 3-7 seconds
        int randLife = randNum.nextInt(4001) + 3000;

        // the randomized shape size must be converted from dp to pixels so it can be
        // drawn on different screen sizes
        int sizedp = randNum.nextInt(25) + 32;
        int sizepx = (int) (sizedp * getContext().getResources().getDisplayMetrics().density);

        Shape newShape;
        if (randShape == 0) {
            newShape = new ShapeCircle(sizepx);
        } else {
            newShape = new ShapeSquare(sizepx);
        }

        newShape.type = randShape;
        newShape.color = randColor;
        newShape.lifespan = randLife;

        // these are the random X and Y positions for the shape
        // the bounds ensure that the shapes are always completely inside the view and don't fall
        // off the screen
        newShape.X = randNum.nextInt(viewWidth - Math.round(sizepx) * 2) + Math.round(sizepx);
        newShape.Y = randNum.nextInt(viewHeight - Math.round(sizepx) * 2) + Math.round(sizepx);


        // NOTE:
        // THIS BLOCK WAS MADE TO IMPLEMENT ANTI-COLLISION
        // THE SHAPE ANTI-COLLISION ALGORITHMS SHOULD BE SOUND
        // BUT WHEN I TRIED IMPLEMENTING THIS IT WOULD FREEZE THE PROGRAM
        /*
        if (theGame.shapes.size() > 0) {
            for (int shapeIndex = 0; shapeIndex < theGame.shapes.size(); shapeIndex++) {

                if (theGame.shapes.get(shapeIndex).intersects(newShape)) {
                    while (theGame.shapes.get(shapeIndex).intersects(newShape)) {
                        newShape.X = randNum.nextInt(viewWidth - Math.round(sizepx) * 2) + Math.round(sizepx);
                        newShape.Y = randNum.nextInt(viewHeight - Math.round(sizepx) * 2) + Math.round(sizepx);
                    }
                }
            }
        }
        */

        // the creation date is generated for the new shape and it is added to the shape array
        newShape.createDate = new Date();
        theGame.shapes.add(newShape);

        //NOTE: This was made before the animation was added, and I commented it out
        //because it was causing the animation to speed up with every press
        //invalidate();
    }

    // this method runs each time the user presses the screen
    // it determines whether the point of contact is within any shape
    // and whether that shape is a correct one
    // if it is eiher, it will be removed from the shapes arraylist and will not be
    // redrawn
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        GameActivity theGame = (GameActivity) getContext();

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int touchX = (int)event.getX();
            int touchY = (int)event.getY();

            for (int shapeIndex = 0; shapeIndex < theGame.shapes.size(); shapeIndex++) {
                Shape currentShape = theGame.shapes.get(shapeIndex);

                boolean inside = false;

                int shapeX = currentShape.X;
                int shapeY = currentShape.Y;

                // this is the distance between the point of touch and the XY position of the shape
                // for circles, this is the center
                // for squares, this is the top-left vertex
                double distance = Math.sqrt((touchY - shapeY)*(touchY - shapeY) + (touchX - shapeX)*(touchX - shapeX));

                // for circles, if that distance is smaller than the radius, then it is inside the shape
                if (currentShape.type == 0) {
                    int shapeRad = ((ShapeCircle)currentShape).radius;

                    if (distance < shapeRad) {
                        inside = true;
                    }
                }
                else {
                    // for squares, it has to be between the left and right edges and the top and bottom edges
                    int shapeLen = ((ShapeSquare)currentShape).sideLength;

                    if ((touchX > shapeX && touchX < shapeX+shapeLen) && (touchY > shapeY && touchY < shapeY+shapeLen)) {
                        inside = true;
                    }

                }

                if (inside) {
                    // if the shape is of correct type and color...
                    if (currentShape.type == theGame.correctShape && currentShape.color == theGame.correctColor) {
                        theGame.numCorrect++; // the number of correct shapes pressed wil be incremented

                        // it's destruction date will be saved
                        // and the creation date will be subtracted from it
                        // to get its complete time of life in milliseconds
                        currentShape.destroyDate = new Date();
                        theGame.runningMilliseconds += currentShape.destroyDate.getTime() - currentShape.createDate.getTime();

                        // if enough correct shapes have been pressed, the game will be ended
                        if (theGame.numCorrect == theGame.numWin) {
                            theGame.gameEnd();
                        }

                    }

                    //regardless of whether the shape is a correct one, it will be removed from the game
                    // and a new shape will be created to replace it
                    theGame.shapes.remove(shapeIndex);
                    newShape();
                    return true;
                }

            }

        }
        return false;

    }


    // this handles the animation for shapes
    // if a shapes lifetime exceeds it's lifespan, it is removed from the shapes arraylist
    // and from the view
    private Runnable r = new Runnable() {
        @Override
        public void run()
        {
            GameActivity theGame = (GameActivity) getContext();
            for (int shapeIndex = 0; shapeIndex < theGame.shapes.size(); shapeIndex++) {
                theGame.shapes.get(shapeIndex).lifetime += 1000;

                if (theGame.shapes.get(shapeIndex).lifetime >= theGame.shapes.get(shapeIndex).lifespan) {

                    // if a correct shape disappears, it's lifespan is added to the user time score
                    // and increments the number of missed shapes
                    if (theGame.shapes.get(shapeIndex).type == theGame.correctShape && theGame.shapes.get(shapeIndex).color == theGame.correctColor) {
                        theGame.runningMilliseconds += theGame.shapes.get(shapeIndex).lifetime;
                        theGame.numLost++;
                    }
                    theGame.shapes.remove(shapeIndex);
                    invalidate();
                }
            }

            invalidate();
        }
    };
}
