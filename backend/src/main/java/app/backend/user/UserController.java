package app.backend.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    @GetMapping()
    public List<UserDTO> getUsers() {
        return service.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserDTO getUser(@PathVariable int id) {
        return service.getById(id);
    }

    @GetMapping("/this")
    public UserDTO getUser(Principal principal) {
        return service.getByEmail(principal.getName());
    }

    @PostMapping("/register")
    public UserDTO register(@RequestBody @Valid RegisteredUserDTO newUser) {
        return service.register(newUser);
    }

    @PutMapping("/{id}")
    public UserDTO update(@PathVariable int id, @RequestBody @Valid UserDTO updatedUser) {
        return service.update(id, updatedUser);
    }

}
