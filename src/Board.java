
import java.util.Arrays;

public class Board
{
    public int width = 0;
    public int height = 0;

    public int[][] b;
    public int[][] b_scratch;

    public int total_colors = 0;

    // For board to be filled
    public Board(int w, int h)
    {
        this.width = w;
        this.height = h;

        this.b = new int[width][height];
        this.b_scratch = new int[width][height];
    }

    public Board(Board board)
    {
        this(board.getWidth(), board.getHeight(), board.getTotal_colors(), board.getB());
    }

    // Board where we already know which colors it'll have
    public Board(int w, int h, int t_c)
    {
        this.width = w;
        this.height = h;

        this.b = new int[width][height];
        this.b_scratch = new int[width][height];

        this.total_colors = t_c;
    }

    // Just useful for deep copy of object
    public Board(int w, int h, int t_c, int[][] b)
    {
        this.width = w;
        this.height = h;

        this.b = new int[width][height];
        this.b_scratch = new int[width][height];

        this.total_colors = t_c;
    }

    public static void printBoard(int [][] st)
    {
        for (int i = 0; i < st[0].length; i++)
        {
            for (int[] ints : st)
                System.out.print(ints[i] + " ");
            System.out.println();
        }
        System.out.println();
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public int[][] getB()
    {
        return b;
    }

    public int getTotal_colors()
    {
        return total_colors;
    }
}