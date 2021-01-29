import java.util.*;

import static java.lang.Math.sqrt;

public class Main
{
    public static ArrayList<Color> colors = new ArrayList<>();

    public static final int[][] starting = {{1, 0, 0, 0, 0}, {0, 0, 0, 3, 1}, {3, 2, 0, 0, 2}, {0, 0, 0, 4, 5}, {4, 5, 0, 0, 0}};
    //public static final int[][] starting = {{0, 1, 2, 0, 0, 3}, {0, 0, 0, 0, 0, 0}, {0, 0, 4, 5, 0, 3}, {0, 0, 0, 6, 0, 2}, {0, 5, 4, 0, 6, 1}, {0, 0, 0, 0, 0, 0}};

    public static int[][] working = new int[5][5];

    public static int iteration = 0;

    public static void main(String [] args)
    {
        colors = scanColor(starting);
        Board board = new Board(starting.length, starting[0].length, colors.size());
        Queue queue = new Queue(colors.get(0).start_x, colors.get(0).start_y);
        setLatestDirection(queue, possibleDirections(board, queue.q.get(0)));

        System.out.println(colors);

        // Start
        int [][] st = deepCopy(starting);
        Board.printBoard(st);
        while (!queue.done) {
            st = search(st, queue);
            iteration++;
        }
        System.out.println(queue.toString());
        Board.printBoard(st);
    }

    public static void setLatestDirection(Queue queue, int direction)
    {
        queue.q.get(queue.q.size() - 1).dir = direction;
    }

