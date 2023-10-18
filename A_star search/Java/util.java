import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

public class util {
    public static void main(String[] args) throws FileNotFoundException {

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the size >");

        int size = sc.nextInt();
        int[][] puzzle1 = new int[size][size];
        int[][] puzzle2 = new int[size][size];

        System.out.println("Enter the initial state: ");
        for(int i=0; i<size; i++)
        {
            for(int j=0; j<size; j++)
            {
                String input = sc.next();
                if(input.equalsIgnoreCase("*"))
                    puzzle1[i][j] = 0;
                else
                    puzzle1[i][j] = Integer.parseInt(input);
            }
        }

        // -- user input of goal state -- //
//        System.out.println("Enter the goal state: ");
//        for(int i=0; i<size; i++)
//        {
//            for(int j=0; j<size; j++)
//            {
//                puzzle2[i][j] = sc.next();
//            }
//        }

        // -- default goal state -- //
        int entry = 1;
        for(int i=0; i<size; i++)
        {
            for(int j=0; j<size; j++)
            {
                if(i==size-1 && j==size-1)
                    puzzle2[i][j] = 0;
                else {
                    puzzle2[i][j] = entry;
                    entry += 1;
                }
            }
        }

        System.out.println("Goal State: ");
        for(int i=0; i<size; i++)
        {
            for(int j=0; j<size; j++)
            {
                if(puzzle2[i][j] == 0)
                    System.out.print("*");
                else
                    System.out.print(puzzle2[i][j] + " ");
            }
            System.out.print("\n");
        }

//        // redirecting output to file
//        File file = new File("E:\\sample.txt");
//        PrintStream stream = new PrintStream(file);
//        System.setOut(stream);

//        System.out.println("Initial State: ");
//        for(int i=0; i<size; i++)
//        {
//            for(int j=0; j<size; j++)
//            {
//                System.out.print(puzzle1[i][j] + " ");
//            }
//            System.out.print("\n");
//        }
//
//        System.out.println("Goal State: ");
//        for(int i=0; i<size; i++)
//        {
//            for(int j=0; j<size; j++)
//            {
//                System.out.print(puzzle2[i][j] + " ");
//            }
//            System.out.print("\n");
//        }

        Node init = new Node(puzzle1, 0, 0, size, null);
        Node goal = new Node(puzzle2, 0, 0, size, null);

        Puzzle p = new Puzzle(size, init, goal);

        System.out.println("\n###################\n");
        System.out.println("Hamming: ");
        p.simulate_hamming();

        System.out.println("\n");

        System.out.println("Manhattan: ");
        p.simulate_manhattan();

        System.out.println("\n");

        System.out.println("Linear Conflict");
        p.simulate_lc();
    }
}
