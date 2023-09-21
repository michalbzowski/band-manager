package pl.bzowski.bandmanager.musicevent.events;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MusicEventDateUpdated {
    
    private UUID musicEventId;
    private LocalDateTime currentDateTime;
    private LocalDateTime newDateTime;
}
