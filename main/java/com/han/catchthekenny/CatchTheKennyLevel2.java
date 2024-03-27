package com.han.catchthekenny;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;


//The Game Rules
/*
1.You have 4 heart
2.There are Innocent Kenny's and there is an Imposter Kenny (the one with the knife).
3.When you click on the innocent kenny's your score increases,
if you click on the Imposter kenny there is a life bar in the left corner above and it says how much life you have left
4.When you reach 10 points you are entitled to move to the next level
5.In 2nd level  the work gets a little more difficult and the speed of the kenny's starts to increase
6.Also in 2nd level  there is a start button and a stop button
With the start button you can start the game at any time you want
And with the stop button you can stop it at any time you want
7.success :)




*/

public class CatchTheKennyLevel2 extends AppCompatActivity {

    TextView scoreText;
    TextView timeText;
    TextView heartText;

    ImageView heartImageView;

    int score;
    int hearts; // Heart stick
    ImageView imageView;
    ImageView imageView2;
    ImageView imageView3;
    ImageView imageView4;
    ImageView imageView5;
    ImageView imageView6;
    ImageView imageView7;
    ImageView imageView8;
    ImageView imageView9;
    ImageView imageView10;
    ImageView imageView11;

    ImageView[] imageArray;
    Handler handler;
    Runnable runnable;

    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catch_the_kenny_level2); // You must change the name of the corresponding layout file!

        // Initialize
        timeText = findViewById(R.id.timeText);
        scoreText = findViewById(R.id.scoreText);
        heartText = findViewById(R.id.heartText);
        heartImageView = findViewById(R.id.heartImageView);
        imageView = findViewById(R.id.imageView);
        imageView2 = findViewById(R.id.imageView2);
        imageView3 = findViewById(R.id.imageView3);
        imageView4 = findViewById(R.id.imageView4);
        imageView5 = findViewById(R.id.imageView5);
        imageView6 = findViewById(R.id.imageView6);
        imageView7 = findViewById(R.id.imageView7);
        imageView8 = findViewById(R.id.imageView8);
        imageView9 = findViewById(R.id.imageView9);
        imageView10 = findViewById(R.id.imageView10);
        imageView11 = findViewById(R.id.imageView11);
        imageArray = new ImageView[]{imageView, imageView2, imageView3, imageView4, imageView5, imageView6, imageView7, imageView8, imageView9, imageView10, imageView11};
        hideImages();

        score = 0;
        hearts = 4;


        // Find Start and Stop buttons and assign click events
        Button startBtn = findViewById(R.id.startBtn);
        Button stopBtn = findViewById(R.id.stopBtn);
        if (startBtn != null && stopBtn != null) {
            startBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startGame();
                    Toast.makeText(CatchTheKennyLevel2.this, "Game is started", Toast.LENGTH_SHORT).show();
                }
            });
            stopBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    stopGame();
                    Toast.makeText(CatchTheKennyLevel2.this, "Game is stopped", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Button not found", Toast.LENGTH_SHORT).show();
        }
    }



    public void startGame() {
        countDownTimer = new CountDownTimer(12000, 1300) { // Zamanı ve intervali değiştirmeniz gerekebilir
            @Override
            public void onTick(long millisUntilFinished) {
                timeText.setText("Time: " + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                timeText.setText("Time off!");
                handler.removeCallbacks(runnable);
                for (ImageView image : imageArray) {
                    image.setVisibility(View.INVISIBLE);
                }

                AlertDialog.Builder alert = new AlertDialog.Builder(CatchTheKennyLevel2.this);
                alert.setTitle("Restart?");
                alert.setMessage("Are you sure to restart game?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        recreate(); // restart game
                    }
                });
                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish(); // Finish the game
                    }
                });
                alert.show();
            }
        }.start();
    }



    public void stopGame() {
        // Cancel CountDownTimer
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        // Hide all kenny images
        for (ImageView image : imageArray) {
            image.setVisibility(View.INVISIBLE);
            image.setOnClickListener(null);
        }
    }
    public void increaseScore(View view) {
        ImageView clickedImageView = (ImageView) view;
        if (clickedImageView.getVisibility() == View.INVISIBLE) {
            return;
        }
        String tag = (String) clickedImageView.getTag();
        if ("imposterKenny".equals(tag)) {
            hearts--;
            updateHearts();
        } else if ("normalKenny".equals(tag)) {
            score++;
            scoreText.setText("Score: " + score);
        }
        clickedImageView.setVisibility(View.INVISIBLE);
        clickedImageView.setClickable(false);
    }

    private void updateHearts() {
        heartText.setText("Hearts: " + hearts);
        if (hearts <= 0 || score <= 0) {
            endGame();
        }
    }

    private void endGame() {
        Toast.makeText(CatchTheKennyLevel2.this, "Game Over!", Toast.LENGTH_SHORT).show();
        if (!isFinishing()) {
            new AlertDialog.Builder(this)
                    .setTitle("Game Over")
                    .setMessage("Would you like to restart the game?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            recreate();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .show();
        }
    }

    public void hideImages() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                for (ImageView image : imageArray) {
                    image.setVisibility(View.INVISIBLE);
                    image.setClickable(true);
                }
                Random random = new Random();
                int i = random.nextInt(11);
                if (random.nextBoolean()) {
                    imageArray[i].setImageResource(R.drawable.kenny);
                    imageArray[i].setTag("normalKenny");
                } else {
                    imageArray[i].setImageResource(R.drawable.imposterkenny);
                    imageArray[i].setTag("imposterKenny");
                }
                imageArray[i].setVisibility(View.VISIBLE);
                imageArray[i].setClickable(true);
                handler.postDelayed(this, 400); //You may need to change the display speed in the new level
            }
        };
        handler.post(runnable);
    }
}