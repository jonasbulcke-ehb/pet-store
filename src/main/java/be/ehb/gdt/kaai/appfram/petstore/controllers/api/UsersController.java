package be.ehb.gdt.kaai.appfram.petstore.controllers.api;

import be.ehb.gdt.kaai.appfram.petstore.models.AppUser;
import be.ehb.gdt.kaai.appfram.petstore.models.inputModels.UserRole;
import be.ehb.gdt.kaai.appfram.petstore.service.UserService;
import be.ehb.gdt.kaai.appfram.petstore.utility.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("api")
public class UsersController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Autowired
    public UsersController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("users")
    public ResponseEntity<Iterable<AppUser>> getUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    @PostMapping("register")
    public ResponseEntity<Map<String, String>> registerUser(HttpServletRequest request) {
        AppUser user = new AppUser();
        user.setUsername(request.getParameter("username"));
        user.setEmail(request.getParameter("email"));

        AppUser savedUser = userService.saveUser(user, request.getParameter("password"));

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access-token", jwtUtil.generateAccessToken(savedUser, request));
        tokens.put("refresh-token", jwtUtil.generateRefreshToken(savedUser.getUsername(), request));

        return new ResponseEntity<>(tokens, HttpStatus.CREATED);
    }

    @PostMapping("users/assignRole")
    public ResponseEntity<?> assignRoleToUser(@RequestBody UserRole userRole) {
        userService.assignRole(userRole.getUsername(), userRole.getRole());
        return ResponseEntity.ok().build();
    }

    @GetMapping("refresh-token")
    public ResponseEntity<Map<String, String>> refreshToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        Map<String, String> error = new HashMap<>();

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String refreshToken = authorizationHeader.substring("Bearer ".length());

            if(!jwtUtil.isValid(refreshToken)) {
                error.put("error-message", "Refresh token is expired");
                return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
            }

            try {
                String username = jwtUtil.retrieveUsername(refreshToken);
                AppUser user = userService.getUser(username);

                String accessToken = jwtUtil.generateAccessToken(user, request);

                Map<String, String> tokens = new HashMap<>();
                tokens.put("access-token", accessToken);
                tokens.put("refresh-token", refreshToken);

                return ResponseEntity.ok(tokens);
            } catch (Exception e) {
                error.put("error-message", e.getMessage());
                return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
            }
        }


        error.put("error-message", "Refresh token is missing");
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

}
