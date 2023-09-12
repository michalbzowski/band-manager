package pl.bzowski.bandmanager.musician.queries;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MusicianHistoryDto {

    private UUID musicianId;
    private UUID snapshotId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
    private LocalDate joinDate;
    private Boolean active;
    private String address;
    private LocalDateTime modificationDate;
}
