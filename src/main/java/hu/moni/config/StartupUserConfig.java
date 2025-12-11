package hu.moni.config;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class StartupUserConfig {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationDataRepository applicationDataRepository;
    private final MonitorEndpointRepository monitorEndpointRepository;


    private final ObjectMapper mapper = new ObjectMapper();

    private final File userFile = new File("users.json");

    @Bean
    CommandLineRunner createAdminUserAndMediciApp() {
        return args -> {

            loadOrInitializeUsersJson();

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
            // 3. MONITOR ENDPOINTOK LÉTREHOZÁSA A MEDICI-HEZ
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

    private List<User> loadOrInitializeUsersJson() {

        try {

            // Ha nem létezik → létrehozzuk
            if (!userFile.exists()) {
                System.out.println("users.json not found → creating...");
                return createDefaultJsonFile();
            }

            // Ha létezik, de üres → mintha nem létezne
            if (userFile.length() == 0 || isFileWhitespaceOnly(userFile)) {
                System.out.println("users.json is empty → recreating with default admin user...");
                return createDefaultJsonFile();
            }

            // Ha van tartalom → beolvassuk
            System.out.println("Loading users.json...");
            return Arrays.asList(mapper.readValue(userFile, User[].class));

        } catch (Exception e) {
            throw new RuntimeException("Failed to load/create users.json", e);
        }
    }


    private List<User> createDefaultJsonFile() {

        try {
            User admin = new User();
            admin.setEmail("admin@admin.com");
            admin.setFirstName("System");
            admin.setFamilyName("Admin");
            admin.setRole(Role.ADMIN);
            admin.setTwoFactorEnabled(false);
            admin.setPassword(passwordEncoder.encode("test1234"));
            userRepository.save(admin);
            List<User> defaultUsers = List.of(admin);

            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(userFile, defaultUsers);

            System.out.println("users.json created with default admin user.");

            return defaultUsers;

        } catch (Exception e) {
            throw new RuntimeException("Could not create default users.json", e);
        }
    }

    private boolean isFileWhitespaceOnly(File file) {
        try {
            String content = Files.readString(file.toPath());
            return content.trim().isEmpty();
        } catch (IOException e) {
            return true;
        }
    }

    private void refreshUserTableFromJson(List<User> jsonUsers) {

        userRepository.deleteAll();

        for (User u : jsonUsers) {
            // ha nincs kódolva a password → kódoljuk
            if (!u.getPassword().startsWith("$2a$")) {
                u.setPassword(passwordEncoder.encode(u.getPassword()));
            }

            userRepository.save(u);
            System.out.println("Loaded user into DB: " + u.getEmail());
        }
    }

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