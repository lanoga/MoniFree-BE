package hu.moni.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.moni.model.User;
import hu.moni.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ObjectMapper mapper = new ObjectMapper();

    private final File userFile = new File("users.json");

    public User save(User user) {
        User saved = userRepository.save(user);
        saveToFile();
        return saved;
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
        saveToFile();
    }

    public void saveToFile() {
        try {
            List<User> users = userRepository.findAll();

            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(userFile, users);

            System.out.println("users.json updated.");

        } catch (Exception e) {
            System.out.println("Failed to write users.json");
            log.error(e.getMessage());
        }
    }

    public Optional<User> findById(Long id) {
       return userRepository.findById(id);
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }


}
