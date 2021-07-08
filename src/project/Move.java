package project;


public class Move
{
	String start,end;                  //start and end specify start end location of moved piece
	String val;
	boolean check;
	boolean mate;
	boolean stalemate;
	boolean iswhite;
	
	public Move(String start,String end,String val,boolean check,boolean mate,boolean stalemate)
	{
		this.end=end;
		this.start=start;
		this.val=val;
		this.check=check;
		this.mate=mate;
		this.stalemate=stalemate;
	}
	public Move(String start,String end,boolean check,boolean mate,boolean stalemate)
	{
		this.end=end;
		this.start=start;
		val=null;
		this.check=check;
		this.mate=mate;
		this.stalemate=stalemate;
		
	}
	public Move(boolean iswhite,String start,String end,String val,boolean check,boolean mate,boolean stalemate)
	{
		this.iswhite=iswhite;
		this.end=end;
		this.start=start;
		this.val=val;
		this.check=check;
		this.mate=mate;
		this.stalemate=stalemate;
	}
	
}
