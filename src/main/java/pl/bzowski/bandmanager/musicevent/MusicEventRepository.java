package pl.bzowski.bandmanager.musicevent;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import pl.bzowski.bandmanager.data.entity.MusicEvent;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface MusicEventRepository  extends JpaRepository<MusicEvent, UUID>,
        JpaSpecificationExecutor<MusicEvent> {
        @Query("SELECT me from MusicEvent me where me.dateTime >= :joinDate")
        List<MusicEvent> findAllAfter(LocalDateTime joinDate);
}
