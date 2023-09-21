package pl.bzowski.bandmanager.musician.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class MusicianSignedUpEvent {
    private final UUID musicianId;
    private final String firstName;
    private final String lastName;
    private final String phone;
    private final LocalDate dateOfBirth;
    private final LocalDate joinDate;
    private final Boolean active;
    private final String address;
}
