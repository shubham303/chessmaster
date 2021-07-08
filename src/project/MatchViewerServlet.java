package project;


import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class MatchViewerServlet
 */
@WebServlet("/MatchViewerServlet")
public class MatchViewerServlet extends HttpServlet 
{
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession s=request.getSession(false);
		
		//int number=Integer.parseInt(request.getParameter("number"));
		if(s==null)
		{
			request.getRequestDispatcher("login.html").forward(request,response);
		}
		else
		{
			HashMap<String,Match> list=((HashMap<String,Match>) s.getServletContext().getAttribute("ongoing_matches"));
			Match match;
			String match_id;
			synchronized(list)
			{
				Set<String> keys=list.keySet();
				if(!keys.isEmpty())
				{
					for(String key: keys)
					{
						match_id=key;
						//match=list.get();
						s.setAttribute("match_id",key);
						try
						{
							request.getRequestDispatcher("view.html").forward(request,response);
						} catch (ServletException | IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						break;
					}
					for(String key:keys)
					{
						System.out.println(key);
					}
				}
				else
				{
					response.getWriter().write("no matches");
				}
			}
			//match.addViewer((String)s.getAttribute("user_id"));
			
			
		}
	}

}
