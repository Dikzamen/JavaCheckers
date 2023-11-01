import javax.swing.*;

public class Man {
    public int x;
    public int y;
    public int moveCount = 0;
    public int previousMove = 0;
    public boolean white;
    public boolean alive = true;
    public boolean temporarilyDead = false;

    public static String whiteMan = "\u26C2";
    public static String blackMan = "⛀";
    public CheckersSquare currentSquare;
    protected static String filename;
    public ImageIcon icon;

    public boolean recentlyMoved(){
        return JavaCheckers.moveNum - previousMove < 2;
    }

    public Man(int _x, int _y, boolean isWhite){
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

    public boolean enemyBetween(CheckersSquare square1, CheckersSquare square2){
        int directionX = Math.abs(square1.getRow() - square2.getRow()) / (square2.getRow() - square1.getRow());
        int directionY = Math.abs(square1.getCol() - square2.getCol()) / (square2.getCol() - square1.getCol());

        for (int i = square1.getRow() + directionX, j = square1.getCol() + directionY;
             i != square2.getRow() && j != square2.getCol(); i += directionX, j += directionY){
            if (JavaCheckers.jButtons[i][j].getPiece() != null)
                return true;
        }
        return false;
    }
    public boolean isValidMove(CheckersSquare square1, CheckersSquare square2) {
        if (!square2.isPlayableSquare()) return false;
        CheckersSquare oldSquare = currentSquare;
        if (square2.getPiece() != null) return false;
        if (!isPossibleMove(square2))
            return false;
        int horizontalDiff = Math.abs(x - square2.getRow());
        int verticalDiff = Math.abs(y - square2.getCol());
        int distance = horizontalDiff;
        if (!enemyBetween(square1, square2) && horizontalDiff > 1) return false;
        Man enemyPiece = temporarilyMove(square2);
        temporarilyUnmove(oldSquare, enemyPiece);
        return true;
    }

    public boolean isPossibleMove(CheckersSquare square2) {
        int horizontalDiff = Math.abs(x - square2.getRow());
        int verticalDiff = Math.abs(y - square2.getCol());
//        if ()
        if (horizontalDiff != verticalDiff) return false;
        if (Math.abs(x - square2.getRow()) + Math.abs(y - square2.getCol()) < 6)
            return true;
        return false;
    }

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
//        if (square1.getRow() != x || square1.getCol() != y){
//            System.out.println("row" + square1.getRow() +"!="+ x);
//            System.out.println("col" + square1.getCol() +"!="+ y);
//            System.out.println("coordinates wrong");
//        }
//        if (square1.getRow() != y || square1.getCol() != x){
//            System.out.println();
//        }
        if (!isValidMove(square1, square2)) {
            JavaCheckers.removeCurrentSquare();
            return false;
        }

        x = square2.getRow();
        y = square2.getCol();
        moveCount += 1;

        System.out.println(square2.getText());
        square2.setText(square1.getText());
        square1.setText("");
        square2.setPiece(this);
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

