package pl.bzowski.bandmanager.data.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import pl.bzowski.bandmanager.data.entity.InstrumentType;

public interface InstrumentTypeRepository
        extends
            JpaRepository<InstrumentType, Long>,
            JpaSpecificationExecutor<InstrumentType> {

}
