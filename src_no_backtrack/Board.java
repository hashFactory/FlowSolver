
import java.util.Arrays;

public class Board
{
    public int width = 0;
    public int height = 0;

    public int[][] b;

    public int total_colors = 0;

    // For board to be filled
    public Board(int w, int h)
    {
        this.width = w;
        this.height = h;

        this.b = new int[width][height];
    }

    // Board where we already know which colors it'll have
    public Board(int w, int h, int t_c)
    {
        this.width = w;
        this.height = h;

        this.b = new int[width][height];

        this.total_colors = t_c;
    }

    /*
    public static Node[][] init(Board board)
    {
    }*/

    // Prints the board
    public static void printBoard(Board board)
    {
        for (int i = 0; i < board.height; i++)
        {
            for (int j = 0; j < board.width; j++)
            {
                System.out.print(board.b[j][i] + "\t");
            }
            System.out.println("\n");
        }
        System.out.println("\n");
    }
}
