package pl.bzowski.bandmanager.data.entity;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.Email;
import java.time.LocalDate;

@Entity
public class Musician extends AbstractEntity {

    private String firstName;
    private String lastName;
    private String phone;
    private LocalDate dateOfBirth;
    private LocalDate joinDate;
    private Boolean active;
    private String address;


    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public LocalDate getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(LocalDate joinDate) {
        this.joinDate = joinDate;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public boolean joinDateIsDifferent(LocalDate joinDate) {
        return !this.joinDate.equals(joinDate);
    }
    public boolean joinDateIsBefore(LocalDate joinDate) {
        return this.joinDate.isBefore(joinDate);
    }
    public boolean joinDateIsAfter(LocalDate joinDate) {
        return this.joinDate.isAfter(joinDate);
    }
    public String getFullName() {
        return firstName + " " + lastName;
    }
}
