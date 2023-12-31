import javax.swing.*;

public class CheckersSquare extends JButton {
    int row;
    int col;

    Man piece;

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void setRow(int row){
        this.row = row;
    }
    public void setCol(int col) {
        this.col = col;
    }

    public Man getPiece() {
        return piece;
    }

    public boolean isPlayableSquare(){
        return (getRow() + getCol()) % 2 != 0;
    }

    public void setPiece(Man piece) {
        this.piece = piece;
    }
    public void removePiece() {
        setPiece(null);
    }
    public void removeIcon(){
        setIcon(null);
    }
    public String boardLocation(){
        return String.format("(%d, %d)", getCol(), getRow());
    }

}
