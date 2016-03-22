package models;

/**
 * Created by ngadre on 2/23/16.
 */

import com.avaje.ebean.annotation.Index;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import play.Logger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonSchema extends BaseModel {

    /**
     *  JsonSchema Table
     */

    // Concurrent Hash Map to create any type of Schema
    public static ConcurrentHashMap<String, JsonSchema> types = new ConcurrentHashMap<String, JsonSchema>();

    @Id
    /**
     *
     * @param id
     * The id
     */

    @JsonProperty("id")
    public String id;
    /**
     *
     * @param name
     * The name
     */
    @JsonProperty("name")
    @Index
    public String name;
    /**
     *
     * @param string
     * The string
     */
    @JsonIgnore
    @Column(columnDefinition = "TEXT")
    public String string;
    /**
     *
     * @param schema
     * The schema
     */
    @JsonProperty("schema")
    public transient JsonNode json_object;

    @JsonIgnore
    public transient Schema schema_object;

    public JsonSchema(){

    }

    public JsonSchema(String data){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            this.json_object = objectMapper.readTree(data);
            if(this.missingId())
                this.id = generateId();
            this.setRawSchema(data);
            this.name = this.json_object.get("title").textValue();
            this.string = data;
            this.autoInjectFields();

        } catch (IOException e) {
            Logger.error(" Reading Json data - " + e.getLocalizedMessage());
            this.json_object = null;
        }
    }

    private void autoInjectFields() {
        JsonNode schema_properties = this.json_object.get("properties");

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode stringNode = null;
        try {
            stringNode = objectMapper.readTree("{\"type\":\"string\"}");
        } catch (IOException e) {
            Logger.error("Error processing auto injected JSON schema fields - " + e.getMessage());
        }

        JSONObject jsonObject = new JSONObject();

        if(schema_properties.get("id") == null)
            ((ObjectNode)schema_properties).set("id", stringNode);
        if(schema_properties.get("name") == null)
            ((ObjectNode)schema_properties).set("name", stringNode);
        if(schema_properties.get("owner_id") == null)
            ((ObjectNode)schema_properties).set("owner_id", stringNode);
        if(schema_properties.get("description") == null)
            ((ObjectNode)schema_properties).set("description", stringNode);

        this.setJson_object((JsonNode)((ObjectNode)this.json_object).set("properties", schema_properties));

        try {

            this.setString(objectMapper.writeValueAsString(this.json_object));
            this.populateOtherFields();

        } catch (JsonProcessingException e) {
            Logger.error("Error processing auto injected JSON schema fields - " + e.getMessage());
        }
    }

    public boolean missingId() {
        if (this.id == null || this.id.equals("")) {
            return true;
        } else {
            return false;
        }
    }

    public void generateOwnerId() {
        this.id = UUID.randomUUID().toString();
    }

    public void save(){
        if(this.missingId())
            this.generateOwnerId();
        this.setRawSchema(json_object.toString());
        this.setString(json_object.toString());
        super.save();
        Logger.info("Json Schema - " + this.getId() + " : Saved!");
    }

    public static Finder<Long, JsonSchema> find = new Finder<>(JsonSchema.class);

    public static JsonSchema findByIdWithoutCache(String id) {
        return find.where().eq("id", id).findUnique();
    }

    public static JsonSchema of(String string) {
        if(types.containsKey(string)){
            return types.get(string);
        }
        else return null;
    }

    public static boolean reload() {

        if(find.all() != null){

            for (JsonSchema j: find.all()) {
                Logger.info("Reading Schema  - id:" + j.getId() + " name:" + j.getName());
                types.put(j.getName(), j);
            }

            if(types.size() > 0){
                return true;
            }
            else{
                return false;
            }
        }

        return false;

    }

    public static JsonSchema findByName(String name) {
        JsonSchema type = JsonSchema.types.get(name);
        if(type == null){
            JsonSchema schema = find.where().eq("name", name).findUnique();
            schema.populateOtherFields();
            return schema;
        } else {
            return type;
        }
    }

    public Schema getSchema_object() {
        this.setRawSchema(this.string);
        return schema_object;
    }

    public void setSchema_object(Schema schema_object) {
        this.schema_object = schema_object;
    }

    private void populateOtherFields(){
        this.setRawSchema(this.string);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            this.json_object = objectMapper.readTree(this.string);
        } catch (IOException e) {
            Logger.error(" Could not load JSON object - " + e.getLocalizedMessage());
            this.json_object = null;
        }
    }

    public void setRawSchema(String rawSchemaString) {
        Schema schema = null;
        try {
            JSONObject rawSchema = new JSONObject(new JSONTokener(new ByteArrayInputStream(rawSchemaString.getBytes(StandardCharsets.UTF_8))));
            schema = SchemaLoader.load(rawSchema);
        } catch (ValidationException e) {
            Logger.error("Invalid Json Schema - ", e.getMessage());
        }

        this.schema_object = schema;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getString() {
        ObjectMapper objectMapper = new ObjectMapper();
        if(string == null)
        {
            if(json_object != null){
                try {
                    return objectMapper.writeValueAsString(this.json_object);
                } catch (Exception e) {
                    Logger.error("Error Reading Json Object - " + e.getLocalizedMessage());
                }
            }
        }
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public JsonNode getJson_object() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readTree(this.string);
        } catch (IOException e) {
            Logger.error("Error Reading Json String - " + e.getLocalizedMessage());
        }
        return null;
    }

    public void setJson_object(JsonNode json_object) {
        this.json_object = json_object;
    }

}
