package be.ehb.gdt.kaai.appfram.petstore.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Min;

@Entity
@Table(name = "product_items")
public class ProductItem {
    @Id
    @GeneratedValue
    private long id;
    @ManyToOne
    @JsonIgnore
//    @JoinColumn(name = "cart_id")
    private Cart cart;
    @ManyToOne
    @JsonIgnore
//    @JoinColumn(name = "order_id")
    private Order order;
    @Min(value = 1)
    private int amount;
    @ManyToOne
    private Product product;

    public ProductItem() {

    }

    public ProductItem(Product product, int amount) {
        this.product = product;
        this.amount = amount;
    }

    public ProductItem(Product product, int amount, Cart cart) {
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
