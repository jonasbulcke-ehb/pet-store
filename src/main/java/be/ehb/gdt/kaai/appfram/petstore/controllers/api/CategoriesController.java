package be.ehb.gdt.kaai.appfram.petstore.controllers.api;

import be.ehb.gdt.kaai.appfram.petstore.data.CategoryRepository;
import be.ehb.gdt.kaai.appfram.petstore.models.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/categories")
public class CategoriesController {
    private final CategoryRepository repo;

    @Autowired
    public CategoriesController(CategoryRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public ResponseEntity<Iterable<Category>> getCategories() {
        return ResponseEntity.ok(repo.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Category> getCategory(@PathVariable long id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Category> postCategory(@RequestBody Category category) {
        return new ResponseEntity<>(repo.save(category), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<Category> putCategory(@PathVariable long id, @RequestBody Category category) {
        if (category.getId() != id) {
            return ResponseEntity.badRequest().build();
        }

        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        repo.save(category);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Category> deleteCategory(@PathVariable long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

}
