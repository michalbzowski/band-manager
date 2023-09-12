package pl.bzowski.bandmanager.musicevent.queries;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor
@Data
public class GetMusicEventQuery {
    private UUID musicEventId;
}
