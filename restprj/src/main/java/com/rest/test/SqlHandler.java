package com.rest.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Logger;

public enum SqlHandler {

	INSTANCE;
//	  private static Logger logger = LoggerHandler.INSTANCE.getNewLogger(SqlHandler.class);
	  //private static BeanProcessor beanProcessor = new BeanProcessor();

	  // TODO check for closure of RS and Statement
	public static HashMap<Integer,String> cityLocalityMap; // to access from sqlhandler
	public static HashMap<Integer,String> cityIdyMap; // to access from sqlhandler
	static{
		populateCityLocalityMap();
	}
	
	  public HashMap<Integer,Coordinates> getLocalityMap(String query) {
		  Connection conEventDB = null;
		  Statement inventoryStatement = null;
		  ResultSet resultSet = null;
		  HashMap<Integer,Coordinates> localityMap = new HashMap<Integer,Coordinates>();
		  try {
			 conEventDB  =  EventsDBDataSource.datasource.getConnection();
			 inventoryStatement = conEventDB.createStatement();
			 resultSet = inventoryStatement.executeQuery(query);
			 while (resultSet.next()) {
				 int city_wise_id = resultSet.getInt("city_wise_id");
				 String lat = resultSet.getString("lat");
				 String lon = resultSet.getString("lon");			 
				 Coordinates point = new Coordinates();
				 point.setLat(lat);
				 point.setLon(lon);
				 localityMap.put(city_wise_id, point);
			      }
			 
			 
		} catch (Exception e) {
			return null;
		}
		  return localityMap;
	  }
	  
	  public int getCityId(String cityName,String state){
		  Connection conEventDB = null;
		  PreparedStatement inventoryStatement = null;
		  ResultSet resultSet = null;
		  HashMap<Integer,Coordinates> localityMap = new HashMap<Integer,Coordinates>();
		  try {
			 conEventDB  =  EventsDBDataSource.datasource.getConnection();
			 String query = "select id from city where name = ?" + " and state= ? ";
			 inventoryStatement = conEventDB.prepareStatement(query);
			 inventoryStatement.setString(1, cityName);
			 inventoryStatement.setString(2, state);
			 resultSet = inventoryStatement.executeQuery();
			 if(resultSet!=null){
				 if(resultSet.next()){
					 return resultSet.getInt("id");
				 }
			 }
				 
		} catch (Exception e) {
			System.out.println(e);
			
		}
		  return -1;
	  }
	  
	  public HashMap<Integer,String> getSpaceIdMap(){
		  Connection conEventDB = null;
		  Statement spaceStatement = null;
		  ResultSet resultSet = null;
		  HashMap<Integer,String> spaceIdMap = new HashMap<Integer,String>();
		  try {
				 conEventDB  =  EventsDBDataSource.datasource.getConnection();
				 spaceStatement = conEventDB.createStatement();
				 String query = "select * from space";
				 resultSet = spaceStatement.executeQuery(query);
				 while (resultSet.next()) {
					 int id = resultSet.getInt("id");
					 String name = resultSet.getString("name");
					 spaceIdMap.put(id, name);
				      }
				 return spaceIdMap;
			} catch (Exception e) {
				return null;
			}
	  }

	public boolean updateLocality(int cityWiseId, String city ,int venueId) {
		// first fetch the record then update the list
		Connection conEventDB = null;
		Statement localityStatement = null;
		ResultSet resultSet = null;
		try {
			conEventDB  =  EventsDBDataSource.datasource.getConnection();
			String query = "select * from locality where city_wise_id =" + cityWiseId;
			localityStatement = conEventDB.createStatement();
			resultSet = localityStatement.executeQuery(query);
			String currentVenueList = null;
			while(resultSet.next()){
				currentVenueList = resultSet.getString("venue_list");
				currentVenueList=currentVenueList+";" +venueId;
			}
			// use prepared statement here
			java.sql.PreparedStatement statement3 = null;
			statement3 = conEventDB.prepareStatement("update locality set venue_list = '" + currentVenueList +"' where city_wise_id =" + cityWiseId);
			statement3.executeUpdate();
			return true;
			
		} catch (Exception e) {
			return false;
		}
	}
	
