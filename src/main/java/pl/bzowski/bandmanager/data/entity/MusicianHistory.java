package pl.bzowski.bandmanager.data.entity;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.Email;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class MusicianHistory extends AbstractEntity {

    private UUID musicianId;
    private UUID snapshotId;

    private String firstName;
    private String lastName;
    @Email
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
    private LocalDate joinDate;
    private Boolean active;
    private String address;

    private LocalDateTime modificationDate;

    public MusicianHistory(Musician entity) {
        super.setId(UUID.randomUUID());
        this.musicianId = entity.getId();
        this.snapshotId = UUID.randomUUID();
        this.modificationDate = LocalDateTime.now();
        this.firstName =entity.getFirstName();
        this.lastName = entity.getLastName();
        this.email = entity.getEmail();
        this.phone = entity.getPhone();
        this.joinDate = entity.getJoinDate();
        this.dateOfBirth = entity.getDateOfBirth();
        this.active = entity.getActive();
        this.address = entity.getAddress();
    }

    public MusicianHistory() {

    }
}
