/* This java file controls the activity for the main activity of the game. It
provides instructions for how to play the game as well as two buttons, one
for accessing the high scores and the other to start the game.

Written by Joseth Holman (jdh160630) at University of Texas at Dallas
starting March 28th, 2020
 */
package com.example.jdh160630asg5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.app.Activity;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    int randColor;
    int randShape;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // the rest of this code was demo/testing for shape/color randomization
        Random randNum = new Random();

        String[] colors = getResources().getStringArray(R.array.colors_array);
        String[] colorNames=  {"red", "orange", "yellow", "green", "blue", "purple", "white"};

        TextView instructions = (TextView)findViewById(R.id.textView2);
        TextView instructions2 = (TextView)findViewById(R.id.textView6);
        TextView instructions3 = (TextView)findViewById(R.id.textView8);

        // This color is the color that must be touched to win
        randColor = new Random().nextInt(colors.length);

        String color = colors[randColor];
        String colorName = colorNames[randColor];

        String shapetype;

        // This shape is the type that must be touched in order to win
        randShape = new Random().nextInt(2);

        if (randShape > 0) {
            shapetype = "squares";
        }
        else {
            shapetype = "circles";
        }

        instructions.setText("Only touch the " + colorName + " " + shapetype + "!");
        instructions.setTextColor(Color.parseColor(color));
        instructions.setBackgroundColor(Color.parseColor("#BF000000"));

        instructions2.setTextColor(Color.parseColor(color));
        instructions3.setTextColor(Color.parseColor(color));
    }

    public static final int REQUEST_CODE = 1;

    // when we begin the game, we will pass the winning shape and colors to the game activity
    public void btnBeginGame(View view) {
        Intent gameIntent = new Intent(this, GameActivity.class);
        gameIntent.putExtra("win color", randColor);
        gameIntent.putExtra("win shape", randShape);
        startActivityForResult(gameIntent, REQUEST_CODE);
    }


    public void btnSeeHighScores(View view) {
        Intent scoresIntent = new Intent(this, HighScoresActivity.class);
        startActivity(scoresIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {

            if (resultCode == RESULT_CANCELED) {
                String result = data.getStringExtra("canceled");
            }
            else if (resultCode == RESULT_OK) {

            }
        }
    }



}
