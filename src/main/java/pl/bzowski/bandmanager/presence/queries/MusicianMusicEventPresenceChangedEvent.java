package pl.bzowski.bandmanager.presence.queries;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class MusicianMusicEventPresenceChangedEvent {

    private UUID presenceId;
    private UUID musicianId;
    private UUID eventId;
    private boolean present;

}
