package hu.moni.repository;

import hu.moni.model.ServiceLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceLogRepository extends JpaRepository<ServiceLog, Long> {
}
