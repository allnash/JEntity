package factory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.JsonDocument;
import models.JsonSchema;
import models.Owner;

import java.util.Map;

/**
 * Created by ngadre on 3/28/16.
 */
public class OwnerDocumentFactory {

    public static JsonDocument createOwnerDocument(Owner myOwner, JsonSchema schema){
        JsonDocument d = new JsonDocument();
        d.setEnabled(1);
        d.setObject_type(schema.getTitle());
        d.setSchema_id(schema.getId());
        d.setOwner_id(myOwner.getId());
        d.set_id(myOwner.getId());
        d.save();
        return d;
    }
}
