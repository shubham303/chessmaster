package project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class AppContextListener implements ServletContextListener {

	public void contextInitialized(ServletContextEvent servletContextEvent) 
	{

		// create the thread pool
		System.out.println("ss");
		ThreadPoolExecutor executor = new ThreadPoolExecutor(100, 200, 50000L,
				TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(100));
		servletContextEvent.getServletContext().setAttribute("executor",
				executor);
		
		HashMap<String,Match> list=new HashMap<String,Match>();               //  ongoing matches list
		MatchInfo info=new MatchInfo();                                         // requested match 
		TreeSet<String> active_users=new TreeSet<String>();                 // active users
		
		servletContextEvent.getServletContext().setAttribute("ongoing_matches",list);
		servletContextEvent.getServletContext().setAttribute("requested_match",info);
		servletContextEvent.getServletContext().setAttribute("active_users",active_users);
	}

	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		ThreadPoolExecutor executor = (ThreadPoolExecutor) servletContextEvent
				.getServletContext().getAttribute("executor");
		executor.shutdown();
	}

}