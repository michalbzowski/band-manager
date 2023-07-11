package pl.bzowski.bandmanager.musician.queries;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import pl.bzowski.bandmanager.data.entity.Musician;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface MusicianRepository
        extends
            JpaRepository<Musician, UUID>,
            JpaSpecificationExecutor<Musician> {

    @Query("SELECT m FROM Musician m WHERE m.active = true AND m.joinDate < :eventDateTime")
    List<Musician> findAllActiveOnMusicEventTime(LocalDate eventDateTime);
}
