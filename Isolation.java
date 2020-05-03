import java.util.Arrays;
import java.util.ArrayList;
import java.util.Scanner;

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
            if(newLocation[0] == 0 && newLocation[1] == 0)
            {
                System.out.println("Team X wins!");
            }
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
    public static Integer[] getCoordinates(String Move)// from form D4 to numbers so should be 3,4
    {
        int height = Move.toUpperCase().charAt(0);
        int length = Integer.parseInt(String.valueOf(Move.charAt(1)));
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
    public Agent( boolean isTurn,  Board boardToUse)
    {
        this.isTurn = isTurn;
        this.board = boardToUse;
        movesToMake = getAvalibleMoves();

    }
    public void printMovesToMake()
    {
        for(int i = 0; i < this.movesToMake.size(); i++)
        {
            System.out.print("(" + this.movesToMake.get(i)[0] + ", " + this.movesToMake.get(i)[1] + ") ");
        }
        System.out.println();
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
        int x = currentLoc[0]+1;//row down
        int y = currentLoc[1]-1;//col left
        while(x < 8 && y >= 0)
        {
            if(this.board.getItem(x, y) == '-')
            {
                /*if(this.board.getItem(x-1, y) != '-' && this.board.getItem(x, y+1) != '-')
                    break;
                else*/
                    moveSet.add(new Integer[]{x,y});           
            }
            else    
                break;
            x++;
            y--;
        }
        //check up left
        x = currentLoc[0]-1;//up
        y = currentLoc[1]-1;//left
        while(x >= 0 && y >= 0)
        {
            if(this.board.getItem(x, y) == '-')
            {
                /*if(this.board.getItem(x+1, y) != '-' && this.board.getItem(x, y+1) != '-')
                    break;
                else*/
                    moveSet.add(new Integer[]{x,y});           
            }
            else    
                break;
            x--;
            y--;
        }

        //check down right
        x = currentLoc[0]+1;//down
        y = currentLoc[1]+1;//right
        while( x < 8 && y < 8)
        {
            if(this.board.getItem(x, y) == '-')
            {
                /*if(this.board.getItem(x-1, y) != '-' && this.board.getItem(x, y-1) != '-')
                    break;
                else*/
                    moveSet.add(new Integer[]{x,y});
            }
            else 
                break;
            x++;
            y++;
        }

        //check up right
        x = currentLoc[0]-1;//up
        y = currentLoc[1]+1;//right
        while( x >= 0 && y < 8)
        {
            if(this.board.getItem(x, y) == '-')
            {
                /*if(this.board.getItem(x+1, y) != '-' && this.board.getItem(x, y-1) != '-')
                    break;
                else*/
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
    Board board;
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
    float maxTime = 20;
    double maxDepth = 6;

    public Isolation()
    {
        this.board = new Board(8);
    }

    public Isolation(Board board)
    {
        this.board = new Board(board.n, board.board, board.teamX, board.teamO);
    }

    public String stringMaker(Integer[] moves)
    {
        char toReturn = (char)(moves[0] + 65);
        return  toReturn + "" + (moves[1]+1);
    }

    public static Integer[] getCoordinates( String Move,  Boolean isXTurn)// from form D4 to numbers so should be 3,4
    {
         int height = Move.toUpperCase().charAt(0);
         int length = Integer.parseInt(String.valueOf(Move.charAt(1)));
        if (height < 65 || height > 72 || length > 8) // greater than H
        {
            return new Integer[] { -1 };
        } 
        else 
        {
            return new Integer[] {height-65, length - 1 };
        }
    }

    public Integer[] MinMax(boolean turn, Board b)//turn true = comp == max turn false = opponent == min
    {
        Isolation TestBoard = new Isolation(b);
        int depth = (int)Math.floor(maxDepth);
        Board changedBoard1 = new Board(this.board.n, this.board.board, this.board.teamX, this.board.teamO);
        Integer[] alpha = new Integer[]{0, 0, Integer.MIN_VALUE};
        Integer[] beta = new Integer[]{7,7, Integer.MAX_VALUE};
        Agent minAgent = new Agent(false, changedBoard1);
        Integer[] maxlocation = max(changedBoard1, TestBoard, alpha, beta, System.currentTimeMillis(), depth, minAgent);
        maxDepth +=.01;
        return maxlocation;
    }

    public Integer[] min(Board b, Isolation iso, Integer alpha[], Integer[] beta, float startTime, int depth, Agent maxAgent)
    {
        Integer[] minValInteger = new Integer[]{iso.board.teamO[0],iso.board.teamO[1],Integer.MAX_VALUE};//{loc1,loc2,minVal}
        while((System.currentTimeMillis() - startTime) < this.maxTime)
        {
            Agent minAgent = new Agent(false, b);
            if(minAgent.movesToMake.isEmpty())//an error
            {
                if(depth == (int)Math.floor(maxDepth))
                    return new Integer[]{b.teamX[0],b.teamX[1],Integer.MAX_VALUE};
                else
                    return new Integer[]{iso.board.teamX[0],iso.board.teamX[1],Integer.MAX_VALUE};
            }

            for(int i = 0; i < minAgent.movesToMake.size();i++) // minimizing
            {
                if(depth >1)
                {
                    Integer[] newLocation = minAgent.movesToMake.get(i);
                    Board changedBoard2 = new Board(b.n, b.board, b.teamX, b.teamO);
                    if(changedBoard2.movePiece(newLocation, 'O'))
                    {
                        Agent potentialTwo = new Agent(false, changedBoard2);
                        Integer[] currentLoc = new Integer[]{potentialTwo.board.teamX[0], potentialTwo.board.teamX[1], (potentialTwo.movesToMake.size()-maxAgent.movesToMake.size())};
                        minValInteger = minInteger(currentLoc, max(changedBoard2, iso, alpha, beta, startTime, depth-1, minAgent));
                        if(minValInteger[2] <= alpha[2])
                        {
                            return minValInteger;
                        }
                        
                        beta = minInteger(beta, minValInteger);
                    }
                }
            }
            if(depth == maxDepth - 1)
            {
                minValInteger[0] = minAgent.board.teamO[0];
                minValInteger[1] = minAgent.board.teamO[1];
            }
            return minValInteger;
        }
        return minValInteger;
    }

    public Integer[] max(Board b, Isolation iso, Integer[] alpha, Integer[] beta, float startTime, int depth, Agent minAgent)
    {
        Integer[] maxValInteger = new Integer[]{iso.board.teamX[0],iso.board.teamX[1],Integer.MIN_VALUE};//{loc1, loc2, maxVal}
        while((System.currentTimeMillis() - startTime) < this.maxTime)
        {
            Agent maxAgent = new Agent(true, b);
            if(maxAgent.movesToMake.isEmpty())
            {
                if(depth == (int)Math.floor(maxDepth))
                    return new Integer[]{b.teamO[0],b.teamO[1],Integer.MIN_VALUE};
                else
                    return new Integer[]{iso.board.teamO[0],iso.board.teamO[1],Integer.MIN_VALUE};
            }
            for(int i = 0; i < maxAgent.movesToMake.size();i++) // maximizing
            {
                if(depth > 1 )
                {
                    Integer[] newLocation = maxAgent.movesToMake.get(i);
                    Board changedBoard2 = new Board(b.n, b.board, b.teamX, b.teamO);
                    if(changedBoard2.movePiece(newLocation, 'X'))
                    {
                        Agent potentialTwo = new Agent(true, changedBoard2);             
                        Integer[] currentloc = new Integer[]{potentialTwo.board.teamX[0], potentialTwo.board.teamX[1], (potentialTwo.movesToMake.size() - minAgent.movesToMake.size())};
                        maxValInteger = maxInteger(currentloc, min(changedBoard2, iso, alpha, beta, startTime, depth, maxAgent));
                        if(maxValInteger[2] >= beta[2])
                        {
                            return maxValInteger;
                        }
                        alpha = maxInteger(alpha, maxValInteger);
                    }
                }
            }
            if(depth == maxDepth - 1)
            {
                maxValInteger[0] = maxAgent.board.teamX[0];
                maxValInteger[1] = maxAgent.board.teamX[1];
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
        kb.nextLine();
        iso.board.printBoard(whoStarted, iso.ComputerMovesMade, iso.OpponentMovesMade);
        boolean gamePlaying = true;
        
        while(gamePlaying)
        {
            Agent P = new Agent(isCompTurn, iso.board);
            if(P.movesToMake.isEmpty())
            {
                if(isCompTurn)
                    System.out.println("Player O has won");
                else
                    System.out.println("Computer has won");
                gamePlaying = false;
            }
            boolean gettingMove = true;
            if(gamePlaying)
            {
                if (isCompTurn)// x is comp eventually
                {
                    Integer[] move = new Integer[2];
                    while(gettingMove)
                    {
                        System.out.println("Player X Please enter your move: ");
                        Integer[] bestMove = iso.MinMax(isCompTurn, iso.board);
                        if(bestMove[2] == Integer.MIN_VALUE)//games over so break
                        {
                            gamePlaying = false;
                            break;
                        }
                        if(bestMove[0] != -1)
                        {
                            if (iso.board.movePiece(bestMove, 'X'))
                            {
                                System.out.println("\nThe TeamX piece has been moved to: " + bestMove[0] + ", " + (bestMove[1] + 1));
                                iso.ComputerMovesMade.add(iso.stringMaker(bestMove));
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
                        String OMove = "";
                        OMove = kb.nextLine();
                        move = getCoordinates(OMove, isCompTurn);
                        Agent OmoveAgent = new Agent(false, iso.board);
                        if(move[0] != -1)
                        {
                            //ArrayList<Integer[]> potMoves = OmoveAgent.getAvalibleMoves();
                            boolean contains = false;
                            for(int i=0; i < OmoveAgent.movesToMake.size(); i++)
                            {
                                if(OmoveAgent.movesToMake.get(i)[0] == move[0] && OmoveAgent.movesToMake.get(i)[1] == move[1])
                                {
                                    contains = true;
                                    break;
                                }
                            }
                            if (contains && iso.board.movePiece(move, 'O'))
                            {
                                System.out.println("\nThe TeamO piece has been moved to: " + move[0] + ", " + (move[1] + 1));
                                iso.OpponentMovesMade.add(OMove.toUpperCase());
                                iso.board.printBoard(whoStarted, iso.ComputerMovesMade, iso.OpponentMovesMade);
                                gettingMove = false;
                                isCompTurn = true;
                            }

                            else
                            {
                                System.out.println("Please make sure you enter a valid move");
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
}