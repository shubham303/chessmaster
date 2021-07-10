package bot;
public abstract class ChessBoard
{
	protected final int rook=4;
	protected int knight=3;
	protected int bishop=2;
	protected int king=6;
	protected int queen=5;
	protected int pawn=1;
	
	int board[][];
	int enemy_pieces_location[];
	int bot_pieces_location[];
	
}
