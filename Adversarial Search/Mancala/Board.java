import java.util.ArrayList;

public class Board {

    public int no_of_bins;
    public int stones_each_bin;
    public int[] score_bins;
    public int[] p1_bins;
    public int[] p2_bins;

    // captures stones records are needed for heuristic
    private int captured_stones_p1;
    private int captured_stones_p2;

    // bonus move records are needed for heuristic
    private boolean bonus_move_p1;
    private boolean bonus_move_p2;

    public Board(int no_of_bins, int stones_each_bin)
    {
        this.no_of_bins = no_of_bins;
        this.stones_each_bin = stones_each_bin;
        reset();
    }

    // copy constructor //
    public Board(Board another_board)
    {
        this.no_of_bins = another_board.no_of_bins;
        this.stones_each_bin = another_board.stones_each_bin;
        this.score_bins = new int[2];
        this.p1_bins = new int[no_of_bins];
        this.p2_bins = new int[no_of_bins];

        for(int i=0; i<no_of_bins; i++)
        {
            p1_bins[i] = another_board.p1_bins[i];
            p2_bins[i] = another_board.p2_bins[i];
        }

        score_bins[0] = another_board.score_bins[0];
        score_bins[1] = another_board.score_bins[1];

        this.captured_stones_p1 = another_board.captured_stones_p1;
        this.captured_stones_p2 = another_board.captured_stones_p2;

        this.bonus_move_p1 = another_board.bonus_move_p1;
        this.bonus_move_p2 = another_board.bonus_move_p2;
    }

    // resets the board to initial position -- //
    public void reset()
    {
        score_bins = new int[2];                    // 2 score bins
        p1_bins = new int[no_of_bins];              // 6 bins each
        p2_bins = new int[no_of_bins];

        for (int i=0; i<no_of_bins; i++)
        {
            p1_bins[i] = stones_each_bin;           // 4 stones in each bin
            p2_bins[i] = stones_each_bin;
        }

        score_bins[0] = 0;                          // score is 0 initially
        score_bins[1] = 0;

        captured_stones_p1 = 0;
        captured_stones_p2 = 0;

        bonus_move_p1 = false;
        bonus_move_p2 = false;

//        p1_bins[0] = 0;
//        p1_bins[1] = 3;
//        p1_bins[2] = 4;
//
//        p2_bins[0] = 0;
//        p2_bins[1] = 1;
//        p2_bins[2] = 1;
//
//        score_bins[0] = 1;
//        score_bins[1] = 2;
    }

    public void print_board()
    {
        // prints the board in the following format //
        /*

                P L A Y E R  2

            4	4	4	4	4	4
        0  	  	  	  	  	  		0
            4	4	4	4	4	4

                P L A Y E R  1

        */

        System.out.print("\n");
        System.out.println("\t\tP L A Y E R  2\n");

        // show player 2's bins in ascending order
        for(int i=0; i<no_of_bins; i++)
            System.out.print("\t" + p2_bins[i]);

        System.out.print("\n");

        System.out.print(score_bins[0]);
        for(int i=0; i<no_of_bins; i++)
            System.out.print("  " + "\t");
        System.out.print("\t" + score_bins[1]);
        System.out.print("\n");

        // show player 1's bins in descending order
        for(int i=no_of_bins-1; i>=0; i--)
            System.out.print("\t" + p1_bins[i]);

        System.out.println("\n");
        System.out.println("\t\tP L A Y E R  1");
        System.out.print("\n");
    }

    // -- sets the captured stone count -- //
    public void set_captured_stones(int x, int player_num)
    {
        if(player_num == 1)
            captured_stones_p1 = x;
        else
            captured_stones_p2 = x;
    }

    // -- returns the captured stone count and makes it zero -- //
    public int get_captured_stones(int player_num)
    {
        int temp;
        if(player_num == 1)
        {
            temp = captured_stones_p1;
            captured_stones_p1 = 0;
        }
        else
        {
            temp = captured_stones_p2;
            captured_stones_p2 = 0;
        }
        return temp;
    }

    // -- sets the bonus flag on -- //
    public void set_bonus_move(int player_num)
    {
        if(player_num == 1)
            bonus_move_p1 = true;
        else
            bonus_move_p2 = true;
    }

    // -- returns the bonus flag and turns it off -- //
    public boolean get_bonus_move(int player_num) {
        boolean temp;
        if (player_num == 1)
        {
            temp = bonus_move_p1;
            bonus_move_p1 = false;
        }
        else
        {
            temp = bonus_move_p2;
            bonus_move_p2 = false;
        }
        return temp;
    }

    // -- checks if a move is legal -- //
    // -- here, bin range is [1, 6] --//
    public boolean is_legal_move(Player p, int bin)
    {
        int[] bins;

        if(p.get_player_num() == 1)
            bins = p1_bins;
        else
            bins = p2_bins;

        return ((bin > 0) && (bin <= bins.length) && (bins[bin-1]>0));
    }

    // -- returns all the possible moves of a player -- //
    public ArrayList<Integer> possible_moves(Player p)
    {
        int[] bins;

        if(p.get_player_num() == 1)
        {
            bins = p1_bins;
        }
        else
        {
            bins = p2_bins;
        }

        ArrayList<Integer> moves = new ArrayList<>();

        // if there is any non empty bin, that is a valid move //
        for(int i=0; i<bins.length; i++)
        {
            if(bins[i] != 0)
                moves.add(i+1);                     // moves range is [1,6]
        }
        return moves;
    }

