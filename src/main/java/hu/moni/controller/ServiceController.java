package hu.moni.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.moni.model.ServiceResult;
import hu.moni.model.dto.ServiceResultDto;
import hu.moni.repository.ServiceRepository;
import hu.moni.repository.ServiceResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/moni/service")
@RequiredArgsConstructor
public class ServiceController {

    private final ServiceRepository serviceRepository;
    private final ObjectMapper objectMapper;
    private final ServiceResultRepository serviceResultRepository;

    @GetMapping("/findAllByApplicationId")
    public ResponseEntity<String> findAllByApplicationIdId(@RequestParam("applicationId") Long applicationId) throws JsonProcessingException {
        return new ResponseEntity<>(objectMapper.writeValueAsString(serviceRepository.findAllByApplicationId(applicationId)), HttpStatus.OK);
    }

    @GetMapping("/getResults")
    public ResponseEntity<String> getresults() throws JsonProcessingException {
        List<ServiceResult> results = serviceResultRepository.findAll();

        List<ServiceResultDto> dtoList = results.stream().map(r -> {
            Object resultJson = parseJsonSafely(r.getResult());
            return new ServiceResultDto(
                    r.getId(),
                    r.getServiceName(),
                    r.getServiceId(),
                    resultJson,
                    r.getFragment(),
                    r.getDateTime()
            );
        }).toList();

        String result = objectMapper.writeValueAsString(dtoList);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/getResultsByFragment")
    public ResponseEntity<String> getresultsByFragment(@RequestParam("fragment") String fragment) throws JsonProcessingException {
        List<ServiceResult> results = serviceResultRepository.findAllByFragment(fragment);

        List<ServiceResultDto> dtoList = results.stream().map(r -> {
            Object resultJson = parseJsonSafely(r.getResult());
            return new ServiceResultDto(
                    r.getId(),
                    r.getServiceName(),
                    r.getServiceId(),
                    resultJson,
                    r.getFragment(),
                    r.getDateTime()
            );
        }).toList();

        String result = objectMapper.writeValueAsString(dtoList);

        return ResponseEntity.ok(result);
    }

    private Object parseJsonSafely(String json) {
        try {
            return objectMapper.readValue(json, Object.class);
        } catch (Exception e) {
            return json;
        }
    }

}
