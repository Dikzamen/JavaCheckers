import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;


public class JavaCheckers extends JPanel {

    public static int moveNum = 0;
    public static final int totalRows = 8;
    public static final int totalColumns = 8;
    static final Color blackSquare = new Color(202,170,119,255);
    static final Color whiteSquare = new Color(133,88,53,255);
    public static CheckersSquare[][] jButtons = new CheckersSquare[totalColumns][totalRows];
    public static JFrame frame;
    boolean moveWhite = true;

    public static CheckersSquare currentSquare;

    public static void removeCurrentSquare(){
        currentSquare = null;
    }
    public static CheckersSquare getSquare(int row, int col){
        if (row < 0 || row >= totalRows || col < 0 || col > totalColumns) return null;
        return jButtons[row][col];
    }
    public JavaCheckers() throws InterruptedException {
        GridLayout ticTacToeGridLayout = new GridLayout(totalRows, totalColumns);
        setLayout(ticTacToeGridLayout);
        createButtons();
        createPieces();
    }

    public void recolor(){
        for (int i = 0; i < totalColumns; i++) {
            for (int j = 0; j < totalRows; j++) {
                JButton button = jButtons[i][j];
                if ((i + j) % 2 == 0)
                    button.setBackground(blackSquare);
                else
                    button.setBackground(whiteSquare);

            }
        }
    }
        public void createPieces() {
            for (int i = 0; i < totalColumns; i++){
            }


        }

        public void createButtons() {
            Font f = new Font("serif", Font.PLAIN, 40);
        for (int i = 0; i < totalRows; i++) {
            for (int j = 0; j < totalColumns; j++) {

                CheckersSquare button = new CheckersSquare();
                button.setHorizontalAlignment(SwingConstants.CENTER);
                button.setHorizontalAlignment(SwingConstants.CENTER);
//                button.setTe
                button.setRow(i);
                button.setCol(j);
                button.setFont(f);
                button.setText("\u26C0");
                button.setBorder(null);
                button.setFocusable(false);


                jButtons[i][j] = button;

                String text = i + ",  " + j;
                if (i + j % 2 == 0)
                    button.setText(text);


                button.addActionListener(e ->
                {
                    CheckersSquare clickedBtn = (CheckersSquare) e.getSource();
                    recolor();

                    if (currentSquare == clickedBtn) {
                        currentSquare = null;
                    }
                    else if (currentSquare == null || (clickedBtn.getPiece() != null && clickedBtn.getPiece().white == currentSquare.getPiece().white)){
                        if (clickedBtn.getIcon() != null && clickedBtn.getPiece().white == moveWhite) {
                            currentSquare = clickedBtn;
                            Man piece = currentSquare.getPiece();
                            for (int row = 0; row < totalRows; row++ ){
                                for (int col = 0; col < totalColumns; col++ ){

                                    if (piece.isValidMove(clickedBtn, jButtons[row][col])) {
                                        jButtons[row][col].setBackground(UIManager.getColor("control"));
                                    }
                                }

                            }
                        }
                        else {
                        System.out.println("what???");

                        }
                    }

                    else {

                        Man piece = JavaCheckers.currentSquare.getPiece();
                        Man targetPiece = clickedBtn.getPiece();
                        boolean move = piece.move(JavaCheckers.currentSquare,clickedBtn);
                        if (!move) return;
                        boolean victory = checkForVictory();
                        if (victory){
                            String color = "black";
                            if (moveWhite) color = "white";

                            frame.setTitle(color + " won");
                            endGame();
                            return;
                        }
                        moveWhite = !moveWhite;
                        if (moveWhite)
                            frame.setTitle("white moves");
                        else {
                            frame.setTitle("black moves");
                        }
                        moveNum += 1;




                    }


                }
                );

                add(button);
            }
        }
        recolor();
    }


    private void endGame(){
        for (int i = 0; i < totalRows; i++){
            for (int j = 0; j < totalColumns; j++){
                jButtons[i][j].setEnabled(false);
            }
        }
    }
    private boolean checkForVictory() {
        return false;
    }


    public static void main(String[] args) throws InterruptedException {
        JFrame jFrame = new JFrame("Java Chess");

        JavaCheckers.frame = jFrame;
        jFrame.getContentPane().add(new JavaCheckers());
        jFrame.setBounds(500, 500, 600, 550);
        jFrame.setVisible(true);
        jFrame.setLocationRelativeTo(null);
        jFrame.setResizable(false);

    }
}