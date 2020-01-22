import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

public class Main
{
    public static ArrayList<Color> colors = new ArrayList<>();

    public static Board referenceBoard;

    public static boolean keepGoing = true;

    public static int iteration = 0;

    public static void main(String [] args)
    {
        Board board = new Board(5, 5, 5);
        board = fillDefaultBoard(board);

        referenceBoard = board;

        Board.printBoard(board);
        colors = scanColor(board);

        Queue queue = new Queue(colors.get(0).start_x, colors.get(0).start_y);

        queue = setLatestDirection(queue, possibleDirections(board, queue.q.get(0)));

        // Start

        System.out.println(colors);

        search(board, queue);
    }

    public static Queue setLatestDirection(Queue queue, int direction)
    {
        Node latestNode = queue.q.get(queue.q.size() - 1);

        queue.q.remove(queue.q.size() - 1);

        latestNode.dir = direction;

        queue.q.add(latestNode);
        return queue;
    }

    // TODO
    public static Queue search(Board board, Queue queue)
    {
        // COMMENT: Also maybe order colors to try by how far apart they are from each other
        // Ordre croissant, to optimize

        if (!keepGoing)
            return queue;

        Node latestNode = queue.q.get(queue.q.size() - 1);

        // Update board values with current queue with updateBoard()
        // Creates a more restrictive board based on what the current queue is
        Board tempBoard = referenceBoard;
        tempBoard = updateBoard(referenceBoard, queue);

        Board.printBoard(tempBoard);
        System.out.println(queue.toString());

        int foundEnd = checkIfEnd(tempBoard, latestNode);

        // We struck gold for this color
        if (foundEnd != 0)
        {
            // Go to end
            queue = pursue(board, queue, foundEnd);

            // That would mean we're done!
            if (latestNode.color == board.total_colors)
                keepGoing = false;
            else
            {
                // Start up next color
                int start_x = colors.get(latestNode.color).start_x;
                int start_y = colors.get(latestNode.color).start_y;
                Node n = new Node(latestNode.color + 1, start_x, start_y);
                queue.q.add(n);
            }
        }
        else
        {
            int directions = possibleDirections(tempBoard, latestNode);

            if (directions == 0)
            {
                queue = backtrack(queue);
            }
            else
            {
                queue = pursue(tempBoard, queue, directions);
            }
        }

        // Check if next to end node
        // if so:
        //      Create end node and add to queue, then create new node with next color and call search again
        //      Also add connection for if node connected is last color: exit with success

        // Check possible directions of latest node
        // if latestNode has 0 directions to go in
        //      backtrack() and call search again
        // if latestNode has > 0 directions to go in
        //      create new node with first possible direction and call search again

        return search(board, queue);

    }

    // TODO
    // Given a queue, generate the hypothetical board
    public static Board updateBoard(Board board, Queue queue)
    {
        for (Node n : queue.q)
        {
            board.b[n.x][n.y] = n.color;
        }

        return board;
    }

    // TODO
    public static Queue pursue(Board board, Queue queue, int direction)
    {
        int x = queue.q.get(queue.q.size() - 1).x;
        int y = queue.q.get(queue.q.size() - 1).y;

        if (direction >= 8)
            x += 1;

        else if (direction >= 4)
            y += 1;

        else if (direction >= 2)
            x -= 1;

        else if (direction >= 1)
            y -= 1;

        Node n = new Node(queue.q.get(queue.q.size() - 1).color, x, y);

        n.dir = possibleDirections(board, n);

        queue.q.add(n);
        return queue;
    }

    // Checks if node is near end point
    // TODO
    public static int checkIfEnd(Board board, Node n)
    {
        // Compare adjacent squares to end points of respective color

        // This would be smart except I also need to know what direction to go in
        if (distance(n, colors.get(n.color - 1).end_x, colors.get(n.color - 1).end_y) < sqrt(2))
            // Then end node is adjacent
            ;

        int target_x = colors.get(n.color - 1).end_x;
        int target_y = colors.get(n.color - 1).end_y;

        if (n.y == target_y)
        {
            if ((n.x < board.width - 1) && (n.x + 1 == target_x))
                return 8;

            if ((n.x > 0) && (n.x - 1 == target_x))
                return 2;
        }

        if (n.x == target_x)
        {
            if ((n.y < board.height - 1) && (n.y + 1 == target_y))
                return 4;

            if ((n.y > 0) && (n.y - 1 == target_y))
                return 1;
        }

        return 0;
    }

    public static Queue backtrack(Queue queue)
    {
        Node latestNode = queue.q.get(queue.q.size() - 1);
        // Make sure to delete latest node to be able to override it
        queue.q.remove(queue.q.size() - 1);

        // It has nowhere to go, so return the original queue without the latest node
        if (latestNode.dir == 0)
            return queue;

        if (latestNode.dir >= 8) // Right
            latestNode.dir -= 8;

        else if (latestNode.dir >= 4)
            latestNode.dir -= 4;

        else if (latestNode.dir >= 2)
            latestNode.dir -= 2;

        else if (latestNode.dir >= 1)
            latestNode.dir -= 1;

        // get rid of attempted direction and add Node back without it
        queue.q.add(latestNode);
        return queue;
    }

    public static int possibleDirections(Board board, Node n)
    {
        int x = n.x;
        int y = n.y;

        int directions = 0;

        if ((n.x < board.width - 1) && (board.b[x+1][y] == 0))
            directions += 8;

        if ((n.y < board.height - 1) && (board.b[x][y+1] == 0))
            directions += 4;

        if ((n.x > 0) && (board.b[x-1][y] == 0))
            directions += 2;

        if ((n.y > 0) && (board.b[x][y-1] == 0))
            directions += 1;

        return directions;
    }

    public static Node stripDirection(Node n, int d)
    {
        if (n.dir >= d)
        {
            n.dir -= d;
        }
        else
        {
            System.out.println("ERROR: Node trying to get rid of a direction it doesn't have");
        }
        return n;
    }

    // Fills the board with the default values
    public static Board fillDefaultBoard(Board board)
    {
        board.b[0][0] = 1;
        board.b[1][4] = 1;
        board.b[2][1] = 2;
        board.b[2][4] = 2;
        board.b[2][0] = 3;
        board.b[1][3] = 3;
        board.b[4][0] = 4;
        board.b[3][3] = 4;
        board.b[4][1] = 5;
        board.b[3][4] = 5;

        return board;
    }

    // Returns an arrayList of the starting and end points of every color
    public static ArrayList<Color> scanColor(Board board)
    {
        Map<Integer, Color> map = new HashMap<>();

        for (int i = 0; i < board.height; i++)
        {
            for (int j = 0; j < board.width; j++)
            {
                if (board.b[j][i] != 0)
                {
                    Color current = map.get(board.b[j][i]);
                    if (current != null)
                    {
                        current.end_x = j;
                        current.end_y = i;
                    }

                    else
                    {
                        Color new_color = new Color(board.b[j][i], j, i);
                        map.put(board.b[j][i], new_color);
                    }
                }
            }
        }

        return new ArrayList<>(map.values());
    }

    public static double distance(Node n1, Node n2)
    {
        return distance(n1, n2.x, n2.y);
    }

    public static double distance(Node n1, int x, int y)
    {
        double n1_x = (double)n1.x;
        double n1_y = (double)n1.y;

        double n2_x = (double)x;
        double n2_y = (double)y;

        return sqrt(((n1_x - n2_x) * (n1_x - n2_x)) + ((n1_y - n2_y) * (n1_y - n2_y)));
    }
}
