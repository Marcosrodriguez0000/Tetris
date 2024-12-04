// TetrisView.java
package com.example.tetris;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;

public class TetrisView extends View {
    private Tetris tetris;
    private Paint paint;
    private Paint blockPaint;
    private int cellSize;
    private boolean isFastDrop = false;

    public TetrisView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFocusable(true);
        setFocusableInTouchMode(true);
        paint = new Paint();
        blockPaint = new Paint();
        blockPaint.setColor(0xFF000000); // Black color for the block grid
        blockPaint.setStyle(Paint.Style.STROKE);
        blockPaint.setStrokeWidth(2); // Set the stroke width for the block grid
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
}