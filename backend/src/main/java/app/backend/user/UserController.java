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

    @PutMapping
    public UserDTO update(@RequestBody @Valid UserDTO updatedUser, Principal principal) {
        return service.update(updatedUser, principal.getName());
    }

    @PutMapping("/password")
    public void resetPassword(@RequestBody @Valid ResetPasswordDTO passwords, Principal principal) {
        service.resetPassword(passwords, principal.getName());
    }

}
