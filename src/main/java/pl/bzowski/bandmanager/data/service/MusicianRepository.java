package pl.bzowski.bandmanager.data.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import pl.bzowski.bandmanager.data.entity.Musician;

public interface MusicianRepository
        extends
            JpaRepository<Musician, Long>,
            JpaSpecificationExecutor<Musician> {

}
