package pl.bzowski.bandmanager.musicevent.events;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class MusicEventCreatedEvent {

    private UUID musicEventId;
    private String name;
    private String address;
    private LocalDateTime dateTime;

}
