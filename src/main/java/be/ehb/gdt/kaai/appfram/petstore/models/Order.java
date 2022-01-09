package be.ehb.gdt.kaai.appfram.petstore.models;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue
    private long id;
    @ManyToOne
    private AppUser user;
    @OneToMany(mappedBy = "order")
    private Set<ProductItem> items;
    @OneToOne
    private Address shippingAddress;

    public Order() {

    }

    public Order(Cart cart, Address shippingAddress) {
        this.shippingAddress = shippingAddress;
        user = cart.getUser();
        items = cart.getItems();
    }

    public double getTotal() {
        return items.stream().map(ProductItem::getSubtotal).reduce(0.0, Double::sum);
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

    public Set<ProductItem> getItems() {
        return items;
    }

    public void setItems(Set<ProductItem> items) {
        this.items = items;
    }

    public Address getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(Address shippingAddress) {
        this.shippingAddress = shippingAddress;
    }
}
