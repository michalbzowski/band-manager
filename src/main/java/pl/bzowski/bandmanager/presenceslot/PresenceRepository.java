package pl.bzowski.bandmanager.presenceslot;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import pl.bzowski.bandmanager.data.entity.Presence;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface PresenceRepository extends
        JpaRepository<Presence, UUID>,
        JpaSpecificationExecutor<Presence> {

    @Query("SELECT new pl.bzowski.bandmanager.presence.PresenceDto(" +
            " p.id, " +
            " p.musicianId, " +
            " p.musicianFullName, " +
            " p.present" +
            ") " +
            "FROM Presence p " +
            "WHERE p.musicEvent.id = :eventId")
    List<PresenceDto> findAllForEventId(UUID eventId);

    @Query("SELECT p " +
            "FROM Presence p " +
            "WHERE p.musicianId = :musicianId " +
            "AND p.musicEvent.id = :eventId")
    Presence findForMusicianIdAndEventId(UUID musicianId, UUID eventId);

    @Query("SELECT p  " +
           " FROM Presence p " + 
           " WHERE p.musicianId = :musicianId  " +
           " AND p.musicEvent.dateTime between :from and :to")
    Collection<Presence> findAllBetween(UUID musicianId, LocalDate from, LocalDate to);

    @Query("SELECT p  " +
                " FROM Presence p " + 
                " WHERE p.musicEvent.dateTime between :from and :to")
    Collection<Presence> findAllBetween(LocalDate from, LocalDate to);
}
