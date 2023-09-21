package pl.bzowski.bandmanager.musician.events;

import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MusicianJoinDateCreatedEvent {

    private UUID musicianId;
    private LocalDate currentJoinDate;
    private String musicianFullName;
    private boolean musicianActive;
}
