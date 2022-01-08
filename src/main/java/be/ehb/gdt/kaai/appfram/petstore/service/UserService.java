package be.ehb.gdt.kaai.appfram.petstore.service;

import be.ehb.gdt.kaai.appfram.petstore.data.UserRepository;
import be.ehb.gdt.kaai.appfram.petstore.models.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

/**
 * source: https://www.youtube.com/watch?v=VVn9OG9nfH0
 */
@Service
public class UserService implements IUserService, UserDetailsService {
    private final UserRepository userRepo;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepo, BCryptPasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AppUser saveUser(AppUser user, String password) {
        user.setHashedPassword(passwordEncoder.encode(password));
        user.setRole("Customer");
        return userRepo.save(user);
    }

    @Override
    public AppUser getUser(String username) {
        AppUser user = userRepo.findByUsername(username);
        if(user == null) {
            throw new RuntimeException("User " + username + " not found in db");
        }
        return user;
    }

    @Override
    public Iterable<AppUser> getUsers() {
        return userRepo.findAll();
    }

    @Override
    public void assignRole(String username, String role) {
        AppUser user = userRepo.findByUsername(username);
        user.setRole(role);
        userRepo.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = userRepo.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found in the db");
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole()));
        return new User(user.getUsername(), user.getHashedPassword(), authorities);
    }
}
