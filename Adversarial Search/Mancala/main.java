import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        Board board = new Board(6,4);
        Player p1;
        Player p2;

        System.out.println("Choose Player Option: ");
        System.out.println("1. Human vs Human");
        System.out.println("2. Human vs Computer");
        System.out.println("3. Computer vs Computer");

        int type_choice = sc.nextInt();

        if(type_choice == 1)
        {
            p1 = new Player(1, 0, 5, 5);
            p2 = new Player(2, 0, 10, 5);
        }
        else if(type_choice == 2)
        {
            System.out.println("Choose Opponent Heuristic");
            System.out.println("1. Simple Probabilistic");
            System.out.println("2. Difference of Goal Stones");
            System.out.println("3. Difference of Total Stones");
            System.out.println("4. How Close to Victory");
            System.out.println("5. How Close to Goal");
            System.out.println("6. Captured Stones");
            System.out.println("7. Additional Move Earned");

            int heuristic_choice = sc.nextInt();

            while(heuristic_choice<1 || heuristic_choice>7)
            {
                System.out.println("Invalid Option");
                heuristic_choice = sc.nextInt();
            }

            System.out.print("Enter Depth Limit >");
            int depth_limit = sc.nextInt();

            p1 = new Player(1, 0, depth_limit, heuristic_choice);
            p2 = new Player(2, 1, depth_limit, heuristic_choice);
        }
        else
        {
            System.out.println("Choose Heuristic For Player 1");
            System.out.println("1. Simple Probabilistic");
            System.out.println("2. Difference of Goal Stones");
            System.out.println("3. Difference of Total Stones");
            System.out.println("4. How Close to Victory");
            System.out.println("5. How Close to Goal");
            System.out.println("6. Captured Stones");
            System.out.println("7. Additional Move Earned");

            int heuristic_choice_1 = sc.nextInt();

            while(heuristic_choice_1<1 || heuristic_choice_1>7)
            {
                System.out.println("Invalid Option");
                heuristic_choice_1 = sc.nextInt();
            }

            System.out.println("Choose Heuristic For Player 2");
            System.out.println("1. Simple Probabilistic");
            System.out.println("2. Difference of Goal Stones");
            System.out.println("3. Difference of Total Stones");
            System.out.println("4. How Close to Victory");
            System.out.println("5. How Close to Goal");
            System.out.println("6. Captured Stones");
            System.out.println("7. Additional Move Earned");

            int heuristic_choice_2 = sc.nextInt();

            while(heuristic_choice_2<1 || heuristic_choice_2>7)
            {
                System.out.println("Invalid Option");
                heuristic_choice_2 = sc.nextInt();
            }

            System.out.print("Enter Depth Limit For Player 1>");
            int depth_limit_1 = sc.nextInt();
            System.out.print("Enter Depth Limit For Player 2>");
            int depth_limit_2 = sc.nextInt();

            p1 = new Player(1,1, depth_limit_1, heuristic_choice_1);
            p2 = new Player(2,1, depth_limit_2, heuristic_choice_2);
        }

        long start_time = System.nanoTime();
        board.play_game(p1, p2);
        long end_time = System.nanoTime();

        System.out.println("\nExecution Time: " + (end_time-start_time) + " nanosecond");

//        Board board = new Board(6,4);
//        Player p1 = new Player(1, 1, 2, 2);
//        Player p2 = new Player(2, 1, 2, 2);
//
//        board.print_board();
//        System.out.println(p1.close_to_storage(board, p1.player_num));
//        System.out.println(p2.close_to_storage(board, p2.player_num));
//
//        board.single_move(p1, 3);
//        board.print_board();
//        System.out.println(p1.close_to_storage(board, p1.player_num));
//        System.out.println(p2.close_to_storage(board, p2.player_num));
//
//        board.single_move(p2, 1);
//        board.print_board();
//        System.out.println(p1.close_to_storage(board, p1.player_num));
//        System.out.println(p2.close_to_storage(board, p2.player_num));
    }
}