package pl.bzowski.bandmanager.data.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import pl.bzowski.bandmanager.data.entity.MusicEvent;

import java.util.UUID;

public interface EventRepository extends JpaRepository<MusicEvent, UUID>, JpaSpecificationExecutor<MusicEvent> {

}
