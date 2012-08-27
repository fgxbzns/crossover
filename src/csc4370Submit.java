

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 * Servlet implementation class csc4370Submit
 */
public class csc4370Submit extends HttpServlet {
      

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, 
			HttpServletResponse response) 
	throws ServletException, IOException {
		// TODO Auto-generated method stub
		
	
		response.setContentType("text/html");
	    PrintWriter out = response.getWriter();
	    String title = "User Information";
	    String docType =
	      "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 " +
	      "Transitional//EN\">\n";
	    out.println(docType +
	                "<HTML>\n" +
	                "<HEAD><TITLE>" + title + "</TITLE></HEAD>\n" +
	                "<BODY BGCOLOR=\"#FDF5E6\">\n" +
	                "<H1 ALIGN=\"CENTER\">" + title + "</H1>\n" +
	                "<UL>\n" +
	                "  <LI><B>First name</B>: "
	                + request.getParameter("fname") + "\n" +
	                "  <LI><B>Last name</B>: "
	                + request.getParameter("lname") + "\n" +
	                "  <LI><B>Address</B>: "
	                + request.getParameter("address") + "\n" +
	                "  <LI><B>Zipcode</B>: "
	                + request.getParameter("zip") + "\n" +
	                "</UL>\n" +
	                "</BODY></HTML>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, 
			HttpServletResponse response) 
	throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
