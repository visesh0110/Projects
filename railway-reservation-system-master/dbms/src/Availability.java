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
 
 
@WebServlet("/Availability")
public class Availability extends HttpServlet {
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
                                        
                                         String trainno = request.getParameter("trainno");
                                         String date = request.getParameter("date");
                                         String month = request.getParameter("month");
                                         String source_stn = request.getParameter("st");
                                         String destination_stn = request.getParameter("dt");
                                         String clas = request.getParameter("cl");
                                         String query = "Select Train_ID,Available_Date,Available_seat3,Available_seat2,Available_seat1,Booked_seat3,Booked_seat2,Booked_seat1,Waiting_seat3,Waiting_seat2,Waiting_seat1 from train_status where Train_ID='"+trainno+"' and Available_Date='" +date+month+"'";
                                         st=con.createStatement();
                                         ResultSet rs=st.executeQuery(query);
                                         
                                                                    
                                       int avs=0;
                                       int w=0;
                                       int b=0;
                                       
                                         while(rs.next()){ //if(rs.getString(columnIndex))
                                                       avs = rs.getInt("Available_seat"+clas);
                                                       w=rs.getInt("Booked_seat"+clas);
                                                       b=rs.getInt("Waiting_seat"+clas);
                                                       out.println("<html><body>"+"<h1> Availability "+avs+"| booked seat "+w+" | waiting seat "+b+"</body></html>"); 
                                                       
                                                      
                                         }
                                         RequestDispatcher rd=request.getRequestDispatcher("/form0.html");
                                     	 rd.include(request, response);
                                                    
                                        
              }
              catch(Exception e){
              out.println("ERROR"+e);     
              RequestDispatcher rd=request.getRequestDispatcher("/form10.html");
          	rd.include(request, response);
              }
              }
 
              protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                           doGet(request,response);
              }
 
}