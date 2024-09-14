import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Board implements Ilayout, Cloneable{

    private static final int dim=3;
    private int board[][];

    public Board(){
        board=new int[dim][dim];
    }
    public Board(String str) throws IllegalStateException{
        if(str.length() != dim*dim) throw new IllegalStateException("Invalid arg in Board constructor");
        board=new int[dim][dim];
        int si = 0;
        for(int i=0; i<dim; i++){
            for(int j=0; j<dim; j++){
                board[i][j] = Character.getNumericValue(str.charAt(si++));
            }
        }
    }

    public String toString(){
        //TODO
        return null;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Board board1)) return false;
        return Objects.deepEquals(board, board1.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }


    @Override
    public List<Ilayout> children() {
        return List.of();
    }

    @Override
    public boolean isGoal(Ilayout l) {
        return false;
    }

    @Override
    public double getG() {
        return 0;
    }
}
