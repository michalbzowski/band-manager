package pl.bzowski.bandmanager.presenceslot.queries;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetMusicianPresenceInMusicEventQuery {

    private UUID musicEventId;
}
