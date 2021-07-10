package project;


import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// TODO Auto-generated method stub
		String id=request.getParameter("id");
		String pass=request.getParameter("pass");
		
		ResultSet rset;
		
		if(!id.equals("")&&!pass.equals(""))
		{
			Connection con;
			try 
			{
				con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "c##project", "project");
				String sql="Select * from Accounts where id='"+id+"'and pass='"+pass+"'";
				PreparedStatement st=con.prepareStatement(sql);
			    rset=st.executeQuery();
			
		
				if(rset.next())
				{
					HttpSession s=request.getSession(true);
					s.setAttribute("id",id);
					TreeSet active_users =(TreeSet) request.getServletContext().getAttribute("active_users");
					synchronized(active_users)
					{
						if(!active_users.contains(id))
							active_users.add(id);
						else
							request.getRequestDispatcher("signin.html").forward(request,response);  
					}
					con.close();
					request.getRequestDispatcher("userpage.html").forward(request,response);        //redirect to home page
			
				}
				
				else
				{
					con.close();
					request.getRequestDispatcher("signin.html").forward(request,response);		
				}
			}catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
		else
			request.getRequestDispatcher("signin.html").forward(request,response);			
	}

}
