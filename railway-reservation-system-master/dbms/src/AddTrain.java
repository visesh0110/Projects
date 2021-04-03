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
 
@WebServlet("/AddTrain")
public class AddTrain extends HttpServlet {
             
              protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
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
        int train_id=Integer.parseInt(request.getParameter("train_id"));
        String name=request.getParameter("train_name");
        String train_type =request.getParameter("train_type");
        String station=request.getParameter("source_stn");
        int station_id=Integer.parseInt(request.getParameter("source_id"));
        String dest_station=request.getParameter("destination_stn");
        int dest_id=Integer.parseInt(request.getParameter("destination_id"));
        String sun=request.getParameter("Sun")+"1";
        String mon=request.getParameter("Mon")+"1";
        String tue =request.getParameter("Tue")+"1";
        String wed=request.getParameter("Wed")+"1";
        String thur=request.getParameter("Thur")+"1";
        String fri=request.getParameter("Fri")+"1";
        String sat=request.getParameter("Sat")+"1";
        String available_date=request.getParameter("available_date");
        int fare1=Integer.parseInt(request.getParameter("fare1"));
        int fare2=Integer.parseInt(request.getParameter("fare2"));
        int fare3=Integer.parseInt(request.getParameter("fare3"));
        int available_seat1=Integer.parseInt(request.getParameter("available_seat1"));
        int available_seat2=Integer.parseInt(request.getParameter("available_seat2"));
        int available_seat3=Integer.parseInt(request.getParameter("available_seat3"));
        String query="insert into Train values(?,?,?,?,?,?,?)";
        pstmt=conn.prepareStatement(query);
        pstmt.setInt(1, train_id);
        pstmt.setString(2, name);
        pstmt.setString(3, train_type);
        pstmt.setString(4, station);
        pstmt.setString(5, dest_station);
        pstmt.setInt(6, station_id);
        pstmt.setInt(7, dest_id);
        pstmt.execute();
        String query2="insert into Train_class values(?,?,?,?,?,?,?)";
        pstmt=conn.prepareStatement(query2);
        pstmt.setInt(1,train_id );
        pstmt.setInt(2,fare1 );
        pstmt.setInt(3, available_seat1);
        pstmt.setInt(4,fare2);
        pstmt.setInt(5,available_seat2 );
        pstmt.setInt(6, fare3);
        pstmt.setInt(7, available_seat3);
        pstmt.execute();
        String query3="insert into Days_Available values(?,?)";
        pstmt=conn.prepareStatement(query3);
        if(sun.equals("11"))
        {
              pstmt.setInt(1,train_id );
              pstmt.setString(2, "sun");
              pstmt.execute();
        }
        if(mon.equals("11"))
        {
              pstmt.setInt(1,train_id );
              pstmt.setString(2, "mon");
              pstmt.execute();
        }
        if(tue.equals("11"))
        {
              pstmt.setInt(1,train_id );
              pstmt.setString(2, "tue");
              pstmt.execute();
        }
        if(wed.equals("11"))
        {
              pstmt.setInt(1,train_id );
              pstmt.setString(2, "wed");
              pstmt.execute();
        }
        if(thur.equals("11"))
        {
              pstmt.setInt(1,train_id );
              pstmt.setString(2, "thur");
              pstmt.execute();
        }
        if(fri.equals("11"))
        {
              pstmt.setInt(1,train_id );
              pstmt.setString(2, "fri");
              pstmt.execute();
        }
        if(sat.equals("11"))
        {
              pstmt.setInt(1,train_id );
              pstmt.setString(2, "sat");
              pstmt.execute();
        }
        
        
        out.println("sucess");
        RequestDispatcher rd=request.getRequestDispatcher("/form14.html");
     	rd.include(request, response);
       
        
  }
              catch(Exception e)
              {
                           out.println("ERROR"+e);
                            RequestDispatcher rd=request.getRequestDispatcher("/form3.html");
                        	 rd.include(request, response);
                           
              }
              }
             
             
              protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                          
                           doGet(request, response);
              }
 
}