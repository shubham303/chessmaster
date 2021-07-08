package project;


import java.io.IOException;
import java.util.HashMap;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class Logout
 */
@WebServlet("/Logout")
public class Logout extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Logout() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		HttpSession s=request.getSession(false);
		TreeSet<String> t= (TreeSet<String>) request.getServletContext().getAttribute("active_users");
		t.remove(s.getAttribute("id"));
		
		String match_id=(String) s.getAttribute("match_id");
		if(match_id!=null)
		{
			HashMap<String,Match> list=(HashMap<String, Match>) request.getServletContext().getAttribute("ongoing_matches");
			Match m=list.get("match_id");
			if(m!=null)
				m.abort((String)s.getAttribute("id"));
		}
		s.invalidate();
		request.getRequestDispatcher("index1.html").forward(request,response);
	}

}
