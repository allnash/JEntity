package controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.JsonDocument;
import models.JsonSchema;
import models.Owner;
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
        String className = ControllerHelper.jsonKeyNameForClass(JsonSchema.class);

        JsonNode jsonSchema = jsonData.get(className);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            object = objectMapper.readValue(jsonSchema.toString(), JsonSchema.class);
            object.save();
        } catch (NullPointerException e){
            return ControllerHelper.standardParseErrorResponse();
        } catch (Exception e){
            return ControllerHelper.standardParseErrorResponse();
        }

        if(object == null){
            return ControllerHelper.standardParseErrorResponse();
        }

        try {
            //objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
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
            return createDevice(myOwner,jsonDevice);
        }  else {
            return ControllerHelper.standardDataValidationErrorResponse();
        }
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result createDeviceWithExternalOwner(String ownerId) {
        Owner myOwner = isValidOwnerId(ownerId);

        if(myOwner != null){
            JsonNode jsonData = request().body().asJson();
            String className = ControllerHelper.jsonKeyNameForClass(controllerClass);
            JsonNode jsonDevice = jsonData.get(className);
            return createDevice(myOwner,jsonDevice);
        }  else {
            return ControllerHelper.standardDataValidationErrorResponse();
        }
    }

    private Owner isValidOwnerId(String ownerId){
        Owner requestOwner = Owner.findById(ownerId);
        if(requestOwner == null){
            requestOwner = Owner.findByExternalId(ownerId);
        }
        return requestOwner;
    }

    private Result createDevice(Owner ownerObject, JsonNode jsonDevice)
    {
        ObjectNode result = Json.newObject();
        JsonDocument object;
        String className = ControllerHelper.jsonKeyNameForClass(controllerClass);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            object = objectMapper.readValue(jsonDevice.toString(), controllerClass);
            if(ownerObject != null) object.setOwner_id(ownerObject.id);
            object.save();
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
            result.set(ControllerHelper.jsonKeyNameForClass(controllerClass),jsonNode);
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
            object.save();
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
