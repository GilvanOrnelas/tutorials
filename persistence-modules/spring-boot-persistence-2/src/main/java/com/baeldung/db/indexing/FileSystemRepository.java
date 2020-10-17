package com.baeldung.db.indexing;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface FileSystemRepository extends JpaRepository<Image, Long> {

    String RESOURCES_DIR = FileSystemRepository.class.getResource("/")
        .getPath() + "/";

    default Image saveToFileSystem(Image image) throws Exception {
        Path newFile = Paths.get(RESOURCES_DIR + new Date().getTime() + "-" + image.name);
        Files.createDirectories(newFile.getParent());

        Files.write(newFile, image.content);

        image.setName(image.name);
        image.setLocation(newFile.toAbsolutePath()
            .toString());
        image.setContent(null);

        return save(image);
    }

    default byte[] findInFileSystem(Long id) throws Exception {
        Path image = Paths.get(findById(id).get().location);

        return Files.readAllBytes(image);
    }
}
