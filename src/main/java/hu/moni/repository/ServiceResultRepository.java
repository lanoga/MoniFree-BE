package hu.moni.repository;

import hu.moni.model.ServiceResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceResultRepository extends JpaRepository<ServiceResult, Long> {
    List<ServiceResult> findAllByFragment(String fragment);
}
