package hu.moni.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.moni.model.Service;
import hu.moni.repository.ApplicationDataRepository;
import hu.moni.repository.ServiceRepository;
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
    private final ApplicationDataRepository applicationDataRepository;

    @GetMapping("/findAllByApplicationId")
    public ResponseEntity<String> findAllByApplicationIdId(@RequestParam("applicationId") Long applicationId) throws JsonProcessingException {
        return new ResponseEntity<>(objectMapper.writeValueAsString(serviceRepository.findAllByApplicationId(applicationId)), HttpStatus.OK);
    }



}
