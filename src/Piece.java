public class Piece {

    private final pieceType type;
    private final pieceColor color;

    public Piece(pieceType type, pieceColor color) {
        this.type = type;
        this.color = color;
    }

    public pieceType getType() {
        return type;
    }

    public pieceColor getColor() {
        return color;
    }

    public String getIcon() {
        return type.getIcon(color);
    }

}