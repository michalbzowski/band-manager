package pl.bzowski.bandmanager.musician.queries;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import pl.bzowski.bandmanager.data.entity.Musician;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface MusicianRepository
        extends
            JpaRepository<Musician, UUID>,
            JpaSpecificationExecutor<Musician> {

    @Query("SELECT m FROM Musician m WHERE m.active = true AND m.joinDate < :eventDateTime")
    List<Musician> findAllActiveOnMusicEventTime(LocalDate eventDateTime);

    @Query("SELECT new pl.bzowski.bandmanager.musician.queries.MusicianHistoryDto(" +
            " mh.id," +
            " mh.snapshotId," +
            " mh.firstName," +
            " mh.lastName, " +
            " mh.email, " +
            " mh.phone, " +
            " mh.dateOfBirth, " +
            " mh.joinDate, " +
            " mh.active, " +
            " mh.address," +
            " mh.modificationDate" +
            " ) " +
            " FROM MusicianHistory mh" +
            " WHERE mh.musicianId = :musicianId" +
            " ORDER BY mh.modificationDate DESC ")
    List<MusicianHistoryDto> findHistoryById(UUID musicianId);
}
