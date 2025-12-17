package hu.moni.repository;

import hu.moni.model.ServiceResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ServiceResultRepository extends JpaRepository<ServiceResult, Long> {
    List<ServiceResult> findAllByFragment(String fragment);

    @Query("""
    select sr
    from ServiceResult sr
    where sr.fragment = :fragment
      and sr.serviceId in (
          select s.id
          from Service s
          where s.applicationId = :applicationId
      )
    order by sr.dateTime desc
""")
    List<ServiceResult> findByApplicationAndFragment(
            @Param("applicationId") Long applicationId,
            @Param("fragment") String fragment
    );

}
