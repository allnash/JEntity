package factory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import xyz.gadre.jentity.models.JsonDocument;
import xyz.gadre.jentity.models.JsonSchema;
import xyz.gadre.jentity.models.Owner;
import play.api.libs.json.Json;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ngadre on 3/27/16.
 */
public class DeviceDocumentFactory {

    public static JsonDocument createDeviceDocument(JsonNode jsonNode, Owner myOwner, JsonSchema schema) throws Exception{
        JsonDocument d = new JsonDocument();
        d.setEnabled(1);
        d.setObject_type(schema.getTitle());
        d.setSchema_id(schema.getId());
        d.setOwner_id(myOwner.getId());
        if(jsonNode.get("id") != null)
            throw new Exception("ID should not be set");
        if(jsonNode.get("name") != null)
            d.setName(jsonNode.get("name").textValue());
        if(jsonNode.get("description") != null)
            d.setDescription(jsonNode.get("description").textValue());
        ObjectMapper objectMapper = new ObjectMapper();
        String textValue = objectMapper.writeValueAsString(jsonNode);
        Map<String, Object> data = objectMapper.readValue(textValue, new TypeReference<Map<String, String>>(){});
        d.setData(data);
        d.save();
        addDeviceToOwner(myOwner,d.get_id());
        return d;
    }

    private static void addDeviceToOwner(Owner myOwner,String deviceId){
        JsonDocument ownerDocument = null;
        try {
            ownerDocument = JsonDocument.findById(myOwner.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String,Object> ownerData = ownerDocument.getData();
        List<String> device_ids = (List<String>) ownerData.get("device_ids");
        if(device_ids == null)
            device_ids = new ArrayList<>();
        device_ids.add(deviceId);
        ownerData.put("device_ids",device_ids);
        ownerDocument.setData(ownerData);
        ownerDocument.save();
    }

    public static JsonDocument updateDeviceDocument(String deviceDocumentId,JsonNode jsonNode, Owner myOwner, JsonSchema schema) throws Exception{
     if(deviceDocumentId == null || myOwner == null || jsonNode == null || schema == null)
            throw new Exception("Missing Data");
        JsonDocument d = (JsonDocument) JsonDocument.find(JsonDocument.class,deviceDocumentId);
        ObjectMapper objectMapper = new ObjectMapper();
        String textValue = objectMapper.writeValueAsString(jsonNode);
        Map<String, Object> data = objectMapper.readValue(textValue, new TypeReference<Map<String, String>>(){});
        d.setData(data);
        d.save();
        return d;
    }
}
