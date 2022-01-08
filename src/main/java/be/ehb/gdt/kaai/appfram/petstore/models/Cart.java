package be.ehb.gdt.kaai.appfram.petstore.models;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue
    private long id;
    @OneToOne
    @JoinColumn(name = "user_id")
    private AppUser user;
    @OneToMany(mappedBy = "cart", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private Set<CartItem> cartItems;

    public double getTotal() {
        return cartItems.stream().map(CartItem::getSubtotal).reduce(0.0, Double::sum);
    }

    public int getTotalAmount() {
        return cartItems.stream().map(CartItem::getAmount).reduce(0, Integer::sum);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    public Set<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(Set<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public void addProduct(CartItem cartItem) {
        cartItems.add(cartItem);
    }

    public void addProduct(Product product, int amount) {
        CartItem cartItem = new CartItem(product, amount);
        cartItems.add(cartItem);
    }
}
