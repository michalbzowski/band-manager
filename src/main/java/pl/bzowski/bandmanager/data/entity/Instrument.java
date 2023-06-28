package pl.bzowski.bandmanager.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;

@Entity
public class Instrument extends AbstractEntity {

    private String type;
    private String brand;
    private String model;
    private String condition;
    private String description;
    private boolean toLearn;
    private boolean toFix;
    private String owner;
    private String tenant;
    @Lob
    @Column(length = 1000000)
    private byte[] picture;

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getBrand() {
        return brand;
    }
    public void setBrand(String brand) {
        this.brand = brand;
    }
    public String getModel() {
        return model;
    }
    public void setModel(String model) {
        this.model = model;
    }
    public String getCondition() {
        return condition;
    }
    public void setCondition(String condition) {
        this.condition = condition;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public boolean isToLearn() {
        return toLearn;
    }
    public void setToLearn(boolean toLearn) {
        this.toLearn = toLearn;
    }
    public boolean isToFix() {
        return toFix;
    }
    public void setToFix(boolean toFix) {
        this.toFix = toFix;
    }
    public String getOwner() {
        return owner;
    }
    public void setOwner(String owner) {
        this.owner = owner;
    }
    public String getTenant() {
        return tenant;
    }
    public void setTenant(String tenant) {
        this.tenant = tenant;
    }
    public byte[] getPicture() {
        return picture;
    }
    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

}
