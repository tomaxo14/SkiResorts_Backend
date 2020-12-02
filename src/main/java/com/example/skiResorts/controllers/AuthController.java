package com.example.skiResorts.controllers;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.example.skiResorts.entities.ERole;
import com.example.skiResorts.entities.Role;
import com.example.skiResorts.entities.User;
import com.example.skiResorts.payload.request.LoginRequest;
import com.example.skiResorts.payload.request.SignupRequest;
import com.example.skiResorts.payload.response.JwtResponse;
import com.example.skiResorts.payload.response.MessageResponse;
import com.example.skiResorts.repository.RoleRepository;
import com.example.skiResorts.repository.UserRepository;
import com.example.skiResorts.security.jwt.JwtUtils;
import com.example.skiResorts.security.services.UserDetailsImpl;
import com.example.skiResorts.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
//    @Autowired
//    AuthenticationManager authenticationManager;
//
//    @Autowired
//    UserRepository userRepository;
//
//    @Autowired
//    RoleRepository roleRepository;
//
//    @Autowired
//    PasswordEncoder encoder;
//
//    @Autowired
//    JwtUtils jwtUtils;

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder, JwtUtils jwtUtils,
                            UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getLogin(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getLogin(),
                userDetails.getEmail(),
                userDetails.getName(),
                userDetails.getSurname(),
                roles,
                userDetails.getFavourites(),
                userDetails.getRatings()));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByLogin(signUpRequest.getLogin())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Użytkownik o podanym loginie już istnieje"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Podany adres e-mail jest już w użyciu"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getLogin(),
                signUpRequest.getName(),
                signUpRequest.getSurname(),
                encoder.encode(signUpRequest.getPassword()),
                signUpRequest.getEmail());

        Set<String> strRoles = signUpRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/changePassword")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> changePassword(Principal principal,
                                            @RequestParam String oldPassword,
                                            @RequestParam String newPassword1,
                                            @RequestParam String newPassword2) {

        int changed = userService.changePassword(principal.getName(), oldPassword, newPassword1, newPassword2);

        switch (changed) {
            case UserService.STATUS_OK:
                return ResponseEntity.ok(new MessageResponse("Hasło zostało zmienione."));
            case UserService.USER_NOT_FOUND:
                return ResponseEntity.badRequest()
                        .body(new MessageResponse("Nie udało się zmienić hasła- nie znaleziono użytkownika."));
            case UserService.INCORRECT_PASSWORD:
                return ResponseEntity.badRequest()
                        .body(new MessageResponse("Nie udało się zmienić hasła - niepoprawne hasło."));
            case UserService.PASSWORDS_NOT_EQUALS:
                return ResponseEntity.badRequest()
                        .body(new MessageResponse("Nie udało się zmienić hasła - hasła nie są identyczne."));
            default:
                return ResponseEntity.badRequest()
                        .body(new MessageResponse("Nie udało się zmienić hasła."));
        }

    }
}
