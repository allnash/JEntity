package utils;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.lightcouch.*;
import play.Logger;

import java.util.List;

public class CouchDB {
	
	public static CouchDbClient dbClient;

	public static void configureDB(){
		
		Config conf = ConfigFactory.load();

		CouchDbProperties properties = new CouchDbProperties()
		  .setDbName(conf.getString("couchdb.dbname"))
		  .setCreateDbIfNotExist(conf.getBoolean("couchdb.createdb-if-not-exist"))
		  .setProtocol(conf.getString("couchdb.protocol"))
		  .setHost(conf.getString("couchdb.host"))
		  .setPort(conf.getInt("couchdb.port"))
		  .setUsername(conf.getString("couchdb.username"))
		  .setPassword(conf.getString("couchdb.secret"))
		  .setMaxConnections(conf.getInt("couchdb.max-connections"))
		  .setConnectionTimeout(0);

		try{
			dbClient = new CouchDbClient(properties);
		}
		catch(CouchDbException e){
			Logger.error("CouchDB - There was an error establishing a connection, please check");
			Logger.error("CouchDB - Exception - "+e.getMessage());
		}
	}

	public static void releaseDB() {
		try{

			dbClient.shutdown();
		}
		catch(CouchDbException e){
			Logger.error("CouchDB - There was an error Shutting down, please check");
			Logger.error("CouchDB - Exception - "+e.getMessage());
		}
	}
	
	public static void saveDocument(Object o) {
		Response response = dbClient.save(o);
		Logger.debug("Saved | id->"+response.getId() + ", rev->" + response.getRev());
		Logger.info("Saved | "+ response.getId());

	}
	
	public static void updateDocument(Object o) {
		Response response = dbClient.update(o);
		Logger.info("Updated | id->"+response.getId() + ", rev->" + response.getRev());
		Logger.info("Updated | "+response.getId());
	}
	
	
	
	public void dbInfo() {
		CouchDbInfo dbInfo = dbClient.context().info();

	}

	public void serverVersion() {
		String version = dbClient.context().serverVersion();
	}

	public void compactDb() {
		dbClient.context().compact();
	}

	
	public void allDBs() {
		List<String> allDbs = dbClient.context().getAllDbs();
	}

	public void ensureFullCommit() {
		dbClient.context().ensureFullCommit();
	}
	
	public void uuids() {
		List<String> uuids = dbClient.context().uuids(10);
	}

	public static <T> Object find(Class<T> objType,String device_id) {
		try{
			Object document = dbClient.find(objType, device_id);
			return document;
		} catch ( org.lightcouch.NoDocumentException e){
			Logger.info("Couch DB Document not found - " + device_id);
			return null;
		}
	}

}
