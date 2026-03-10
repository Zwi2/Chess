public enum pieceType {

    KING("♔","♚"),
    QUEEN("♕","♛"),
    ROOK("♖","♜"),
    BISHOP("♗","♝"),
    KNIGHT("♘","♞"),
    PAWN("♙","♟");

    private final String whiteIcon;
    private final String blackIcon;

    pieceType(String whiteIcon, String blackIcon) {
        this.whiteIcon = whiteIcon;
        this.blackIcon = blackIcon;
    }

    public String getIcon(pieceColor color) {
        return color == pieceColor.WHITE ? whiteIcon : blackIcon;
    }
}