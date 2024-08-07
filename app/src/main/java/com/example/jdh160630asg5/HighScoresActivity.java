/* This java file controls the main activity of the high score program.
It fills the high score table when the program begins and allows the
user to start another activity where they can input their own high score
to add to the table on this main activity.
Written by Joseth Holman (jdh160630) at University of Texas at Dallas
starting April 5th, 2020
 */
package com.example.jdh160630asg5;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class HighScoresActivity extends AppCompatActivity {

    // Making this into a class variable so that the same
    // high score arraylist can be used at any point in MainActivity
    ScoreFileIO scoreIo = new ScoreFileIO();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);

        // When the app starts, this will also initialize the high scores list
        scoreIo.initializeFile(this);

        // and make a copy of that list on MainActivity
        ArrayList<String> highscores = scoreIo.getScores();

        // I ended up using a tablelayout to make the high score table
        // The instructions say to make the list scrollable, but I think
        // using a tableLayout made it so that the list will take up the same
        // space on any screen? (I may be wrong)
        TableLayout table = (TableLayout)findViewById(R.id.tableLayout3);

        Bundle extras = getIntent().getExtras();
        String result;

        if (extras != null) {
            result = extras.getString("result");
            scoreIo.setScore(result, this);
        }

        // these for loops will get the components of each score and
        // fill the high score table row by row with each component
        for (int rowIndex=1; rowIndex < 13; rowIndex++) {
            String score = highscores.get(rowIndex-1);

            String[] scoreParts = score.split("\\s+");

            TableRow row = (TableRow)table.getChildAt(rowIndex);
            for (int columnIndex=0; columnIndex < 3; columnIndex++) {
                TextView text = (TextView)row.getChildAt(columnIndex);

                if (columnIndex == 0) {
                    text.setText(scoreParts[0]);
                }
                else if (columnIndex == 1) {
                    text.setText(scoreParts[1]);
                }
                else if (columnIndex == 2) {
                    //some changes
                    // instead of treating the score as an int, it is treated as a long (milliseconds)
                    // and converted into mm:ss format
                    long longTime = Long.parseLong(scoreParts[2]);
                    SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
                    Date time = new Date(longTime);
                    text.setText(sdf.format(time));
                }

            }

        }

    }

    public static final int REQUEST_CODE = 1;

    // This method will run if the Add Score button is pressed
    // it will start the Add New Score activity
    public void btnReturnToMain(View view) {
        Intent returnIntent = new Intent(HighScoresActivity.this, MainActivity.class);
        returnIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        HighScoresActivity.this.startActivity(returnIntent);
        //setResult(Activity.RESULT_CANCELED, returnIntent);
        //finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // after the AddNewScoreActivity has returned to MainActivity
        if (requestCode == REQUEST_CODE) {

            // if the user successfully entered a new score
            // then that score will be added to the high scores
            // and the MainActivity table will be changed if necessary
            if (resultCode == Activity.RESULT_OK) {
                // get the user input from AddNewScoreActivity...
                String result = data.getStringExtra("result");

                //...and pass it to setScore() to add it to the high scores
                scoreIo.setScore(result, this);

                // reusing this block from onCreate to again rewrite the high score
                // table on MainActivity
                // NOTE TO SELF: COME BACK LATER AND MAKE THIS BLOCK INTO A SEPARATE METHOD
                ArrayList<String> highscores = scoreIo.getScores();
                TableLayout table = (TableLayout)findViewById(R.id.tableLayout3);
                for (int rowIndex=1; rowIndex < 13; rowIndex++) {
                    String score = highscores.get(rowIndex-1);

                    String[] scoreParts = score.split("\\s+");

                    TableRow row = (TableRow)table.getChildAt(rowIndex);
                    for (int columnIndex=0; columnIndex < 3; columnIndex++) {
                        TextView text = (TextView)row.getChildAt(columnIndex);

                        if (columnIndex == 0) {
                            text.setText(scoreParts[0]);
                        }
                        else if (columnIndex == 1) {
                            text.setText(scoreParts[1]);
                        }
                        else if (columnIndex == 2) {
                            long longTime = Long.parseLong(scoreParts[2]);
                            SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
                            Date time = new Date(longTime);
                            text.setText(sdf.format(time));
                        }

                    }

                }

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                // if not then it will return to MainActivity with nothing special happening
                String result = data.getStringExtra("canceled");
            }
        }
    }
}
