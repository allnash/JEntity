package utils;
import models.JsonSchema;
import play.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * Created by ngadre on 3/28/16.
 */
public class Seeds {

    public static void loadDefaultSchemas(){

        try {
            Files.walk(Paths.get("conf/schemas")).forEach(filePath -> {
                if (Files.isRegularFile(filePath)) {
                    if (filePath.toFile().isFile()) {
                        String temp = filePath.toFile().getName();
                        if ((temp.substring(temp.lastIndexOf('.') + 1, temp.length()).toLowerCase()).equals("json"))
                        {
                            try {
                                String content = new Scanner(filePath.toFile()).useDelimiter("\\Z").next();
                                JsonSchema j = new JsonSchema(content);
                                j.save();
                                Logger.info("Schema Loaded : " + filePath);
                            } catch (FileNotFoundException e) {
                                Logger.warn("File not found : " + filePath.getFileName());
                            }
                        }
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
