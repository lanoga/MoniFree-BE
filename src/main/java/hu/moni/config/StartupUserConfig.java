package hu.moni.config;

import hu.moni.enums.Role;
import hu.moni.model.ApplicationData;
import hu.moni.model.MonitorEndpoint;
import hu.moni.model.User;
import hu.moni.repository.ApplicationDataRepository;
import hu.moni.repository.MonitorEndpointRepository;
import hu.moni.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class StartupUserConfig {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationDataRepository applicationDataRepository;
    private final MonitorEndpointRepository monitorEndpointRepository;

    @Bean
    CommandLineRunner createAdminUserAndMediciApp() {
        return args -> {

            // =====================================================
            // 1. ADMIN USER LÉTREHOZÁS
            // =====================================================
            String adminEmail = "admin@admin.com";

            if (userRepository.findByEmail(adminEmail).isEmpty()) {

                User admin = new User();
                admin.setEmail(adminEmail);
                admin.setFirstName("System");
                admin.setFamilyName("Admin");
                admin.setRole(Role.ADMIN);
                admin.setPassword(passwordEncoder.encode("test1234"));

                userRepository.save(admin);
                System.out.println("✅ Default ADMIN user created.");
            } else {
                System.out.println("ℹ️ Default ADMIN user already exists.");
            }

            // =====================================================
            // 2. MEDICI APPLICATIONDATA LÉTREHOZÁS
            // =====================================================
            ApplicationData mediciApp = applicationDataRepository.findByName("MEDICI")
                    .orElseGet(() -> {

                        ApplicationData app = new ApplicationData();
                        app.setName("MEDICI");
                        app.setUrl("http://localhost:8080");
                        app.setUser("admin");
                        app.setPass("makoslecso2025");

                        applicationDataRepository.save(app);
                        System.out.println("MEDICI application created.");
                        return app;
                    });

            // =====================================================
            // ✅ 3. MONITOR ENDPOINTOK LÉTREHOZÁSA A MEDICI-HEZ
            // =====================================================
            createEndpointIfNotExists(mediciApp, "MEMORY", 60, "Memory usage");
            createEndpointIfNotExists(mediciApp, "CPU", 120, "CPU usage");
            createEndpointIfNotExists(mediciApp, "HDD", 300, "Disk usage");
            createEndpointIfNotExists(mediciApp, "UPTIME", 60, "Uptime");
            createEndpointIfNotExists(mediciApp, "THREADS", 120, "Threads");
            createEndpointIfNotExists(mediciApp, "DBCONN", 60, "DB Connections");

            System.out.println(" MEDICI monitor endpoints startup init finished.");
        };
    }

    // =====================================================
    // SEGÉDMETÓDUS – DUPLIKÁCIÓMENTES ENDPOINT LÉTREHOZÁS
    // =====================================================
    private void createEndpointIfNotExists(
            ApplicationData app,
            String fragment,
            int intervalSec,
            String name
    ) {
        monitorEndpointRepository
                .findByApplicationIdAndFragment(app.getId(), fragment)
                .ifPresentOrElse(
                        ep -> System.out.println(" Endpoint already exists: " + fragment),
                        () -> {
                            MonitorEndpoint endpoint = new MonitorEndpoint();
                            endpoint.setApplication(app);
                            endpoint.setFragment(fragment);
                            endpoint.setIntervalSec(intervalSec);

                            monitorEndpointRepository.save(endpoint);
                            System.out.println("Endpoint created: " + fragment);
                        }
                );
    }

}