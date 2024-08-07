/* This java file controls the game-over activity for the game.
After the game has ended, the user will be allowed to either return
to the main menu or try to add their score to the high score list.

Written by Joseth Holman (jdh160630) at University of Texas at Dallas
starting April 13th, 2020
 */
package com.example.jdh160630asg5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GameEndActivity extends AppCompatActivity {

    long time;

    // when the activity is created, it will show the user their score
    // in terms of reaction speed and number of missed shapes
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_end);

        Bundle extras = getIntent().getExtras();
        time = extras.getLong("milliseconds score");
        int missed = extras.getInt("missed");

        TextView timeScore = (TextView)findViewById(R.id.userScore);
        TextView missedScore = (TextView)findViewById(R.id.userMissed);

        missedScore.setText("Correct Shapes Missed: " + missed);
        missedScore.setGravity(Gravity.CENTER_HORIZONTAL);

        timeScore.setText("Reaction Time: " + (new SimpleDateFormat("mm:ss")).format(new Date(time)));
        timeScore.setGravity(Gravity.CENTER_HORIZONTAL);
    }

    // the user can either try to add their score to the high score list
    public void btnAddScore(View view) {
        Intent addScoreIntent = new Intent(GameEndActivity.this, NewScoreActivity.class);
        addScoreIntent.putExtra("user score", time);
        GameEndActivity.this.startActivity(addScoreIntent);
    }


    // or go back to the game's main menu
    public void btnMainMenu(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


}
