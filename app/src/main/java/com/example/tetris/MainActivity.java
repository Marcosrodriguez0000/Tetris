package com.example.tetris;

import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Tetris tetris;
    private TetrisView tetrisView;
    private TextView lineCounter;
    private Button buttonLeft;
    private Button buttonRight;
    private Button buttonRotate;
    private Button buttonFastDrop;
    private Handler handler;
    private Runnable gameLoopRunnable;
    private boolean isFastDrop = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tetris = new Tetris();
        tetrisView = findViewById(R.id.tetrisView);
        tetrisView.setTetris(tetris);

        lineCounter = findViewById(R.id.lineCounter);
        buttonLeft = findViewById(R.id.buttonLeft);
        buttonRight = findViewById(R.id.buttonRight);
        buttonRotate = findViewById(R.id.buttonRotate);
        buttonFastDrop = findViewById(R.id.buttonFastDrop);

        tetris.setLineCountListener(new Tetris.LineCountListener() {
            @Override
            public void onLineCountChanged(int newLineCount) {
                lineCounter.setText("SCORE: " + newLineCount);
            }
        });

        buttonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tetris.movePieceLeft();
                tetrisView.invalidate();
            }
        });

        buttonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tetris.movePieceRight();
                tetrisView.invalidate();
            }
        });

        buttonRotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tetris.rotatePiece();
                tetrisView.invalidate();
            }
        });

        buttonFastDrop.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    isFastDrop = true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    isFastDrop = false;
                }
                return true;
            }
        });

        handler = new Handler();
        startGameLoop();
    }

    private void startGameLoop() {
        gameLoopRunnable = new Runnable() {
            @Override
            public void run() {
                tetris.movePieceDown();
                tetrisView.invalidate();
                handler.postDelayed(this, isFastDrop ? 100 : 500); // Adjust the delay as needed
            }
        };
        handler.postDelayed(gameLoopRunnable, 500);
    }
}