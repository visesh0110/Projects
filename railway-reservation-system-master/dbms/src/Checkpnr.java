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
 
 
@WebServlet("/Checkpnr")
public class Checkpnr extends HttpServlet {
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                  response.setContentType("text/html;charset=UTF-8");
                           PrintWriter out = response.getWriter();
                           String url = "jdbc:mysql://localhost:3306/";
                  String dbName = "railway";
                  String driver = "com.mysql.jdbc.Driver";
                  String user = "root"; 
                           String password = "";
                           Statement st=null;
                           try {
                                  
                      Class.forName(driver);
                      Connection conn = DriverManager.getConnection(url+dbName, user, password);
                      st=conn.createStatement();
                      int pnr=Integer.parseInt(request.getParameter("pnr"));
                      String query="select pt.Reservation_status,pt.Train_ID,p.Seat_number from passenger p,passenger_ticket pt where pt.PNR="+pnr+" and p.PNR=pt.PNR";
                      ResultSet rs=st.executeQuery(query);
                     // out.println("aa");
                      if(!rs.isBeforeFirst())
                      {
                           out.println("Invalid PNR\n");
                           out.println("please enter again");
                           RequestDispatcher rd=request.getRequestDispatcher("/form9.html");
                           rd.include(request, response);
                      }
                     if(rs.isBeforeFirst())
                      {   //out.println("aa");
                           while(rs.next())
                           {
                                         if(rs.getString("pt.Reservation_status").equals("W"))
                                                       out.println("STATUS : WAITING\n ");
                                         if(rs.getString("pt.Reservation_status").equals("C"))
                                                       out.println("STATUS : CANCELED\n ");
                                         else if(rs.getString("pt.Reservation_status").equals("B"))
                                         {              //out.println("aa");
                                                       out.println("TRAIN_ID : "+ rs.getString("pt.Train_ID")+"SEAT_NUMBER : "+" | " + rs.getString("p.Seat_number")+" | "+"STATUS : BOOKED");
                                                      
                                         }
                           }
                          
                           RequestDispatcher rd=request.getRequestDispatcher("/form15.html");
                        	rd.include(request, response);
                      }
                     
                }
              		
                  	catch (Exception e) { 
                  		
                  	    System.out.println("ERROR"+e);
                  	    out.println("error"+e);
                  	    RequestDispatcher rd=request.getRequestDispatcher("/form9.html");
                  	    rd.include(request, response);
                  	    
                  	}
               
                           }
                          
              protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                          
                           doGet(request, response);
              }
 
}