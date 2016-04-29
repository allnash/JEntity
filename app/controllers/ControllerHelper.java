package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.CaseFormat;
import xyz.gadre.jentity.models.Owner;
import play.libs.Json;
import play.mvc.Result;

import static play.mvc.Results.badRequest;
import static play.mvc.Results.ok;

/**
 * Created by ngadre on 2/26/16.
 */
public class ControllerHelper {

    public static Owner isValidOwnerId(String ownerId){
        Owner requestOwner = Owner.findById(ownerId);
        if(requestOwner == null){
            requestOwner = Owner.findByExternalId(ownerId);
        }
        return requestOwner;
    }

    public static String jsonKeyNameForClass(Class c){
        String className = c.getSimpleName();
        className = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, className);
        return className;
    }

    public static Result standardParseErrorResponse(){
        ObjectNode result = Json.newObject();
        result.put("status", "error");
        result.put("error", "parse");
        return badRequest(result);
    }

    public static Result standardDuplicateIDErrorResponse(){
        ObjectNode result = Json.newObject();
        result.put("status", "error");
        result.put("error", "Duplicate External ID");
        return badRequest(result);
    }

    public static Result standardNullPointerErrorResponse(){
        ObjectNode result = Json.newObject();
        result.put("status", "error");
        result.put("error", "object was not saved properly");
        return badRequest(result);
    }

    public static Result standardDataValidationErrorResponse(){
        ObjectNode result = Json.newObject();
        result.put("status", "error");
        result.put("error", "invalid data");
        return badRequest(result);
    }

    public static Result standardInvalidOwnerErrorResponse(){
        ObjectNode result = Json.newObject();
        result.put("status", "error");
        result.put("error", "invalid owner");
        return badRequest(result);
    }

}
