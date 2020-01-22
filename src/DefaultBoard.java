public class DefaultBoard extends Board
{
    public int[][] original;

    public DefaultBoard(Board board)
    {
        super(board);
    }

    public DefaultBoard(int w, int h, int t_c)
    {
        super(w, h, t_c);
    }

    public int[][] getOriginal()
    {
        return original;
    }

    public void setOriginal(int[][] original)
    {
        this.original = original;
    }
}
