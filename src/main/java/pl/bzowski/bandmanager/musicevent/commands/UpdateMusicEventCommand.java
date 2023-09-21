package pl.bzowski.bandmanager.musicevent.commands;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class UpdateMusicEventCommand {
    private UUID id;
    private String name;
    private String address;
    private LocalDateTime dateTime;
}
