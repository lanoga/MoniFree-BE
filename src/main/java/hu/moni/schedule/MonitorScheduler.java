package hu.moni.schedule;

import hu.moni.model.ApplicationData;
import hu.moni.model.MonitorEndpoint;
import hu.moni.model.Service;
import hu.moni.repository.ApplicationDataRepository;
import hu.moni.repository.ServiceRepository;
import hu.moni.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
@EnableScheduling
public class MonitorScheduler {

    private final ServiceRepository requestRepository;
    private final RequestService requestService;
    private final ApplicationDataRepository applicationDataRepository;

    private final Map<String, LocalDateTime> lastRunMap = new ConcurrentHashMap<>();

    @Scheduled(fixedRate = 60_000)
    public void runMonitoring() {

        LocalDateTime now = LocalDateTime.now();
        List<ApplicationData> apps = applicationDataRepository.findAll();

        for (ApplicationData app : apps) {

            for (MonitorEndpoint endpoint : app.getEndpoints()) {

                String key = app.getName() + "_" + endpoint.getFragment();
                LocalDateTime lastRun = lastRunMap.get(key);

                boolean shouldRun =
                        lastRun == null ||
                                Duration.between(lastRun, now).getSeconds() >= endpoint.getIntervalSec();

                if (shouldRun) {

                    Service request = new Service();
                    request.setServerUrl(app.getUrl());
                    request.setFragment(endpoint.getFragment());
                    request.setName(app.getName());
                    request.setIntervalSec(endpoint.getIntervalSec());
                    request.setDateTime(now);
                    request.setApplicationId(app.getId());

                    try {
                        requestService.callMonitor(request,app);
                        lastRunMap.put(key, now);
                    } catch (Exception e) {
                        System.err.println("Scheduler error for " + key + ": " + e.getMessage());
                    }
                }
            }
        }
    }
}