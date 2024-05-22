package com.smatech.smatrentalpro.backend.commons;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;


@Setter
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AbstractEntity implements Serializable {

    @Serial
    @Getter
    private static final long serialVersionUID = 1L;
    @Id
    @NotNull
    @Column(name = "id", length = 36)
    private String id;

//    @CreatedDate
//    @PastOrPresent
//    @Column(name = "created_at", updatable = false)
//    private LocalDateTime createdAt;
//
//    @LastModifiedDate
//    @PastOrPresent
//    @Column(name = "updated_at")
//    private LocalDateTime updatedAt;
//
//    @Column(name = "deleted_at")
//    private LocalDateTime deletedAt;

    // GETTER AND SETTERS

    //    public LocalDateTime getCreatedAt() {
//        return createdAt;
//    }
//
//    public void setCreatedAt(LocalDateTime createdAt) {
//        this.createdAt = createdAt;
//    }
//
//    public LocalDateTime getUpdatedAt() {
//        return updatedAt;
//    }
//
//    public void setUpdatedAt(LocalDateTime updatedAt) {
//        this.updatedAt = updatedAt;
//    }
//
//    public LocalDateTime getDeletedAt() {
//        return deletedAt;
//    }
//
//    public void setDeletedAt(LocalDateTime deletedAt) {
//        this.deletedAt = deletedAt;
//    }
//

    // METHODS
    @PrePersist
    private void onPersist() {
        if (this.id == null) this.id = UUID.randomUUID().toString();
    }

    @Override
    public int hashCode() {
        if (getId() != null) {
            return getId().hashCode();
        }
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        AbstractEntity other = (AbstractEntity) obj;
        if (getId() == null || other.getId() == null) {
            return false;
        }
        return getId().equals(other.getId());
    }
}
