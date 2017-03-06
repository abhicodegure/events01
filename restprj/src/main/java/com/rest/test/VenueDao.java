package com.rest.test;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.logging.log4j.Logger;

public class VenueDao {

//	private static Logger logger = LoggerHandler.INSTANCE.getNewLogger(VenueDao.class);

	  @PersistenceContext
	  private EntityManager entityManager;

	  public EntityManager getEntityManager() {
	    return entityManager;
	  }

	  public void setEntityManager(EntityManager entityManager) {
	    this.entityManager = entityManager;
	  }

	  public int addVenue(Venue venue){
//		  logger.info("save venue details for venue: " + venue);
//		  entityManager.persist(venue);
//		  entityManager.flush();
		  
		  return SqlHandler.INSTANCE.addVenue(venue);
//		  return venue.getId();
	  }
	  
	  public boolean updateLocalityMap(int id, String localityMap){
		  
		  String query = "update venue set locality_map = ? where id = ?";
		  return SqlHandler.INSTANCE.updateLocalityMapForVenue(id, localityMap); 
  
	  }
	  
	
}
