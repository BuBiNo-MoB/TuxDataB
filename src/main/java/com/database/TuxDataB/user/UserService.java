package com.database.TuxDataB.user;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.database.TuxDataB.email.EmailService;
import com.database.TuxDataB.security.*;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder encoder;
    private final UserRepository usersRepository;
    private final RolesRepository rolesRepository;
    private final AuthenticationManager auth;
    private final JwtUtils jwt;
    private final EmailService emailService;
    private final Cloudinary cloudinary;

    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxFileSize;

    public Optional<LoginResponseDTO> login(String username, String password) {
        try {
            var a = auth.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            a.getAuthorities();
            SecurityContextHolder.getContext().setAuthentication(a);

            var user = usersRepository.findOneByUsername(username).orElseThrow();
            var dto = LoginResponseDTO.builder()
                    .withUser(RegisteredUserDTO.builder()
                            .withId(user.getId())
                            .withFirstName(user.getFirstName())
                            .withLastName(user.getLastName())
                            .withEmail(user.getEmail())
                            .withRoles(user.getRoles())
                            .withUsername(user.getUsername())
                            .build())
                    .build();
            dto.setToken(jwt.generateToken(a));
            return Optional.of(dto);
        } catch (NoSuchElementException e) {
            log.error("User not found", e);
            throw new InvalidLoginException(username, password);
        } catch (AuthenticationException e) {
            log.error("Authentication failed", e);
            throw new InvalidLoginException(username, password);
        }
    }

    public RegisteredUserDTO register(RegisterUserDTO register) {
        return register(register, null);
    }

    public RegisteredUserDTO register(RegisterUserDTO register, MultipartFile avatar) {
        if(usersRepository.existsByUsername(register.getUsername())){
            throw new EntityExistsException("Utente gia' esistente");
        }
        if(usersRepository.existsByEmail(register.getEmail())){
            throw new EntityExistsException("Email gia' registrata");
        }
        Roles roles = rolesRepository.findById(Roles.ROLES_USER).get();

        User u = new User();
        BeanUtils.copyProperties(register, u);
        u.setPassword(encoder.encode(register.getPassword()));
        u.getRoles().add(roles);

        if (avatar != null && !avatar.isEmpty()) {
            try {
                Map<String, Object> uploadResult = cloudinary.uploader().upload(avatar.getBytes(), ObjectUtils.emptyMap());
                String avatarUrl = (String) uploadResult.get("url");
                u.setAvatar(avatarUrl);
            } catch (IOException e) {
                log.error("Failed to upload avatar", e);
            }
        }

        usersRepository.save(u);
        RegisteredUserDTO response = new RegisteredUserDTO();
        BeanUtils.copyProperties(u, response);
        response.setRoles(List.of(roles));
        emailService.sendWelcomeEmail(u.getEmail());

        return response;
    }

    public RegisteredUserDTO registerAdmin(RegisterUserDTO register) {
        if(usersRepository.existsByUsername(register.getUsername())){
            throw new EntityExistsException("Utente gia' esistente");
        }
        if(usersRepository.existsByEmail(register.getEmail())){
            throw new EntityExistsException("Email gia' registrata");
        }
        Roles roles = rolesRepository.findById(Roles.ROLES_ADMIN).get();
        User u = new User();
        BeanUtils.copyProperties(register, u);
        u.setPassword(encoder.encode(register.getPassword()));
        u.getRoles().add(roles);
        usersRepository.save(u);
        RegisteredUserDTO response = new RegisteredUserDTO();
        BeanUtils.copyProperties(u, response);
        response.setRoles(List.of(roles));
        return response;
    }

    public User updateUser(Long id, User updatedUser, MultipartFile avatar) throws IOException {
        User user = usersRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setUsername(updatedUser.getUsername());
        user.setEmail(updatedUser.getEmail());

        // Aggiorna i ruoli
        user.getRoles().clear();
        for (Roles role : updatedUser.getRoles()) {
            Roles existingRole = rolesRepository.findById(role.getRoleType())
                    .orElseThrow(() -> new EntityNotFoundException("Role not found"));
            user.getRoles().add(existingRole);
        }

        if (avatar != null && !avatar.isEmpty()) {
            if (user.getAvatar() != null) {
                cloudinary.uploader().destroy(user.getAvatar(), ObjectUtils.emptyMap());
            }
            Map<String, Object> uploadResult = cloudinary.uploader().upload(avatar.getBytes(), ObjectUtils.emptyMap());
            String avatarUrl = (String) uploadResult.get("url");
            user.setAvatar(avatarUrl);
        }

        return usersRepository.save(user);
    }

    @Transactional
    public String uploadAvatar(Long id, MultipartFile image) throws IOException {
        long maxFileSize = getMaxFileSizeInBytes();
        if (image.getSize() > maxFileSize) {
            throw new FileSizeExceededException("File size exceeds the maximum allowed size");
        }

        Optional<User> optionalUser = usersRepository.findById(id);
        User user = optionalUser.orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        String existingPublicId = user.getAvatar();
        if (existingPublicId != null && !existingPublicId.isEmpty()) {
            cloudinary.uploader().destroy(existingPublicId, ObjectUtils.emptyMap());
        }

        Map<String, Object> uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
        String publicId = (String) uploadResult.get("public_id");
        String url = (String) uploadResult.get("url");

        user.setAvatar(publicId);
        usersRepository.save(user);

        return url;
    }

    @Transactional
    public String deleteAvatar(Long id) throws IOException {
        Optional<User> optionalUser = usersRepository.findById(id);
        User user = optionalUser.orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        String publicId = user.getAvatar();
        if (publicId != null && !publicId.isEmpty()) {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            user.setAvatar(null);
            usersRepository.save(user);
            return "Avatar deleted successfully";
        } else {
            return "No avatar found for deletion";
        }
    }

    @Transactional
    public String updateAvatar(Long id, MultipartFile updatedImage) throws IOException {
        deleteAvatar(id);
        return uploadAvatar(id, updatedImage);
    }

    public long getMaxFileSizeInBytes() {
        String[] parts = maxFileSize.split("(?i)(?<=[0-9])(?=[a-z])");
        long size = Long.parseLong(parts[0]);
        String unit = parts[1].toUpperCase();
        switch (unit) {
            case "KB":
                size *= 1024;
                break;
            case "MB":
                size *= 1024 * 1024;
                break;
            case "GB":
                size *= 1024 * 1024 * 1024;
                break;
        }
        return size;
    }

    public Optional<User> findOneByUsername(String username) {
        return usersRepository.findOneByUsername(username);
    }

    public List<User> findAllUsers() {
        return usersRepository.findAll();
    }

    public void deleteUser(Long id) {
        if (!usersRepository.existsById(id)) {
            throw new EntityNotFoundException("User not found with id: " + id);
        }
        usersRepository.deleteById(id);
    }
}
