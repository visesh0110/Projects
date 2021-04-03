import java.io.*; 
import java.util.*; 
import javax.servlet.*; 
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
@WebServlet ("/GreetServlet")
public class GreetServlet extends HttpServlet { 
 
  public void doGet(HttpServletRequest req,  HttpServletResponse res)  
  throws ServletException, IOException { 
 
    // Get print writer. 
    PrintWriter out = res.getWriter(); 
    res.setContentType("text/html"); 
	String name=req.getParameter("NAME");
	out.println("<html><body>"
	           +"<h1>Hello " +name+ "Welcome To Servlet Programming...."
			   +"</body></html>");
  }
//Servlet to handle “post” method
 public void doPost(HttpServletRequest req,  HttpServletResponse res)  
 throws ServletException, IOException {
	  doGet(req,res);
 }
}
