package project;
import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AbortMatch extends HttpServlet 
{
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		HttpSession s=request.getSession(false);
		if(s==null)
		{
			request.getRequestDispatcher("login.html").forward(request,response);
		}
		else
		{
			String match_id=(String) s.getAttribute("match_id");
			if(match_id!=null)
			{
				HashMap<String,Match> list=((HashMap<String,Match>) s.getServletContext().getAttribute("ongoing_matches"));
				Match m;
			
				synchronized(list)
				{
					m=list.get(match_id);
				}
				synchronized(m)
				{
					m.abort((String) s.getAttribute("id"));
				}	
				s.removeAttribute("match_id");
				request.getRequestDispatcher("index.html").forward(request,response);
			}
		}
	}
}