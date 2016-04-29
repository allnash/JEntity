package xyz.gadre.jentity.models;


/**
 * Created by ngadre on 10/28/15.
 */

import java.sql.Timestamp;
import java.util.UUID;

import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;

/**
 * Base domain object with Id, version, whenCreated and whenUpdated.
 *
 * <p>
 * Extending Model to enable the 'active record' style.
 *
 * <p>
 * whenCreated and whenUpdated are generally useful for maintaining external search services (like
 * elasticsearch) and audit.
 */
@MappedSuperclass
public abstract class BaseModel extends Model {

    @Version
    Long version;

    @CreatedTimestamp
    Timestamp created_at;

    @UpdatedTimestamp
    Timestamp updated_at;

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public Timestamp getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }

    public String generateId() {
        return UUID.randomUUID().toString();
    }

}
