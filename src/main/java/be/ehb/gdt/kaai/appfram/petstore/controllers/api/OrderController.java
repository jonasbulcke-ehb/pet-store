package be.ehb.gdt.kaai.appfram.petstore.controllers.api;

import be.ehb.gdt.kaai.appfram.petstore.data.AddressRepository;
import be.ehb.gdt.kaai.appfram.petstore.data.CartRepository;
import be.ehb.gdt.kaai.appfram.petstore.data.OrderRepository;
import be.ehb.gdt.kaai.appfram.petstore.data.ProductItemRepository;
import be.ehb.gdt.kaai.appfram.petstore.models.Address;
import be.ehb.gdt.kaai.appfram.petstore.models.Cart;
import be.ehb.gdt.kaai.appfram.petstore.models.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/orders")
public class OrderController {
    private final OrderRepository orderRepo;
    private final CartRepository cartRepo;
    private final ProductItemRepository productItemRepo;
    private final AddressRepository addressRepo;

    @Autowired
    public OrderController(
            OrderRepository oderRepo,
            CartRepository cartRepo,
            ProductItemRepository productItemRepo,
            AddressRepository addressRepo) {
        this.orderRepo = oderRepo;
        this.cartRepo = cartRepo;
        this.productItemRepo = productItemRepo;
        this.addressRepo = addressRepo;
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
        if (cartOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Cart cart = cartOptional.get();
        if (cart.getItems().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setShippingAddress(shippingAddress);
        Order savedOrder = orderRepo.save(order);
        savedOrder.setItems(cart.getItems().stream()
                .peek(item -> item.setCart(null))
                .peek(item -> item.setOrder(savedOrder))
                .collect(Collectors.toSet())
        );
        cart.empty();
        cartRepo.save(cart);
        productItemRepo.saveAll(order.getItems());
        return ResponseEntity.ok(savedOrder);
    }

    private String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
