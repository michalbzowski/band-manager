package pl.bzowski.bandmanager.musician.queries;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.atmosphere.config.service.Get;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class GetMusicianHistory {

    private UUID musicianId;
}
