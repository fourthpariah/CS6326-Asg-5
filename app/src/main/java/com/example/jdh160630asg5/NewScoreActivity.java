/* This java file controls the activity for adding new scores to the high score list
It essentially implements two methods for the activity, one activated by the user
pressing the cancel button and the other by pressing the save button.
In the former case, the activity will simply quit with a Canceled result and return
to the main activity.
If the latter, then the activity will validate and return the user input in the
three text lines back to the main activity.

Written by Joseth Holman (jdh160630) at University of Texas at Dallas
starting March 15th, 2020
 */

package com.example.jdh160630asg5;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewScoreActivity extends AppCompatActivity {

    long time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_score);

        // by default, the Date EditText in activity_add_new_score will be set
        // to the current date in mm/dd/yyyy format
        EditText dateBox = (EditText) findViewById(R.id.DateText);
        SimpleDateFormat curDate = new SimpleDateFormat("MM/dd/yyyy");
        dateBox.setText(curDate.format(new Date()));

        Bundle extras = getIntent().getExtras();
        time = extras.getLong("user score");
        TextView scoreBox = (TextView)findViewById(R.id.ScoreText);
        scoreBox.setText((new SimpleDateFormat("mm:ss")).format(new Date(time)));

    }


    /*
    // If the Cancel button in activity_add_new_score.xml is clicked by the user
    // the activity will end with RESULT_CANCELED
    public void btnCancel(View view) {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }
    */

    // If the Save button in activity_add_new_Score is clicked by the user
    // this method will check all three inputs to validate them
    // if valid, the activity will end with RESULT_OK and all three
    // user inputs will be returned to activity_main as well
    // if invalid, the save button will do nothing
    public void btnSave(View view) {
        EditText name = (EditText) findViewById(R.id.NameText);
        EditText date = (EditText) findViewById(R.id.DateText);

        // the player name is easy to validate, it simply cannot be empty
        if (!TextUtils.isEmpty(name.getText())) {

            // if the first input is valid, then the date must be validated
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

            Date input = null;
            Date today = new Date();

            // grab the date input from the EditText as a string
            String testUserDate = date.getText().toString();

            // first we check that the date itself is valid by parsing it
            // in our date format
            // if it is not valid, the button will do nothing
            try {
                input = dateFormat.parse(testUserDate);
            }
            catch (ParseException e){
                return;
            }

            // if the date format is valid, then we also must check that the date is valid
            // (i.e. is not a date in the future)
            if (!input.after(today)) {

                // if all three inputs are valid, the activity will end and all
                // three inputs will be returned to activity_main
                Intent intent = new Intent(NewScoreActivity.this, HighScoresActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                String entry = name.getText().toString() + "\t" + date.getText().toString()
                        + "\t" + Long.toString(time);
                intent.putExtra("result", entry);
                NewScoreActivity.this.startActivity(intent);

                //setResult(Activity.RESULT_OK, intent);
                //finish();

            }

        }

    }

}
