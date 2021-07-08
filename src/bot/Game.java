package bot;

public class Game 
{
	private ChessBoard chessboard;
	private Bot bot;
	private Opposition opposition;
	private String player_id;

	public Game(String player_id,boolean isWhite)
	{
		this.player_id=player_id;
		if(isWhite)
			chessboard=new WhiteBoard();
		bot =new Bot(chessboard);
		opposition=new Opposition(player_id);
	}
	
	
	
}
