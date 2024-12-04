// Tetris.java
package com.example.tetris;

import java.util.Random;

public class Tetris {
    private int[][] board;
    private int[][] boardColors;
    private int[][] currentPiece;
    private int currentPieceRow;
    private int currentPieceCol;
    private int currentPieceColor;
    private int lineCount;
    private LineCountListener lineCountListener;
    private Random random = new Random();
    private int viewHeight;
    private int cellSize;

    public interface LineCountListener {
        void onLineCountChanged(int newLineCount);
    }

    public void setLineCountListener(LineCountListener listener) {
        this.lineCountListener = listener;
    }

    public Tetris() {
        // Initialize the game board
        board = new int[20][10];
        boardColors = new int[20][10];
        generateNewPiece();
    }

    public void setViewHeight(int height) {
        this.viewHeight = height;
        this.cellSize = viewHeight / board.length;
    }

    public void rotatePiece() {
        // Rotate the piece 90 degrees clockwise
        int rows = currentPiece.length;
        int cols = currentPiece[0].length;
        int[][] rotatedPiece = new int[cols][rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                rotatedPiece[j][rows - 1 - i] = currentPiece[i][j];
            }
        }
        if (canMovePiece(currentPieceRow, currentPieceCol, rotatedPiece)) {
            currentPiece = rotatedPiece;
        }
    }

    public void movePieceDown() {
        // Calculate the maximum row index based on a little more than half the screen height
        int maxRow = (int) ((viewHeight * 0.69) / cellSize) - currentPiece.length;

        // Move the piece down if within bounds
        if (currentPieceRow < maxRow && canMovePiece(currentPieceRow + 1, currentPieceCol)) {
            currentPieceRow++;
        } else {
            // Fix the piece on the board and generate a new piece
            placePiece();
            checkForCompleteLines();
            generateNewPiece();
        }
    }

    private boolean canMovePiece(int newRow, int newCol, int[][] piece) {
        // Check if the piece can move to the new position
        for (int i = 0; i < piece.length; i++) {
            for (int j = 0; j < piece[i].length; j++) {
                if (piece[i][j] != 0) {
                    int boardRow = newRow + i;
                    int boardCol = newCol + j;
                    if (boardRow >= (int) ((viewHeight * 0.69) / cellSize) || boardCol < 0 || boardCol >= board[0].length || board[boardRow][boardCol] != 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void generateNewPiece() {
        // Generate a new random piece
        int pieceIndex = random.nextInt(PIECES.length);
        currentPiece = PIECES[pieceIndex];
        currentPieceColor = COLORS[random.nextInt(COLORS.length)];
        currentPieceRow = 0;
        currentPieceCol = (board[0].length - currentPiece[0].length) / 2; // Center the piece horizontally
    }

    public void movePieceLeft() {
        if (canMovePiece(currentPieceRow, currentPieceCol - 1)) {
            currentPieceCol--;
        }
    }

    public void movePieceRight() {
        if (canMovePiece(currentPieceRow, currentPieceCol + 1)) {
            currentPieceCol++;
        }
    }

    private boolean canMovePiece(int newRow, int newCol) {
        return canMovePiece(newRow, newCol, currentPiece);
    }

    private void placePiece() {
        // Fix the piece on the board
        for (int i = 0; i < currentPiece.length; i++) {
            for (int j = 0; j < currentPiece[i].length; j++) {
                if (currentPiece[i][j] != 0) {
                    board[currentPieceRow + i][currentPieceCol + j] = currentPiece[i][j];
                    boardColors[currentPieceRow + i][currentPieceCol + j] = currentPieceColor;
                }
            }
        }
    }

    private static final int[][][] PIECES = {
            // Define the shapes of the Tetris pieces
            {
                    {1, 1, 1, 1} // I piece
            },
            {
                    {1, 1, 1},
                    {0, 1, 0} // T piece
            },
            {
                    {1, 1, 0},
                    {0, 1, 1} // S piece
            },
            {
                    {0, 1, 1},
                    {1, 1, 0} // Z piece
            },
            {
                    {1, 1},
                    {1, 1} // O piece
            },
            {
                    {1, 1, 1},
                    {1, 0, 0} // L piece
            },
            {
                    {1, 1, 1},
                    {0, 0, 1} // J piece
            }
    };

    private static final int[] COLORS = {
            0xFF00FFFF, // Cyan
            0xFF800080, // Purple
            0xFF00FF00, // Green
            0xFFFF0000, // Red
            0xFFFFFF00, // Yellow
            0xFFFFA500, // Orange
            0xFF0000FF  // Blue
    };

    private void checkForCompleteLines() {
        for (int i = 0; i < board.length; i++) {
            boolean isComplete = true;
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == 0) {
                    isComplete = false;
                    break;
                }
            }
            if (isComplete) {
                removeLine(i);
                lineCount++;
                if (lineCountListener != null) {
                    lineCountListener.onLineCountChanged(lineCount);
                }
            }
        }
    }

    private void removeLine(int line) {
        for (int i = line; i > 0; i--) {
            board[i] = board[i - 1];
            boardColors[i] = boardColors[i - 1];
        }
        board[0] = new int[10];
        boardColors[0] = new int[10];
    }

    public int[][] getBoard() {
        return board;
    }

    public int[][] getBoardColors() {
        return boardColors;
    }

    public int[][] getCurrentPiece() {
        return currentPiece;
    }

    public int getCurrentPieceRow() {
        return currentPieceRow;
    }

    public int getCurrentPieceCol() {
        return currentPieceCol;
    }

    public int getCurrentPieceColor() {
        return currentPieceColor;
    }
}