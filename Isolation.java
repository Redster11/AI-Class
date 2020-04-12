import java.util.Arrays;

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
                this.board[this.teamO[0]][this.teamO[1]] = '#';
                this.board[newLocation[0]][newLocation[1]] = 'O';
            }
            else
            {
                this.board[this.teamX[0]][this.teamX[1]] = '#';
                this.board[newLocation[0]][newLocation[1]] = 'X';
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
        this.teamO = new int[]{0, 0};
        this.teamX = new int[]{this.n-1, this.n-1};
        this.board[teamO[0]][teamO[1]] = 'O'; // team O
        this.board[teamX[0]][teamX[1]] = 'X'; // team X
    }
    public void printBoard()
    {
        for (int j = 0; j < this.n; j++)
        {
            for(int i = 0; i < this.n; i++)
            {
                System.out.print("  " + this.board[i][j]);
            }
            System.out.println();
        }
    }
    
}
public class Isolation
{
    Board board = new Board(8);
    

    public static void main(String[] args) {
        Isolation iso = new Isolation();
        iso.board.printBoard();
        int[] move = new int[] { 6, 6 };
        if (iso.board.movePiece(move, 'X'))
        {
            System.out.println("\nThe TeamX piece has been moved to: " + move[0] + ", " + move[1]);
            iso.board.printBoard();
        }
        else   
            System.out.println("Please enter a different move");
    }
    

}