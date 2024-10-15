import java.util.ArrayList;
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
        StringBuilder s = new StringBuilder();
        for(int i = 0; i < 3; i++)
        {
            for(int j = 0; j <3; j++)
            {
                if(this.board[i][j] == 0)
                    s.append(" ");
                else
                    s.append(this.board[i][j]);
            }
            s.append('\n');
        }
        return s.toString();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Board board1)) return false;
        return Objects.deepEquals(board, board1.board);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Board cloned = (Board) super.clone();
        cloned.board = new int[dim][dim];
        for (int i = 0; i < dim; i++) {
            cloned.board[i] = Arrays.copyOf(this.board[i], dim);
        }
        return cloned;
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }


    @Override
    public List<Ilayout> children() {
        List<Ilayout> children=new ArrayList<>();
        int zero_row = -1;
        int zero_col = -1;
        int[] offsets = {-1,1};
        //Board A = new Board();
        //Find 0 coordinates
        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board[i].length; j++) {
                if(this.board[i][j] == 0) {
                    zero_row = i;
                    zero_col = j;
                    break;
                }
            }
        }
        if(zero_row == -1){
            return null;
        }
        for (int offset : offsets) {
            if ((zero_col + offset < dim) && (zero_col + offset >= 0)) {
                try {
                    Board A = (Board) this.clone();
                    int temp = A.board[zero_row][zero_col + offset];
                    A.board[zero_row][zero_col + offset] = 0;
                    A.board[zero_row][zero_col] = temp;
                    children.add(A);
                } catch (CloneNotSupportedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        for (int offset : offsets) {
            if ((zero_row + offset < dim) && (zero_row + offset >= 0)) {
                try {
                    Board B = (Board) this.clone();
                    int temp = B.board[zero_row + offset][zero_col];
                    B.board[zero_row + offset][zero_col] = 0;
                    B.board[zero_row][zero_col] = temp;
                    children.add(B);
                } catch (CloneNotSupportedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return children;
    }

    @Override
    public boolean isGoal(Ilayout l) {
        return this.equals(l);
    }

    @Override
    public double getG() {
        return 1;
    }

    @Override
    public double getH(Ilayout l) {
        return 0;
    }
}
