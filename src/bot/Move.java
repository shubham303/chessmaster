package bot;

public class Move
{
	short start,end;                  //start and end specify start end location of moved piece
	
	boolean left_castling;          //specify whether move is castling with left or right rook
	boolean right_castling;
	boolean our_turn;
	
	public Move(boolean is_left_castling,boolean our_turn)
	{
		left_castling=is_left_castling;
		right_castling=!left_castling;
		this.our_turn=our_turn;
	}
	public Move(short start,short end)
	{
		this.end=end;
		this.start=start;
		left_castling=false;
		right_castling=false;
	}
	
	
	
}
