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
 
 
 
 
 
@WebServlet("/AddStation")
public class AddStation extends HttpServlet {
              protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                           response.setContentType("text/html;charset=UTF-8");
                           PrintWriter out = response.getWriter();
                           String url = "jdbc:mysql://localhost:3306/";
                  String dbName = "railway";
                  String driver = "com.mysql.jdbc.Driver";
                  String user = "root"; 
                           String password = "";
                           java.sql.PreparedStatement pstmt;
                           try {
                                  
                      Class.forName(driver);
                      Connection conn = DriverManager.getConnection(url+dbName, user, password);
                      int station_id=Integer.parseInt(request.getParameter("station_id"));
                      String name=request.getParameter("station_name");
                       String query="insert into station values(?,?)";
                      pstmt=conn.prepareStatement(query);
                      pstmt.setInt(1, station_id);
                      pstmt.setString(2,name);
                      pstmt.execute();
                      out.println("sucess");
                      RequestDispatcher rd=request.getRequestDispatcher("/form14.html");
                    	rd.include(request, response);
                     
                    }
                           catch(Exception e)
                           {
                                         out.println("ERROR"+e);
                                         RequestDispatcher rd=request.getRequestDispatcher("/form4.html");
                                      	 rd.include(request, response);
                           }
              }
              protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                          
                           doGet(request, response);
              }
 
}