package hu.moni.repository;

import hu.moni.model.Dashboard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DashboardRepository extends JpaRepository<Dashboard, Long> {
    List<Dashboard> findByApplicationId(Long applicationId);
}
