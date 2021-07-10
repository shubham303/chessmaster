package project;


import java.io.IOException;
import java.util.concurrent.ThreadPoolExecutor;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class MatchHandlerServlet
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/MatchHandlerServlet" })
public class MatchHandlerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MatchHandlerServlet() 
    {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException 
    {
    	
    	HttpSession s=request.getSession(false);
    	
    	if(s==null)
    	{
    		try
			{
				request.getRequestDispatcher("login.html").forward(request,response);
			} catch (ServletException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	else
    	{
    		request.setAttribute("org.apache.catalina.ASYNC_SUPPORTED", true);
    		AsyncContext asyncCtx = request.startAsync();
    		asyncCtx.addListener(new AppAsyncListener());
    		asyncCtx.setTimeout(3000000);
    		
    		ThreadPoolExecutor executor = (ThreadPoolExecutor) request.getServletContext().getAttribute("executor");
			executor.execute(new MoveReader(asyncCtx));
    	}
    }
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		HttpSession s=request.getSession(false);
    	if(s==null)
    	{
    		try
			{
				request.getRequestDispatcher("login.html").forward(request,response);
			} catch (ServletException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	else
    	{
    		request.setAttribute("org.apache.catalina.ASYNC_SUPPORTED", true);
		
    		AsyncContext asyncCtx = request.startAsync();
    		asyncCtx.addListener(new AppAsyncListener());
    		asyncCtx.setTimeout(3000000);
		
    		ThreadPoolExecutor executor = (ThreadPoolExecutor) request.getServletContext().getAttribute("executor");

			executor.execute(new MoveWriter(asyncCtx));
    	}
	}

}
