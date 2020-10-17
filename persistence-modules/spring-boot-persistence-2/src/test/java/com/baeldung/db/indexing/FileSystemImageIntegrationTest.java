package com.baeldung.db.indexing;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.InputStream;

import org.assertj.core.api.Assertions;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.baeldung.db.indexing.ImageUploaderApplication;

@SpringBootTest(classes = ImageUploaderApplication.class)
@AutoConfigureMockMvc
@FixMethodOrder
class FileSystemImageIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @Order(1)
    void givenJpegImage_whenUploadIt_shallReturnItsId() throws Exception {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        InputStream image = classLoader.getResourceAsStream("baeldung.jpeg");

        MockMultipartHttpServletRequestBuilder multipartRequest = MockMvcRequestBuilders.multipart("/file-system/image")
            .file(new MockMultipartFile("image", "baeldung", MediaType.TEXT_PLAIN_VALUE, image));

        MvcResult result = mockMvc.perform(multipartRequest)
            .andExpect(status().isOk())
            .andReturn();

        Assertions.assertThat(result.getResponse()
            .getContentAsString())
            .isEqualTo("1");
    }

    @Test
    @Order(2)
    void givenBaeldungImage_whenDownloadIt_shallReturnTheImage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
            .get("/file-system/image/1")
            .contentType(MediaType.IMAGE_JPEG_VALUE))
            .andExpect(status().isOk());
    }

}
