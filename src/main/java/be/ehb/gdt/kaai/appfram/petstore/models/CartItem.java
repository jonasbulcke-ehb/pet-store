package be.ehb.gdt.kaai.appfram.petstore.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Min;

@Entity
@Table(name = "cart_items")
public class CartItem {
    @Id
    @GeneratedValue
    private long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Cart cart;
    @Min(1)
    private int amount;
    @ManyToOne
    private Product product;

    public CartItem() {

    }

    public CartItem(Product product, int amount) {
        this.product = product;
        this.amount = amount;
    }

    public CartItem(Product product, int amount, Cart cart) {
        this.product = product;
        this.amount = amount;
        this.cart = cart;
    }

    public double getSubtotal() {
        return product.getPrice() * amount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
