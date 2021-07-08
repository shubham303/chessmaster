package bot;

public class WhiteBoard extends ChessBoard
{
	public WhiteBoard()
	{
		board=new int[8][];
		
		board[0][0]=4;board[0][1]=3;board[0][2]=2;
		board[0][3]=6;board[0][4]=5;board[0][5]=2;
		board[0][6]=3;board[0][7]=4;
		for(int i=0;i<8;i++)
		{	
			board[1][i]=1;
			board[6][i]=-1;
		}
		board[7][0]=-4;board[7][1]=-3;board[7][2]=-2;
		board[7][3]=-6;board[7][4]=-5;board[7][5]=-2;
		board[7][6]=-3;board[7][7]=-4;
		
	}
	
	public static void main(String args[])
	{
		ChessBoard c=new WhiteBoard();
	}
	
}