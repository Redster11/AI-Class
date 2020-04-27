import java.util.Arrays;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;
/*
TO DO LIST
-need to check the agent directions (ex. up right) make sure its being added to potential moves
-need to make sure heuristic is working
-need to kill self
-need to figure out which is the best move
    -need to figure out how to return that move
-need to fix our turn (teamO)
*/
class Board
{
    int n; // number of squares
    char [][] board; // blocked spots
    Integer[] teamX = new Integer[2];
    Integer[] teamO = new Integer[2];
    public Board(int n, char[][] board, Integer[] teamX, Integer[] teamO)
    {
        this.n = n;
        this.board = new char[n][n];
        for(int i = 0; i < n; i++)
        {
            for(int j = 0; j < n; j++)
            {
                this.board[i][j] = board[i][j];
            }
        }
        for(int i = 0; i < 2; i++)
        {
            this.teamO[i] = teamO[i];
            this.teamX[i] = teamX[i];
        }
    }
    public Board(int n)
    {
        this.n = n;
        this.board = new char[n][n];//row column
        for (int i = 0; i < n; i++) 
            Arrays.fill(this.board[i], '-'); 
        initialPlace();
    }
    public char getItem( int x,  int y)//current location of agent
    {
        return board[x][y];
    }

    public boolean movePiece( Integer[] newLocation,  char team) {
        if (this.board[newLocation[0]][newLocation[1]] != '#' )
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
        this.teamX = new Integer[]{0, 0};
        this.teamO = new Integer[]{this.n-1, this.n-1};
        this.board[teamO[0]][teamO[1]] = 'O'; // team O
        this.board[teamX[0]][teamX[1]] = 'X'; // team X
    }
    public void printBoard( boolean CompStarted,  Moves Computer,  Moves Opponent)
    {
        System.out.print("   1  2  3  4  5  6  7  8");
        if(Computer.getMoves().isEmpty() && Opponent.getMoves().isEmpty())
            System.out.println();
        else if (CompStarted)
            System.out.println("\tComputer  vs.  Opponent");
        else
            System.out.println("\tOpponent  vs.  Computer");
            
        for (int j = 0; j < this.n; j++)
        {
             int letter = j + 65;
            System.out.print((char)(letter));
            for(int i = 0; i < this.n; i++)
            {
                System.out.print("  " + this.board[j][i]);
            }
            if(CompStarted)
            {
                if(!Computer.getMoves().isEmpty() && j < Computer.getMoves().size())
                    System.out.print("\t" + (j+1) + ". " + Computer.getMoves().get(j));
                if(!Opponent.getMoves().isEmpty() && j < Opponent.getMoves().size())
                    System.out.print("\t\t  " + Opponent.getMoves().get(j));
            }
            else
            {
                if(!Opponent.getMoves().isEmpty() && j < Opponent.getMoves().size())
                    System.out.print("\t" + (j+1) + ". " + Opponent.getMoves().get(j));
                if(!Computer.getMoves().isEmpty() && j < Computer.getMoves().size())
                    System.out.print("\t\t  " + Computer.getMoves().get(j));
            }
            System.out.println();
        }
    }
    
}
class Moves
{
    ArrayList<String> moves = new ArrayList<>();
    ArrayList<Integer[]> movesInt = new ArrayList<Integer[]>();
    void add( String move)
    {
        moves.add(move);
        movesInt.add(getCoordinates(move));

    }
    ArrayList<String> getMoves()
    {
        return moves;
    }
    ArrayList<Integer[]> getMovesIntegers()
    {
        return movesInt;
    }
    public static Integer[] getCoordinates( String Move)// from form D4 to numbers so should be 3,4
    {
        int height = Move.toUpperCase().charAt(0);
        int length = Integer.parseInt(String.valueOf(Move.charAt(1)));
        // System.out.println("Did not fail is valid number: " + height + ", " +
        // length);
        return new Integer[] {height-65, length - 1 };
    }
}