    // -- simulate a single move of a given player -- //
    public boolean single_move_util(Player p, int bin)
    {
        int[] self_bins;
        int[] opponent_bins;

        if(p.get_player_num() == 1)
        {
            self_bins = p1_bins;
            opponent_bins = p2_bins;
        }
        else
        {
            self_bins = p2_bins;
            opponent_bins = p1_bins;
        }

        int[] init_cups = self_bins;                // iteration will start from self side bins
        int stones = self_bins[bin-1];              // taking the stones of that bin

        self_bins[bin-1] = 0;                       // emptying the bin
        bin++;                                      // moving on to the next bin

        boolean play_again = false;                 // keeps track of bonus move

        while(stones > 0)
        {
            play_again = false;

            while((bin<=self_bins.length) && stones>0)      // iterating over self bins
            {
                self_bins[bin-1]++;                         // stone in bin gets increased by 1
                stones--;                                   // stone in hand decrease
                bin++;                                      // moving on to next bin
            }
            if(stones == 0)
                break;

            if(self_bins == init_cups)                      // if on our side
            {
                score_bins[p.get_player_num()-1]++;         // put one in score bin
                stones--;
                play_again = true;
            }

            // swapping the turn
            int[] temp_bins = self_bins;
            self_bins = opponent_bins;
            opponent_bins = temp_bins;
            bin = 1;
        }

        if(play_again)
            return true;

        if((self_bins == init_cups) && (self_bins[bin-2]==1) && (opponent_bins[no_of_bins-bin+1]!=0))    // if we end up on an empty bin
        {
            int stolen_stones = opponent_bins[no_of_bins-bin+1];
            score_bins[p.get_player_num()-1] += stolen_stones;                      // steal
            opponent_bins[no_of_bins-bin+1] = 0;                                    // opponent bin = 0
            score_bins[p.get_player_num()-1]++;                                     // count that 1 stone of ours too
            self_bins[bin-2] = 0;                                                   // make bin empty

            set_captured_stones(stolen_stones, p.player_num);
        }
        return false;
    }

    // -- simulates the single move and checks whether game over -- //
    public boolean single_move(Player p, int bin)
    {
        boolean move_again = single_move_util(p, bin);

        if(move_again)
            set_bonus_move(p.get_player_num());                 // if bonus turn occurs, set the bonus flag on  -> useful while predicting

        if(game_over())                                         // if game is over
        {                                                       // get all the stones of a player in his score_bin
            for(int i=0; i<p1_bins.length; i++)
            {
                score_bins[0] += p1_bins[i];
                p1_bins[i] = 0;
            }
            for(int i=0; i< p2_bins.length; i++)
            {
                score_bins[1] += p2_bins[i];
                p2_bins[i] = 0;
            }
            return false;
        }
        else
            return move_again;
    }

    // -- checks if the game is over -- //
    public boolean game_over()
    {
        boolean p1_empty = true;
        boolean p2_empty = true;

        for (int p1_bin : p1_bins) {
            if (p1_bin != 0)                        // if one bin is not empty
            {
                p1_empty = false;                   // game not over for p1
                break;
            }
        }
        if(p1_empty)
            return true;

        for (int p2_bin : p2_bins) {
            if (p2_bin != 0)                        // if one bin is not empty
            {
                p2_empty = false;                   // game not over for p2
                break;
            }
        }
        return p2_empty;
    }

    // -- returns if the Player has won or not -- //
    public boolean has_won(int player_num)
    {
        if(game_over())
        {
            int opponent_num = 2-player_num+1;

            int player_total_stones = 0;
            int opponent_total_stones = 0;
            int[] player_bins;
            int[] opponent_bins;

            if(player_num == 1)
            {
                player_bins = p1_bins;
                opponent_bins = p2_bins;
            }
            else
            {
                player_bins = p2_bins;
                opponent_bins = p1_bins;
            }

            for(int i=0; i<no_of_bins; i++)
            {
                player_total_stones += player_bins[i];
                opponent_total_stones += opponent_bins[i];
            }

            player_total_stones += score_bins[player_num-1];
            opponent_total_stones += score_bins[opponent_num-1];

            return (player_total_stones > opponent_total_stones);     // true if player has more stones in score_bin
        }
        else
            return false;
    }

    // -- play the game -- //
    public void play_game(Player p1, Player p2)
    {
        reset();

        Player current_player = p1;
        Player waiting_player = p2;
        Player temp;
        int move_count = 0;

        // keep playing while game not over
        while(!game_over())
        {
            System.out.println("\n----------------- Move: " + move_count + " -----------------");
            System.out.println("Turn for Player " + current_player.get_player_num());

            boolean bonus_move = true;
            while(bonus_move && !game_over())                           // keep playing if bonus move
            {
                print_board();
                int move = current_player.choose_move(this);

                while(!is_legal_move(current_player, move))
                {
                    System.out.println("This move is not legal");
                    move = current_player.choose_move(this);
                }
                bonus_move = single_move(current_player, move);

                if(bonus_move)
                    System.out.println("Bonus Move");

                move_count++;
            }

            // swap the players before next turn
            temp = current_player;
            current_player = waiting_player;
            waiting_player = temp;
        }

        // after game over
        print_board();
        if(has_won(p1.get_player_num()))
            System.out.println("Player " + p1.get_player_num() + " wins");
        else if(has_won(p2.get_player_num()))
            System.out.println("Player " + p2.get_player_num() + " wins");
        else
            System.out.println("Game Tied");
    }
}
