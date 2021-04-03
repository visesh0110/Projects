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
 
 
@WebServlet("/Cancelpnr")
public class Cancelpnr extends HttpServlet {
              protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                           response.setContentType("text/html");
                           PrintWriter out = response.getWriter();
                           String url = "jdbc:mysql://localhost:3306/";
                           String dbname = "railway";
                           String driver = "com.mysql.jdbc.Driver";
                           String usr = "root";
                           String pwd = "";
                           Statement st;
                           java.sql.PreparedStatement pst;
                           try{
                                         Class.forName(driver);
                                         Connection con = DriverManager.getConnection(url+dbname,usr,pwd);
                                        
                                     int pnr=Integer.parseInt(request.getParameter("pnr"));
                                       String query = "Update passenger_ticket set Reservation_status='C' where pnr="+pnr+"";
                                       pst=con.prepareStatement(query);
                                   
                                         //st=con.createStatement();
                                         pst.execute();
                                         out.println("Success");  
                                         RequestDispatcher rd=request.getRequestDispatcher("/form15.html");
                                       	rd.include(request, response);                             
                                      
                                                    
                                        
                              }
              catch(Exception e){
              out.println("ERROR"+e);     
              RequestDispatcher rd=request.getRequestDispatcher("/form16.html");
          	rd.include(request, response);
              }
              }
 
              protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                           doGet(request,response);
              }
 
}