package eif.viko.lt.predictionappserver.Controllers;

import eif.viko.lt.predictionappserver.Dto.UpdateProfileRequest;
import eif.viko.lt.predictionappserver.Entities.ChatUser;
import eif.viko.lt.predictionappserver.Entities.UserProfile;
import eif.viko.lt.predictionappserver.Repositories.ChatUserRepository;
import eif.viko.lt.predictionappserver.Services.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class CustomProfileController {

    private final ChatUserRepository userRepository;
    private final JwtService jwtService;

    @GetMapping("/me")
    public ResponseEntity<?> getProfile(@AuthenticationPrincipal ChatUser user) {
        return ResponseEntity.ok(user.getProfile());
    }

    @PutMapping("/me")
    public ResponseEntity<?> updateProfile(@AuthenticationPrincipal ChatUser user,
                                           @RequestBody UpdateProfileRequest update) {

        UserProfile profile = user.getProfile();

        if (update.getFirstName() != null) profile.setFirstName(update.getFirstName());
        if (update.getLastName() != null) profile.setLastName(update.getLastName());
        if (update.getBio() != null) profile.setBio(update.getBio());

        userRepository.save(user);

        return ResponseEntity.ok(profile);
    }
}
