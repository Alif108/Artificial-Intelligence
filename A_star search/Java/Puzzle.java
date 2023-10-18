import java.util.*;

public class Puzzle {
    private int size;

    private Node init_state;
    private Node goal_state;

    Puzzle(int size, Node init_state, Node goal_state)
    {
        this.size = size;
        this.init_state = init_state;
        this.goal_state = goal_state;
    }

    // gives the count of inversion in the init node
    public int count_inversion()
    {
        // -- inversion count is the occurrence of smaller numbers after a certain number -- //
        int inversion_count = 0;
        int[][] init_matrix = init_state.get_matrix();
        ArrayList<Integer> array_of_num = new ArrayList<>();

        // converting to a 1d array
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                    array_of_num.add(init_matrix[i][j]);
            }
        }

        // iterating over the array
        for (int i = 0; i < array_of_num.size(); i++) {
            int x = array_of_num.get(i);
            for (int j = i + 1; j < array_of_num.size(); j++) {
                if (array_of_num.get(j) < x && array_of_num.get(j)!=0)                         // if there is any smaller number after index of x
                {
                    inversion_count += 1;
                }
            }
        }
        return inversion_count;
    }

    // returns if the initial puzzle is solvable or not
    public boolean is_solvable()
    {
        if(size %2 != 0)                                // if grid size is odd
        {
            if(count_inversion() %2 == 0)               // if inversion is even -> solvable
                return true;
            else                                        // if inversion is odd -> not solvable
                return false;
        }
        else                                            // if grid size is even
        {
            Position blank_pos = init_state.find(0);

            if((blank_pos.x %2 == 0) && (count_inversion() %2 != 0))            // if blank is in even row and inversion count is odd -> solvable
                return true;
            else if((blank_pos.x %2 !=0 ) && (count_inversion() %2 == 0 ))      // if blank is in odd row and inversion count is even -> solvable
                return true;
            else
                return false;
        }
    }

    // prints the path
    public void print_path(Node state)
    {
        ArrayList<Node> path = new ArrayList<>();
        path.add(state);                                        // adding the last node

        while(state.get_parent() != null)
        {
            path.add(state.get_parent());                       // adding the parent
            state = state.get_parent();                         // setting pointer to parent
        }

//        Collections.reverse(path);                              // reversing the path
//
//        for(int i=0; i< path.size(); i++) {
//            path.get(i).print();
//            System.out.print("\n");
//        }
        for(int i=path.size()-1; i>=0; i--)
        {
            path.get(i).print();
            System.out.print("\n");
        }
    }

    // calculate hamming distance of two nodes
    public int hamming_distance(Node target, Node goal)
    {
        int sum = 0;
        int[][] target_matrix = target.get_matrix();
        int[][] goal_matrix = goal.get_matrix();

        for(int i=0; i<size; i++)
        {
            for(int j=0; j<size; j++)
            {
                // if the entry is not blank and not same as the goal entry
                if((target_matrix[i][j] != goal_matrix[i][j]) && (target_matrix[i][j] !=0 ))
                    sum += 1;
            }
        }
        return sum;
    }

    // calculate f_n using hamming distance
    public int hamming_f_n(Node target, Node goal)
    {
        return (hamming_distance(target, goal) + target.get_depth());           // f(n) = h(n) + g(n); here g(n) is depth
    }

    // calculate manhattan distance in two nodes
    public int manhattan_distance(Node target, Node goal)
    {
        int sum = 0;

        int[][] target_matrix = target.get_matrix();

        for(int i=0; i<size; i++)
        {
            for(int j=0; j<size; j++)
            {
                int num = target_matrix[i][j];                                          // taking a number of the puzzle
                if(num != 0)                                                            // if the number is not a blank
                {
                    Position pos = goal.find(num);                                      // getting the position of the number in goal matrix
                    int goal_x = pos.x;                                                 // getting the x value
                    int goal_y = pos.y;                                                 // getting the y value

                    sum += (Math.abs( i - goal_x ) + Math.abs( j - goal_y ));           // adding to the manhattan sum
                }
            }
        }
        return sum;
    }

    // calculate f_n using manhattan distance
    public int manhattan_f_n(Node target, Node goal)
    {
        return manhattan_distance(target, goal) + target.get_depth();               // f(n) = g(n) + h(n)
    }

    // calculate linear conflict for rows
    public int row_linear_conflict(Node init, Node goal)
    {
        // 2 tiles tⱼ and tₖ are in linear conflict if tⱼ and tₖ are both in the same line,
        // the goal positions of tⱼ and tₖ are both in that line, tⱼ is to the right of tₖ,
        // and the goal position of tⱼ is to the left of the goal position of tₖ.

        int conflict = 0;
        int[][] init_matrix = init.get_matrix();

        for(int i=0; i<size; i++)
        {
            for(int j=0; j<size; j++)
            {
                Position goal_pos_of_tj = goal.find(init_matrix[i][j]);

                if(goal_pos_of_tj.x == i)
                {
                    for (int k = j + 1; k < size; k++)                                  // iterate over the next columns
                    {
                        if(init_matrix[i][j]==0 || init_matrix[i][k]==0)                // if any of the entries are blank
                            continue;

                        Position goal_pos_of_tk = goal.find(init_matrix[i][k]);

                        if(goal_pos_of_tk.x == i)
                        {
                            if(goal_pos_of_tk.y < goal_pos_of_tj.y)
                                conflict += 1;
                        }
                    }
                }
            }
        }
        return conflict;
    }

    // calculate column linear conflict for columns
    public int column_linear_conflict(Node init, Node goal)
    {
        int conflict = 0;
//        int[][] init_matrix = init.get_matrix();
//
//        for(int i=0; i<size; i++)
//        {
//            for(int j=0; j<size; j++)
//            {
//                Position goal_pos_of_tj = goal.find(init_matrix[j][i]);
//
//                if(goal_pos_of_tj.y == i)
//                {
//                    for (int k = j + 1; k < size; k++)                                  // iterate over the next columns
//                    {
//                        Position goal_pos_of_tk = goal.find(init_matrix[k][i]);
//
//                        if(goal_pos_of_tk.y == i)
//                        {
//                            if(goal_pos_of_tk.x < goal_pos_of_tj.x)
//                                conflict += 1;
//                        }
//                    }
//                }
//            }
//        }
        return conflict;
    }

    // calculate linear conflict
    public int linear_conflict(Node init, Node goal)
    {
        return manhattan_distance(init, goal) + 2 * ( row_linear_conflict(init, goal) + column_linear_conflict(init, goal) );
    }

    // calculate f_n using linear conflict + manhattan
    public int lc_f_n(Node init, Node goal)
    {
        return linear_conflict(init, goal) + init.get_depth();         // f(n) = h(n) + g(n)
    }

    // solves using hamming distance
    public void simulate_hamming()
    {
        if(is_solvable()) {
            int cost = 0;
            int expanded_nodes = 0;
            int explored_nodes = 0;
            Node final_node;
            PriorityQueue<Node> open_set = new PriorityQueue<Node>(Comparator.comparing(Node::get_fval));
            PriorityQueue<Node> closed_set = new PriorityQueue<Node>(Comparator.comparing(Node::get_fval));

            init_state.set_fval(hamming_f_n(init_state, goal_state));               // calculating f_n for initial state
            open_set.add(init_state);                                               // adding initial state to open state

            while (true) {

                Node target = open_set.poll();                                      // popping the element with the lowest f_n
                cost = target.get_fval();

                if (hamming_distance(target, goal_state) == 0)                      // if the hamming distance is 0, aka, the goal state has been reached
                {
                    final_node = target;
                    break;
                }
                ArrayList<Node> children = target.generate_children();              // generating the child nodes

                children.forEach(child ->                                           // for each child node
                {
                    if (!closed_set.contains(child))                                // if closed set does not already contain child
                    {
                        child.set_fval(hamming_f_n(child, goal_state));             // set the f_val
                        open_set.add(child);                                        // add the child in the open list
                    }
                });
                explored_nodes += children.size();                                  // all the children are explored
                closed_set.add(target);                                             // add the popped node in the closed set

                expanded_nodes += 1;
            }
            print_path(final_node);
            System.out.println("Cost Using Hamming: " + cost);
            System.out.println("Explored Nodes: " + explored_nodes);
            System.out.println("Expanded Nodes: " + expanded_nodes);
        }
        else
            System.out.println("Puzzle is not solvable");
    }

    // solves using manhattan distance
    public void simulate_manhattan()
    {
        if(is_solvable()) {
            int cost = 0;
            int expanded_nodes = 0;
            int explored_nodes = 0;
            Node final_node;
            PriorityQueue<Node> open_set = new PriorityQueue<Node>(Comparator.comparing(Node::get_fval));
            PriorityQueue<Node> closed_set = new PriorityQueue<Node>(Comparator.comparing(Node::get_fval));

            init_state.set_fval(manhattan_f_n(init_state, goal_state));                 // calculating f_n for initial state
            open_set.add(init_state);                                                   // adding initial state to open state

            while (true) {
                Node target = open_set.poll();                                          // popping the node with minimum f_val

                cost = target.get_fval();

                if (manhattan_distance(target, goal_state) == 0)                         // if manhattan distance is 0 aka, reached the goal state
                {
                    final_node = target;
                    break;
                }
                ArrayList<Node> children = target.generate_children();                  // generating the children

                children.forEach(child ->                                               // for each child
                {
                    if (!closed_set.contains(child))                                     // if closed set does not already contain child
                    {
                        child.set_fval(manhattan_f_n(child, goal_state));               // set the f_val
                        open_set.add(child);                                            // add the child in the open list
                    }
                });
                explored_nodes += children.size();                                      // all the children are explored
                closed_set.add(target);                                                 // add the popped node in the closed set

                expanded_nodes += 1;
            }
            print_path(final_node);
            System.out.println("Cost Using Manhattan: " + cost);
            System.out.println("Explored Nodes: " + explored_nodes);
            System.out.println("Expanded Nodes: " + expanded_nodes);
        }
        else
            System.out.println("Puzzle is not solvable");
    }

    // solves using linear conflict
    public void simulate_lc()
    {
        if(is_solvable()) {
            int cost = 0;
            int expanded_nodes = 0;
            int explored_nodes = 0;
            Node final_node;
            PriorityQueue<Node> open_set = new PriorityQueue<Node>(Comparator.comparing(Node::get_fval));
            PriorityQueue<Node> closed_set = new PriorityQueue<Node>(Comparator.comparing(Node::get_fval));

            init_state.set_fval(lc_f_n(init_state, goal_state));                    // calculating f_n for initial state
            open_set.add(init_state);                                               // adding initial state to open state

            while (true) {
                Node target = open_set.poll();                                      // popping the element with the lowest f_n
                cost = target.get_fval();

                if (linear_conflict(target, goal_state) == 0)                       // if the linear conflict is 0, aka, the goal state has been reached
                {
                    final_node = target;
                    break;
                }
                ArrayList<Node> children = target.generate_children();              // generating the child nodes

                children.forEach(child ->                                           // for each child node
                {
                    if (!closed_set.contains(child))                                // if closed set does not already contain child
                    {
                        child.set_fval(lc_f_n(child, goal_state));                  // set the f_val
                        open_set.add(child);                                        // add the child in the open list
                    }
                });
                explored_nodes += children.size();                                  // all the children are explored
                closed_set.add(target);                                             // add the popped node in the closed set

                expanded_nodes += 1;                                                // +1 expanded node
            }
            print_path(final_node);
            System.out.println("Cost Using Linear Conflict: " + cost);
            System.out.println("Explored Nodes: " + explored_nodes);
            System.out.println("Expanded Nodes: " + expanded_nodes);
        }
        else
            System.out.println("Puzzle is not solvable");
    }
}
