package com.codeplanet.application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FirstController {	
	
	
	
	@PostMapping("/Doctor/Register/API")
	public String register(String username ,String password) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		    Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/Assignment1", "root", "priyanka@8949");
		    Statement stm=con.createStatement();
		    String query="insert into  Register values('"+username+"' ,'" +password+"')";
		    int i=stm.executeUpdate(query);
		   if(i>0)
			   return"you register successfully";
		}
		catch(ClassNotFoundException | SQLException c)
		{
			c.printStackTrace();
		}
			return "somethimg error please check your connection";
		}
	
	
	
	@PostMapping("/api/LogIn_info")	
	public String loginInfo(String username,String password)
	{
		try {
	
			Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con=	DriverManager.getConnection("jdbc:mysql://localhost:3306/Assignment1", "root", "priyanka@8949");
			Statement st=con.createStatement();
			String query="select password from  Register where  username='"+username+"' ";
		ResultSet rs=st.executeQuery(query);
				if(rs.next()) {
				String pwd=rs.getString("password");
				if(pwd.equals(password)) 
					return "you are valid user";				
					else					
						return " please enter valid password";	
				}
							
					else					
						return"your are not registered, register first";				
				}
		
			
		 catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "enter required info";
}	
	
	
	@PostMapping("/api/Patient/Register")
	public String patientRegister( String username,String mobileNumb,String password)throws SQLException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con=	DriverManager.getConnection("jdbc:mysql://localhost:3306/Assignment1", "root", "priyanka@8949");
			Statement st=con.createStatement();
			if(mobileNumb!=null) {
				Pattern p=Pattern.compile("(0/91)?[6-9][0-9]{9}");
				Matcher m=p.matcher(mobileNumb);	
				System.out.println(mobileNumb);
				if(m.find()==true) {
			 PreparedStatement stm= con.prepareStatement("insert into PatientRegister values(?,?,?)");
			stm.setString(1, username);
			stm.setString(2, mobileNumb);
			stm.setString(3, password);
			int i = stm.executeUpdate();
			if(i>0)
				return "your registration is successful";
				}
			}
		}
				 catch (ClassNotFoundException | SQLException e) {
						e.printStackTrace();
			}
		return"enter correct info";
	}
		
	
	
	@PostMapping("/api/Patient/Report-Details")
	public String patientDetails( String username,String drName,String mobileNumb,String Status)throws SQLException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
Connection con=	DriverManager.getConnection("jdbc:mysql://localhost:3306/Assignment1", "root", "priyanka@8949");
			Statement st=con.createStatement();
			if(mobileNumb!=null) {
				Pattern p=Pattern.compile("(0/91)?[6-9][0-9]{9}");
				Matcher m=p.matcher(mobileNumb);	
				System.out.println(mobileNumb);
				if(m.find()==true) {
				String patId=createrandomId(7);
 PreparedStatement stm= con.prepareStatement("insert into PatientReportDetailes values('"+patId+"',?,?,?,current_date(),?)");
			stm.setString(1,drName);
			stm.setString(2, username);
			stm.setString(3, mobileNumb);
			stm.setString(4, Status);
			int i = stm.executeUpdate();
			if(i>0)
				return "data inserted successful";
				}
			}
		}
				 catch (ClassNotFoundException | SQLException e) {
						e.printStackTrace();
			}
		return"enter correct info";
	}	
	
	
	
	public static String createrandomId(  int targetStringLength ) {
		int leftLimit = 48; // numeral '0'
		int rightLimit = 122; // letter 'z'

		Random random = new Random();

		String generatedString = random.ints(leftLimit, rightLimit + 1)
		  .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
		  .limit(targetStringLength)
		  .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
		  .toString();

		return generatedString;
		}	
	
	
	
	
	@GetMapping("/api/Get/Patient/All-Details")
	public Object getDetails(String username,String mobileNumb)throws SQLException {		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/Assignment1","root","priyanka@8949");
			PreparedStatement stm= con.prepareStatement("Select*from  PatientReportDetailes where mobileNo='"+mobileNumb+"' and PatientName='"+username+"'");			
			ResultSet rs=stm.executeQuery();
			ArrayList list=new ArrayList();
			while(rs.next()) {
				Map map=new HashMap();
				map.put("PatientId", rs.getString("PatientId"));
				map.put("PatientName", rs.getString("PatientName"));
				map.put("mobileNo", rs.getString("mobileNo"));
				map.put("CheckupDate", rs.getString("CheckupDate"));
				map.put("PatientStatus", rs.getString("PatientStatus"));
				map.put("CreateByDoctor", rs.getString("CreateByDoctor"));
				list.add(map);		
			}			
				Map newMap=new HashMap();
				newMap.put("patient_info", list);
				newMap.put("status", "ok");			
				return newMap;			
		} 
		catch (Exception e) {
			
			e.printStackTrace();
		}		
			return null;
		}
		
	
	
	@GetMapping("/api/Get/Details-By-Status")
	public Object getDetailsByStatus(String Status)throws SQLException {			
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/Assignment1","root","priyanka@8949");
				PreparedStatement stm= con.prepareStatement("Select*from  PatientReportDetailes	 where PatientStatus='"+Status +"'");				
				ResultSet rs=stm.executeQuery();
				ArrayList list=new ArrayList();
				while(rs.next()) {
					Map map=new HashMap();
					map.put("PatientId", rs.getString("PatientId"));
					map.put("PatientName", rs.getString("PatientName"));
					map.put("mobileNo", rs.getString("mobileNo"));
					map.put("CheckupDate", rs.getString("CheckupDate"));
					map.put("PatientStatus", rs.getString("PatientStatus"));
					map.put("CreateByDoctor", rs.getString("CreateByDoctor"));
					list.add(map);		
				}			
					Map newMap=new HashMap();
					newMap.put("patient_info", list);
					newMap.put("status", "ok");					
					return newMap;			
			} 
			catch (Exception e) {
				
				e.printStackTrace();
			}		
				return null;
			}
			
		
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	




}










