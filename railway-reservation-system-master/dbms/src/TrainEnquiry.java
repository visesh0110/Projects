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
//import com.mysql.jdbc.*;
 
@WebServlet("/TrainEnquiry")
public class TrainEnquiry extends HttpServlet {
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
                                         String sstn=request.getParameter("sstn");
                                         String dstn=request.getParameter("dstn");
                                         String date=request.getParameter("date");
                                         String month=request.getParameter("month");
                                         String cl=request.getParameter("class");
                                         String query="select distinct route.Train_ID,train.Train_name,train_status.Available_seat"+cl+" from route,train,train_status where (route.Train_ID=train.Train_ID) and (train.Train_ID=train_status.Train_ID) and (route.Train_ID in (select source.Train_ID from route as source,route as destination where (source.Train_ID=destination.Train_ID) and((source.Stop_number-destination.Stop_number)<0) and (destination.Station_ID=(select Station_ID from station where Station_Name='"+dstn+"' )) and source.Station_ID=(select Station_ID from station where Station_Name='"+sstn+"' ))) and (train_status.Available_Date='"+date+month+"')";
                                         st=con.createStatement();
                                         ResultSet rs=st.executeQuery(query);
                                         while(rs.next())
                                         {
                                        	 out.println("train id: "+ rs.getInt("route.Train_ID")+" train name "+rs.getString("train.Train_name")+" available seats "+rs.getInt("train_status.Available_seat"+cl));
                                        	 out.println("<br>");
                                         }
                                         RequestDispatcher rd=request.getRequestDispatcher("/form13.html");
                                     	
                                         rd.include(request, response);
                                }
                           catch(Exception e)
                           {
                                         out.print("error"+e);
                                         RequestDispatcher rd=request.getRequestDispatcher("/form12.html");
                                     	 rd.include(request, response);
                           }
              }
              protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                          
                           doGet(request, response);
              }
 
}