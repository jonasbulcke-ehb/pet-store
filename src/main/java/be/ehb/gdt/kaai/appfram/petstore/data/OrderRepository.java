package be.ehb.gdt.kaai.appfram.petstore.data;

import be.ehb.gdt.kaai.appfram.petstore.models.Order;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface OrderRepository extends CrudRepository<Order, Long> {
    Iterable<Order> findAllByUserUsername(String username);
    Optional<Order> findByIdAndUserUsername(long id, String username);
}
