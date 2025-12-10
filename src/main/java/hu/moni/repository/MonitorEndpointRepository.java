package hu.moni.repository;

import hu.moni.model.MonitorEndpoint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MonitorEndpointRepository extends JpaRepository<MonitorEndpoint, Long> {
    Optional<MonitorEndpoint> findByApplicationIdAndFragment(Long applicationId, String fragment);

}
