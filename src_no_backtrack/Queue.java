
import java.util.ArrayList;

public class Queue
{
    // Contains queue of nodes that we'll be going through
    public ArrayList<Node> q = new ArrayList<>();

    /*public Queue()
    {

    }*/

    // Constructor for queue where we specify the starting x and y
    public Queue(int start_x, int start_y)
    {
        Node n = new Node(1, start_x, start_y);
        this.q.add(n);
    }

    @Override
    public String toString()
    {
        return "Queue{" +
                "q=" + q +
                '}';
    }
}
