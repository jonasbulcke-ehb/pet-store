package be.ehb.gdt.kaai.appfram.petstore.controllers.api;

import be.ehb.gdt.kaai.appfram.petstore.data.CartRepository;
import be.ehb.gdt.kaai.appfram.petstore.data.OrderRepository;
import be.ehb.gdt.kaai.appfram.petstore.models.Address;
import be.ehb.gdt.kaai.appfram.petstore.models.Cart;
import be.ehb.gdt.kaai.appfram.petstore.models.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/orders")
public class OrderController {
    private final OrderRepository orderRepo;
    private final CartRepository cartRepo;

    public OrderController(OrderRepository oderRepo, CartRepository cartRepo) {
        this.orderRepo = oderRepo;
        this.cartRepo = cartRepo;
    }

    @GetMapping
    public ResponseEntity<Iterable<Order>> getOrders() {
        return ResponseEntity.ok(orderRepo.findAllByUserUsername(getCurrentUsername()));
    }

    @GetMapping("{orderId}")
    public ResponseEntity<Order> getOrder(@PathVariable long orderId) {
        return orderRepo.findByIdAndUserUsername(orderId, getCurrentUsername())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("place")
    public ResponseEntity<Order> placeOrder(@RequestBody Address shippingAddress) {
        Optional<Cart> cartOptional = cartRepo.findByUserUsername(getCurrentUsername());
        if(cartOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Cart cart = cartOptional.get();
        Order order = new Order(cart, shippingAddress);
        cart.empty();
        cartRepo.save(cart);
        return ResponseEntity.ok(orderRepo.save(order));
    }

    private String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
