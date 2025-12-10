package hu.moni.controller;

import hu.moni.model.User;
import hu.moni.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/moni/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository repo;

    @PostMapping
    public User save(@RequestBody User user) {
        return repo.save(user);
    }

    @GetMapping
    public List<User> findAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public User findById(@PathVariable Long id) {
        return repo.findById(id).orElseThrow();
    }

    @PutMapping("/{id}")
    public User update(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        return repo.save(user);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repo.deleteById(id);
    }
}

//TODO: összes app lekérés, applicationhoz tartozó szervizek, összes , dashbosard crud - nem kell szűrés