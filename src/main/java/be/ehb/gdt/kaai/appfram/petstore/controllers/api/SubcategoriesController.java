package be.ehb.gdt.kaai.appfram.petstore.controllers.api;

import be.ehb.gdt.kaai.appfram.petstore.data.CategoryRepository;
import be.ehb.gdt.kaai.appfram.petstore.data.SubcategoryRepository;
import be.ehb.gdt.kaai.appfram.petstore.models.Category;
import be.ehb.gdt.kaai.appfram.petstore.models.Subcategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/categories/{catId}/subcategories")
public class SubcategoriesController {
    private final SubcategoryRepository subcatRepo;
    private final CategoryRepository catRepo;

    @Autowired
    public SubcategoriesController(SubcategoryRepository subcatRepo, CategoryRepository catRepo) {
        this.subcatRepo = subcatRepo;
        this.catRepo = catRepo;
    }

    @GetMapping
    public ResponseEntity<Iterable<Subcategory>> getSubcategories(@PathVariable long catId) {
        Iterable<Subcategory> categories = subcatRepo.findAllByCategoryId(catId);
        return ResponseEntity.ok(categories);
    }

    @GetMapping("{subcatId}")
    public ResponseEntity<Subcategory> getSubcategory(@PathVariable long catId, @PathVariable long subcatId) {
        Subcategory subcategory = subcatRepo.findById(subcatId).orElse(null);

        if(subcategory == null) {
            return ResponseEntity.notFound().build();
        }

        if(subcategory.getCategory().getId() != catId) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(subcategory);
    }

    @PostMapping
    public ResponseEntity<Subcategory> postSubcategory(@PathVariable long catId, @RequestBody Subcategory subcategory) {
        if(subcategory.getCategory().getId() != catId) {
            return ResponseEntity.badRequest().build();
        }

        Category category = catRepo.findById(catId).orElse(null);

        if(category == null) {
            return ResponseEntity.notFound().build();
        }

        subcategory.setCategory(category);


        return new ResponseEntity<>(subcatRepo.save(subcategory), HttpStatus.CREATED);
    }

    @PutMapping("{subcatId}")
    public ResponseEntity<Subcategory> putSubcategory(@PathVariable long catId, @PathVariable long subcatId, @RequestBody Subcategory subcategory) {
        if(subcategory.getId() != subcatId || subcategory.getCategory().getId() != catId) {
            return ResponseEntity.badRequest().build();
        }

        subcatRepo.save(subcategory);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{subcatId}")
    public ResponseEntity<Subcategory> deleteSubcategoryId(@PathVariable long catId, @PathVariable long subcatId) {
        Subcategory subcategory = subcatRepo.findById(subcatId).orElse(null);

        if(subcategory == null) {
            return ResponseEntity.notFound().build();
        }

        if(subcategory.getCategory().getId() != catId) {
            return ResponseEntity.badRequest().build();
        }

        subcatRepo.delete(subcategory);

        return ResponseEntity.noContent().build();
    }

}
