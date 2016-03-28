package controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import factory.DeviceDocumentFactory;
import models.JsonDocument;
import models.JsonSchema;
import models.Owner;
import org.everit.json.schema.ValidationException;
import org.json.JSONObject;
import play.Logger;
import play.libs.Json;
import play.mvc.*;

import java.io.IOException;

public class Application extends Controller {

    public Result index() {
        return ok("ok");
    }

    /**
     *
     * JSON API to store a JsonSchema
     * @return Result with JSON String ->
     */

    private static Class<JsonDocument> controllerClass = JsonDocument.class;

    @BodyParser.Of(BodyParser.Json.class)
    public Result createSchema()
    {
        JsonNode jsonData = request().body().asJson();
        ObjectNode result = Json.newObject();
        JsonSchema object;
        ObjectMapper objectMapper = new ObjectMapper();
        String className = ControllerHelper.jsonKeyNameForClass(JsonSchema.class);
        JsonNode jsonSchema = jsonData.get(className);
        try {
            object = new JsonSchema(jsonSchema.toString());
            JsonSchema exitingSchema = JsonSchema.findByTitle(object.title);
            if(exitingSchema != null){
                exitingSchema.setString(objectMapper.writeValueAsString(jsonSchema));
                exitingSchema.save();

            } else {
                object.save();
            }
            JsonSchema.reload();

        }catch (NullPointerException e){
            return ControllerHelper.standardNullPointerErrorResponse();
        } catch (Exception e){
            return ControllerHelper.standardParseErrorResponse();
        }

        if(object == null){
            return ControllerHelper.standardParseErrorResponse();
        }

        try {
            String json = objectMapper.writeValueAsString(object);
            JsonNode jsonNode = objectMapper.readTree(json);
            result.set(ControllerHelper.jsonKeyNameForClass(JsonSchema.class),jsonNode);
        } catch (JsonProcessingException e) {
            return ControllerHelper.standardParseErrorResponse();
        } catch (IOException e) {
            return ControllerHelper.standardParseErrorResponse();
        }

        result.put("status","ok");
        return ok(result);
    }


    @BodyParser.Of(BodyParser.Json.class)
    public Result createDeviceWithOwner(String ownerId)
    {
        Owner myOwner = isValidOwnerId(ownerId);

        if(myOwner != null){
            JsonNode jsonData = request().body().asJson();
            String className = ControllerHelper.jsonKeyNameForClass(controllerClass);
            JsonNode jsonDevice = jsonData.get(className);
            return createDevice(myOwner,jsonDevice,myOwner);
        }  else {
            return ControllerHelper.standardInvalidOwnerErrorResponse();
        }
    }

    public Result getByOwnerIdDeviceId(String ownerId,String deviceId)
    {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode result = Json.newObject();
        String json = null;
        try {
            Owner myOwner = isValidOwnerId(ownerId);
            if(myOwner == null)
                return ControllerHelper.standardInvalidOwnerErrorResponse();
            json = objectMapper.writeValueAsString(JsonDocument.find(JsonDocument.class,deviceId));
        } catch (JsonProcessingException e) {
            Logger.error(e.getMessage());
            return ControllerHelper.standardParseErrorResponse();
        }
        result.set("device",Json.parse(json));
        result.put("status","ok");
        return ok(result);
    }

    public Result getByExternalOwnerIdDeviceId(String ownerId,String deviceId)
    {
       return getByOwnerIdDeviceId(ownerId,deviceId);
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result createDeviceWithExternalOwner(String ownerId) {
        Owner myOwner = isValidOwnerId(ownerId);

        if(myOwner != null){
            JsonNode jsonData = request().body().asJson();
            String className = "device";
            JsonNode jsonDevice = jsonData.get(className);
            return createDevice(myOwner,jsonDevice,myOwner);
        }  else {
            return ControllerHelper.standardInvalidOwnerErrorResponse();
        }
    }

    private Owner isValidOwnerId(String ownerId){
        Owner requestOwner = Owner.findById(ownerId);
        if(requestOwner == null){
            requestOwner = Owner.findByExternalId(ownerId);
        }
        return requestOwner;
    }

    private Result createDevice(Owner ownerObject, JsonNode jsonDevice,Owner myOwner)
    {
        ObjectNode result = Json.newObject();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonDocument object;
        String className = "device";
        try
        {
            JsonSchema j = JsonSchema.findByTitle("device");
            j.schema_object.validate((new JSONObject(objectMapper.writeValueAsString(jsonDevice))));

        }  catch (ValidationException e){
            Logger.info("Error validating  - " + className + " "  + e.getMessage());
        } catch (JsonProcessingException e) {
            Logger.info("Error Parsing  - " + className + " "  + e.getMessage());
        }

        try {
            object = DeviceDocumentFactory.createDeviceDocument(jsonDevice,myOwner,JsonSchema.findByTitle("device"));
        } catch (NullPointerException e){
            return ControllerHelper.standardParseErrorResponse();
        } catch (Exception e){
            return ControllerHelper.standardParseErrorResponse();
        }

        try {
            String json = objectMapper.writeValueAsString(object);
            result.set("device",Json.parse(json));
        } catch (JsonProcessingException e) {
            Logger.error(e.getMessage());
            return ControllerHelper.standardParseErrorResponse();
        } catch (IOException e) {
            return ControllerHelper.standardParseErrorResponse();
        }

        result.put("status","ok");
        return ok(result);
    }


    /**
     *
     * JSON API to create a JsonDocument
     * @return Result with JSON String ->
     */

    @BodyParser.Of(BodyParser.Json.class)
    public Result createOwner()
    {
        JsonNode jsonData = request().body().asJson();
        ObjectNode result = Json.newObject();
        Owner object;
        String className = ControllerHelper.jsonKeyNameForClass( Owner.class);
        JsonNode jsonOwner = jsonData.get(className);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            object = objectMapper.readValue(jsonOwner.toString(),  Owner.class);
            Owner duplicateOwner = isValidOwnerId(object.getExternal_id());
            if(duplicateOwner == null){
                object.save();
            } else {
                return ControllerHelper.standardDuplicateIDErrorResponse();
            }
        } catch (NullPointerException e){
            return ControllerHelper.standardParseErrorResponse();
        } catch (Exception e){
            return ControllerHelper.standardParseErrorResponse();
        }

        if(object == null){
            return ControllerHelper.standardParseErrorResponse();
        }

        try {
            String json = objectMapper.writeValueAsString(object);
            JsonNode jsonNode = objectMapper.readTree(json);
            result.set(ControllerHelper.jsonKeyNameForClass( Owner.class),jsonNode);
        } catch (JsonProcessingException e) {
            return ControllerHelper.standardParseErrorResponse();
        } catch (IOException e) {
            return ControllerHelper.standardParseErrorResponse();
        }

        result.put("status","ok");
        return ok(result);
    }

}
