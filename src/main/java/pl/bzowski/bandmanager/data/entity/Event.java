package pl.bzowski.bandmanager.data.entity;

import jakarta.persistence.Entity;
import java.time.LocalDateTime;

@Entity
public class Event extends AbstractEntity {

    private String name;
    private String address;
    private LocalDateTime dateTime;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public LocalDateTime getDateTime() {
        return dateTime;
    }
    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

}
