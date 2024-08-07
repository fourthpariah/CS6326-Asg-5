/* This java file controls the gameplay of the game. It holds the view
where the shapes are drawn and touched. This activity creates and maintains
many of the variables and conditions for the custom view and is capable of
ending the game, whether by user choice or by winning the game.
Written by Joseth Holman (jdh160630) at University of Texas at Dallas
starting March 28th, 2020
 */
package com.example.jdh160630asg5;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;


public class GameActivity extends AppCompatActivity {

    // these hold all the shapes and colors used to draw shapes
    ArrayList<Shape> shapes = new ArrayList<Shape>();
    String[] colors;

    int correctColor;
    int correctShape;
    int numShapes, numCircles, numSquares; // distribution of circles and squares among the total number of shapes
    int numWin = 10; // the number needed to win. This can't be random so the scores can be gauged equally
    int numCorrect = 0; //how many correct shapes the user pressed
    int numLost = 0; //how many correct shapes the user missed

    // this long is the running total for the user score
    // score is stored by the game in millisecond units and converted to mm:ss format
    // when printed
    long runningMilliseconds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Bundle extras = getIntent().getExtras();
        correctColor = extras.getInt("win color");
        correctShape = extras.getInt("win shape");

        colors = getResources().getStringArray(R.array.colors_array);

        Random randNum = new Random();

        // the total number of shapes is chosen randomly but is within the range of 6-12
        numShapes = randNum.nextInt(7) + 6;

        // whatever the winning shape type is, it should be a minority among the total shapes
        int distribution = randNum.nextInt((numShapes-3) + 1) + 3;
        if (correctShape == 0) {
            if (distribution > (numShapes)/2) {
                numSquares = distribution;
                numCircles = numShapes - distribution;
            }
            else {
                numSquares = numShapes - distribution;
                numCircles = distribution;
            }
        }
        else if (correctShape == 1) {
            if (distribution < (numShapes/2)) {
                numSquares = distribution;
                numCircles = numShapes - distribution;
            }
            else {
                numSquares = numShapes - distribution;
                numCircles = distribution;
            }
        }

        // or by chance there could be an even split of circles and squares
        if (distribution == (numShapes/2)) {
            numCircles = numSquares = distribution;
        }

        GameView playbox = (GameView)findViewById(R.id.gameView3);
        playbox.setColors(); // passing the colors array in this activity to the gameview class

    }

    // the user can choose to quit the game without finishing
    public void btnQuitGame(View view) {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

    // when the game is completed, the gameOver activity will be started and the
    // win variables (score and misses) will be passed to it
    public void gameEnd() {
        Intent gameOverIntent = new Intent(GameActivity.this, GameEndActivity.class);
        gameOverIntent.putExtra("milliseconds score", runningMilliseconds);
        gameOverIntent.putExtra("missed", numLost);
        GameActivity.this.startActivity(gameOverIntent);
    }

    public static final int REQUEST_CODE = 1;

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {

            if (resultCode == RESULT_CANCELED) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
                //String result = data.getStringExtra("canceled");
            }
            else if (resultCode == RESULT_OK) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        }
    }


}