class Agent
{
    boolean isTurn; // this is going to tell the agent if it is their turn
    ArrayList<Integer[]> movesToMake = new ArrayList<Integer[]>(); // hold moves that can be made, so unoccupied spaces in directions agent can move
    Board board;
    Moves CompMovesMade;
    Moves OppennetMovesMade;
    public Agent( boolean isTurn,  Board boardToUse,  Moves Computer,  Moves Opponent)
    {
        this.isTurn = isTurn;
        this.board = boardToUse;
        this.CompMovesMade = Computer;
        this.OppennetMovesMade = Opponent;
        movesToMake = getAvalibleMoves();

    }
    public ArrayList<Integer[]> getAvalibleMoves()
    {
         ArrayList<Integer[]> moveSet = new ArrayList<>();
        Integer[] currentLoc;
        if(isTurn)
            currentLoc = this.board.teamX;
        else
            currentLoc = this.board.teamO;
        // check Right
        for(int y = currentLoc[1]+1; y < 8; y++)
        {
            //check conflict
            if(this.board.getItem(currentLoc[0], y) == '-')
            {
                moveSet.add(new Integer[]{currentLoc[0],y});
            }
            else
                break;
        }
        //check Left
        for(int y = currentLoc[1]-1; y >= 0; y--)
        {
            //check conflict
            if(this.board.getItem(currentLoc[0], y) == '-')
            {
                moveSet.add(new Integer[]{currentLoc[0],y});
            }
            else
                break;
        }

        //check Down
        for(int x = currentLoc[0]+1; x < 8; x++)
        {
            //check conflict
           if(this.board.getItem(x, currentLoc[1]) == '-')
            {
                moveSet.add(new Integer[]{x,currentLoc[1]});
            }
            else
                break;
        }

        //check up
        for(int x = currentLoc[0]-1; x >= 0; x--)
        {
            //check conflict
            if(this.board.getItem(x, currentLoc[1]) == '-')
            {
                moveSet.add(new Integer[]{x,currentLoc[1]});
            }
            else
                break;
        }
        //check down left
        int x = currentLoc[0]+1;
        int y = currentLoc[1]-1;
        while(x < 8 && y >= 0)
        {
            if(this.board.getItem(x, y) == '-')
            {
                moveSet.add(new Integer[]{x,y});           
            }
            else    
                break;
            x++;
            y--;
        }
        //check up left
        x = currentLoc[0]-1;
        y = currentLoc[1]-1;
        while(x > 0 && y >= 0)
        {
            if(this.board.getItem(x, y) == '-')
            {
                moveSet.add(new Integer[]{x,y});           
            }
            else    
                break;
            x--;
            y--;
        }

        //check down right
        x = currentLoc[0]+1;
        y = currentLoc[1]+1;
        while( x < 8 && y < 8)
        {
            if(this.board.getItem(x, y) == '-')
            {
                moveSet.add(new Integer[]{x,y});
            }
            else 
                break;
            x++;
            y++;
        }

        //check up right
        x = currentLoc[0]-1;
        y = currentLoc[1]+1;
        while( x >= 0 && y < 8)
        {
            if(this.board.getItem(x, y) == '-')
            {
                moveSet.add(new Integer[]{x,y});
            }
            else 
                break;
            x--;
            y++;
        }
        return moveSet;   
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
    static Moves ComputerMovesMade = new Moves();
    static Moves OpponentMovesMade = new Moves();
    ArrayList<Board> Backtracking = new ArrayList<Board>();
    float maxTime = 20;

    public static Integer[] getCoordinates( String Move,  Boolean isXTurn)// from form D4 to numbers so should be 3,4
    {
         int height = Move.toUpperCase().charAt(0);
         int length = Integer.parseInt(String.valueOf(Move.charAt(1)));
        if (height < 65 || height > 72 || length > 8) // greater than H
        {
            // System.out.println("We failed because the values are: "+ height+", "+
            // length);
            return new Integer[] { -1 };
        } else {
            // System.out.println("Did not fail is valid number: " + height + ", " +
            // length);
            if (isXTurn)
                ComputerMovesMade.add(Move);
            else
                OpponentMovesMade.add(Move);

            return new Integer[] {height-65, length - 1 };
        }
    }

    public Integer[] MinMax(boolean turn)//turn true = comp == max turn false = opponent == min
    {
        Backtracking.clear();
        Isolation TestBoard = new Isolation();
        Board changedBoard1 = new Board(this.board.n, this.board.board, this.board.teamX, this.board.teamO);
        Backtracking.add(changedBoard1);
        return max(changedBoard1, TestBoard, Integer.MIN_VALUE, Integer.MAX_VALUE, System.currentTimeMillis());
        
    }

    public Integer[] min(Board b, Isolation iso, Integer alpha, Integer beta, float startTime)
    {
        Integer[] minValInteger = new Integer[]{iso.board.teamO[0],iso.board.teamO[1],Integer.MAX_VALUE};
        Integer[] toReturnLoc = new Integer[3];//{loc1,loc2,minVal}
        b.printBoard(true, iso.ComputerMovesMade, iso.OpponentMovesMade);
        while(System.currentTimeMillis() - startTime < this.maxTime)
        {
            Agent minAgent = new Agent(false, b, iso.ComputerMovesMade, iso.OpponentMovesMade);
            if(minAgent.movesToMake.isEmpty())//an error
            {
                return new Integer[]{iso.board.teamO[0],iso.board.teamO[1],Integer.MAX_VALUE};
            }
            for(int i = 0; i < minAgent.movesToMake.size();i++) // minimizing
            {
                Integer[] newLocation = minAgent.movesToMake.get(i);
                Board changedBoard2 = new Board(b.n, b.board, b.teamX, b.teamO);
                changedBoard2.printBoard(true, iso.ComputerMovesMade, iso.OpponentMovesMade);
                if(changedBoard2.movePiece(newLocation, 'O'))
                {
                    Agent potentialTwo = new Agent(false, changedBoard2, iso.ComputerMovesMade, iso.OpponentMovesMade);
                    Integer[] currentLoc = new Integer[]{potentialTwo.board.teamX[0], potentialTwo.board.teamX[1], potentialTwo.movesToMake.size()};
                    minValInteger = minInteger(currentLoc, max(changedBoard2, iso, alpha, beta, startTime));
                    if(minValInteger[2] <= alpha)
                    {
                        return minValInteger;
                    }
                    
                    beta = Math.min(beta, minValInteger[2]);
                }
            }
            return minValInteger;
        }
        return minValInteger;
    }

    public Integer[] max(Board b, Isolation iso, Integer alpha, Integer beta, float startTime)
    {
        Integer[] maxValInteger = new Integer[]{iso.board.teamX[0],iso.board.teamX[1],Integer.MIN_VALUE};
        Integer[] toReturnLoc = new Integer[3];//{loc1, loc2, maxVal}
        b.printBoard(true, iso.ComputerMovesMade, iso.OpponentMovesMade);
        while(System.currentTimeMillis() - startTime < this.maxTime)
        {
            Agent maxAgent = new Agent(true, b, iso.ComputerMovesMade, iso.OpponentMovesMade);
            if(maxAgent.movesToMake.isEmpty())
            {
                return new Integer[]{iso.board.teamX[0],iso.board.teamX[1],Integer.MIN_VALUE};
            }
            for(int i = 0; i < maxAgent.movesToMake.size();i++) // maximizing
            {
                Integer[] newLocation = maxAgent.movesToMake.get(i);
                Board changedBoard2 = new Board(b.n, b.board, b.teamX, b.teamO);
                b.printBoard(true, iso.ComputerMovesMade, iso.OpponentMovesMade);
                if(changedBoard2.movePiece(newLocation, 'X'))
                {
                    Agent potentialTwo = new Agent(true, changedBoard2, iso.ComputerMovesMade, iso.OpponentMovesMade);
                    Integer[] currentloc = new Integer[]{potentialTwo.board.teamX[0], potentialTwo.board.teamX[1], potentialTwo.movesToMake.size()};
                    maxValInteger = maxInteger(currentloc, min(changedBoard2, iso, alpha, beta, startTime));
                    if(maxValInteger[2] >= beta)
                    {
                        
                        return maxValInteger;
                    }
                    alpha = Math.max(alpha, maxValInteger[2]);
                }
            }
            return maxValInteger;
        }
        return maxValInteger;
    }

    public Integer[] maxInteger(Integer[] current, Integer[] min)
    {
        Integer maxVal = Math.max(current[2], min[2]);
        if(maxVal == current[2])
        {
            return current;
        }
        else
            return min;
    }
    
    public Integer[] minInteger(Integer[] current, Integer[] min)
    {
        Integer minVal = Math.min(current[2], min[2]);
        if(minVal == current[2])
        {
            return current;
        }
        else
            return min;
    }

    public static void main( String[] args) 
    {
        Scanner kb = new Scanner(System.in);
        Isolation iso = new Isolation();
        boolean enteringFirst = true;
        boolean isCompTurn = false;
        boolean whoStarted = true;//true for comp false for opponent
        while(enteringFirst)
        {
            System.out.println("Please enter who goes First (C or O)");
            String first = kb.nextLine().toUpperCase();// person who goes first
            if(first.startsWith("C"))
            {
                isCompTurn = true;
                whoStarted = true;
                enteringFirst = false;
            }
            else if (first.startsWith("O"))
            {
                isCompTurn = false;
                whoStarted = false;
                enteringFirst = false;
            }
            else
            {
                continue;
            }
        }
        System.out.println("Please enter the amount of time the agent has to run in seconds");
        iso.maxTime = kb.nextFloat();
        Agent P = new Agent(isCompTurn, iso.board, iso.ComputerMovesMade, iso.OpponentMovesMade);
        iso.board.printBoard(whoStarted, iso.ComputerMovesMade, iso.OpponentMovesMade);
        
        for(int run = 0; run < 4; run++)
        {
            boolean gettingMove = true;
            if (isCompTurn)// x is comp eventually
            {
                Integer[] move = new Integer[2];
                while(gettingMove)
                {
                    System.out.println("Player X Please enter your move: ");

                    // this is for player input
                    //String XMove = kb.nextLine();
                    //move = getCoordinates(XMove, isCompTurn);
                    Integer[] bestMove = iso.MinMax(isCompTurn);
                    move[0] = bestMove[0];
                    move[1] = bestMove[1]; 
                    if(move[0] != -1)
                    {
                        if (iso.board.movePiece(move, 'X'))
                        {
                            System.out.println("\nThe TeamX piece has been moved to: " + move[0] + ", " + (move[1] + 1));
                            iso.board.printBoard(whoStarted, iso.ComputerMovesMade, iso.OpponentMovesMade);
                            gettingMove = false;
                            isCompTurn = false;
                        }
                    }
                    else
                    {
                        System.out.println("Please make sure that the values are on the board");    
                    }
                }
                
                //Change them to a x y coordinate system
                isCompTurn = false;
            }
            else
            {
                Integer[] move = new Integer[2];
                while(gettingMove)
                {
                    System.out.print("Player O Please enter your move: ");
                    String OMove = kb.nextLine();
                    move = getCoordinates(OMove, isCompTurn);
                    if(move[0] != -1){
                        if (iso.board.movePiece(move, 'O'))
                        {
                            System.out.println("\nThe TeamO piece has been moved to: " + move[0] + ", " + (move[1] + 1));
                            iso.board.printBoard(whoStarted, iso.ComputerMovesMade, iso.OpponentMovesMade);
                            gettingMove = false;
                            isCompTurn = true;
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