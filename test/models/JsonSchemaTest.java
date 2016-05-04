package models;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import helpers.TestLogger;
import org.everit.json.schema.ValidationException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import play.Application;
import play.Logger;
import play.Mode;
import play.inject.guice.GuiceApplicationBuilder;
import play.test.WithApplication;
import xyz.gadre.jentity.models.JsonSchema;

import static org.junit.Assert.assertEquals;


/**
 *
 * Simple (JUnit) tests that can call all parts of a play app.
 * If you are interested in mocking a whole application, see the wiki for more details.
 *
 */
public class JsonSchemaTest extends WithApplication{

    /**
     * Build a new fake application.
     */
    @Override
    protected Application provideApplication()
    {
        return new GuiceApplicationBuilder().in(Mode.DEV)
                .build();
    }

    @Override
    public void startPlay()
    {
        super.startPlay();
        // mock or otherwise provide a context
    }

    @Before
    public void setUp(){
        Config conf = ConfigFactory.load();
        JsonSchema.reload();
    }

    @Test
    public void simpleCheck() {
        int a = 1 + 1;
        assertEquals(2, a);
    }

    @Test
    public void createJSchema() {
        JsonSchema j = new JsonSchema("{\"title\":\"Example Schema\",\"type\":\"object\",\"properties\":{\"firstName\":{\"type\":\"string\"},\"lastName\":{\"type\":\"string\"},\"age\":{\"description\":\"Age in years\",\"type\":\"integer\",\"minimum\":0}},\"required\":[\"firstName\",\"lastName\"]}");
        j.save();
        try
        {
           j.schema_object.validate((new JSONObject("{\"firstName\" : \"nash\",\"lastName\" : \"gadre\",\"age\" : 10 }")));
        } catch (Exception e){
           Assert.assertNull(e);
        }

        try
        {
            j.schema_object.validate((new JSONObject("{\"firstName\" : \"nash\",\"lastName\" : \"gadre\",\"age\" : -1 }")));
        } catch (ValidationException e){
            Assert.assertNotNull(e);
            TestLogger.info(e.getMessage());
        }
    }

    @Test
    public void createDeviceSchema() {
        Logger.info("Creating Device Schema");
        JsonSchema j = new JsonSchema("{\"title\":\"device\",\"type\":\"object\",\"properties\":{\"id\":{\"type\":\"string\"},\"power_on\":{\"type\":\"string\"},\"meter_reading\":{\"description\":\"Meter Reading for Water\",\"type\":\"integer\",\"minimum\":0}},\"required\":[\"id\",\"power_on\"]}");
        j.save();
        try
        {
            j.schema_object.validate((new JSONObject("{\"id\" : \"1234\",\"power_on\" : \"ON\",\"meter_reading\" : 10 }")));
        } catch (Exception e){
            Assert.assertNull(e);
        }

    }


}
