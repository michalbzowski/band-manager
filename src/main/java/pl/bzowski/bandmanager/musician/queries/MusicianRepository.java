package pl.bzowski.bandmanager.musician.queries;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import pl.bzowski.bandmanager.data.entity.Musician;

import java.util.UUID;

public interface MusicianRepository
        extends
            JpaRepository<Musician, UUID>,
            JpaSpecificationExecutor<Musician> {

}
