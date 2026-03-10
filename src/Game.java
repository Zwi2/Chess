import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

public class Game {

    Piece[][] board = new Piece[8][8];

    public boolean isValid(int row1, int col1, int row2, int col2){

        if (row1 == row2 && col1 == col2) return false;

        Piece from = board[row1][col1];
        Piece to = board[row2][col2];

        if (from == null) return false; //no piece to move

        if (to != null && from.getColor() == to.getColor()) return false; //capturing and moving to an empty square

//        return rookMove(row1, col1, row2, col2);
        return switch (from.getType()) {
            case PAWN -> pawnMove(row1, col1, row2, col2);
            case ROOK -> rookMove(row1, col1, row2, col2);
            case BISHOP -> bishopMove(row1, col1, row2, col2);
            case KNIGHT -> knightMove(row1, col1, row2, col2);
            case QUEEN -> queenMove(row1, col1, row2, col2);
            case KING -> kingMove(row1, col1, row2, col2);
        };
    }
    public List<Point> getLegalMoves(int row, int col) {
    //This is hour like 10 of working on this and I just found out about points
        //gonna kms tomorrow
        List<Point> moves = new ArrayList<>();

        for (int row1 = 0; row1 < 8; row1++) {
            for (int col1 = 0; col1 < 8; col1++) {
                if (isValid(row, col, row1, col1)) {
                    moves.add(new Point(row1, col1));
                }
            }
        }

        return moves;
    }

    private boolean knightMove(int row1, int col1, int row2, int col2){
        return (abs(row2-row1) == 1 && abs(col1-col2) == 2) || (abs(row2-row1) == 2 && abs(col1-col2) == 1);
    }
    private boolean rookMove(int row1, int col1, int row2, int col2){
        return row2 == row1 || col2 == col1;
    }
    private boolean kingMove(int row1, int col1, int row2, int col2){
        return abs(row2-row1) <= 1 && abs(col2-col1) <= 1;
    }
    private boolean bishopMove(int row1, int col1, int row2, int col2){
        return abs(row2-row1) == abs(col2-col1);
    }
    private boolean queenMove(int row1, int col1, int row2, int col2){
        return (abs(row2-row1) == abs(col2-col1)) || (row2 == row1 || col2 == col1);
    }
    private boolean pawnMove(int row1, int col1, int row2, int col2){

        int direction = board[row1][col1].getColor() == pieceColor.WHITE ? -1 : 1;

        //double move
        if(row1 == 1 || row1 == 6){
            if (col1 == col2 && board[row2][col2] == null){
                if (board[row1][col1].getColor() == pieceColor.WHITE){
                    if (row2 == row1 + direction - 1) return true;
                }
                if (row2 == row1 + direction + 1) return true;
            }
        }
        // move forward
        if (col1 == col2 && board[row2][col2] == null){
            if (row2 == row1 + direction) return true;
        }

        // capture diagonally
        if (abs(col1 - col2) == 1 && row2 == row1 + direction){
            return board[row2][col2] != null;
        }

        return false;
    }


    public void move(int row1, int col1, int row2, int col2){
        board[row2][col2] = board[row1][col1];
        board[row1][col1] = null;
    }

    public Piece getPiece(int row, int col){
        return board[row][col];
    }

    public String getPieceIcon(int row, int col){
        Piece piece = board[row][col];
        return (piece == null) ? "" : piece.getIcon();
    }

    public Game() {
        reset();
    }
    public void reset(){

        board[0][0] = new Piece(pieceType.ROOK, pieceColor.BLACK);
        board[0][7] = new Piece(pieceType.ROOK, pieceColor.BLACK);

        board[7][0] = new Piece(pieceType.ROOK, pieceColor.WHITE);
        board[7][7] = new Piece(pieceType.ROOK, pieceColor.WHITE);

        board[0][1] = board[0][6] = new Piece(pieceType.KNIGHT, pieceColor.BLACK);
        board[7][1] = board[7][6] = new Piece(pieceType.KNIGHT, pieceColor.WHITE);

        board[0][2] = board[0][5] = new Piece(pieceType.BISHOP, pieceColor.BLACK);
        board[7][2] = board[7][5] = new Piece(pieceType.BISHOP, pieceColor.WHITE);

        board[0][3] = new Piece(pieceType.QUEEN, pieceColor.BLACK);
        board[7][3] = new Piece(pieceType.QUEEN, pieceColor.WHITE);

        board[0][4] = new Piece(pieceType.KING, pieceColor.BLACK);
        board[7][4] = new Piece(pieceType.KING, pieceColor.WHITE);

        for (int i = 0; i < 8; i++){
            board[1][i] = new Piece(pieceType.PAWN, pieceColor.BLACK);
            board[6][i] = new Piece(pieceType.PAWN, pieceColor.WHITE);
        }
    }


}
