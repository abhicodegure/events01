package com.rest.test;

import org.apache.logging.log4j.Logger;

public enum SqlHandler {

	INSTANCE;
	  private static Logger logger = LoggerHandler.INSTANCE.getNewLogger(SqlHandler.class);
	  //private static BeanProcessor beanProcessor = new BeanProcessor();

	  // TODO check for closure of RS and Statement
	  public ResultSet getInventoryDetails(String query) {
		  
	  }
	
}