	private static void populateCityLocalityMap() {
		cityLocalityMap = SqlHandler.INSTANCE.getCityData();
	}
	public boolean addLocality(Coordinates coordinates,String city, String localityName, String state){
		Connection conEventDB = null;
		ResultSet resultSet = null;
		try {
			conEventDB  =  EventsDBDataSource.datasource.getConnection();
			// first find the no. of locaties currently in that city
			// if 0 then add the city first if not added
			int cityId = SqlHandler.INSTANCE.getCityId(city, state);
			String localityList = cityLocalityMap.get(cityId);
			
			int index =localityList.indexOf(";");
		    int count = Integer.parseInt(localityList.substring(0, index));
		    count++;
		    localityList=localityList+";" + localityName; 
		    localityList =localityList.substring(index+2,localityList.length());
		    localityList="" +count + ";" + localityList + ";";
		    cityLocalityMap.put(cityId, localityList);
			updateCityLocalityList(cityId,localityList);
			PreparedStatement statement3 = null;
			
			// now insert into locality table
			statement3 = conEventDB.prepareStatement(""
			          + "INSERT INTO locality(city_wise_id, name,city_id,lat,lon) values(?,?,?,?,?)");
			      int i = 1;
			      statement3.setString(i++, count+"");
			      statement3.setString(i++, localityName);
			      statement3.setString(i++, ""+getCityId(city, state));
			      statement3.setString(i++, coordinates.getLat());
			      statement3.setString(i++, coordinates.getLon());
			      statement3.executeUpdate();			
			return true;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}

	private void updateCityLocalityList(int cityId, String localityList) {
		PreparedStatement cityStatement = null;
		Connection conEventDB = null;
		ResultSet resultSet = null;
		try {
			conEventDB  =  EventsDBDataSource.datasource.getConnection();
			String query = "update city set locality_list= ?" + "where id = ?";
			cityStatement = conEventDB.prepareStatement(query);
			cityStatement.setString(1, localityList);
			cityStatement.setString(2, cityId+"");
			int updatedCount = cityStatement.executeUpdate();
			System.out.println("no of rows updated in city table for id "+cityId +" is "+ updatedCount);
			
		} catch (Exception e) {
			System.out.println(e);
		}
		
	}

	public HashMap<Integer,String> getCityData() {
		Connection conEventDB = null;
		Statement localityStatement = null;
		ResultSet resultSet = null;
		try {
			conEventDB  =  EventsDBDataSource.datasource.getConnection();
			String query = "select * from city";
			localityStatement = conEventDB.createStatement();
			resultSet = localityStatement.executeQuery(query);
			HashMap<Integer,String>  cityLocalityMap = new HashMap<Integer,String>();
			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String localityList = resultSet.getString("locality_list");
				cityLocalityMap.put(id, localityList);
			}
			return cityLocalityMap;
		}catch (Exception e) {
			System.out.println(e);
			return null;
		}
		
	}

	public int addVenue(Venue venue) {
		Connection conEventDB = null;
		PreparedStatement addVenueStatement = null;
		ResultSet resultSet = null;
		try {
			conEventDB  =  EventsDBDataSource.datasource.getConnection();
			
			addVenueStatement = conEventDB.prepareStatement(""
			          + "INSERT INTO venue(name, locality_map,space_map) values(?,?,?)", Statement.RETURN_GENERATED_KEYS);
			      int i = 1;
			      addVenueStatement.setString(i++, venue.getName());
			      addVenueStatement.setString(i++, venue.getLocalityMap());
			      addVenueStatement.setString(i++, venue.getSpaceMap());
			      addVenueStatement.executeUpdate();
			      resultSet = addVenueStatement.getGeneratedKeys();
			      while(resultSet.next()){
			    	  return resultSet.getInt(1);
			      }
			
		}catch (Exception e){
			System.out.println(e);
		}
		return -1;
	}

	public boolean updateLocalityMapForVenue(int id, String localityMap) {
		Connection conEventDB = null;
		PreparedStatement updateVenueStatement = null;
		ResultSet resultSet = null;
		try {
			conEventDB  =  EventsDBDataSource.datasource.getConnection();
			updateVenueStatement = conEventDB.prepareStatement(""
			          + "UPDATE venue SET locality_map = ? where id =?");
			int i = 1;
			updateVenueStatement.setString(i++, localityMap);
			updateVenueStatement.setString(i++, id+"");
			updateVenueStatement.executeUpdate();
			return true;
		}catch(Exception e){
			System.out.println(e);
			return false;
		}
	}

	public ArrayList<String> getVenueIdsList(int localityId, String state) {
		Connection conEventDB = null;
		Statement localityStatement = null;
		ResultSet resultSet = null;
		try {
			conEventDB  =  EventsDBDataSource.datasource.getConnection();
			String query = "select * from locality where id =" + localityId;
			localityStatement = conEventDB.createStatement();
			resultSet = localityStatement.executeQuery(query);
			while(resultSet.next()){
		    	  String venueIdsListString = resultSet.getString("venue_list");
		    	  
		    	  String [] venueIdsList = venueIdsListString.substring(venueIdsListString.indexOf(';')+1,venueIdsListString.length()) .split(";");
		    	  return (new ArrayList(Arrays.asList(venueIdsList)));
		      }
			return null;
			}catch(Exception e){
				System.out.println(e);
				return null;
			}
		
	}
	
	
}
