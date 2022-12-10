package team.segroup.etms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenInfo {
    private Instant expiresAt;
    private String nid;
    private boolean admin;
}
