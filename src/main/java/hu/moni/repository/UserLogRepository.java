package hu.moni.repository;

import hu.moni.model.UserLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLogRepository extends JpaRepository<UserLog, Long> {}
