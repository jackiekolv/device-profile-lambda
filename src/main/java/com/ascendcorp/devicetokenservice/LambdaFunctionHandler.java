package com.ascendcorp.devicetokenservice;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.ascendcorp.snspushnotification.RequestObject;


public class LambdaFunctionHandler implements RequestHandler<RequestObject, String> {

//    @Override
    public String handleRequest(RequestObject input, Context context) {
    	if(context != null)
    		context.getLogger().log("Input: " + input);
    	queryDatabase();
    	return "SUCCESS";
    }
    
    public static void main(String[] args) {
    	queryDatabase();
    }
    
    public static void queryDatabase() {

    	Connection conn = null;
    	Statement stmt = null;
    	ResultSet rs = null;
    	
    	try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
    	    conn = DriverManager.getConnection(
    	    		// dbone
    	    		"jdbc:mysql://dbone.cbvpxlkpqfey.ap-southeast-1.rds.amazonaws.com/test?" +
    	           "useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&user=root&password=rootroot");
    	    
    	    stmt = conn.createStatement();
    	    rs = stmt.executeQuery("SELECT * FROM device_profile");
    	    
    	    while (rs.next()) {
                String device_id = rs.getString("device_id");
        	    System.out.println(device_id);
            }
    	    
    	    conn.close();
    	    
    	} catch (SQLException ex) {
    	    // handle any errors
    	    System.out.println("SQLException: " + ex.getMessage());
    	    System.out.println("SQLState: " + ex.getSQLState());
    	    System.out.println("VendorError: " + ex.getErrorCode());
    	} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		    // it is a good idea to release
		    // resources in a finally{} block
		    // in reverse-order of their creation
		    // if they are no-longer needed

		    if (rs != null) {
		        try {
		            rs.close();
		        } catch (SQLException sqlEx) { } // ignore

		        rs = null;
		    }

		    if (stmt != null) {
		        try {
		            stmt.close();
		        } catch (SQLException sqlEx) { } // ignore

		        stmt = null;
		    }
		}

	
	}

}
