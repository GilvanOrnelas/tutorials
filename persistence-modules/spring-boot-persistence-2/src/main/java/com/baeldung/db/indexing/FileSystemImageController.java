package com.baeldung.db.indexing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("file-system")
class FileSystemImageController {

    @Autowired
    FileSystemRepository imageRepository;

    @PostMapping("/image")
    Long uploadImage(@RequestParam MultipartFile image) throws Exception {
        Image dbImage = new Image();
        dbImage.setName(image.getOriginalFilename());
        dbImage.setContent(image.getBytes());

        return imageRepository.saveToFileSystem(dbImage)
            .getId();
    }

    @GetMapping(value = "/image/{imageId}", produces = MediaType.IMAGE_JPEG_VALUE)
    Resource downloadImage(@PathVariable Long imageId) throws Exception {
        return new ByteArrayResource(imageRepository.findInFileSystem(imageId));
    }
}
