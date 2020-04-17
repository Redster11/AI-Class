import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Scanner;

class Board
{
    int n; // number of squares
    char [][] board; // blocked spots
    int[] teamX = new int[2];
    int[] teamO = new int[2];
    
    public Board(int n)
    {
        this.n = n;
        this.board = new char[n][n];//row column
        for (int i = 0; i < n; i++) 
            Arrays.fill(this.board[i], '-'); 
        initialPlace();
    }
    public boolean movePiece(int[] newLocation, char team) {
        if (this.board[newLocation[0]][newLocation[1]] != '#')
        {
            if(team == 'O')
            {
                if(this.board[newLocation[0]][newLocation[1]] == 'O')
                {
                    System.out.println("Please move the piece");
                    return false;
                }
                else if(this.board[newLocation[0]][newLocation[1]] != 'X')
                {
                    this.board[this.teamO[0]][this.teamO[1]] = '#';
                    this.board[newLocation[0]][newLocation[1]] = 'O';
                    this.teamO[0] = newLocation[0];
                    this.teamO[1] = newLocation[1];
                }
                else
                {
                    System.out.println("Location already in use");
                    return false;
                }
            }
            else
            {
                if(this.board[newLocation[0]][newLocation[1]] == 'X')
                {
                    System.out.println("Please move the piece");
                    return false;
                }
                else if(this.board[newLocation[0]][newLocation[1]] != 'O')
                {
                    this.board[this.teamX[0]][this.teamX[1]] = '#';
                    this.board[newLocation[0]][newLocation[1]] = 'X';
                    this.teamX[0] = newLocation[0];
                    this.teamX[1] = newLocation[1];
                }
                 else
                {
                    System.out.println("Location already in use");
                    return false;
                }
            }
        }
        else {
            System.out.println("Location already in use");
            return false;
        }
        return true;
    }
    public void initialPlace()
    {
        this.teamX = new int[]{0, 0};
        this.teamO = new int[]{this.n-1, this.n-1};
        this.board[teamO[0]][teamO[1]] = 'O'; // team O
        this.board[teamX[0]][teamX[1]] = 'X'; // team X
    }
    public void printBoard(Moves Computer, Moves Opponent)
    {
        System.out.print("   1  2  3  4  5  6  7  8");
        if(Computer.getMoves().isEmpty())
            System.out.println();
        else
            System.out.println("\tComputer  vs.  Opponent");
        for (int j = 0; j < this.n; j++)
        {
            int letter = j + 65;
            System.out.print((char)(letter));
            for(int i = 0; i < this.n; i++)
            {
                System.out.print("  " + this.board[j][i]);
            }
            if(!Computer.getMoves().isEmpty() && j < Computer.getMoves().size())
                System.out.print("\t" + (j+1) + ". " + Computer.getMoves().get(j));
            if(!Opponent.getMoves().isEmpty() && j < Opponent.getMoves().size())
                System.out.print("\t\t  " + Opponent.getMoves().get(j));
            System.out.println();
        }
    }
    
}
class Moves
{
    ArrayList<String> moves = new ArrayList<>();
    void add(String move)
    {
        moves.add(move);

    }
    ArrayList<String> getMoves()
    {
        return moves;
    }
}
public class Isolation
{
    Board board = new Board(8);
    /*
    A == 0
    B == 1
    C == 2
    D == 3
    E == 4
    F == 5
    G == 6
    H == 7
    */
    static Moves Computer = new Moves();
    static Moves Oppenent = new Moves();

    public static int[] getCoordinates(String Move, Boolean isXTurn)// from form D4 to numbers so should be 3,4
    {
        int height = Move.toUpperCase().charAt(0);
        int length = Integer.parseInt(String.valueOf(Move.charAt(1)));
        if (height < 65 || height > 72 || length > 8) // greater than H
        {
            // System.out.println("We failed because the values are: "+ height+", "+
            // length);
            return new int[] { -1 };
        } else {
            // System.out.println("Did not fail is valid number: " + height + ", " +
            // length);
            if (isXTurn)
                Computer.add(Move);
            else
                Oppenent.add(Move);

            return new int[] {height-65, length - 1 };
        }
    }

    public static void main(String[] args) 
    {
        Isolation iso = new Isolation();
        iso.board.printBoard(iso.Computer, iso.Oppenent);
        boolean isTeamXTurn = true;
        Scanner kb = new Scanner(System.in);
        
        for(int run = 0; run < 4; run++)
        {
            boolean gettingMove = true;
            if (isTeamXTurn)// x is comp eventually
            {
                int[] move = new int[2];
                while(gettingMove)
                {
                    System.out.print("Player X Please enter your move: ");
                    String XMove = kb.nextLine();
                    move = getCoordinates(XMove, isTeamXTurn);
                    if(move[0] != -1)
                    {
                        if (iso.board.movePiece(move, 'X'))
                        {
                            System.out.println("\nThe TeamX piece has been moved to: " + move[0] + ", " + (move[1] + 1));
                            iso.board.printBoard(iso.Computer, iso.Oppenent);
                            gettingMove = false;
                            isTeamXTurn = false;
                        }
                    }
                    else
                    {
                        System.out.println("Please make sure that the values are on the board");    
                    }
                }
                
                //Change them to a x y coordinate system
                
                
                isTeamXTurn = false;
            }
            else
            {
                int[] move = new int[2];
                while(gettingMove)
                {
                    System.out.print("Player O Please enter your move: ");
                    String OMove = kb.nextLine();
                    move = getCoordinates(OMove, isTeamXTurn);
                    if(move[0] != -1){
                        if (iso.board.movePiece(move, 'O'))
                        {
                            System.out.println("\nThe TeamO piece has been moved to: " + move[0] + ", " + (move[1] + 1));
                            iso.board.printBoard(iso.Computer, iso.Oppenent);
                            gettingMove = false;
                            isTeamXTurn = true;
                        }
                    }
                    else
                    {   
                        System.out.println("Please make sure that the values are on the board");
                    }
                    
                }
            }
        
        }
    }
}