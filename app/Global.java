import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import xyz.gadre.jentity.models.JsonDocument;
import xyz.gadre.jentity.models.JsonSchema;
import xyz.gadre.jentity.*;

import play.Application;
import play.GlobalSettings;
import play.Logger;

public class Global extends GlobalSettings {


	public void onStart(Application app) {
		Config conf = ConfigFactory.load();
		//////////////////////////////////
		// Load CACHED OBJECTS
		//////////////////////////////////
        JsonSchema.reload();
        JsonSchemaLoader.loadDefaultSchemas();
        // Application start after this.
		Logger.info("Application has started");

    }

	public void onStop(Application app) {
		//////////////////////
		// RELEASE CouchDB HERE
		//////////////////////
		JsonDocument.releaseDB();
		Logger.info("Application shutdown...");
	}


}