package models;

import com.fasterxml.jackson.annotation.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.lightcouch.Attachment;
import utils.DGCouchDB;

import java.sql.Timestamp;
import java.util.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonDocument extends CouchModel{

	private final String Type = "";

    /**
     *
     * @param id
     * The id
     */
    @JsonProperty("id")
	private String _id;

    /**
     *
     * @param revision
     * The revision
     */
    @JsonProperty("revision")
	private String _rev;

    /**
     *
     * @param name
     * The name
     */
    @JsonProperty("name")
	private String name;

	/**
	 *
	 * @param owner_id
	 * The owner_id
	 */
	@JsonIgnore
	private String owner_id;

    /**
     *
     * @param content
     * The content
     */
    @JsonProperty("description")
    private String description;

    /**
     *
     * @param data
     * The data
     */
    @JsonProperty("data")
    private Map<String, Object> data = new HashMap<String, Object>();

    /**
     *
     * @param schema_url
     * The name
     */
    @JsonProperty("schema_url")
    private String schema_url;

    @JsonIgnore
    private String schema_id;

	@JsonIgnore
	private int enabled;

	@JsonIgnore
	private int position;

	/**
	 *
	 * @param tags
	 * The tags
	 */
    @JsonProperty("tags")
    private List<String> tags;
    @JsonIgnore
    private int[] complexDate;
    /**
     *
     * @param created_at
     * The name
     */
    @JsonProperty("created_at")
	private Long created_at;
    /**
     *
     * @param modified_at
     * The name
     */
    @JsonProperty("modified_at")
    private Long modified_at;
    /**
     *
     * @param deleted_at
     * The name
     */
    @JsonProperty("deleted_at")
    private Long deleted_at;
	private Map<String, Attachment> _attachments;
	private List<RevInfo> _revs_info;

	public JsonDocument() {
		super();
	}

	public JsonDocument(String _id) {
		this._id = _id;
	}

	public JsonDocument(String _id, String name) {
		this._id = _id;
		this.name = name;
	}

	public JsonDocument(String _id, String name, String description) {
		this._id = _id;
		this.name = name;
        this.description = description;
	}

    public static JsonDocument findById(String s) {
        return (JsonDocument) CouchModel.findByIdAndObjectClass(s, JsonDocument.class);
    }

    public void save(){

        if(this._id == null){
            this.set_id(UUID.randomUUID().toString());
        }

        if(this.created_at == null){
            this.created_at = System.currentTimeMillis() / 1000L;
        }

        this.modified_at = System.currentTimeMillis() / 1000L;
        super.save();
    }

    public String get_id() {
		return _id;
	}

	public String get_rev() {
		return _rev;
	}

	public String getName() {
		return name;
	}

	public int getPosition() {
		return position;
	}

	public List<String> getTags() {
		return tags;
	}

	public int[] getComplexDate() {
		return complexDate;
	}

	public Map<String, Attachment> get_attachments() {
		return _attachments;
	}

	public List<RevInfo> get_revs_info() {
		return _revs_info;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public void set_rev(String _rev) {
		this._rev = _rev;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public void setComplexDate(int[] complexDate) {
		this.complexDate = complexDate;
	}

	public void set_attachments(Map<String, Attachment> _attachments) {
		this._attachments = _attachments;
	}

	public void set_revs_info(List<RevInfo> _revs_info) {
		this._revs_info = _revs_info;
	}

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }


    public static String getDeviceData(String deviceId)
	{
		JsonDocument state = (JsonDocument) DGCouchDB.find(JsonDocument.class,deviceId);
		return null;

	}

	public String getType() {
		return Type;
	}

    public String getOwner_id() { return this.owner_id; }

    public void setOwner_id(String owner_id) { this.owner_id = owner_id; }

    @JsonAnyGetter
    public Map<String, Object> getData() {
        return data;
    }

    @JsonAnySetter
    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public String getSchema_url() {
        return schema_url;
    }

    public void setSchema_url(String schema_url) {
        this.schema_url = schema_url;
    }

    public String getSchema_id() {
        return schema_id;
    }

    public void setSchema_id(String schema_id) {
        this.schema_id = schema_id;
    }

	public int getEnabled() {
		return enabled;
	}

	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}

    public Long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Long created_at) {
        this.created_at = created_at;
    }

    public Long getModified_at() {
        return modified_at;
    }

    public void setModified_at(Long modified_at) {
        this.modified_at = modified_at;
    }

    public Long getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(Long deleted_at) {
        this.deleted_at = deleted_at;
    }

    public static class RevInfo {
		private String rev;
		private String status;

		public String getRev() {
			return rev;
		}

		public void setRev(String rev) {
			this.rev = rev;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		@Override
		public String toString() {
			return "RevInfo [rev=" + rev + ", status=" + status + "]";
		}
	} // end RevInfo

} // end Foo