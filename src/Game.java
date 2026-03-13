import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Game {

    Piece[][] board = new Piece[8][8];
    public Game() {
        reset();
    }
    boolean h1rookMoved = false;
    boolean a1rookMoved = false;
    boolean h8rookMoved = false;
    boolean a8rookMoved = false;
    //this line begins the wonderful piece of shit king methods
    int[] blackKing = {0, 4}; boolean blackKingMoved = false;
    int[] whiteKing = {7, 4}; boolean whiteKingMoved = false;
    public boolean isKingInCheck(pieceColor kingColor){

        int kingRow = (kingColor == pieceColor.WHITE) ? whiteKing[0] : blackKing[0];
        int kingCol = (kingColor == pieceColor.WHITE) ? whiteKing[1] : blackKing[1];

        Point king = new Point(kingRow, kingCol);

        for(int row = 0; row < 8; row++){
            for(int col = 0; col < 8; col++){

                Piece piece = board[row][col];

                if(piece != null && piece.getColor() != kingColor){

                    if(getLegalMoves(row,col).contains(king)){
                        return true;
                    }

                }

            }
        }

        return false;
    }
    public boolean checkMate(pieceColor kingColor){
        int kingRow = (kingColor == pieceColor.WHITE) ? whiteKing[0] : blackKing[0];
        int kingCol = (kingColor == pieceColor.WHITE) ? whiteKing[1] : blackKing[1];
        return isKingInCheck(kingColor) && getSafeMoves(kingRow, kingCol).isEmpty();
    }
    public String endGame(){
        String msg;
        if(checkMate(pieceColor.WHITE)){
            msg = "Checkmate!  Black wins!";
        return msg;
        }
        if(checkMate(pieceColor.BLACK)){
            msg = "Checkmate!  White wins!";
            return msg;
        }
        return null;
    }
    private boolean wouldCauseCheck(int row1, int col1, int row2, int col2){

        Piece moving = board[row1][col1];
        Piece captured = board[row2][col2];

        int oldKingRow = -1;
        int oldKingCol = -1;

        //temporarily move
        board[row2][col2] = moving;
        board[row1][col1] = null;

        //update king position if king moved
        if(moving.getType() == pieceType.KING){

            if(moving.getColor() == pieceColor.WHITE){
                oldKingRow = whiteKing[0];
                oldKingCol = whiteKing[1];
                whiteKing[0] = row2;
                whiteKing[1] = col2;
            } else {
                oldKingRow = blackKing[0];
                oldKingCol = blackKing[1];
                blackKing[0] = row2;
                blackKing[1] = col2;
            }
        }

        boolean check = isKingInCheck(moving.getColor());

        //undo move
        board[row1][col1] = moving;
        board[row2][col2] = captured;

        if(moving.getType() == pieceType.KING){
            if(moving.getColor() == pieceColor.WHITE){
                whiteKing[0] = oldKingRow;
                whiteKing[1] = oldKingCol;
            } else {
                blackKing[0] = oldKingRow;
                blackKing[1] = oldKingCol;
            }
        }

        return check;
    }

    //this like concludes the wonderful piece of shit king methods
    public void switchTurns(){
        turn = (turn == pieceColor.WHITE) ? pieceColor.BLACK : pieceColor.WHITE;
    }
    pieceColor turn = pieceColor.WHITE;
    public boolean isValid(int row1, int col1, int row2, int col2){
        Piece piece = board[row1][col1];

        if (piece.getColor() == turn) {
            return getSafeMoves(row1, col1).contains(new Point(row2, col2));
        }

        return false;
    }

    public List<Point> getSafeMoves(int row, int col){

        List<Point> safeMoves = new ArrayList<>();

        for(Point move : getLegalMoves(row, col)){

            if(!wouldCauseCheck(row, col, move.x, move.y)){
                safeMoves.add(move);
            }

        }

        return safeMoves;
    }

    public List<Point> getLegalMoves(int row, int col) {
        List<Point> moves = new ArrayList<>();

        Piece piece = board[row][col];
        if (piece == null) return moves;

        pieceType type = piece.getType();
        pieceColor color = piece.getColor();

        switch (type) {
            //the arrays are directions: verticals are flipped since the top left corner is 0,0
            // {1,0 } = down
            // {-1, 0} = up
            // {0, 1} = right
            //{0, -1} = left
            case ROOK -> addSlidingMoves(moves, row, col,
                    new int[][]{{1,0},{-1,0},{0,1},{0,-1}}, color);

            case BISHOP -> addSlidingMoves(moves, row, col,
                    new int[][]{{1,1},{1,-1},{-1,1},{-1,-1}}, color);

            case QUEEN -> addSlidingMoves(moves, row, col,
                    new int[][]{
                            {1,0},{-1,0},{0,1},{0,-1},
                            {1,1},{1,-1},{-1,1},{-1,-1}
                    }, color);

            case KNIGHT -> {
                int[][] jumps = {
                        {2,1},{2,-1},{-2,1},{-2,-1},
                        {1,2},{1,-2},{-1,2},{-1,-2}
                };

                for (int[] j : jumps) {
                    int r = row + j[0];
                    int c = col + j[1];

                    if (inBounds(r,c) && (board[r][c] == null || board[r][c].getColor() != color)) {
                        moves.add(new Point(r,c));
                    }
                }
            }

            case KING -> {
                for (int r = -1; r <= 1; r++) {
                    for (int c = -1; c <= 1; c++) {

                        if (r == 0 && c == 0) continue;

                        int nr = row + r;
                        int nc = col + c;

                        if (inBounds(nr,nc) &&
                                (board[nr][nc] == null || board[nr][nc].getColor() != color)) {
                            moves.add(new Point(nr,nc));
                            //kingside castle
                            if(color == pieceColor.WHITE && !whiteKingMoved && !h1rookMoved){
                                if(board[7][1] == null && board[7][2] == null && board[7][3] == null){
                                    moves.add(new Point(7,2));
                                }
                            }
                            if(color == pieceColor.BLACK && !blackKingMoved && !h8rookMoved){
                                if(board[0][5] == null && board[0][6] == null){
                                    moves.add(new Point(0,6));
                                }
                            }
                            //queenside castle
                            if(color == pieceColor.WHITE && !whiteKingMoved && !h1rookMoved){
                                if(board[7][1] == null && board[7][2] == null && board[7][3] == null){
                                    moves.add(new Point(7,2));
                                }}
                                if(color == pieceColor.BLACK && !blackKingMoved && !a8rookMoved){
                                    if(board[0][1] == null && board[0][2] == null && board[0][3] == null){
                                        moves.add(new Point(0,2));
                                    }}
                        }
                    }
                }
            }

            case PAWN -> {
                int direction = (color == pieceColor.WHITE) ? -1 : 1;
                int startRow = (color == pieceColor.WHITE) ? 6 : 1;

                int forward = row + direction;

                //single move
                if (inBounds(forward,col) && board[forward][col] == null) {
                    moves.add(new Point(forward,col));

                    //double move
                    if (row == startRow) {
                        int doubleForward = row + 2*direction;
                        if (board[doubleForward][col] == null) {
                            moves.add(new Point(doubleForward,col));
                        }
                    }
                }

                //captures
                int[] cols = {col-1, col+1};
                for (int c : cols) {
                    if (inBounds(forward,c) &&
                            board[forward][c] != null &&
                            board[forward][c].getColor() != color) {
                        moves.add(new Point(forward,c));
                    }
                }
            }
        }

        return moves;
    }
    private void addSlidingMoves(List<Point> moves, int row, int col, int[][] directions, pieceColor color) {
        //all this method does is keep trying to move the piece further into the direction given
        for (int[] direction : directions) {
            //this means direction is an array of 2 numbers.
            // Up until it's invalid, the row1 and col2 values will be increased by the direction vertex, and then the next.
            int row1 = row + direction[0];
            int col1 = col + direction[1];

            while (inBounds(row1,col1)) {

                if (board[row1][col1] == null) {
                    moves.add(new Point(row1,col1));
                } else {
                    if (board[row1][col1].getColor() != color) {
                        moves.add(new Point(row1,col1));
                    }
                    break;
                }

                row1 += direction[0];
                col1 += direction[1];
            }
        }
    }

    private boolean inBounds(int row, int col){
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }


    public void move(int row1, int col1, int row2, int col2){
        //kingside castle
        if(board[row1][col1].getType() == pieceType.KING && col2 == 6){
            //move king
            board[row2][col2] = board[row1][col1];
            board[row1][col1] = null;

            //move rook
            board[row1][5] = board[row1][7];
            board[row1][7] = null;

            switchTurns();
            return;
        }
        if(board[row1][col1].getType() == pieceType.KING){
            if(board[row1][col1].getColor() == pieceColor.WHITE){
                whiteKing[0] = row2;
                whiteKing[1] = col2;
                whiteKingMoved = true;
            }
            else{
                blackKing[0] = row2;
                blackKing[1] = col2;
                blackKingMoved = true;
            }
        }
        if(board[row1][col1].getType() == pieceType.KING && col2 == 2){

            //move king
            board[row2][col2] = board[row1][col1];
            board[row1][col1] = null;

            //move rook
            board[row1][3] = board[row1][0];
            board[row1][0] = null;

            switchTurns();
            return;
        }
        if(board[row1][col1].getType() == pieceType.KING){
            if(board[row1][col1].getColor() == pieceColor.WHITE){
                whiteKing[0] = row2;
                whiteKing[1] = col2;
                whiteKingMoved = true;
            }
            else{
                blackKing[0] = row2;
                blackKing[1] = col2;
                blackKingMoved = true;
            }
        }
        if(board[row1][col1].getType() == pieceType.ROOK){
            if(row1 == 7 && col1 == 0){a1rookMoved = true;}
            else if(row1 == 7 && col1 == 7){h1rookMoved = true;}
            else if(row1 == 0 && col1 == 0){a8rookMoved = true;}
            else if(row1 == 0 && col1 == 7){h8rookMoved = true;}
        }

        board[row2][col2] = board[row1][col1];
        board[row1][col1] = null;
        switchTurns();

    }

    public Piece getPiece(int row, int col){
        return board[row][col];
    }

    public String getPieceIcon(int row, int col){
        Piece piece = board[row][col];
        return (piece == null) ? "" : piece.getIcon();
    }


    public void reset(){
        board = new Piece[8][8];
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
