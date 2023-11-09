import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

import javax.swing.*;


public class JavaCheckers extends JPanel {

    public static int moveNum = 0;
    public static final int totalRows = 8;
    public static final int totalColumns = 8;
    static final Color blackSquare = new Color(202,170,119,255);
    static final Color whiteSquare = new Color(133,88,53,255);
    public static CheckersSquare[][] jButtons = new CheckersSquare[totalColumns][totalRows];
    public static JFrame frame;
    public static boolean chainCapture = false;
    public static boolean moveWhite = true;

    public static CheckersSquare currentSquare;

    public static void setCurrentSquare(CheckersSquare square){
        currentSquare = square;
    }
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
        showPossibleMoves();
    }
        public void createPieces() {
            for (int i = 0; i < totalRows; i++){
                for (int j = 0; j < totalColumns; j++){
                    if ((i + j) % 2 == 0) continue;
                    CheckersSquare button = getSquare(i, j);
                    assert button != null;
                    if (i < 2) {
                        button.setPiece(new Man(i, j , false) );
                        button.setText(Man.blackMan);
                    }
                    if (i > 5) {
                        button.setPiece(new Man(i, j , true) );
                        button.setText(Man.whiteMan);
                    }

                }
            }


        }

        public ArrayList<CheckersSquare> getPossibleMoves(){
            ArrayList<CheckersSquare> moves = new ArrayList<>();
            CheckersSquare enemySquare;
            if(currentSquare == null) return moves;
            Man piece = currentSquare.getPiece();
            for (int row = 0; row < totalRows; row++) {
                for (int col = 0; col < totalColumns; col++) {
                    if (piece.isValidMove(currentSquare, jButtons[row][col])) {
                        if (chainCapture) {
                            enemySquare = piece.getEnemyBetween(currentSquare, jButtons[row][col]);
                            if (enemySquare == null) continue;
                        }
                        moves.add(jButtons[row][col]);
//                        jButtons[row][col].setBackground(UIManager.getColor("control"));
                    }
                }
            }
            return moves;
        }
        public void showPossibleMoves(){
            ArrayList<CheckersSquare> possibleMoves = getPossibleMoves();
            for (CheckersSquare square: possibleMoves)
                square.setBackground(UIManager.getColor("control"));
        }

        public void createButtons() {
            Font f = new Font("serif", Font.PLAIN, 40);
            for (int i = 0; i < totalRows; i++) {
                for (int j = 0; j < totalColumns; j++) {

                    CheckersSquare button = new CheckersSquare();
                    button.setHorizontalAlignment(SwingConstants.CENTER);
                    button.setHorizontalAlignment(SwingConstants.CENTER);

                    button.setRow(i);
                    button.setCol(j);
                    button.setFont(f);

                    button.setBorder(null);
                    button.setFocusable(false);
                    button.setEnabled(false);

                    jButtons[i][j] = button;
                    add(button);
                    if ((i + j) % 2 == 0) continue;

                    button.setEnabled(true);
                    button.addActionListener(e ->
                    {
                        CheckersSquare clickedBtn = (CheckersSquare) e.getSource();
                        System.out.println("pressed " +  " moveWhite" + moveWhite);
                        if (currentSquare != null)
                            System.out.println("currentSqaure " + currentSquare.boardLocation());

                        extracted(clickedBtn);
                        recolor();

                    }
                );

                }
            }
            recolor();
        }

    private boolean extracted(CheckersSquare clickedBtn) {
        if (currentSquare == clickedBtn && !chainCapture) {
            removeCurrentSquare();
            chainCapture = false;
        } else if (currentSquare != null && clickedBtn.getPiece() != null) {
            if (clickedBtn.getPiece().isWhite == currentSquare.getPiece().isWhite && !chainCapture)
                currentSquare = clickedBtn;

        } else if (currentSquare == null){
            if (!Objects.equals(clickedBtn.getText(), "")
                    && clickedBtn.getPiece().isWhite == moveWhite
            ) {
                setCurrentSquare(clickedBtn);
                chainCapture = false;
                System.out.println("\n\n\n");
                showPossibleMoves();
            }
        }
        else if (clickedBtn.getPiece() != null && !chainCapture){
            if (clickedBtn.getPiece().isWhite == currentSquare.getPiece().isWhite){
                setCurrentSquare(clickedBtn);
                chainCapture = false;
            }
        }
        else {
            CheckersSquare oldSquare = JavaCheckers.currentSquare;
            Man piece = JavaCheckers.currentSquare.getPiece();
            boolean movePossible;
            if (chainCapture)
                movePossible = piece.move(clickedBtn, true);
            else
                movePossible = piece.move(clickedBtn);

            if (!movePossible) {
                if (chainCapture) {
                    setCurrentSquare(oldSquare);
                }
                return true;
            }
            CheckersSquare enemySquare = piece.getEnemyBetween(oldSquare, clickedBtn);
            if (enemySquare != null) enemySquare.getPiece().die();

            boolean victory = checkForVictory();
            if (victory){
                String color = "black";
                if (moveWhite) color = "white";

                frame.setTitle(color + " won");
                endGame();
                return true;
            }
            chainCapture = false;
            if (enemySquare != null)
            {
                chainCapture = true;
                setCurrentSquare(clickedBtn);
                showPossibleMoves();
            }
            System.out.println("end of the move chainCapture="+ chainCapture + currentSquare);
            if (getPossibleMoves().isEmpty()) {
                System.out.println("possible moves empty" + currentSquare);
                changePlayer();
                return true;
            }
            else {
                setCurrentSquare(clickedBtn);
            }
            if (moveWhite)
                frame.setTitle("white moves");
            else {
                frame.setTitle("black moves");
            }
            moveNum += 1;

        }
        return false;
    }

    private static void changePlayer() {
        System.out.println("changed move");
        moveWhite = !moveWhite;
        moveNum++;
        removeCurrentSquare();
    }


    private void endGame(){
        for (int i = 0; i < totalRows; i++){
            for (int j = 0; j < totalColumns; j++){
                jButtons[i][j].setEnabled(false);
            }
        }
    }
    private boolean checkForVictory() {
        ArrayList<Man> enemyTeam = getEnemyTeam();
        return enemyTeam.isEmpty();
    }

    public static ArrayList<Man> getEnemyTeam(){
        ArrayList<Man> team = new ArrayList<>();
        ArrayList<Man> fullTeam = Man.whiteTeam;
        if (moveWhite) fullTeam = Man.blackTeam;
        for(Man m: fullTeam){
            if (m.alive) team.add(m);
        }
        return team;
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