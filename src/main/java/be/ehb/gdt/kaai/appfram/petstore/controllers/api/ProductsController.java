package be.ehb.gdt.kaai.appfram.petstore.controllers.api;

import be.ehb.gdt.kaai.appfram.petstore.data.ProductRepository;
import be.ehb.gdt.kaai.appfram.petstore.data.SubcategoryRepository;
import be.ehb.gdt.kaai.appfram.petstore.models.Product;
import be.ehb.gdt.kaai.appfram.petstore.models.Subcategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.function.Function;
import java.util.stream.Collectors;


@RestController
@RequestMapping("api")
public class ProductsController {
    private final ProductRepository repo;
    private final SubcategoryRepository subcatRepo;

    @Autowired
    public ProductsController(ProductRepository repo, SubcategoryRepository subcatRepo) {
        this.repo = repo;
        this.subcatRepo = subcatRepo;
    }

    @GetMapping("products")
    public ResponseEntity<Iterable<Product>> getProducts() {
        return ResponseEntity.ok(repo.findAll());
    }

    @GetMapping("products/{prodId}")
    public ResponseEntity<Product> getProduct(@PathVariable long prodId) {
        Product product = repo.findById(prodId).orElse(null);

        return product == null ? ResponseEntity.badRequest().build() : ResponseEntity.ok(product);
    }

    @GetMapping("categories/{catId}/products")
    public ResponseEntity<Iterable<Product>> getProductsByCategory(@PathVariable long catId) {
        return ResponseEntity.ok(repo.findAllBySubcategoryCategoryId(catId));
    }

    @GetMapping("categories/{catId}/products/{prodId}")
    public ResponseEntity<Product> getProductByCategory(@PathVariable long catId, @PathVariable long prodId) {
        Product product = repo.findById(prodId).orElse(null);

        if (product == null || product.getSubcategory().getCategory().getId() != catId) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(product);
    }

    @GetMapping("categories/{catId}/subcategories/{subcatId}/products")
    public ResponseEntity<Iterable<Product>> getProductsBySubcategory(@PathVariable long catId, @PathVariable long subcatId) {
        Subcategory subcategory = subcatRepo.findById(subcatId).orElse(null);

        if (subcategory == null || subcategory.getCategory().getId() != catId) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(repo.findAllBySubcategoryId(subcatId));
    }

    @PostMapping("categories/{catId}/subcategories/{subcatId}/products")
    public ResponseEntity<Object> postProduct(@PathVariable long catId, @PathVariable long subcatId, @RequestBody @Valid Product product) {
        Subcategory subcategory = subcatRepo.findById(subcatId).orElse(null);

        if (subcategory == null || subcategory.getCategory().getId() != catId) {
            return ResponseEntity.notFound().build();
        }

        product.setSubcategory(subcategory);

        return new ResponseEntity<>(repo.save(product), HttpStatus.CREATED);
    }


    @PutMapping("categories/{catId}/subcategories/{subcatId}/products/{prodId}")
    public ResponseEntity<Product> putProduct(@PathVariable long catId, @PathVariable long subcatId, @PathVariable long prodId, @RequestBody Product product) {
        if (product.getId() != prodId) {
            return ResponseEntity.badRequest().build();
        }

        if (product.getSubcategory().getId() != subcatId || product.getSubcategory().getCategory().getId() != catId) {
            return ResponseEntity.notFound().build();
        }

        repo.save(product);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("categories/{catId}/subcategories/{subcatId}/products/{prodId}")
    public ResponseEntity<Product> deleteProduct(@PathVariable long catId, @PathVariable long subcatId, @PathVariable long prodId) {
        Product product = repo.findById(prodId).orElse(null);

        if (product == null || product.getSubcategory().getId() != subcatId || product.getSubcategory().getCategory().getId() != catId) {
            return ResponseEntity.notFound().build();
        }

        repo.delete(product);

        return ResponseEntity.noContent().build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleException(MethodArgumentNotValidException ex) {
        return ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + " " + e.getDefaultMessage())
                .collect(Collectors.joining("\n"));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public String handleException(ConstraintViolationException ex) {
        return ex.getConstraintViolations().stream()
                .map(e -> e.getPropertyPath() + " " + e.getMessage())
                .collect(Collectors.joining());
    }
}
