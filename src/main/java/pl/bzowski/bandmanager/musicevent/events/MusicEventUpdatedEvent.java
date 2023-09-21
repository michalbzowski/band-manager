package pl.bzowski.bandmanager.musicevent.events;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class MusicEventUpdatedEvent {

    private UUID musicEventId;
    private String name;
    private String address;
    private LocalDateTime dateTime;

}
