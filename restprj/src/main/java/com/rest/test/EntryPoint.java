package com.rest.test;


import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Logger;

import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;



@Path("/entry-point")
public class EntryPoint {
	
//	private static Logger logger = LoggerHandler.INSTANCE.getNewLogger(EntryPoint.class);
	
	@GET
    @Path("test")
    @Produces(MediaType.TEXT_PLAIN)
    public String test() {
        return "india";
    }
	
	@POST
    @Path("test")
    @Produces(MediaType.TEXT_PLAIN)
    public String getVenueList() {
        return "india";
    }
	
	@POST
	@Path("add_venue")
	@Consumes(MediaType.APPLICATION_JSON)
	public String addVenue(String data) {
//		logger.info("Pack confirm all for data "+data);
		if( StringUtils.isBlank(data))
			  throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR)
					  .entity("No input").build());
		BasicDBObject venueData = (BasicDBObject) JSON.parse(data);
		  // Ignore empty objects in data
//		  if(venueData == null){
//			  logger.info("null object for venue data");
//		  }
		  if(VenueController.addVenue(venueData)){
			  //logger.info("venue added successfully");
		  }//else{
//			  logger.info("venue addition failed");
//		  } 
		return venueData.toString();
	}
	
	@POST
	@Path("add_locality")
	@Consumes(MediaType.APPLICATION_JSON)
	public boolean addLocality(String data) {
		BasicDBObject localityData = (BasicDBObject) JSON.parse(data);
		String lat = localityData.getString("lat");
		String lon = localityData.getString("lon");
		String name = localityData.getString("name");
		String state = localityData.getString("state");
		String city = localityData.getString("city");
		Coordinates localityPoint = new Coordinates();
		localityPoint.setLat(lat);
		localityPoint.setLon(lon);
		return SqlHandler.INSTANCE.addLocality(localityPoint, city, name, state);

	}
	@POST
	@Path("get_venue_list")
	@Consumes(MediaType.APPLICATION_JSON)
	public String getVenueList(String data) {
		BasicDBObject localityData = (BasicDBObject) JSON.parse(data);
		return VenueController.getVenues(localityData);
	}
	
	
	
}
