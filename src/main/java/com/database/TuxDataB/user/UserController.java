package com.database.TuxDataB.user;

import com.cloudinary.Cloudinary;
import com.database.TuxDataB.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository usersRepository;

    @Autowired
    private Cloudinary cloudinary;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RegisteredUserDTO> register(
            @RequestPart("user") @Validated RegisterUserModel model,
            @RequestPart(value = "avatar", required = false) MultipartFile avatar,
            BindingResult validator) {
        if (validator.hasErrors()) {
            throw new ApiValidationException(validator.getAllErrors());
        }
        var registeredUser = userService.register(
                RegisterUserDTO.builder()
                        .withFirstName(model.firstName())
                        .withLastName(model.lastName())
                        .withUsername(model.username())
                        .withEmail(model.email())
                        .withPassword(model.password())
                        .build(),
                avatar);

        return new ResponseEntity<>(registeredUser, HttpStatus.OK);
    }

    @PostMapping("login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Validated LoginModel model, BindingResult validator) {
        if (validator.hasErrors()) {
            throw new ApiValidationException(validator.getAllErrors());
        }
        return new ResponseEntity<>(userService.login(model.username(), model.password()).orElseThrow(), HttpStatus.OK);
    }

    @PostMapping("/registerAdmin")
    public ResponseEntity<RegisteredUserDTO> registerAdmin(@RequestBody RegisterUserDTO registerUser){
        return ResponseEntity.ok(userService.registerAdmin(registerUser));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<User> updateUser(
            @PathVariable Long id,
            @RequestPart("user") User updatedUser,
            @RequestPart(value = "avatar", required = false) MultipartFile avatar) throws IOException {
        User user = userService.updateUser(id, updatedUser, avatar);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findOneByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return ResponseEntity.ok(user);
    }

    @PostMapping("/{username}/avatar")
    public ResponseEntity<String> uploadAvatar(@PathVariable String username, @RequestParam("file") MultipartFile file) {
        try {
            var uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    com.cloudinary.utils.ObjectUtils.asMap("public_id", username + "_avatar"));
            String url = uploadResult.get("url").toString();
            Optional<User> userOptional = usersRepository.findOneByUsername(username);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                user.setAvatar(url);
                usersRepository.save(user);
                return ResponseEntity.ok(url);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload avatar");
        }
    }

    @GetMapping("/{username}/avatar")
    public ResponseEntity<String> getUserAvatar(@PathVariable String username) {
        Optional<User> user = usersRepository.findOneByUsername(username);
        if (user.isPresent() && user.get().getAvatar() != null) {
            return ResponseEntity.ok(user.get().getAvatar());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Avatar not found");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
