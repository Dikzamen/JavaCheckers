import javax.swing.*;

public abstract class King {
    public int x;
    public int y;
    public int moveCount = 0;
    public int previousMove = 0;
    public boolean white;
    public boolean alive = true;
    public boolean temporarilyDead = false;

    String[] colors = new String[]{"white", "black"};
    public CheckersSquare currentSquare;
    protected static String filename;
    public ImageIcon icon;

    public boolean recentlyMoved(){
        return JavaCheckers.moveNum - previousMove < 2;
    }

    public King(int _x, int _y, boolean isWhite){
        x = _x;
        y = _y;
        white = isWhite;
        setIcon();
        setButton();

    }
    public void setIcon(){
        String color = "white";
        if (!white) color = "black";
        String name = getFilename() + "_" + color;
        icon = new ImageIcon(name);
    }
    public void setButton(){
        CheckersSquare button = JavaCheckers.jButtons[x][y];
        button.setIcon(icon);
        currentSquare = button;
    }

    public String getFilename(){
        return filename;
    }

    public boolean isValidMove(CheckersSquare square1, CheckersSquare square2) {
        CheckersSquare oldSquare = currentSquare;
        if (isTeamMate(square2))
            return false;
        if (!isPossibleMove(square2))
            return false;
        return true;
    }

    abstract boolean isPossibleMove(CheckersSquare square2);

    private boolean isTeamMate(CheckersSquare square2) {
        return square2.getPiece() != null && square2.getPiece().white == white;
    }

    public int getDirection(){
        int multiplier = white ? 1 : 0;
        multiplier *= 2;
        multiplier -= 1;
        return -multiplier;
    }
    public boolean move(CheckersSquare square1, CheckersSquare square2){
        if (!isValidMove(square1, square2)) {
            JavaCheckers.removeCurrentSquare();
            return false;
        }

        x = square2.getRow();
        y = square2.getCol();
        moveCount += 1;
        square2.setIcon(icon);
        JavaCheckers.currentSquare.removeIcon();
        JavaCheckers.currentSquare.removePiece();
        JavaCheckers.removeCurrentSquare();
        previousMove = JavaCheckers.moveNum;
        System.out.println("moved" + this.getClass() + "move = "+ previousMove);
        currentSquare = square2;
        return true;

    }
    public void die(){
//        if(ChessJava.currentSquare != null && ChessJava.currentSquare.getPiece() == this) {
//            ChessJava.currentSquare.removePiece();
//            ChessJava.currentSquare.removeIcon();
//
//        }
        CheckersSquare square = JavaCheckers.getSquare(x, y);
        if (square != null) {
            square.removePiece();
            square.removeIcon();
        }
        alive = false;
        x = -1;
        y = -1;
    }
    public String location(){
        return "(" + x + "," + y + ")";
    }
}

