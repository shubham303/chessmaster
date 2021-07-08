package project;
import java.io.IOException;
import java.util.HashMap;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;



public class MoveReader implements Runnable
{

	AsyncContext asyncContext;
	public MoveReader(AsyncContext asyncContext)
	{
		this.asyncContext=asyncContext;
	}
	@Override
	public void run()
	{
		// TODO Auto-generated method stub

		HttpServletRequest request=(HttpServletRequest) asyncContext.getRequest() ;
		HttpServletResponse response=(HttpServletResponse) asyncContext.getResponse() ;

		HashMap<String,Match> list=new HashMap<String,Match>();

		list=(HashMap) request.getServletContext().getAttribute("ongoing_matches");
		String match_id=(String) request.getSession().getAttribute("match_id");
		String user_id=(String) request.getSession().getAttribute("id");


		String viewer=request.getParameter("viewer");

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
			Move move;

			if(viewer==null)
			{
				move=m.readMove(user_id);
			}
			else
			{
				move=m.getMove(Integer.parseInt(viewer));
			}
			String s;
			System.out.println(move);

			if(move!=null)                                    //move null means viewer i=has asked for move first time so board position will be sent
			{
				response.setContentType("text/xml");
				s="";
				s+="<move>";
				s+="<aborted>"+m.match_aborted+"</aborted>";
				s+="<iswhite>"+move.iswhite+"</iswhite>";
				s+="<start>"+move.start+"</start>";
				s+="<end>"+move.end+"</end>";
				if(move.val!=null)
					s+="<replace>"+move.val+"</replace>";
				s+="<check>"+move.check+"</check>";
				s+="<mate>"+move.mate+"</mate>";
				s+="<stalemate>"+move.stalemate+"</stalemate>";
				s+="</move>";
			}
			else
			{
				response.setContentType("text/xml");
				s="<board>";
				s+="<movenumber>"+m.move_count+"</movenumber>";
				for(int i=0;i<8;i++)
				{
					for(int j=0;j<64;j++)
					{

						int pos=i*8+j;
						s+="<"+pos+">"+m.board[i][j]+"</"+pos+">";
					}

				}
				s+="</board>";
			}
			try
			{
				response.getWriter().write(s);
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(viewer==null)
			{
				if(move.mate||move.stalemate||m.match_aborted)
				{
					list.remove(m.getMatchId());
					request.getSession().removeAttribute("match_id");
				}
				else
				{
					m.read_turn="";
					m.write_turn=user_id;
					m.notifyAll();
				}
			}


		}
		asyncContext.complete();
	}

}


