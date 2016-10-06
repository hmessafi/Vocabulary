package pl.nikowis.entities.base;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import java.util.Date;

/**
 * Base entity. Manages creation and update date as well as versioning of entities.
 * Created by nikowis on 02.09.2016
 *
 * @author nikowis
 */
@MappedSuperclass
public abstract class BaseEntity {

    @Column(nullable = true)
    private Date createDate;

    @Column(nullable = true)
    private Date lastModifiedDate;

    @Column(nullable = false)
    @Version
    private Long version;

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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
        createDate = date;
        lastModifiedDate = date;
    }

    @PreUpdate
    void preUpdate() {
        lastModifiedDate = new Date();
    }
}
