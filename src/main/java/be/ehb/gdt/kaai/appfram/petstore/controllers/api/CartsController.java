package be.ehb.gdt.kaai.appfram.petstore.controllers.api;

import be.ehb.gdt.kaai.appfram.petstore.data.CartRepository;
import be.ehb.gdt.kaai.appfram.petstore.data.ProductRepository;
import be.ehb.gdt.kaai.appfram.petstore.models.Cart;
import be.ehb.gdt.kaai.appfram.petstore.models.Product;
import be.ehb.gdt.kaai.appfram.petstore.models.ProductItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
public class CartsController {
    private final CartRepository cartRepo;
    private final ProductRepository productRepo;

    @Autowired
    public CartsController(CartRepository cartRepo, ProductRepository productRepo) {
        this.cartRepo = cartRepo;
        this.productRepo = productRepo;
    }

    @GetMapping("cart")
    public ResponseEntity<Cart> getCart() {
        return ResponseEntity.ok(getCurrentCart());
    }

    @PostMapping("cart/add/{productId}/{amount}")
    public ResponseEntity<Cart> addProductToCart(@PathVariable long productId, @PathVariable int amount) {
        Cart cart = saveItem(productId, amount);
        if (cart == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cart);
    }

    @PostMapping("cart/add/{productId}")
    public ResponseEntity<Cart> addProductToCart(@PathVariable long productId) {
        Cart cart = saveItem(productId, 1);
        if (cart == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cart);
    }

    private Cart saveItem(long productId, int amount) {
        Product product = productRepo.findById(productId).orElse(null);
        if (product == null) {
            return null;
        }
        Cart cart = getCurrentCart();
        ProductItem item = getItemById(cart, productId);
        if (item == null) {
            item = new ProductItem(product, amount, cart);
            cart.addProduct(item);
        } else {
            item.setAmount(item.getAmount() + amount);
        }
        return cartRepo.save(cart);
    }

    @DeleteMapping("cart/delete/{productId}")
    public ResponseEntity<Cart> deleteItem(@PathVariable long productId) {
        if (!productRepo.existsById(productId)) {
            return ResponseEntity.notFound().build();
        }
        Cart cart = getCurrentCart();
        cart.getItems().removeIf(item -> item.getProduct().getId() == productId);
        return ResponseEntity.ok(cartRepo.save(cart));
    }

    @PutMapping("cart/update/{productId}/{amount}")
    public ResponseEntity<Cart> updateAmount(@PathVariable long productId, @PathVariable int amount) {
        if (!productRepo.existsById(productId)) {
            return ResponseEntity.notFound().build();
        }
        Cart cart = getCurrentCart();
        ProductItem item = getItemById(cart, productId);
        if (item == null) {
            return ResponseEntity.notFound().build();
        }
        item.setAmount(amount);
        cartRepo.save(cart);

        return ResponseEntity.ok(cart);
    }

    private Cart getCurrentCart() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return cartRepo.findByUserUsername(username).orElse(null);
    }

    public ProductItem getItemById(Cart cart, long productId) {
        return cart.getItems().stream()
                .filter(item -> item.getProduct().getId() == productId)
                .findFirst().orElse(null);
    }
}
