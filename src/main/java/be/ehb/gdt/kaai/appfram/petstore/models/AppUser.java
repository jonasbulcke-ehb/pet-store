package be.ehb.gdt.kaai.appfram.petstore.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "users")
public class AppUser {
    @Id
    @GeneratedValue
    private long id;

    @Column(unique = true)
    @NotBlank
    private String username;

    @NotBlank
    @JsonIgnore
    private String hashedPassword;

    @NotBlank
    @Column(unique = true)
    @Email
    private String email;
    private String role;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
    @JsonIgnore
    private Cart cart;

    public AppUser() {
        cart = new Cart();
        cart.setUser(this);
    }

    public AppUser(String username, String hashedPassword, String email) {
        this();
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }
}
