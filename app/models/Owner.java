package models;

import com.avaje.ebean.annotation.Index;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import play.Logger;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

/**
 *   Owner
 */
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Owner extends BaseModel {

	/**
	 *
	 * @param id
	 * The id
	 */
	@JsonProperty("id")
	@Id
	public String  id;
	/**
	 *
	 * @param external_id
	 * The external_id
	 */
	@JsonProperty("external_id")
	@Index
	public String  external_id;
	/**
	 *
	 * @param enabled
	 * The enabled
	 */
	public Integer enabled;

	public Owner(){

	}

	public Owner(String id){
		this.id = id;
		if(this.missingId())
            this.generateOwnerId();
		this.enabled = 1;
	}

    public boolean missingId() {
        if (this.id == null || this.id.equals("")) {
            return true;
        } else {
            return false;
        }
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getEnabled() {
		return enabled;
	}

	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
	}

	public String getParameter_name() {
		return "owner_object";
	}

	public String getParameter_friendly_name() {
		return "OWNER NOT DISCLOSED";
	}

	public void generateOwnerId() {
		this.id = UUID.randomUUID().toString();
	}

    public static Finder<Long, Owner> find = new Finder<>(Owner.class);

    public static Owner findById(String id) {
        return find.where().eq("id", id).findUnique();
    }

    public static Owner findByExternalId(String external_id) {
        return find.where().eq("external_id", external_id).findUnique();
    }


    public void save(){
        if(this.missingId())
            this.generateOwnerId();
        this.enabled = 1;
		if(Owner.findByExternalId(this.external_id) == null)
        {
            Logger.info("Owner - " + this.getId() + " : Saved!");
            super.save();
        } else {
            Logger.error("Owner - " + this.getId() + " : Duplicate External ID!");
        }
    }

    public String getExternal_id() {
        return external_id;
    }

    public void setExternal_id(String external_id) {
        this.external_id = external_id;
    }
}
