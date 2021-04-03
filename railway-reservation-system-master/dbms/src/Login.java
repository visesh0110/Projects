
 
/**
* Servlet implementation class Login
*/
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

@WebServlet("/Login")
public class Login extends HttpServlet {
              protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                           response.setContentType("text/html;charset=UTF-8");
                           PrintWriter out = response.getWriter();
                         
                           String url = "jdbc:mysql://localhost:3306/";
                            String dbName = "railway";
                          String driver = "com.mysql.jdbc.Driver";
                           String user = "root"; 
                           String password = "";
                           Statement st;
                         
                           ResultSet rs=null;
                           ResultSet rs2=null;
                         
                           try {
                                  
            Class.forName(driver);
            
         
            
            Connection conn = DriverManager.getConnection(url+dbName, user, password);
            String login=request.getParameter("login1");
            String passworrd=request.getParameter("pass");
            String query = "select password,email_id from user where email_id='"+login+"' and password='"+passworrd+"'";
            
            st = conn.createStatement(); 
          
         
            rs=st.executeQuery(query);
          
            String query2="select User_ID from admin_table where User_ID='"+login+"'";
            st = conn.createStatement(); 
            rs2=st.executeQuery(query2);
           
     
           if(!rs2.isBeforeFirst())
           {
        	  // System.out.println("love");
            if(!rs.isBeforeFirst())
            { out.println("Sorry un/pwd error");
                  RequestDispatcher rd=request.getRequestDispatcher("/form2.html");
                  rd.include(request, response);
            }
            else
            {
              RequestDispatcher rd=request.getRequestDispatcher("/form15.html");
               rd.forward(request, response);
            }
           }
           if(!rs.isBeforeFirst())
           {
        	  // System.out.println("love2");
                 if(!rs2.isBeforeFirst())
                 {
                            
                 }
                 else
                 {
                              RequestDispatcher rd=request.getRequestDispatcher("/form14.html");
                 rd.forward(request, response);
                 }
           }
                           }
                           catch(Exception e)
                           {
                                         System.out.println("ERROR"+e);
                           } 
            
            
             
                          
                          
                          
              }
             
              protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                           // TODO Auto-generated method stub
            	  System.out.println("aaaaaaaaaa");
                           doGet(request, response);
              }
 
}