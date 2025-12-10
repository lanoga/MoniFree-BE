package hu.moni.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.moni.model.ApplicationData;
import hu.moni.model.User;
import hu.moni.repository.ApplicationDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/moni/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationDataRepository applicationDataRepository;
    private final ObjectMapper objectMapper;

    @GetMapping
    public ResponseEntity<String> findAllApplication() throws JsonProcessingException {
        return new ResponseEntity<>(objectMapper.writeValueAsString(applicationDataRepository.findAll()), HttpStatus.OK);
    }


    @PostMapping
    public ApplicationData save(@RequestBody ApplicationData app) {
        return applicationDataRepository.save(app);
    }

    @GetMapping("/{id}")
    public ApplicationData findById(@PathVariable Long id) {
        return applicationDataRepository.findById(id).orElseThrow();
    }

    @PutMapping()
    public ApplicationData update(@RequestBody ApplicationData app) {
        return applicationDataRepository.save(app);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        applicationDataRepository.deleteById(id);
    }
}
