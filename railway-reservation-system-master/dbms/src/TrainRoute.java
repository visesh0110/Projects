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
 @WebServlet("/TrainRoute")
public class TrainRoute extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out=response.getWriter();
        String url = "jdbc:mysql://localhost:3306/";
        String dbName = "railway";
        String driver = "com.mysql.jdbc.Driver";
        String user = "root"; 
        String password = "";
        Statement st=null;
        Statement st1=null;
        try
        {
        	  Class.forName(driver);
              Connection conn = DriverManager.getConnection(url+dbName, user, password);
              st=conn.createStatement();
              String train_id=request.getParameter("train_no");
             // request.setAttribute("train_id",train_id);
              String query="select Train_ID,Stop_number,Station_ID,Arrival_time,Departure_time,Source_distance from Route where Train_ID='"+train_id+"'";
              ResultSet rs=st.executeQuery(query);
              while(rs.next())
              {
            	 String query1="Select Station_Name from station where Station_ID="+rs.getInt("Station_ID")+"";
            	 st1=conn.createStatement();
            	 ResultSet rs1=st1.executeQuery(query1);
            	  rs1.next();
            	  
            	  out.println("TRAIN_ID :"+rs.getString("Train_ID")+" | STOP_NUMBER :"+rs.getString("Stop_number")+" | STATION_ID :"+rs.getString("Station_ID")+" |STATION NAME: "+rs1.getString(1)+" | ARRIVAL_TIME :"+rs.getString("Arrival_time")+" | DEPARTURE_TIME :"+rs.getString("Departure_time")+" | SOURCE_DISTANCE :"+rs.getString("Source_distance"));
                  out.println("<br>");
              
              }
              RequestDispatcher rd=request.getRequestDispatcher("/form15.html");
          	  rd.include(request, response); 
              
           }
        
		catch(Exception e)
        {
			System.out.println("errror"+e);
			out.println("error");
			RequestDispatcher rd=request.getRequestDispatcher("/form11.html");
        	rd.include(request, response);
        }
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}
