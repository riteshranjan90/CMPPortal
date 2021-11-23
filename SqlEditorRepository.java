package com.sql.demosqleditor.repository;


import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

@Repository
public class SqlEditorRepository {
	
	@Autowired  
	JdbcTemplate jdbcTemplate; 
	
	public List<String> getDBRecord() {
		String sqlQuery = "select * from city;";
		List<String> myJSONObjects1 = new  ArrayList<String>();
		List<JSONObject> myJSONObjects = new  ArrayList<JSONObject>();
		jdbcTemplate.query(sqlQuery.toString(),new ResultSetExtractor<Integer>() {
		    @SuppressWarnings("unchecked")
			@Override
		    public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
		        ResultSetMetaData rsmd = rs.getMetaData();		        
		        int columnCount = rsmd.getColumnCount();
		        StringBuffer jsonData = new StringBuffer();
		        StringBuffer header = new StringBuffer();
		        
		        header.append("{");
		        for(int i = 1 ; i <= columnCount ; i++){
		        	if(i == 1) {
		        		header.append("\"title\":\"").append(rsmd.getColumnName(i)).append("\", \"data\":\"").append(rsmd.getColumnName(i)).append("\"}");
		        	}else {
		        		header.append(",{ \"title\":\"").append(rsmd.getColumnName(i)).append("\", \"data\":\"").append(rsmd.getColumnName(i)).append("\"}");
		        	}
		        }
		        while(rs.next()) {
	            	 JSONObject obj = new JSONObject();
	            	 for(int i = 1 ; i <= columnCount ; i++){
		            	 obj.put(rsmd.getColumnName(i), rs.getString(rsmd.getColumnName(i)));	
	            	 }
	            	 myJSONObjects.add(obj);
	            }
		        jsonData.append("{\"columns\" : [").append(header.toString()).append("],\"data\":").append(myJSONObjects).append("}");
		        
		      
		        myJSONObjects1.add(jsonData.toString());
		        System.out.println(myJSONObjects1); 
		        return columnCount;
		    }
		});
		return myJSONObjects1;
	}
	
	
}
