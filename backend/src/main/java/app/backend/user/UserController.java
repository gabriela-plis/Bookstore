package app.backend.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService service;

    @GetMapping("/{id}")
    public UserDTO getUser(@PathVariable int id) {
        return service.findById(id);
    }

    @PostMapping("/login")
    public UserDTO login(@RequestBody @Valid LoginDTO loginData) {
        return service.findByLoginData(loginData);
    }

    @PostMapping("/register")
    public UserDTO register(@RequestBody @Valid UserDTO newUser) {
        return service.register(newUser);
    }

    @PutMapping("/{id}")
    public UserDTO update(@PathVariable int id, @RequestBody @Valid UserDTO updatedUser) {
        return service.update(id, updatedUser);
    }



}
