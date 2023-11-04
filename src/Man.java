import javax.swing.*;
import java.util.ArrayList;

public class Man {
    public int x;
    public int y;
    public int moveCount = 0;
    public int previousMove = 0;
    private final int speed = 1;
    public boolean isWhite;
    public boolean alive = true;
    public boolean temporarilyDead = false;

    public static String whiteMan = "\u26C2";
    public static String blackMan = "⛀";
    public CheckersSquare currentSquare;
    protected static String filename;
    public static ArrayList<Man> whiteTeam = new ArrayList<>();
    public static ArrayList<Man> blackTeam = new ArrayList<>();
    public ImageIcon icon;

    public boolean recentlyMoved(){
        return JavaCheckers.moveNum - previousMove < 2;
    }

    public Man(int _x, int _y, boolean isWhite){
        System.out.println("man constr " + _x + " " + _y);
        x = _x;
        y = _y;
        this.isWhite = isWhite;
        setIcon();
        setButton();
        if (this.isWhite) whiteTeam.add(this);
        else blackTeam.add(this);
    }
    public void setIcon(){
        String color = "white";
        if (!isWhite) color = "black";
        String name = getFilename() + "_" + color;
        icon = new ImageIcon(name);
    }
    public void setButton(){
        CheckersSquare button = JavaCheckers.jButtons[x][y];
        button.setPiece(this);
        button.setIcon(icon);
        currentSquare = button;
    }

    public String getFilename(){
        return filename;
    }
    public Man temporarilyMove(CheckersSquare square){
        Man piece = square.getPiece();
        if (piece != null){
            piece.temporarilyDead = true;
        }
        currentSquare = square;
        currentSquare.setPiece(this);
        return piece;
    }
    public void temporarilyUnmove(CheckersSquare square, Man enemyPiece){
        CheckersSquare _currentSquare = currentSquare;
        currentSquare.setPiece(enemyPiece);
        currentSquare = square;
        currentSquare.setPiece(this);
        Man piece = _currentSquare.getPiece();
        if (piece != null){
            piece.temporarilyDead = false;
        }
    }

    public CheckersSquare getEnemyBetween(CheckersSquare square1, CheckersSquare square2){
        int directionX = Math.abs(square1.getRow() - square2.getRow()) / (square2.getRow() - square1.getRow());
        int directionY = Math.abs(square1.getCol() - square2.getCol()) / (square2.getCol() - square1.getCol());

        for (int i = square1.getRow() + directionX, j = square1.getCol() + directionY;
             i != square2.getRow() && j != square2.getCol(); i += directionX, j += directionY){
            CheckersSquare square = JavaCheckers.jButtons[i][j];
            if (square.getPiece() != null && square.getPiece().isWhite != isWhite)
                return JavaCheckers.jButtons[i][j];
        }
        return null;
    }
    public boolean isEnemyBetween(CheckersSquare square1, CheckersSquare square2){
        return getEnemyBetween(square1, square2) != null;
    }
    public boolean isValidMove(CheckersSquare square1, CheckersSquare square2) {
        if (!square2.isPlayableSquare()) return false;
        CheckersSquare oldSquare = currentSquare;
        if (square2.getPiece() != null) return false;
        System.out.println("from " + square1.boardLocation() + " to " + square2.boardLocation());
        if (!isPossibleMove(square2))
            return false;
        int horizontalDiff = Math.abs(x - square2.getRow());
        int verticalDiff = Math.abs(y - square2.getCol());
        int distance = horizontalDiff;
        System.out.println("direction" + getDirection() + " white=" + isWhite);
        if (!isEnemyBetween(square1, square2) && horizontalDiff > 1) return false;
        Man enemyPiece = temporarilyMove(square2);
        temporarilyUnmove(oldSquare, enemyPiece);
        return true;
    }

    public boolean isPossibleMove(CheckersSquare square2) {
        int horizontalDiff = x - square2.getRow();
        int verticalDiff = y - square2.getCol();
        if (Math.abs(horizontalDiff) != Math.abs(verticalDiff)) return false;
        System.out.println("dir = " + getDirection() + " sign=" + Math.signum(getDirection()) + " vertDif=" + verticalDiff);
        if (Math.signum(getDirection()) == Math.signum(horizontalDiff) && Math.abs(horizontalDiff) < 2) return false;
        if (Math.abs(x - square2.getRow()) + Math.abs(y - square2.getCol()) < 6)
            return true;
        return false;
    }

    private boolean isTeamMate(CheckersSquare square2) {
        return square2.getPiece() != null && square2.getPiece().isWhite == isWhite;
    }

    public int getDirection(){
        int multiplier = isWhite ? 1 : 0;
        multiplier *= 2;
        multiplier -= 1;
        return -multiplier;
    }

    public void promote(){
        int xNew = x;
        int yNew = y;
        System.out.println("xNew"  + xNew + " yNew=" + yNew);
        die();
        new King(xNew, yNew, isWhite);
    }
    public boolean move(CheckersSquare square1, CheckersSquare square2){

        if (!isValidMove(square1, square2)) {
            JavaCheckers.removeCurrentSquare();
            return false;
        }

        x = square2.getRow();
        y = square2.getCol();
        moveCount += 1;

        square2.setText(square1.getText());
        square1.setText("");
        square2.setPiece(this);
        JavaCheckers.currentSquare.removeIcon();
        JavaCheckers.currentSquare.removePiece();
        JavaCheckers.removeCurrentSquare();
        previousMove = JavaCheckers.moveNum;
        System.out.println("moved" + this.getClass() + "move = "+ previousMove);
        currentSquare = square2;
        if (isWhite && x == 0)
            promote();
        else if (!isWhite && x == 7)
            promote();

        return true;
    }
    public void die(){
        CheckersSquare square = JavaCheckers.getSquare(x, y);
        if (square != null) {
            square.removePiece();
            square.removeIcon();
            square.setText("");
        }
        alive = false;
        x = -1;
        y = -1;
    }
    public String location(){
        return "(" + x + "," + y + ")";
    }
}

