package hu.moni.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.moni.model.ServiceResult;
import hu.moni.model.dto.ServiceResultDto;
import hu.moni.repository.ServiceResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceResultService {

    private final ServiceResultRepository serviceResultRepo;
    private final ObjectMapper objectMapper;

    public List<ServiceResultDto> findByApplicationAndFragment(
            Long applicationId,
            String fragment
    ) {
        return serviceResultRepo
                .findByApplicationAndFragment(applicationId, fragment)
                .stream()
                .map(this::toDto)
                .toList();
    }

    private ServiceResultDto toDto(ServiceResult sr) {
        return new ServiceResultDto(
                sr.getId(),
                sr.getServiceName(),
                sr.getServiceId(),
                parseJsonSafely(sr.getResult()),
                sr.getFragment(),
                sr.getDateTime()
        );
    }

    private Object parseJsonSafely(String json) {
        try {
            return objectMapper.readValue(json, Object.class);
        } catch (Exception e) {
            return json;
        }
    }
}
