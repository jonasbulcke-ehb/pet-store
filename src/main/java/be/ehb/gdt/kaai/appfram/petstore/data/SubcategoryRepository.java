package be.ehb.gdt.kaai.appfram.petstore.data;

import be.ehb.gdt.kaai.appfram.petstore.models.Subcategory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SubcategoryRepository extends CrudRepository<Subcategory, Long> {
    List<Subcategory> findAllByCategoryId(long id);
}
