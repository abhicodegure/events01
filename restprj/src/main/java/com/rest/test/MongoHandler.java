package com.rest.test;

import java.util.HashMap;
import java.util.Map;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;

public enum MongoHandler {

	INSTANCE;
	
	DBCollection collection;
	  private DBObject sortField = new BasicDBObject("_id", -1);
	  private DBObject publicationTimeField = new BasicDBObject("publicationTime", -1);
	  public static int ASC = 1;
	  public static int DESC = -1;
	  private DB db = null;
	  private static Map<String, MongoClient> connections = new HashMap<String, MongoClient>();
	  private static Map<String, DB> databases = new HashMap<String, DB>();
	
	public boolean upsertOrder(BasicDBObject mongoQuery, BasicDBObject UmongoQuery,WriteConcern concern){
		
		
		return true;
	}
	
	
}
