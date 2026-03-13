import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Point;
import java.util.List;

public class GUI implements ActionListener {

    public void highlightMoves(int row, int col) {

        List<Point> moves = game.getSafeMoves(row, col);

        for (Point p : moves) {
            buttons[p.x][p.y].setBackground(new Color(130, 200, 130));
        }
    }
    public void resetColors() {
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){

                if ((i + j) % 2 == 0) {
                    buttons[i][j].setBackground(new Color(240,217,181));
                } else {
                    buttons[i][j].setBackground(new Color(181,136,99));
                }

            }
        }
    }

    JFrame frame = new JFrame("Chess but bad");
    JButton[][] buttons = new JButton[8][8];

    Game game = new Game();

    public GUI() {

        frame.setSize(600,600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(8,8));

        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){

                JButton button = new JButton("");
                button.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 39));
                button.addActionListener(this);
                buttons[i][j] = button;

                buttons[i][j].putClientProperty("row", i);
                buttons[i][j].putClientProperty("col", j);

                buttons[i][j].setText(game.getPieceIcon(i, j));
                if ((i + j) % 2 == 0) {
                    button.setBackground(new Color(240, 217, 181)); // light square
                } else {
                    button.setBackground(new Color(181, 136, 99)); // dark square
                }
                button.setOpaque(true);
                button.setBorderPainted(false);
                frame.add(button);
            }

        }

        frame.setVisible(true);
    }
    public void updateBoard() {
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                buttons[i][j].setText(game.getPieceIcon(i, j));
            }
        }
    }


    int row1 = -1;
    int col1 = -1;
    boolean isSelected = false;

    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton)e.getSource();
        int row = (int) button.getClientProperty("row");
        int col = (int) button.getClientProperty("col");

        Piece clicked = game.getPiece(row, col);

        if (!isSelected) {
            if (clicked == null) return; //can't select empty square

            row1 = row;
            col1 = col;
            isSelected = true;
            resetColors();
            highlightMoves(row, col);
        }
        else {
            Piece selected = game.getPiece(row1, col1);
            if (clicked != null && clicked.getColor() == selected.getColor()) {
                row1 = row;
                col1 = col;
                resetColors();
                highlightMoves(row, col);
                return;
            }
            //attempt move
            if (game.isValid(row1, col1, row, col)) {
                game.move(row1, col1, row, col);
                updateBoard();
                resetColors();
            }
            resetColors();
            isSelected = false;
            if(game.endGame() != null){
            JOptionPane.showMessageDialog(frame,game.endGame());
        }}
    }}
