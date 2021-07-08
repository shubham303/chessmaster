package project;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.LinkedList;


public class Match
{
	private String match_id;
	private String player1,player2;
	private Move last_move;
	public String write_turn;
	public String read_turn;
	private Move move_list[];
	private String result;
	public int move_count=0;
	public int[][] board=new int[8][8];
	boolean match_aborted=false;

	public Match(String match_id,String player1,String player2)
	{
		this.player1=player1;
		this.player2=player2;
		last_move=null;
		write_turn=player1;
		read_turn="";
		this.match_id=match_id;
		move_list=new Move[100];

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

		result=null;
	}
	String getMatchId()
	{
		return match_id;
	}
	public String  getFirstPlayer()
	{
		return player1;
	}
	public String getSecondPlayer()
	{
		return player2;
	}
	public void writeMove(String player_id,Move move)
	{
		while(!write_turn.equals(player_id))
		{
			try{
				wait();
			}catch(InterruptedException e){}
		}
		last_move=move;
		if(move_count==100)
			move_count=0;

		move_list[move_count++]=move;

		if(player_id.equals(player1))
			makeChangesInBoard(true,move);                   //this method makes changes in board  and true means its a move of white player
		else
			makeChangesInBoard(false,move);

		write_turn="";

		if(player_id==player1)
			read_turn=player2;
		else
			read_turn=player1;

		notifyAll();
	}
	public void makeChangesInBoard(boolean white_turn,Move move)
	{
		int start,val,end;
		if(!white_turn)
		{
			start=63-Integer.parseInt(move.start);
			end=63-Integer.parseInt(move.end);

		}
		else
		{
			start=Integer.parseInt(move.start);
			end= Integer.parseInt(move.end);
		}
		try
		{
			val=Integer.parseInt(move.val);
		}
		catch(Exception e)
		{
			val=0;
		}
		int start_row=start/8;
		int start_column=start%8;

		int end_row=end/8;
		int end_column=end%8;

		if(val==0)
		{
			board[end_row][end_column]=board[start_row][start_column];
		}
		else
			board[end_row][end_column]=val;

		board[start_row][start_column]=0;
	}
	public Move readMove(String player_id)
	{

		while(!read_turn.equals(player_id))
		{
			try
			{
				wait();
			}catch(InterruptedException e){}
		}
		return last_move;
	}
	public Move getMove(int move_number )               //called by viewer of the match
	{
		if(move_number==-1)
			return null;

		else
		{
			while(move_number>=move_count)
			{
				try
				{
					wait();
				} catch (InterruptedException e)
				{
				// TODO Auto-generated catch block
				e.printStackTrace();
				}
			}
		}
		return move_list[move_number];
	}
	public void setResult(String id)    //here id is id of winner
	{
		String  status;
		String winner;

		if(!id.equals("stalemate")&&!id.equals("abort"))
		{
			status="completed";
			winner=id;
		}
		else
		{
			status=id;
		}

		try
		{
			Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "c##project", "project");
			String sql="insert into match values(?,?,?,?,?)";
			PreparedStatement st=con.prepareStatement(sql);
			st.setString(1,match_id);
			st.setString(2,player1);
			st.setString(3,player2);
			st.setString(4,status);
			st.setString(5,id);
			st.executeUpdate();
			con.close();
		}catch(Exception e){System.out.println("exception at set result");}
	}
	public String getResult()
	{
		return result;
	}
	public void abort(String id)
	{
		//save result in this function;
		if(player1.equals(id))
			read_turn=player2;
		else
			read_turn=player1;

		match_aborted=true;
		setResult("abort");
	}
	public void  addViewer(String user_id)
	{
		//viewer_list.add(user_id);
	}
	public void removeViewer(String user_id)
	{
		//viewer_list.remove(user_id);
	}


}
