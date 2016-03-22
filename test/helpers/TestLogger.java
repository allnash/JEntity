package helpers;

import play.Logger;

/**
 * Created by ngadre on 3/10/16.
 */
public class TestLogger {
    public static void info(String message){
        Logger.info("JUnit Test : " + message);
    }
}
