package pl.bzowski.bandmanager.data.entity;

import jakarta.persistence.Entity;

@Entity
public class CompanyBrand extends AbstractEntity {

    private String name;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

}
