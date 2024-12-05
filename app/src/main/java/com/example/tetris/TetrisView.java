package com.example.tetris;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;

public class TetrisView extends View {
    private Tetris tetris;
    private Paint paint;
    private Paint blockPaint;
    private Paint borderPaint;
    private int cellSize;
    private boolean isFastDrop = false;

    public TetrisView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        setFocusable(true);
        setFocusableInTouchMode(true);
        paint = new Paint();
        blockPaint = new Paint();
        blockPaint.setColor(0xFF000000); // Black color for the block grid
        blockPaint.setStyle(Paint.Style.STROKE);
        blockPaint.setStrokeWidth(2); // Set the stroke width for the block grid
        borderPaint = new Paint();
        borderPaint.setColor(Color.GRAY); // Gray color for the border
        borderPaint.setStyle(Paint.Style.FILL); // Set the style to fill for the border
    }

    public void setTetris(Tetris tetris) {
        this.tetris = tetris;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (tetris != null) {
            cellSize = getWidth() / tetris.getBoard()[0].length;
            tetris.setViewHeight(getHeight());
            drawBoard(canvas);
            drawCurrentPiece(canvas);
            drawBorder(canvas);
        }
    }

    private void drawBoard(Canvas canvas) {
        for (int i = 0; i < tetris.getBoard().length; i++) {
            for (int j = 0; j < tetris.getBoard()[0].length; j++) {
                if (tetris.getBoard()[i][j] != 0) {
                    paint.setColor(tetris.getBoardColors()[i][j]);
                    canvas.drawRect(j * cellSize, i * cellSize, (j + 1) * cellSize, (i + 1) * cellSize, paint);
                    canvas.drawRect(j * cellSize, i * cellSize, (j + 1) * cellSize, (i + 1) * cellSize, blockPaint);
                }
            }
        }
    }

    private void drawCurrentPiece(Canvas canvas) {
        int[][] piece = tetris.getCurrentPiece();
        int pieceRow = tetris.getCurrentPieceRow();
        int pieceCol = tetris.getCurrentPieceCol();
        for (int i = 0; i < piece.length; i++) {
            for (int j = 0; j < piece[i].length; j++) {
                if (piece[i][j] != 0) {
                    paint.setColor(tetris.getCurrentPieceColor());
                    canvas.drawRect((pieceCol + j) * cellSize, (pieceRow + i) * cellSize, (pieceCol + j + 1) * cellSize, (pieceRow + i + 1) * cellSize, paint);
                    canvas.drawRect((pieceCol + j) * cellSize, (pieceRow + i) * cellSize, (pieceCol + j + 1) * cellSize, (pieceRow + i + 1) * cellSize, blockPaint);
                }
            }
        }
    }

    private void drawBorder(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        int brickSize = 20; // Size of each brick

        // Draw top border
        for (int x = 0; x < width; x += brickSize) {
            canvas.drawRect(x, 0, x + brickSize - 2, brickSize - 2, borderPaint);
        }

        // Draw bottom border
        for (int x = 0; x < width; x += brickSize) {
            canvas.drawRect(x, height - brickSize, x + brickSize - 2, height, borderPaint);
        }

        // Draw left border
        for (int y = 0; y < height; y += brickSize) {
            canvas.drawRect(0, y, brickSize - 2, y + brickSize - 2, borderPaint);
        }

        // Draw right border
        for (int y = 0; y < height; y += brickSize) {
            canvas.drawRect(width - brickSize, y, width, y + brickSize - 2, borderPaint);
        }
    }

    private void animateLineBreak(final int row) {
        final int neonColor = Color.parseColor("#39FF14"); // Neon green color
        final int originalColor = tetris.getBoardColors()[row][0]; // Assuming all cells in the row have the same color

        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        animator.setDuration(500); // Duration of the animation
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float progress = (float) animation.getAnimatedValue();
                for (int col = 0; col < tetris.getBoard()[0].length; col++) {
                    if (progress < 0.5) {
                        tetris.getBoardColors()[row][col] = neonColor;
                    } else {
                        tetris.getBoardColors()[row][col] = originalColor;
                    }
                }
                invalidate(); // Redraw the view
            }
        });
        animator.start();

        // Wait for the animation to complete before breaking the line
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                breakLine(row);
            }
        }, 500);
    }

    private void breakLine(final int row) {
        // Add logic to break the line into pieces
        // For now, just clear the line
        for (int col = 0; col < tetris.getBoard()[0].length; col++) {
            tetris.getBoard()[row][col] = 0;
        }
        invalidate(); // Redraw the view
    }

    public void onLineComplete(int row) {
        animateLineBreak(row);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            isFastDrop = true;
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            isFastDrop = false;
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    public boolean isFastDrop() {
        return isFastDrop;
    }

    private void init() {
        borderPaint = new Paint();
        borderPaint.setColor(Color.GRAY);
        borderPaint.setStyle(Paint.Style.FILL);
    }
}