package project;



public class MatchInfo 
{	
	boolean player_available;
	String player_id;
	String match_id;
	
	MatchInfo()
	{
		player_available=false;
		player_id="";
	}
	void setMatchId(String id)
	{
		match_id=id;
	}
	public String getMatchId()
	{
		return match_id;
	}
	void setPlayerId(String id)
	{
		player_id=id;
		player_available=true;
	}
	void clear()
	{
		player_available=false;
		player_id="";
	}
	boolean isPlayerAvailable()
	{
		return player_available;
	}
	String getPlayerId()
	{
		return player_id;
	}
}

