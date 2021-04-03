import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.annotation.WebServlet;
import java.sql.Statement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import com.java.sql.Connection;
import com.mysql.jdbc.*;



@WebServlet("/UserRegistration")
public class UserRegistration extends HttpServlet {
	protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		  PrintWriter out = response.getWriter();
		 // out.println("love");
		  String url = "jdbc:mysql://localhost:3306/"; 
		  String dbName = "railway"; 
	     String driver = "com.mysql.jdbc.Driver"; 
	     String user = "root";  
		 String password = ""; 
		int i=0;	   
		 Statement st;
		 java.sql.PreparedStatement pstmt; 
		try { 
			       
	              Class.forName(driver).newInstance(); 
                  Connection conn = DriverManager.getConnection(url+dbName, user, password);
	       
		
         String emailid=req.getParameter("emailid");
         String passworrd=req.getParameter("password");
         String confirm_password=req.getParameter("confirm_password");
         String gender=req.getParameter("gender");
         String security_answer=req.getParameter("security_answer");
         
         String age=req.getParameter("age");
         String mobile=req.getParameter("mobile");
         String city=req.getParameter("city");
         String state=req.getParameter("state");
         
         String nationality=req.getParameter("nationality");
         String fullname=req.getParameter("fullname");
         String security_question=req.getParameter("secret_question");
         
	     String query = "INSERT INTO USER VALUES(?,?,?,?,?,?,?,?,?,?,?)"; 
	     
		 pstmt = conn.prepareStatement(query); 
		 pstmt.setString(1,emailid);
		 pstmt.setString(2,passworrd);
		 pstmt.setString(3,fullname);
		 pstmt.setString(4,gender);
		 pstmt.setString(5,age);
		 pstmt.setString(6,mobile);
		 pstmt.setString(7,city);
		 pstmt.setString(8,state);
		 pstmt.setString(9,nationality);
		 pstmt.setString(10,security_question);
		 pstmt.setString(11,security_answer);
	     pstmt.execute(); 
	     pstmt.close();
	     conn.close();
	 	
	     
		}
		
		
	catch (Exception e) { 
		i=1;
	    System.out.println("ERROR"+e);
	    out.println("error"+e);
	    RequestDispatcher rd=req.getRequestDispatcher("/form1.html");
	    rd.include(req, response);
	  
	   
       }
		
	if(i==0)
	{
		 out.println("success");
		 out.println("login now");
		RequestDispatcher rd=req.getRequestDispatcher("/form2.html");
		rd.include(req, response);
	}	

}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
