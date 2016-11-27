package com.ascendcorp.devicetokenservice;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;


public class LambdaFunctionHandler implements RequestHandler<RequestObject, String> {
//public class LambdaFunctionHandler {
	
    public String handleRequest(RequestObject input, Context context) {
    	if(context != null)
    		context.getLogger().log("Input: " + input);
    	
    	Connection conn = null;
    	String connectionString = System.getenv("connection_string");
        try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection(connectionString);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    
    	String sqlInsert = "INSERT INTO test.device_profile (account_id, device_id, os, phone) "
    					 + "VALUES ('"+input.getWalletId()+"', '"+input.getDeviceId()+"', '"+input.getOs()+"', '"+input.getPhone()+"');";
    	
    	exceuteSql(sqlInsert, conn);
	    
	    try {
			conn.close();
		    conn = null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	    
    	return "SUCCESS by JAVA";
    }

    
    public static void main(String[] args) {
//    	String sqlInsert = "INSERT INTO test.device_profile (account_id, device_id, os, phone) "
//    					 + "VALUES ('4444', '56fg7yuhj', 'ios', '0928747789')";
//    	queryDatabase(sqlInsert);
//    	String returnResult = queryDatabase("SELECT * FROM device_profile");
//    	System.out.println(returnResult);
    }
    
    public static String exceuteSql(String sqlString, Connection conn) {
    	Statement stmt = null;
    	ResultSet rs = null;
    	String returnResult = "";
//    	String connectionString = System.getenv("connection_string");
//    	String connectionString = "jdbc:mysql://dbone.cbvpxlkpqfey.ap-southeast-1.rds.amazonaws.com/test?"
//    				         + "useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&user=root&password=rootroot";

    	try {
    	    
    	    stmt = conn.createStatement();
    	    if(sqlString.startsWith("SELECT"))
    	    	rs = stmt.executeQuery(sqlString);
    	    else
    	    	returnResult = stmt.executeUpdate(sqlString) + "";
    	    
    	    while (rs != null && rs.next()) {
                String device_id = rs.getString("device_id");
        	    returnResult += device_id + ",";
            }
    	    
    	} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		    // it is a good idea to release
		    // resources in a finally{} block
		    // in reverse-order of their creation
		    // if they are no-longer needed

		    if (rs != null) {
		        try { rs.close(); } 
		        catch (SQLException sqlEx) { } // ignore
		        rs = null;
		    }
		    if (stmt != null) {
		        try { stmt.close(); } 
		        catch (SQLException sqlEx) { } // ignore
		        stmt = null;
		    }
		    if (conn != null) {
		        try { conn.close(); } 
		        catch (SQLException sqlEx) { } // ignore
		        conn = null;
		    }
		}
    	
    	return returnResult;
    }

}
