package be.ehb.gdt.kaai.appfram.petstore.utility;

import be.ehb.gdt.kaai.appfram.petstore.models.AppUser;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service
public class JwtUtil {
    private static final String SECRET = "VerySecretApiSigningKey123!MadeByJonasAndThisC4NN0TTbeHACKED";
    private final Algorithm algorithm = Algorithm.HMAC256(SECRET);

    public String generateAccessToken(User user, HttpServletRequest request) {
        String role = user.getAuthorities().stream()
                .findFirst()
                .orElse(new SimpleGrantedAuthority("Customer"))
                .getAuthority();
        return generateAccessToken(user.getUsername(), role, request);
    }

    public String generateAccessToken(AppUser user, HttpServletRequest request) {
        return generateAccessToken(user.getUsername(), user.getRole(), request);
    }

    private String generateAccessToken(String username, String role, HttpServletRequest request) {
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("role", role)
                .sign(algorithm);
    }

    public String generateRefreshToken(String username, HttpServletRequest request) {
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000))
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);
    }

    public String retrieveUsername(String token) {
        return decodeJwt(token).getSubject();
    }

    public String retrieveRole(String token) {
        return decodeJwt(token).getClaim("role").asString();
    }

    private DecodedJWT decodeJwt(String token) {
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }

    public boolean isValid(String token) {
        return decodeJwt(token).getExpiresAt().after(new Date());
    }
}
