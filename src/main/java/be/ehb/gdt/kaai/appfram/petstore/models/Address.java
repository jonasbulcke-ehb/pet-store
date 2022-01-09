package be.ehb.gdt.kaai.appfram.petstore.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue
    private int id;

    @NotBlank
    private String addressLine1;

    private String addressLine2;

    @Min(1000)
    @Max(9999)
    @NotNull
    private int zipcode;

    @NotBlank
    private String city;

    public Address() {

    }

    public Address(String addressLine1, String addressLine2, int zipcode, String city) {
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.zipcode = zipcode;
        this.city = city;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public int getZipcode() {
        return zipcode;
    }

    public void setZipcode(int zipcode) {
        this.zipcode = zipcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
