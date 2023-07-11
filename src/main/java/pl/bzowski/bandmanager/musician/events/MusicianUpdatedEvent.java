package pl.bzowski.bandmanager.musician.events;

import pl.bzowski.bandmanager.data.entity.Musician;

import java.time.LocalDate;
import java.util.UUID;

public class MusicianUpdatedEvent {
    private final UUID musicianId;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String phone;
    private final LocalDate dateOfBirth;
    private final LocalDate joinDate;
    private final Boolean active;
    private final String address;

    public MusicianUpdatedEvent(UUID musicianId, String firstName, String lastName, String email, String phone, LocalDate dateOfBirth, LocalDate joinDate, Boolean active, String address) {
        this.musicianId = musicianId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
        this.joinDate = joinDate;
        this.active = active;
        this.address = address;
    }

    public UUID getId() {
        return musicianId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public LocalDate getJoinDate() {
        return joinDate;
    }

    public Boolean getActive() {
        return active;
    }

    public String getAddress() {
        return address;
    }
}
