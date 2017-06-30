package tech.bfitzsimmons.braingame;

import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Random;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    //Get header layout
    LinearLayout header;

    //Get header views
    TextView timer;
    TextView problem;
    TextView score;

    //Get choices layout and individual choice buttons
    GridLayout choices;
    Button choice1;
    Button choice2;
    Button choice3;
    Button choice4;

    //Declare Random
    Random random;

    //Initialize round and correct answers
    int correctAnswers = 0;
    int round = 0;
    String currentAnswer = "";

    //initialize boolean isGameRunning and reset button and final score text view
    boolean isGameRunning = false;
    Button resetButton;
    TextView finalScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize everything from the class
        header = (LinearLayout) findViewById(R.id.header);
        timer = (TextView) findViewById(R.id.timer);
        choices = (GridLayout) findViewById(R.id.choices);
        random = new Random();
        problem = (TextView) findViewById(R.id.problem);
        score = (TextView) findViewById(R.id.score);
        choice1 = (Button) findViewById(R.id.choice1);
        choice2 = (Button) findViewById(R.id.choice2);
        choice3 = (Button) findViewById(R.id.choice3);
        choice4 = (Button) findViewById(R.id.choice4);
        resetButton = (Button) findViewById(R.id.reset);
        finalScore = (TextView) findViewById(R.id.finalScore);
        finalScore.animate().scaleX(0).scaleY(0);
    }

    public void start(View view){
        TextView goButton = (TextView) findViewById(R.id.start);
        goButton.setVisibility(View.GONE);
        header.setVisibility(View.VISIBLE);
        choices.setVisibility(View.VISIBLE);
        isGameRunning = true;
        generateTimer();
        //start the nextProblem (really the first problem here)
        currentAnswer = nextProblem();
    }

    public void submitAnswer(View view){
        if(isGameRunning){
            String submittedAnswer = ((TextView) view).getText().toString();
            if(submittedAnswer.equals(currentAnswer)){
                correctAnswers++;
            }
            round++;
            String newScore = String.format("%d/%d", correctAnswers, round);
            score.setText(newScore);

            currentAnswer = nextProblem();
        }
    }

    public String nextProblem(){
        int a = random.nextInt(98)+1;
        int b = random.nextInt(98)+1;
        String newProblem = String.format("%d + %d", a, b);
        problem.setText(newProblem);

        //randomize buttons with one having correct answer
        int correctIndex = random.nextInt(4);
        for (int i = 0; i < choices.getChildCount(); i++) {
            if(i == correctIndex){
                String buttonAnswer = String.valueOf(a+b);
                ((TextView) choices.getChildAt(i)).setText(buttonAnswer);
            } else {
                String randomAnswer = String.valueOf(random.nextInt(98)+1);
                ((TextView) choices.getChildAt(i)).setText(randomAnswer);
            }
        }
        return String.valueOf(a + b);
    }

    public void reset(View view){
        isGameRunning = true;
        view.setVisibility(View.INVISIBLE);
        generateTimer();
        round = 0;
        correctAnswers = 0;
        score.setText(String.format("%d/%d", correctAnswers, round));
        finalScore.setVisibility(View.INVISIBLE);
        finalScore.animate().scaleX(0f).scaleY(0f);
        currentAnswer = nextProblem();
    }

    public void generateTimer(){
        new CountDownTimer(31000, 1000){
            @Override
            public void onTick(long l) {
                String timeLeft = String.format("%02ds", l/1000);
                timer.setText(timeLeft);
            }

            @Override
            public void onFinish() {
                timer.setText("00s");
                MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.airhorn);
                mediaPlayer.start();
                isGameRunning = false;
                resetButton.setVisibility(View.VISIBLE);
                finalScore.setText(String.format("Final score: %d/%d", correctAnswers, round));
                finalScore.setVisibility(View.VISIBLE);
                finalScore.animate().scaleY(1f).scaleX(1f).setDuration(800);

            }
        }.start();
    }
}
