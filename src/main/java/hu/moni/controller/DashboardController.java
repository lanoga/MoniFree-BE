package hu.moni.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.moni.model.Dashboard;
import hu.moni.model.dto.DashboardDto;
import hu.moni.model.dto.ServiceResultDto;
import hu.moni.repository.ApplicationDataRepository;
import hu.moni.repository.DashboardRepository;
import hu.moni.repository.ServiceResultRepository;
import hu.moni.service.ServiceResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/moni/dashboards")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardRepository repo;
    private final ApplicationDataRepository applicationRepo;
    private final ServiceResultService serviceResultService;
    private final ObjectMapper objectMapper;


    @PostMapping
    public Dashboard create(@RequestBody Dashboard dashboard) {
        return repo.save(dashboard);
    }


    @GetMapping
    public List<Dashboard> findAll() {
        return repo.findAll();
    }


    @GetMapping("/{id}")
    public Dashboard findById(@PathVariable("id") Long id) {
        return repo.findById(id).orElseThrow();
    }


    @GetMapping("/application/{appId}")
    public List<Dashboard> findByApplication(@PathVariable("id") Long appId) {
        return repo.findByApplicationId(appId);
    }

    @PutMapping("/{id}")
    public Dashboard update( @RequestBody Dashboard dashboard) {
        return repo.save(dashboard);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        repo.deleteById(id);
    }

    @GetMapping("/dto/application/{appId}")
    public List<DashboardDto> findDashboardDtosByApplication(
            @PathVariable("appId") Long appId,
            @RequestParam(name = "fragment", required = false) String fragment
    ) {

        List<Dashboard> dashboards =
                fragment == null
                        ? repo.findByApplicationId(appId)
                        : repo.findByApplicationIdAndFragment(appId, fragment);

        return dashboards.stream()
                .map(dashboard -> new DashboardDto(
                        dashboard.getId(),
                        dashboard.getApplicationId(),
                        dashboard.getFragment(),
                        dashboard.getName(),
                        dashboard.getVisualAppering(),
                        dashboard.getColIndx(),
                        dashboard.getRowIndx(),
                        dashboard.getColor(),
                        serviceResultService.findByApplicationAndFragment(
                                dashboard.getApplicationId(),
                                dashboard.getFragment()
                        )
                ))
                .toList();
    }

}