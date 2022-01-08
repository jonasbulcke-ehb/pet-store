package be.ehb.gdt.kaai.appfram.petstore.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue
    private long id;

    @NotBlank
    private String name;

    @OneToMany(mappedBy = "category")
    @JsonIgnore
    private List<Subcategory> subcategories;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Subcategory> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<Subcategory> categories) {
        this.subcategories = categories;
    }
}
