package eif.viko.lt.predictionappserver.Dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileResponseDto {
    private String email;
    private String username;
    private String firstName;
    private String lastName;
    private String bio;
    private String role;
}

