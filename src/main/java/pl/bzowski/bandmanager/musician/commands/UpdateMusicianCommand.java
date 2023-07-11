package pl.bzowski.bandmanager.musician.commands;


import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public final class UpdateMusicianCommand {
    @TargetAggregateIdentifier
    private final UUID id;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String phone;
    private final LocalDate dateOfBirth;
    private final LocalDate joinDate;
    private final Boolean active;
    private final String address;

    public UpdateMusicianCommand(UUID id, String firstName, String lastName,
                                 String email, String phone, LocalDate dateOfBirth, LocalDate joinDate,
                                 Boolean active, String address) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
        this.joinDate = joinDate;
        this.active = active;
        this.address = address;
    }

    @TargetAggregateIdentifier
    public UUID id() {
        return id;
    }

    public String firstName() {
        return firstName;
    }

    public String lastName() {
        return lastName;
    }

    public String email() {
        return email;
    }

    public String phone() {
        return phone;
    }

    public LocalDate dateOfBirth() {
        return dateOfBirth;
    }

    public LocalDate joinDate() {
        return joinDate;
    }

    public Boolean active() {
        return active;
    }

    public String address() {
        return address;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (UpdateMusicianCommand) obj;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.firstName, that.firstName) &&
                Objects.equals(this.lastName, that.lastName) &&
                Objects.equals(this.email, that.email) &&
                Objects.equals(this.phone, that.phone) &&
                Objects.equals(this.dateOfBirth, that.dateOfBirth) &&
                Objects.equals(this.joinDate, that.joinDate) &&
                Objects.equals(this.active, that.active) &&
                Objects.equals(this.address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, email, phone, dateOfBirth, joinDate, active, address);
    }

    @Override
    public String toString() {
        return "UpdateMusicianCommand[" +
                "id=" + id + ", " +
                "firstName=" + firstName + ", " +
                "lastName=" + lastName + ", " +
                "email=" + email + ", " +
                "phone=" + phone + ", " +
                "dateOfBirth=" + dateOfBirth + ", " +
                "joinDate=" + joinDate + ", " +
                "active=" + active + ", " +
                "address=" + address + ']';
    }

}
