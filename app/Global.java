import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import models.JsonSchema;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import utils.CouchDB;

public class Global extends GlobalSettings {


	public void onStart(Application app) {
		Config conf = ConfigFactory.load();
		//////////////////////
		// Load CouchDB HERE
		//////////////////////
		CouchDB.configureDB();
		//////////////////////////////////
		// Load DEVICE TYPE CACHED OBJECTS
		//////////////////////////////////
		JsonSchema.reload();
		//////////////////////////////////
		// Load  HARDWARE   CACHED OBJECTS
		//////////////////////////////////

		
		Logger.info("Application has started");

    }

	public void onStop(Application app) {
		//////////////////////
		// Load CouchDB HERE
		//////////////////////
		CouchDB.releaseDB();
		Logger.info("Application shutdown...");
	}


}