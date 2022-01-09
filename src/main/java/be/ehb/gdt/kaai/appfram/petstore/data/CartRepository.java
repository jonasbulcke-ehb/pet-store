package be.ehb.gdt.kaai.appfram.petstore.data;

import be.ehb.gdt.kaai.appfram.petstore.models.Cart;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CartRepository extends CrudRepository<Cart, Long> {
    Optional<Cart> findByUserUsername(String username);
}
