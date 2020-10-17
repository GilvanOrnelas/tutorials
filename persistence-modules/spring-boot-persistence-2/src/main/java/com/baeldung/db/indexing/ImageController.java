package com.baeldung.db.indexing;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
class ImageController {

    @Autowired
    ImageRepository imageRepository;

    @PostMapping("/image")
    long uploadImage(@RequestParam MultipartFile image) throws IOException {
        Image dbImage = new Image();
        dbImage.setName(image.getOriginalFilename());
        dbImage.setContent(image.getBytes());

        return imageRepository.save(dbImage)
            .getId();
    }

    @GetMapping(value = "/image/{imageId}", produces = MediaType.IMAGE_JPEG_VALUE)
    Resource downloadImage(@PathVariable Long imageId) {
        byte[] image = imageRepository.findById(imageId)
            .get()
            .getContent();
        return new ByteArrayResource(image);
    }
}
