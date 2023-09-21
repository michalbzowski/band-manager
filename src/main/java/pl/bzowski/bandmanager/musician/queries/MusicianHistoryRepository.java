package pl.bzowski.bandmanager.musician.queries;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import pl.bzowski.bandmanager.data.entity.Musician;
import pl.bzowski.bandmanager.data.entity.MusicianHistory;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface MusicianHistoryRepository
        extends
            JpaRepository<MusicianHistory, UUID>,
            JpaSpecificationExecutor<MusicianHistory> {
}
