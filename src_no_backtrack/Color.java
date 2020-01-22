

public class Color
{
    public int color = -1;

    public int start_x = -1;
    public int start_y = -1;

    public int end_x = -1;
    public int end_y = -1;

    public Color(int c)
    {
        this.color = c;
    }

    public Color(int color, int start_x, int start_y)
    {
        this.color = color;

        this.start_x = start_x;
        this.start_y = start_y;
    }

    // Color is a container for where each color starts and stops
    public Color(int color, int start_x, int start_y, int end_x, int end_y)
    {
        this.color = color;

        this.start_x = start_x;
        this.start_y = start_y;

        this.end_x = end_x;
        this.end_y = end_y;
    }

    @Override
    public String toString()
    {
        return "Color{" +
                "color=" + color +
                ", start_x=" + start_x +
                ", start_y=" + start_y +
                ", end_x=" + end_x +
                ", end_y=" + end_y +
                '}';
    }
}
