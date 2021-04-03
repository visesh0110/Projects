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
 
/**
* Servlet implementation class AddRoute
*/
@WebServlet("/AddRoute")
public class AddRoute extends HttpServlet {
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
                                        
                                         //String train_name = request.getParameter("train_name");
                                         int train_id =Integer.parseInt(request.getParameter("train_id"));
                                         //String Source_stn = request.getParameter("source_stn");
                                         int Stop_station = Integer.parseInt(request.getParameter("station_id"));
                                         int Stop_number = Integer.parseInt(request.getParameter("stop_number"));
                                         int Source_distance = Integer.parseInt(request.getParameter("source_distance"));
                                         String arrival_time = request.getParameter("arrival_time");
                                         String departure_time = request.getParameter("departure_time");
                                        
                                         String query = "INSERT INTO route VALUES(?,?,?,?,?,?)";
                                        
                                         pst = con.prepareStatement(query);
                                         pst.setInt(1,train_id);
                                         pst.setInt(2,Stop_number);
                                         pst.setInt(3,Stop_station);
                                         pst.setString(4,arrival_time);
                                         pst.setString(5,departure_time);
                                         pst.setInt(6,Source_distance);
                                        
                                         pst.execute();
                                         pst.close();
                                         con.close();
                                         out.println("sucess");
                                         RequestDispatcher rd=request.getRequestDispatcher("/form14.html");
                                      	 rd.include(request, response);
                           }
                           catch(Exception e){
                                         out.println("ERORR"+e);
                                         RequestDispatcher rd=request.getRequestDispatcher("/form5.html");
                                      	 rd.include(request, response);
                           }
                      
              }
 
              /**
              * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
              */
              protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                           doGet(request,response);
              }
 
}
 