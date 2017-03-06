package com.rest.test;


import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.RollingFileAppender;
import org.apache.logging.log4j.core.appender.rolling.DefaultRolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.RollingFileManager;
import org.apache.logging.log4j.core.appender.rolling.SizeBasedTriggeringPolicy;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.layout.PatternLayout;
public enum LoggerHandler {

	INSTANCE;
	private RollingFileAppender fileAppender = null;
	private Level level = Level.DEBUG;

	public RollingFileAppender getFileAppender() {
		return fileAppender;
	}

	public void setFileAppender(RollingFileAppender fileAppender) {
		this.fileAppender = fileAppender;
	}

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public Logger getNewLogger(Class<?> c) {
		Logger logger = (org.apache.logging.log4j.core.Logger) LogManager.getLogger(c);

		if (fileAppender != null) {
			logger.addAppender(this.fileAppender);
		}
		logger.setAdditive(false);
		return logger;
	}

	public Logger getNewLoggerWithAppend(String name) {
		Logger logger = (Logger) LogManager.getLogger(name);
		logger.addAppender(this.fileAppender);
		logger.setLevel(this.level);
		logger.setAdditive(false);
		return logger;
	}

	public Logger getNewLogger(String name) {
		Logger logger = (Logger) LogManager.getLogger(name);
		if (fileAppender != null) {
			logger.addAppender(this.fileAppender);
		}
		logger.setLevel(this.level);
		logger.setAdditive(false);
		return logger;
	}

	public RollingFileAppender getFileAppender(String name, String fileName, String maxSize, String logLayoutPattern,
			int maxfilesToKeep, boolean compress) {
//		final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
//		final Configuration config = ctx.getConfiguration();
//		String filePattern = fileName + ".%i";
//		if (compress == true)
//			filePattern += ".gz";
//
//		PatternLayout layout = PatternLayout.newBuilder().withConfiguration(config).withPattern(logLayoutPattern)
//				.build();
//		SizeBasedTriggeringPolicy policy = SizeBasedTriggeringPolicy.createPolicy(maxSize);
//		DefaultRolloverStrategy strategy = DefaultRolloverStrategy.createStrategy(maxfilesToKeep + "", "1", "min", null,
//				config);
//
//		RollingFileManager fileManager = RollingFileManager.getFileManager(fileName, filePattern, true, true, policy,
//				strategy, null, layout, 1024);
//		policy.initialize(fileManager);
//		RollingFileAppender appender = RollingFileAppender.createAppender(fileName, filePattern, "true", name, "true",
//				"1024", "true", policy, strategy, layout, (Filter) null, "false", "false", (String) null, config);
//		appender.start();
//		return appender;
		
		return null;
	}
}
