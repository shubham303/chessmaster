package project;





import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class MatchSetter implements Runnable {

	AsyncContext asyncContext;
	MatchInfo mi;
	Match match;
	
	public MatchSetter(AsyncContext asyncContext)
	{
		this.asyncContext=asyncContext;
	}
	@Override
	public void run() 
	{
		
		HttpServletRequest request=(HttpServletRequest) asyncContext.getRequest() ;
		HttpServletResponse response=(HttpServletResponse) asyncContext.getResponse() ;
		
		HttpSession s=request.getSession(false);
		String id=(String)s.getAttribute("id");
		
		mi=(MatchInfo) request.getServletContext().getAttribute("requested_match");
		
		synchronized(mi)
		{
			if(!mi.isPlayerAvailable()||mi.getPlayerId().equals(id))
			{			
				mi.setPlayerId(id);
				mi.setMatchId(id+System.currentTimeMillis());
				
				try 
				{
					mi.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				s.setAttribute("match_id",mi.getMatchId());
				try
				{
					request.getRequestDispatcher("chess.html").forward(request,response);
				} catch (ServletException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			else
			{		
					
					match=new Match(mi.getMatchId(),mi.getPlayerId(),id);
					
					HashMap<String,Match> list=((HashMap<String,Match>) s.getServletContext().getAttribute("ongoing_matches"));
					
					synchronized(list)
					{
						list.put(match.getMatchId(),match);
					}
					s.setAttribute("match_id",mi.getMatchId());
					mi.clear();
					mi.notifyAll();
				
					try {
							asyncContext.getRequest().getRequestDispatcher("blackchess.html").forward(request,response);	
						} catch ( IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
					} catch (ServletException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}		
						
						
					}	
				}
			asyncContext.complete();
			
		}
		
		
	}


