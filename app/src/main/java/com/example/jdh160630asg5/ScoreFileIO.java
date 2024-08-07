/* This java file controls the file I/O for the high score program.
It implement basic getter and setter methods for accessing the
highscores.txt file, as well as a sort method to ensure
that the high scores list is always sorted by score (or by
date in the case of duplicate scores)

Written by Joseth Holman (jdh160630) at University of Texas at Dallas
starting April 5th, 2020
 */

package com.example.jdh160630asg5;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class ScoreFileIO {

    // for the purpose of this program, all the contents of the highscores.txt file
    //  will be contained in an ArrayList. Each line (consisting of a name, date,
    //  and score) will be a single element in the arraylist
    private ArrayList<String> scores = new ArrayList<String>();

    // MainActivity will call this method to create and fill a file
    // inside internal storage with the contents of our highscores.txt
    // file inside assets.
    // it has to take a context C from MainActivity in order to access
    // the files
    // it will return the scores ArrayList to MainActivity to use to fill
    // the high scores table when the app opens
    public void initializeFile(Context c) {

        BufferedReader fileReader;

        try {
            // we are going to simultaneously read the scores from our assets file into our arraylist
            // and write them into a new identical file inside internal storage
            fileReader = new BufferedReader(new InputStreamReader(c.getAssets().open("highscores.txt")));
            FileOutputStream scoreWriter = c.openFileOutput("highscores.txt", Context.MODE_APPEND);
            String score = fileReader.readLine();
            while (score != null) {
                scoreWriter.write(score.getBytes());
                scoreWriter.write("\n".getBytes());
                scores.add(score);
                score = fileReader.readLine();
            }
            fileReader.close();
            scoreWriter.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        sortScores(c);
    }

    // This is the getter method for the highscore IO
    // it simple returns the score arraylist
    public ArrayList<String> getScores() {
        return scores;
    }

    // this is the setter method for the high score IO
    // it takes in the string of inputs given by the user
    // and the context from MainActivity
    // and adds the string to the arraylist before
    // calling sortScores() with the context
    public void setScore(String newScore, Context c) {
        scores.add(newScore);
        sortScores(c);
    }

    // This sortScores method will be called anytime
    // the highscores.txt file is changed (including at
    // the beginning of the app)
    // it take in the context c and sort the scores,
    // peeling off any scores if there are more than 12
    // and then rewriting the scores to the highscores.txt file
    public void sortScores(Context c) {


        String score;
        String[] scoreParts;

        // first the scores are sorting using a slightly modified selection sort algorithm
        // to sort them in descending order by the actual score number
        for (int scoreIndex = 0; scoreIndex < scores.size(); scoreIndex++) {
            score = scores.get(scoreIndex);
            scoreParts = score.split("\\s+");
            int maxNum = Integer.parseInt(scoreParts[2]);
            int maxIndex = scoreIndex;

            for (int unsortedIndex = scoreIndex+1; unsortedIndex < scores.size(); unsortedIndex++) {
                score = scores.get(unsortedIndex);
                scoreParts = score.split("\\s+");
                if (maxNum > Integer.parseInt(scoreParts[2])) {
                    maxNum = Integer.parseInt(scoreParts[2]);
                    maxIndex = unsortedIndex;
                }
            }

            Collections.swap(scores, scoreIndex, maxIndex);
            //String temp = scores.get(scoreIndex);
            //scores.set(scoreIndex, scores.get(maxIndex));
            //scores.set(maxIndex, temp);

        }

        // after the scores are sorted numerically, this will also check
        // if there are any duplicate scores and sort those by date
        // it helps that we already know that that the list is sorted
        // so we just need to check for adjacent duplicates
        for (int scoreIndex = 0; scoreIndex < scores.size(); scoreIndex++) {
            score = scores.get(scoreIndex);
            scoreParts = score.split("\\s+");

            Date scoreDate;
            try {
                scoreDate = new SimpleDateFormat("MM/dd/yyyy").parse(scoreParts[1]);
                int dateIndex = scoreIndex;
                int maxNum = Integer.parseInt(scoreParts[2]);

                if (scoreIndex < scores.size()-1) {
                    score = scores.get(dateIndex+1);
                    scoreParts = score.split("\\s+");
                    if (maxNum == Integer.parseInt(scoreParts[2])) {

                        try {
                            Date date2 = new SimpleDateFormat("MM/dd/yyyy").parse(scoreParts[1]);

                            if (date2.after(scoreDate)) {
                                Collections.swap(scores, dateIndex, dateIndex+1);
                            }
                        }
                        catch (ParseException e2) {
                            e2.printStackTrace();
                        }

                    }

                }

            }
            catch (ParseException e) {
                e.printStackTrace();
            }
        }


        // if there are more than 12 scores after sorting, the extraneous scores will
        // be removed
        while (scores.size() > 12) {
            scores.remove(scores.size()-1);
        }

        resetFile(c); // the highscores.txt file is reset
        // and then the new (if applicable) scores are rewritten back to the file
        try {
            FileOutputStream scoreWriter = c.openFileOutput("highscores.txt", Context.MODE_APPEND);
            for (int scoreIndex = 0; scoreIndex < scores.size(); scoreIndex++) {
                scoreWriter.write(scores.get(scoreIndex).getBytes());
                scoreWriter.write("\n".getBytes());
            }
            scoreWriter.close();

        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    // This method is as simple as it sounds
    // It simple wipes the highscores.txt file in
    // internal storage so when I can rewrite(append) to it
    // when the high score list is changed
    public void resetFile(Context c) {
        try {
            FileOutputStream scoreWriter = c.openFileOutput("highscores.txt", Context.MODE_PRIVATE);
            scoreWriter.write("".getBytes());
            scoreWriter.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }




}
