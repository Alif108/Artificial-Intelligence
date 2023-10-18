import java.util.ArrayList;

class Position
{
    public int x;
    public int y;

    Position(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
}

public class Node {

   private int[][] matrix;
   private int depth;
   private int f_val;
   private int n;
   private Node parent;

   Node(int[][] matrix, int depth, int f_val, int n, Node parent)
   {
       this.matrix = matrix;
       this.depth = depth;
       this.f_val = f_val;
       this.n = n;
       this.parent = parent;
   }

   public int[][] get_matrix()
   {
       return matrix;
   }

   public int get_depth()
   {
       return depth;
   }

   public Node get_parent()
   {
       return this.parent;
   }

   public int get_fval()
   {
       return f_val;
   }

   public void set_fval(int f_val)
   {
       this.f_val = f_val;
   }

   // returns the position of a number in the matrix
   public Position find(int x)
   {
       for(int i=0; i<n; i++)
       {
           for(int j=0; j<n; j++)
           {
               if (matrix[i][j] == x)
                   return (new Position(i, j));
           }
       }
       return null;
   }

   // performs a single move of tile in the puzzle and returns the updated puzzle
   public Node single_move(int blank_x, int blank_y, int x2, int y2)
   {
       if((x2>=0 && x2<n) && (y2>=0 && y2<n))
       {
           int[][] temp = copy_puzzle(matrix);                  // copying the current state of the puzzle
           int blank = temp[blank_x][blank_y];                  // keeping blank in a temp
           temp[blank_x][blank_y] = temp[x2][y2];               // putting the other number in blank's spot
           temp[x2][y2] = blank;                                // putting blank in the other number's spot

           return new Node(temp, depth + 1, 0, n, this);            // generating a new child
       }
       return null;
   }

   // copies one matrix to another and returns the new one
   public int[][] copy_puzzle(int[][] puzzle)
   {
       int[][] clone = new int[n][n];

       for(int i=0; i<n; i++)
       {
           for(int j=0; j<n; j++) {
               clone[i][j] = puzzle[i][j];
           }
       }
       return clone;
   }

   // generates children of one node
   public ArrayList<Node> generate_children()
   {
       ArrayList<Node> children = new ArrayList<Node>();

       Position blank = find(0);                                          // getting the position of "*"
       int blank_x = blank.x;                                               // x position of blank
       int blank_y = blank.y;                                               // y position of blank

       ArrayList<Position> moves = new ArrayList<Position>();
       moves.add(new Position(blank_x, blank_y+1));
       moves.add(new Position(blank_x, blank_y-1));
       moves.add(new Position(blank_x-1, blank_y));
       moves.add(new Position(blank_x+1, blank_y));                     // 4 moves for a blank

       moves.forEach(position ->
       {
           Node child_node = single_move(blank_x, blank_y, position.x, position.y);
           if(child_node != null)
           {
               if(this.parent!=null)                                        // if it is not the initial node
               {
                   if (!is_same_node(this.parent, child_node))              // if the parent and child are the same node, no need to add it on the list
                       children.add(child_node);
               }
               else                                                         // if it is the initial node
               children.add(child_node);                                // adding the new child in the children list
           }
       });
       return children;
   }

   // prints the matrix
   public void print()
   {
       for(int i=0; i<n; i++)
       {
           for(int j=0; j<n; j++)
           {
               if(matrix[i][j] == 0)
                   System.out.print("* ");
               else
                System.out.print(matrix[i][j] + " ");
           }
           System.out.print("\n");
       }
   }

   // returns true if both node's matrices are same
   public boolean is_same_node(Node x, Node y)
   {
       int[][] x_matrix = x.get_matrix();
       int[][] y_matrix = y.get_matrix();

       for(int i=0; i<n; i++)
       {
           for(int j=0; j<n; j++)
           {
               if(x_matrix[i][j] != y_matrix[i][j])       // if the characters are not same
                   return false;
           }
       }
       return true;
   }
}