package hu.moni.service;

import hu.moni.model.ApplicationData;
import hu.moni.model.ServiceLog;
import hu.moni.model.Service;
import hu.moni.model.ServiceResult;
import hu.moni.repository.ServiceLogRepository;
import hu.moni.repository.ServiceRepository;
import hu.moni.repository.ServiceResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class RequestService {

    private final ServiceRepository requestRepo;
    private final ServiceResultRepository resultRepo;
    private final ServiceLogRepository logRepo;


    private final RestTemplate restTemplate = new RestTemplate();

    public void callMonitor(Service request, ApplicationData app) {

        Service savedRequest = requestRepo.save(request);

        Long serviceId = savedRequest.getId();

        String finalUrl = app.getUrl() + "/monitor/" + savedRequest.getFragment();

        String auth = app.getUser() + ":" + app.getPass();
        String basicAuth = Base64.getEncoder()
                .encodeToString(auth.getBytes(StandardCharsets.UTF_8));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + basicAuth);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    finalUrl,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            ServiceResult result = new ServiceResult();
            result.setServiceName(savedRequest.getName());
            result.setServiceId(serviceId);
            result.setFragment(savedRequest.getFragment());
            result.setDateTime(LocalDateTime.now());
            result.setResult(response.getBody());

            ServiceLog log = new ServiceLog();
            log.setServiceName(savedRequest.getName());
            log.setDateTime(LocalDateTime.now());
            log.setResultCode(response.getStatusCode().value());
            log.setErrorMessage(null);
            logRepo.save(log);

            resultRepo.save(result);

        } catch (Exception e) {

            ServiceLog log = new ServiceLog();
            log.setServiceName(savedRequest.getName());
            log.setDateTime(LocalDateTime.now());
            log.setResultCode(500);
            log.setErrorMessage(e.getMessage());
            logRepo.save(log);

        }
    }
}