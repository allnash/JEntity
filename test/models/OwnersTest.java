
package models;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import play.test.FakeApplication;
import play.test.Helpers;
import play.test.WithApplication;
import utils.CouchDB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;



/**
 *
 * Simple (JUnit) tests that can call all parts of a play app.
 * If you are interested in mocking a whole application, see the wiki for more details.
 *
 */
public class OwnersTest extends WithApplication {

    /**
     * Build a new fake application.
     */
    public static FakeApplication fakeApplication() {
        return new FakeApplication(new java.io.File("."), Helpers.class.getClassLoader(), new HashMap<String,Object>(), new ArrayList<String>(), null);
    }

    @Override
    protected FakeApplication provideFakeApplication() {
        return fakeApplication();
    }

    @Before
    public void setUp(){
        Config conf = ConfigFactory.load();
        JsonSchema.reload();
    }

    @Test
    public void createOwner() {
        UUID uuid = UUID.randomUUID();
        Owner o = new Owner();
        o.save();
        Assert.assertNotNull(Owner.findById(o.id));
    }


}
