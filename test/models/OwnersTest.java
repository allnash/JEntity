
package models;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.Application;
import play.Mode;
import play.inject.guice.GuiceApplicationBuilder;
import play.test.WithApplication;
import xyz.gadre.jentity.models.JsonSchema;
import xyz.gadre.jentity.models.Owner;


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
    private static final Logger LOGGER = LoggerFactory.getLogger(OwnersTest.class);

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
    public void createOwner() {
        UUID uuid = UUID.randomUUID();
        Owner o = new Owner();
        o.save();
        Assert.assertNotNull(Owner.findById(o.id));
    }


}
