package be.ehb.gdt.kaai.appfram.petstore.controllers.api;

import be.ehb.gdt.kaai.appfram.petstore.data.CartRepository;
import be.ehb.gdt.kaai.appfram.petstore.data.ProductRepository;
import be.ehb.gdt.kaai.appfram.petstore.models.Cart;
import be.ehb.gdt.kaai.appfram.petstore.models.CartItem;
import be.ehb.gdt.kaai.appfram.petstore.models.Product;
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
        Cart cart = saveCartItem(productId, amount);
        if(cart == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cart);
    }

    @PostMapping("cart/add/{productId}")
    public ResponseEntity<Cart> addProductToCart(@PathVariable long productId) {
        Cart cart = saveCartItem(productId, 1);
        if(cart == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cart);
    }

    private Cart saveCartItem(long productId, int amount) {
        Product product = productRepo.findById(productId).orElse(null);
        if (product == null) {
            return null;
        }
        Cart cart = getCurrentCart();
        CartItem cartItem = getCartItemById(cart, productId);
        if (cartItem == null) {
            cartItem = new CartItem(product, amount);
            cart.getCartItems().add(cartItem);
        } else {
            cartItem.setAmount(cartItem.getAmount() + amount);
        }
        return cartRepo.save(cart);
    }

    @DeleteMapping("cart/delete/{productId}")
    public ResponseEntity<Cart> deleteCartItem(@PathVariable long productId) {
        if(!productRepo.existsById(productId)) {
            return ResponseEntity.notFound().build();
        }
        Cart cart = getCurrentCart();
        cart.getCartItems().removeIf(item -> item.getProduct().getId() == productId);
        return ResponseEntity.ok(cartRepo.save(cart));
    }

    @PutMapping("cart/update/{productId}/{amount}")
    private ResponseEntity<Cart> updateAmount(@PathVariable long productId, @PathVariable int amount) {
        if (!productRepo.existsById(productId)) {
            return ResponseEntity.notFound().build();
        }
        Cart cart = getCurrentCart();
        CartItem cartItem = getCartItemById(cart, productId);
        if(cartItem == null) {
            return ResponseEntity.notFound().build();
        }
        cartItem.setAmount(amount);
        cartRepo.save(cart);

        return ResponseEntity.ok(cart);
    }

    private Cart getCurrentCart() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return cartRepo.findByUserUsername(username);
    }

    public CartItem getCartItemById(Cart cart, long productId) {
        return cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId() == productId)
                .findFirst().orElse(null);
    }
}
