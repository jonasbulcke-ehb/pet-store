package be.ehb.gdt.kaai.appfram.petstore;

import be.ehb.gdt.kaai.appfram.petstore.models.AppUser;
import be.ehb.gdt.kaai.appfram.petstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class PetStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(PetStoreApplication.class, args);
    }

//    @Bean
//    @Autowired
//    CommandLineRunner run(UserService userService) {
//        return args -> {
//            userService.saveUser(new AppUser("jonasB", "Student1!", "jonas@mail.com"), "Student1!");
//            userService.assignRole("jonasB", "Admin");
//        };
//    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
