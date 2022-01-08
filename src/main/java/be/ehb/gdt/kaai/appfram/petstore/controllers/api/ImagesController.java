package be.ehb.gdt.kaai.appfram.petstore.controllers.api;

import be.ehb.gdt.kaai.appfram.petstore.data.ImageRepository;
import be.ehb.gdt.kaai.appfram.petstore.models.Image;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("api/images")
public class ImagesController {
    private final ImageRepository repo;

    @Autowired
    public ImagesController(ImageRepository repo) {
        this.repo = repo;
    }

    @PostMapping
    public ResponseEntity<?> postImage(@RequestParam MultipartFile file) throws IOException {
        String name = file.getOriginalFilename();
        if( FilenameUtils.isExtension(file.getOriginalFilename(), "jpg", "jpeg", "png")) {
            Image image = new Image();
            image.setContent(file.getBytes());
            image.setName(name);

            repo.save(image);

            return new ResponseEntity<>(image, HttpStatus.CREATED);
        }

        return new ResponseEntity<>("File is not a valid image", HttpStatus.BAD_REQUEST);
    }





}
