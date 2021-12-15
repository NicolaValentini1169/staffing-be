package com.my_virtual_space.staffing.entities.auditor;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.util.Date;
import java.util.UUID;

/**
 * Classe di utilità per la gestione automatica dei dati in creazione e modifica
 */

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable {

    @CreatedBy
    @Column(name = "created_by", columnDefinition = "VARCHAR(36)")
    protected UUID createdBy;

    @CreatedDate
    @Column(name = "dt_ins")
    protected Date dtIns;

    @LastModifiedBy
    @Column(name = "updated_by", columnDefinition = "VARCHAR(36)")
    protected UUID updatedBy;

    @LastModifiedDate
    @Column(name = "dt_upd")
    protected Date dtUpd;

    public UUID getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UUID createdBy) {
        this.createdBy = createdBy;
    }

    public Date getDtIns() {
        return dtIns;
    }

    public void setDtIns(Date dtIns) {
        this.dtIns = dtIns;
    }

    public UUID getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(UUID updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getDtUpd() {
        return dtUpd;
    }

    public void setDtUpd(Date dtUpd) {
        this.dtUpd = dtUpd;
    }
}
