package vttp5a.final_project.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import vttp5a.final_project.models.AppUser;
import vttp5a.final_project.models.exception.UserAlreadyExistsException;
import vttp5a.final_project.repositories.UserRepository;
import vttp5a.final_project.utils.JwtUtil;

@Service
public class AuthService {
    
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private MailSenderService mailSenderService;

    // private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String registerUser(AppUser user) {
        if (userRepo.findUserById(user.getUsername()).isPresent()) {
            // return "User already exists!";
            // throw new RuntimeException("User already exists!");
            throw new UserAlreadyExistsException("User already exists!");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        try {
            userRepo.saveUser(user);
            mailSenderService.sendEmail(user.getEmail(), user.getUsername());
            return ("User registered successfully! Please check your email.");
        } catch (Exception e) {
            System.out.println("Failed to save user to SQL.");
            System.err.println(e);
            throw new RuntimeException();
        }
    }

    public String authenticate(String username, String password) {
        Optional<AppUser> user = userRepo.findUserById(username);
        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            return jwtUtil.generateToken(username);
        }
        throw new RuntimeException("Invalid credentials!");
    }

    public AppUser getUserDetails(String username) {
        return userRepo.findUserById(username).orElseThrow(() -> new RuntimeException("User not found"));
    }
}