    public static int[][] search(int [][] st, Queue queue)
    {
        // COMMENT: Also maybe order colors to try by how far apart they are from each other
        // Ordre croissant, to optimize

        Node latestNode = queue.q.get(queue.q.size() - 1);

        // Update board values with current queue with updateBoard()
        // Creates a more restrictive board based on what the current queue is
        //boards.add(new Board(st[0].length, st.length, colors.size(), deepCopy(st)));
        // TODO: Replace this
        Board tempBoard = new Board(st[0].length, st.length, colors.size(), deepCopy(st));
        tempBoard.b = updateBoard(st, queue);
        working = tempBoard.getB();

        //Board.printBoard(tempBoard);
        //System.out.println(queue.toString());

        int foundEnd = checkIfEnd(tempBoard, latestNode);

        // We struck gold for this color
        if (foundEnd != 0 && ((latestNode.banned_dir & foundEnd) != foundEnd))
        {
            // Go to end
            pursue(tempBoard, queue, foundEnd);

            // That would mean we're done!
            if (latestNode.color == colors.size()) {
                queue.done = true;
                return tempBoard.b;
            }
            else
            {
                // Start up next color
                latestNode.end = true;
                int start_x = colors.get(latestNode.color).start_x;
                int start_y = colors.get(latestNode.color).start_y;
                Node n = new Node(latestNode.color + 1, start_x, start_y);
                n.came_from = latestNode.came_from;
                n.dir = possibleDirections(tempBoard, n);
                n.start = true;
                queue.q.add(n);
            }
        }
        else
        {
            int directions = possibleDirections(tempBoard, latestNode);

            if (directions == 0)
            {
                backtrack(queue);
                tempBoard.b = updateBoard(starting, queue);
            }
            else
                pursue(tempBoard, queue, directions);
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

        return tempBoard.b;
    }

    // Given a queue, generate the hypothetical board
    public static int[][] updateBoard(int[][] b, Queue queue)
    {
        int[][] a = deepCopy(b);

        for (Node n : queue.q)
            a[n.x][n.y] = n.color;

        return a;
    }

    // Travel along that direction (analogous to play())
    public static void pursue(Board board, Queue queue, int direction)
    {
        int x = queue.q.get(queue.q.size() - 1).x;
        int y = queue.q.get(queue.q.size() - 1).y;

        int came_from = -1;

        if (direction >= 8) {
            x += 1;
            came_from = 8;
        }
        else if (direction >= 4) {
            y += 1;
            came_from = 4;
        }
        else if (direction >= 2) {
            x -= 1;
            came_from = 2;
        }
        else if (direction >= 1) {
            y -= 1;
            came_from = 1;
        }

        Node n = new Node(queue.q.get(queue.q.size() - 1).color, x, y);
        n.came_from = came_from;

        n.dir = possibleDirections(board, n);

        queue.q.add(n);
    }

    // Checks if node is near end point
    public static int checkIfEnd(Board board, Node n)
    {
        // Compare adjacent squares to end points of respective color

        // This would be smart except I also need to know what direction to go in
        //if (distance(n, colors.get(n.color - 1).end_x, colors.get(n.color - 1).end_y) < sqrt(2));

        int target_x = colors.get(n.color - 1).end_x;
        int target_y = colors.get(n.color - 1).end_y;

        if (n.y == target_y)
        {
            if ((n.x < board.width - 1) && (n.x + 1 == target_x) && (n.banned_dir & 8) != 8 )
                return 8;

            if ((n.x > 0) && (n.x - 1 == target_x) && (n.banned_dir & 2) != 2 )
                return 2;
        }

        if (n.x == target_x)
        {
            if ((n.y < board.height - 1) && (n.y + 1 == target_y) && (n.banned_dir & 4) != 4 )
                return 4;

            if ((n.y > 0) && (n.y - 1 == target_y) && (n.banned_dir & 1) != 1 )
                return 1;
        }

        return 0;
    }

    public static void backtrack(Queue queue)
    {
        Node latestNode = queue.q.remove(queue.q.size() - 1);
        // Make sure to delete latest node to be able to override it
        //queue.q.remove(queue.q.size() - 1);

        // It has nowhere to go, so return the original queue without the latest node
        if (latestNode.dir <= 0)
        {
            if (latestNode.start)
                queue.q.get(queue.q.size() - 2).banned_dir += latestNode.came_from;
            Node latestNode2 = queue.q.remove(queue.q.size() - 1);

            if (latestNode2.dir >= 8)
                latestNode2.banned_dir += 8;
            else if (latestNode2.dir >= 4)
                latestNode2.banned_dir += 4;
            else if (latestNode2.dir >= 2)
                latestNode2.banned_dir += 2;
            else if (latestNode2.dir >= 1)
                latestNode2.banned_dir += 1;

            queue.q.add(latestNode2);

            return;
        }
        if (latestNode.dir >= 8) // Right
            latestNode.dir -= 8;
        else if (latestNode.dir >= 4)
            latestNode.dir -= 4;
        else if (latestNode.dir >= 2)
            latestNode.dir -= 2;
        else
            latestNode.dir -= 1;

        // get rid of attempted direction and add Node back without it
        queue.q.add(latestNode);
    }

    public static int possibleDirections(Board board, Node n)
    {
        int x = n.x;
        int y = n.y;

        int directions = 0;

        int temp_banned = n.banned_dir;

        if ((n.x < board.width - 1) && (board.b[x+1][y] == 0))
            if (temp_banned < 8)
                directions += 8;
            else
                temp_banned -= 8;

        if ((n.y < board.height - 1) && (board.b[x][y+1] == 0))
            if (temp_banned < 4)
                directions += 4;
            else
                temp_banned -= 4;

        if ((n.x > 0) && (board.b[x-1][y] == 0))
            if (temp_banned < 2)
                directions += 2;
            else
                temp_banned -= 2;

        if ((n.y > 0) && (board.b[x][y-1] == 0))
            if (temp_banned == 0)
                directions += 1;

        return directions;
    }

    // Fills the board with the default values
    public static int[][] fillDefaultBoard(Board board)
    {
        int [][] b = new int[board.getWidth()][board.getHeight()];

        b[0][0] = 1;
        b[1][4] = 1;
        b[2][1] = 2;
        b[2][4] = 2;
        b[2][0] = 3;
        b[1][3] = 3;
        b[4][0] = 4;
        b[3][3] = 4;
        b[4][1] = 5;
        b[3][4] = 5;

        return b;
    }

    // Returns an arrayList of the starting and end points of every color
    public static ArrayList<Color> scanColor(int [][] b)
    {
        Map<Integer, Color> map = new HashMap<>();

        for (int i = 0; i < b.length; i++)
        {
            for (int j = 0; j < b[0].length; j++)
            {
                if (b[j][i] != 0)
                {
                    Color current = map.get(b[j][i]);
                    if (current != null)
                    {
                        current.end_x = j;
                        current.end_y = i;
                    }

                    else
                    {
                        Color new_color = new Color(b[j][i], j, i);
                        map.put(b[j][i], new_color);
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

    // TODO: apply heuristics
    public static double distance(Node n1, int x, int y)
    {
        double n1_x = n1.x;
        double n1_y = n1.y;

        return sqrt(((n1_x - x) * (n1_x - x)) + ((n1_y - y) * (n1_y - y)));
    }

    public static int[][] deepCopy(int [][] b) {
        int[][] a = new int[b.length][b[0].length];
        for (int i = 0; i < a.length; i++) {
            a[i] = Arrays.copyOf(b[i], b[i].length);
        }
        return a;
    }
}
