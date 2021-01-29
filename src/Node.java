

public class Node
{
    // Contains all info about each step of the process
    public int color = 0;

    public int x = -1;
    public int y = -1;

    public int came_from = -1;
    public int dir = -1;
    public int banned_dir = 0;

    public boolean start = false;
    public boolean end = false;

    public Node(int c)
    {
        this.color = c;
    }

    // For when we don't know possible directions
    public Node(int c, int x_coord, int y_coord)
    {
        this.color = c;

        this.x = x_coord;
        this.y = y_coord;
    }

    // For when we do
    public Node(int c, int x_coord, int y_coord, int dir_indicator)
    {
        this.color = c;

        this.x = x_coord;
        this.y = y_coord;

        this.dir = dir_indicator;
    }

    @Override
    public String toString()
    {
        return "Node{" +
                "color=" + color +
                ", x=" + x +
                ", y=" + y +
                ", dir=" + dir +
                ", banned_dir=" + banned_dir +
                '}';
    }
}
