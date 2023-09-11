package pl.bzowski.bandmanager.musician.commands;


import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public final class CreateMusicianCommand {
    @TargetAggregateIdentifier
    private final UUID musicianId;
    private final String firstName;
    private final String lastName;
    private final String phone;
    private final LocalDate dateOfBirth;
    private final LocalDate joinDate;
    private final Boolean active;
    private final String address;

    public CreateMusicianCommand(UUID musicianId, String firstName, String lastName, String phone,
                                 LocalDate dateOfBirth, LocalDate joinDate, Boolean active, String address) {
        this.musicianId = musicianId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
        this.joinDate = joinDate;
        this.active = active;
        this.address = address;
    }

    public UUID musicianId() {
        return musicianId;
    }

    public String firstName() {
        return firstName;
    }

    public String lastName() {
        return lastName;
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
        var that = (CreateMusicianCommand) obj;
        return Objects.equals(this.musicianId, that.musicianId) &&
                Objects.equals(this.firstName, that.firstName) &&
                Objects.equals(this.lastName, that.lastName) &&
                Objects.equals(this.phone, that.phone) &&
                Objects.equals(this.dateOfBirth, that.dateOfBirth) &&
                Objects.equals(this.joinDate, that.joinDate) &&
                Objects.equals(this.active, that.active);
    }

    @Override
    public int hashCode() {
        return Objects.hash(musicianId, firstName, lastName, phone, dateOfBirth, joinDate, active);
    }

    @Override
    public String toString() {
        return "CreateMusicianCommand[" +
                "musicianId=" + musicianId + ", " +
                "firstName=" + firstName + ", " +
                "lastName=" + lastName + ", " +
                "phone=" + phone + ", " +
                "dateOfBirth=" + dateOfBirth + ", " +
                "joinDate=" + joinDate + ", " +
                "active=" + active + ']';
    }

}
