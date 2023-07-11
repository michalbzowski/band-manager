package pl.bzowski.bandmanager.musicevent;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CreateMusicEventCommand {
    private String name;
    private String address;
    private LocalDateTime dateTime;
}
