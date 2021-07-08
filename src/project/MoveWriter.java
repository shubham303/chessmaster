package project;
// class is used for writin the move made by player on server

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class MoveWriter implements Runnable {


	AsyncContext asyncContext;
	public MoveWriter(AsyncContext asyncContext)
	{
		this.asyncContext=asyncContext;
	}

	@Override
	public void run()
	{
		// TODO Auto-generated method stub

		String start;
		String end;
		String replace;
		boolean check;
		boolean mate;
		boolean stalemate;

		HttpServletRequest request=(HttpServletRequest) asyncContext.getRequest() ;
		HttpServletResponse response=(HttpServletResponse) asyncContext.getResponse() ;

		HashMap<String,Match> list=new HashMap<String,Match>();

		list=(HashMap) request.getServletContext().getAttribute("ongoing_matches");

		String match_id=(String) request.getSession().getAttribute("match_id");
		String id=(String) request.getSession().getAttribute("id");


		start=request.getParameter("start");
		end=request.getParameter("end");
		replace=request.getParameter("replace");
		check="true".equals(request.getParameter("check"));
		mate="true".equals(request.getParameter("mate"));
		stalemate="true".equals(request.getParameter("stalemate"));


		Match m=list.get(match_id);

		synchronized(m)
		{
			if(m==null)
			{

				try
				{
					asyncContext.getRequest().getRequestDispatcher("index1.html").forward(request,response);
				} catch ( IOException e) {
				// TODO Auto-generated catch block
					e.printStackTrace();
					} catch (ServletException e) {
				// TODO Auto-generated catch block
						e.printStackTrace();
				}
			}
			if(!m.match_aborted)
			{
				if(id.equals(m.getFirstPlayer()))
					m.writeMove(id,new Move(true,start,end,replace,check,mate,stalemate));

				else
					m.writeMove(id,new Move(false,start,end,replace,check,mate,stalemate));
				if(mate)
				{
					m.setResult(id);
					request.getSession().removeAttribute("match_id");
				}
				if(stalemate)
				{
					m.setResult("stalemate");
					request.getSession().removeAttribute("match_id");
				}
			}
			m.notifyAll();
		}
		asyncContext.complete();
	}
}
