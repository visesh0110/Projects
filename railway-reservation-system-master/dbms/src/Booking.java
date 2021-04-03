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
 
@WebServlet("/Booking")
public class Booking extends HttpServlet {
              protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                           response.setContentType("text/html");
                           PrintWriter out = response.getWriter();
                           String url = "jdbc:mysql://localhost:3306/";
                           String dbname = "railway";
                           String driver = "com.mysql.jdbc.Driver";
                           String usr = "root";
                           String pwd = "";
                           Statement st,st1,st2,st3,st4,st5,st6,st7;
                           java.sql.PreparedStatement pst,pst1;
                         
                        
                           try{
                                         Class.forName(driver);
                                         Connection con = DriverManager.getConnection(url+dbname,usr,pwd);
                                         int train_id=Integer.parseInt(request.getParameter("train_id"));
                                         String name=request.getParameter("name");
                                         int age =Integer.parseInt(request.getParameter("age"));
                                         String gender=request.getParameter("gender");
                                         String sstn=request.getParameter("sstn");
                                         String dstn=request.getParameter("dstn");
                                         String cl=request.getParameter("cl");
                                         String date=request.getParameter("date");
                                         String month=request.getParameter("month");
                                        String query="select Station_Id from station where Station_Name='"+sstn+"'";
                                        String query1="select Station_Id from station where Station_Name='"+dstn+"'";
                                        String query3="insert into  passenger values(?,?,?,?,?,?)";
                                        String query4="select max(pnr) from passenger";
                                        String query6="select max(Seat_number) from passenger";
                                        st=con.createStatement();
                                        ResultSet rs=st.executeQuery(query4);
                                        rs.next();
                                        int pnr=rs.getInt(1)+1;
                                        st1=con.createStatement();
                                        ResultSet rs1=st1.executeQuery(query);
                                        rs1.next();
                                        int sstid=rs1.getInt(1);
                                        st2=con.createStatement();
                                        ResultSet rs2=st2.executeQuery(query1);
                                        rs2.next();
                                        int dstid=rs2.getInt(1);
                                        st3=con.createStatement();
                                        ResultSet rs3=st3.executeQuery(query6);
                                        rs3.next();
                                        int seatno=rs3.getInt(1)+1;
                                        pst=con.prepareStatement(query3);
                                        pst.setInt(1,pnr);
                                        pst.setInt(2,seatno);
                                        pst.setString(3,name);
                                        pst.setInt(4, age);
                                        pst.setString(5, gender);
                                        pst.setInt(6, train_id);
                                        pst.execute();
                                        String query5="INSERT INTO passenger_ticket values(?,?,?,?,?,?)";
                                        pst1=con.prepareStatement(query5);
                                        pst1.setInt(1, pnr);
                                        pst1.setInt(2,sstid);
                                        pst1.setInt(3,dstid);
                                        pst1.setString(4, cl);
                                        pst1.setString(5, "B");
                                        pst1.setInt(6, train_id);
                                        pst1.execute();
                                        out.println("success!    your pnr is "+pnr); 
                                        String cla;
                                        if(cl.equals("s"))
                                        	cla="3";
                                        else if(cl.equals("ac2"))
                                        	cla="2";
                                        	else
                                        		cla="3";
                                        
                                        
                                        String query7="Select max(Booked_seat"+cla+") from train_status where (Train_id="+train_id+") and (Available_Date='"+date+month+"')";
                                        st4=con.createStatement();
                                     
                                        ResultSet rs4=st4.executeQuery(query7);
                                       
                                        rs4.next();
                                        
                                        int available_seat=rs4.getInt(1)+1;
                                        String query8="UPDATE train_status SET Booked_seat"+cla+" = " +available_seat +"  WHERE (Train_ID = "+train_id+") AND (Available_Date='"+date+month+"')";
                                       
                                        st5=con.createStatement();
                                        
                                        st5.execute(query8);
                                        
                                        String query9="Select Available_seat"+cla+" from train_status where (Train_id="+train_id+") and (Available_Date='"+date+month+"')";
                                        st6=con.createStatement();
                                         ResultSet rs6=st6.executeQuery(query9);
                                         rs6.next();
                                         int available_seat1=rs6.getInt(1)-1;
                                         
                                         String query10="UPDATE train_status SET Available_seat"+cla+" = " +available_seat1 +"  WHERE (Train_ID = "+train_id+") AND (Available_Date='"+date+month+"')";
                                         st7=con.createStatement();
                                         st7.execute(query10);
                                       
                                        RequestDispatcher rd=request.getRequestDispatcher("/form9.html");
                                     	
                                        rd.include(request, response);
                                }
                           catch(Exception e)
                           {
                                         out.print("error"+e);
                                         RequestDispatcher rd=request.getRequestDispatcher("/form13.html");
                                     	rd.include(request, response);
                           }
              }
              protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                          
                           doGet(request, response);
              }
 
}