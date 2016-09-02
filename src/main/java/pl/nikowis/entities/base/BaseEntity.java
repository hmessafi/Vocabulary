package pl.nikowis.entities.base;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;

/**
 * Base entity. Manages creation and update date as well as versioning of entities.
 * Created by nikowis on 02.09.2016
 *
 * @author nikowis
 */
@MappedSuperclass
public abstract class BaseEntity {

    @Column(nullable = true)
    private Date createdDate;

    @Column(nullable = true)
    private Date lastModifiedDate;

    @Column(nullable = false)
    @Version
    private Long version;

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @PrePersist
    void prePersist() {
        Date date = new Date();
        createdDate = date;
        lastModifiedDate = date;
    }

    @PreUpdate
    void preUpdate() {
        Date date = new Date();
        lastModifiedDate = date;
    }
}
