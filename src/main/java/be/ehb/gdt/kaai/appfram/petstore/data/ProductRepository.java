package be.ehb.gdt.kaai.appfram.petstore.data;

import be.ehb.gdt.kaai.appfram.petstore.models.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {
    Iterable<Product> findAllBySubcategoryCategoryId(long catId);

    Iterable<Product> findAllBySubcategoryId(long subcatId);
}
