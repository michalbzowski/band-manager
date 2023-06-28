package pl.bzowski.bandmanager.data.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import pl.bzowski.bandmanager.data.entity.UniformPart;

public interface UniformPartRepository extends JpaRepository<UniformPart, Long>, JpaSpecificationExecutor<UniformPart> {

}
