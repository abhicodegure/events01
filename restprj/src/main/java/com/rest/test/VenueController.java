package com.rest.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;



import com.mongodb.BasicDBObject;

public class VenueController {

	private static HashMap<Integer,String> spaceIdMap;
	//public static HashMap<Integer,String> cityLocalityMap; // to access from sqlhandler
	private static double DIS_RANGE =11.0;
	
	static{
		spaceIdMap = populateSpaceIdMap();
		//
	}
	
	private static HashMap<Integer,String> populateSpaceIdMap(){
		return SqlHandler.INSTANCE.getSpaceIdMap();
	}
	
//	private static void populateCityLocalityMap() {
//		cityLocalityMap = SqlHandler.INSTANCE.getCityData();
//	}

	 
	private static StringBuilder initiliazeLocalityFilter(int bytes){
		StringBuilder s = new StringBuilder();
		for(int i=0;i<8*bytes;i++){
			s.append("0");
		}
		return s;
	}
	// is space filter required apriori 
	private static StringBuilder initiliazeSpaceFilter(int bytes){
		StringBuilder s = new StringBuilder();
		for(int i=0;i<8*bytes;i++){
			s.append("0");
		}
		return s;
	}
	
	public static boolean addToLocalityList(){
		
		
		return false;
	}
	
	public static void addToLocalityList(int cityWiseId, String city ,int venueId){
		SqlHandler.INSTANCE.updateLocality(cityWiseId,city,venueId);
		
	}
	
	public static void populateLocalityFilterAndUpdateLocality(int venueId, StringBuilder localityFilter,HashMap<Integer,Coordinates> localityMap, Coordinates venuePoint, String city){
		
		try {
		for (Map.Entry<Integer, Coordinates> entry : localityMap.entrySet()) {
		    int city_wise_id = entry.getKey();
		    Coordinates value = entry.getValue();
		    Double dis = value.findDistUsingGoogle(venuePoint,"K");
		    System.out.println("distance from locality  "+ city_wise_id + "for city"+city +" is" + dis);
		    if(dis < DIS_RANGE){
		    	localityFilter.setCharAt(city_wise_id, '1');
		    	//now this id needs to be or appended in the locality list
		    	addToLocalityList(city_wise_id,city,venueId);	
		    }
		}
		
		
		} catch (Exception e) {
			System.out.println(e);
		}finally {
			
		}
	}
	
	public static void populateSpaceFilter(StringBuilder spaceFilter, BasicDBObject venueData){
		
		//populate space from the hashmap
		for (Map.Entry<Integer, String> entry : spaceIdMap.entrySet()) {
		    int space_id = entry.getKey();
		    String name = entry.getValue();
	    	if(venueData.containsField(name)){
	    		spaceFilter.setCharAt(space_id, '1');
	    	}
		       
		}
	}
	
	
	
	public static boolean addVenue(BasicDBObject venueData){
		
		HashMap<Integer,Coordinates> localityMap = new HashMap<Integer,Coordinates>();
		String query=null;
		
		BasicDBObject location = (BasicDBObject)venueData.get("location");
		String name = venueData.getString("name");
		String venueLat = location.getString("lat");
		String venueLon = location.getString("lon");
		Coordinates venuePoint = new Coordinates(venueLat,venueLon);
		BasicDBObject address = (BasicDBObject)venueData.get("address");
		String state = address.getString("state");
		String city = address.getString("city");
		StringBuilder localityFilter = initiliazeLocalityFilter(12);
		StringBuilder spaceFilter = initiliazeSpaceFilter(16);
		
		int city_id = SqlHandler.INSTANCE.getCityId(city, state);

			try {
			query = "select * from locality where city_id = "+city_id;
			localityMap = SqlHandler.INSTANCE.getLocalityMap(query);
			populateSpaceFilter(spaceFilter,venueData);
			Venue venueObj = new Venue();
//	    	venueObj.setLocalityMap(localityFilter.toString());   //doesnt make sense
	    	venueObj.setName(name);
	    	venueObj.setSpaceMap(spaceFilter.toString());
//	    	EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "rest" );
//	        EntityManager entityManager = emfactory.createEntityManager( );
	        VenueDao venueDao = new VenueDao();
//	        venueDao.setEntityManager(entityManager);
	        int venueId = venueDao.addVenue(venueObj);
	        if(venueId == -1){
	        	return false;
	        }
			populateLocalityFilterAndUpdateLocality(venueId, localityFilter,localityMap, venuePoint,city);
			// we need to update the locality map for the venue
			if(!updateVenueWithLocalityMap(venueId,localityFilter.toString())){
				return false;
			}
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
		return true;
	}

	private static boolean updateVenueWithLocalityMap(int venueId,String localityMap) {
		VenueDao venueDao = new VenueDao();
		return venueDao.updateLocalityMap(venueId, localityMap);
	}

	public static String getVenues(BasicDBObject localityData) {
		// get the locality id from the map
		int localityId = localityData.getInt("locality_id");
		String state = localityData.getString("state");
		ArrayList<String> venueIdsList =  SqlHandler.INSTANCE.getVenueIdsList(localityId,state);
		return venueIdsList.toString();
		
		
	}
	
}
