package hu.moni.repository;

import hu.moni.model.ApplicationData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApplicationDataRepository extends JpaRepository<ApplicationData, Long> {
    Optional<ApplicationData> findByName(String name);
}
