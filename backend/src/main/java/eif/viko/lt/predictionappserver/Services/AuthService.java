package eif.viko.lt.predictionappserver.Services;

import eif.viko.lt.predictionappserver.Dto.LoginRequestDto;
import eif.viko.lt.predictionappserver.Dto.RegisterRequestDto;
import eif.viko.lt.predictionappserver.Entities.ChatUser;
import eif.viko.lt.predictionappserver.Entities.Role;
import eif.viko.lt.predictionappserver.Entities.UserProfile;
import eif.viko.lt.predictionappserver.Repositories.ChatUserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final ChatUserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    public AuthService(
            ChatUserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ChatUser signup(RegisterRequestDto input) {
        UserProfile profile = UserProfile.builder()
                .firstName("")
                .lastName("")
                .bio("New user profile")
                .build();

        ChatUser chatUser = ChatUser.builder()
                .username(input.getUsername())
                .email(input.getEmail())
                .password(passwordEncoder.encode(input.getPassword()))
                .enabled(true)
                .role(Role.USER)
                .profile(profile)
                .build();

        return userRepository.save(chatUser);
    }




    public ChatUser authenticate(LoginRequestDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return userRepository.findByEmail(input.getEmail())
                .orElseThrow();
    }
}