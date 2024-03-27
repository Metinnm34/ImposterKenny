package com.han.catchthekenny;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView scoreText;
    TextView timeText;
    TextView heartText;

    ImageView heartImageView;

    int score;
    int hearts; // Kalp çubuğu
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
    long millisUntilFinished; // Mevcut zamanı tutmak için değişken

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize
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

        countDownTimer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Mevcut zamanı güncelle
                MainActivity.this.millisUntilFinished = millisUntilFinished;
                timeText.setText("Time: " + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                timeText.setText("Time off!");
                handler.removeCallbacks(runnable);
                for (ImageView image : imageArray) {
                    image.setVisibility(View.INVISIBLE);
                }
                endGame();
            }
        }.start();
    }

    public void increaseScore(View view) {
        if (view.getTag() != null && view.getTag().equals("imposterKenny")) {
            // Sadece imposter Kenny'ye tıklandığında score azalacak
            if (score > 0) {
                score--;
            }
            // Kalp sayısını güncelle
            if (hearts > 0) {
                updateHearts();
            }
        } else {
            // Diğer durumda (normal Kenny'ye tıklanması durumu)
            score++;
        }
        // Skoru güncelle
        scoreText.setText("Score: " + score);

        // Skor 10 veya daha büyükse, oyunu bitir ve kullanıcıya seçenekler sun
        if (score >= 10) {
            countDownTimer.cancel(); // Zamanlayıcıyı durdur
            Toast.makeText(MainActivity.this, "Birinci seviyeyi başarıyla tamamladınız!", Toast.LENGTH_SHORT).show();
            showLevelCompletionDialog();
        } else if (score <= 0 || hearts <= 0) {
            countDownTimer.cancel(); // Zamanlayıcıyı durdur
            endGame();
        }
    }

    private void updateHearts() {
        heartText.setText("Hearts: " + hearts);
        if (hearts <= 0 || score <= 0) {
            endGame();
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
                handler.postDelayed(this, 1500); // Yeni levelde gösterim hızını değiştirmeniz gerekebilir
            }
        };
        handler.post(runnable);
    }

    private void showLevelCompletionDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setTitle("Seviye Tamamlandı");
        alert.setMessage("Ne yapmak istersiniz?");
        alert.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Retry seçeneği seçildiğinde, bu aktiviteyi yeniden başlat
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });
        alert.setNegativeButton("Next Level", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Next Level seçeneği seçildiğinde, CatchTheKennyLevel2 Activity'e yönlendir
                Intent intent = new Intent(MainActivity.this, CatchTheKennyLevel2.class);
                // Önceki aktivitedeki zamanlayıcıyı durdur
                handler.removeCallbacks(runnable);
                // Zamanlayıcı durdurulduktan sonra yeni aktiviteye geçmeden önce zamanı gönder
                intent.putExtra("timeLeftInMillis", millisUntilFinished);
                startActivity(intent);
            }
        });
        // Dialogu göster
        alert.show();
    }

    private void endGame() {
        Toast.makeText(MainActivity.this, "Game Over!", Toast.LENGTH_SHORT).show();

        // Tüm Kenny görüntülerini görünmez yap
        for (ImageView image : imageArray) {
            image.setVisibility(View.INVISIBLE);
        }

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
}
