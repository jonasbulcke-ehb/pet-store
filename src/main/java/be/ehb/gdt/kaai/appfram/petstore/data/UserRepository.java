package be.ehb.gdt.kaai.appfram.petstore.data;

import be.ehb.gdt.kaai.appfram.petstore.models.AppUser;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<AppUser, Long> {
    AppUser findByUsername(String username);
    Iterable<AppUser> findAllByRole(String role);
}
