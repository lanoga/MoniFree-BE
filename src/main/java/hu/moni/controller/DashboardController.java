package hu.moni.controller;

import hu.moni.model.Dashboard;
import hu.moni.repository.ApplicationDataRepository;
import hu.moni.repository.DashboardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/moni/dashboards")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardRepository repo;
    private final ApplicationDataRepository applicationRepo;


    @PostMapping
    public Dashboard create(@RequestBody Dashboard dashboard) {
        return repo.save(dashboard);
    }


    @GetMapping
    public List<Dashboard> findAll() {
        return repo.findAll();
    }


    @GetMapping("/{id}")
    public Dashboard findById(@PathVariable Long id) {
        return repo.findById(id).orElseThrow();
    }


    @GetMapping("/application/{appId}")
    public List<Dashboard> findByApplication(@PathVariable Long appId) {
        return repo.findByApplicationId(appId);
    }

    @PutMapping("/{id}")
    public Dashboard update( @RequestBody Dashboard dashboard) {
        return repo.save(dashboard);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repo.deleteById(id);
    }
}