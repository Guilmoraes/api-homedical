package br.com.homedical.service;

import br.com.homedical.HomedicalApp;
import br.com.homedical.config.ApplicationProperties;
import br.com.homedical.domain.ImageS3;
import br.com.homedical.domain.Photo;
import br.com.homedical.service.impl.AmazonS3ImageServiceImpl;
import br.com.homedical.util.GenerateS3NameUtil;
import br.com.homedical.util.ObjectId;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.Region;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("Duplicates")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HomedicalApp.class)
public class PhotoUploadServiceInitTest {

    private AmazonS3ImageService amazonS3ImageService;

    @Autowired
    private AmazonS3ImageService amazonS3ImageServiceReal;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private ResourceLoader resourceLoader;

    private AmazonS3Client amazonS3;

    @Autowired
    private AmazonS3Client amazonS3Real;

    @Before
    public void setup() {
        amazonS3 = mock(AmazonS3Client.class);
        MockitoAnnotations.initMocks(this);
        amazonS3ImageService = new AmazonS3ImageServiceImpl(resourceLoader, amazonS3, applicationProperties);
    }

    String generateName(String name) {
        return String.format("%s/%s/%s/%s",
            GenerateS3NameUtil.generateName(name),
            ObjectId.get().toString(),
            RandomStringUtils.randomAlphanumeric(21),
            name
        );
    }

    @Test
    public void testUpload() {

        CompletableFuture<ImageS3> jabba = amazonS3ImageServiceReal.createImage(Photo.builder().file(createBytes()).originalName(generateName("hue.png")).mediumName(generateName("hue.png")).thumbnailName(generateName("hue.png")).fileName("java.jpg").build());
        jabba.whenComplete((it, t) -> {

        });
    }

    public static byte[] createBytes() {
        File file = new File("src/test/resources/java.jpg");

        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Test
    public void testCreateImageToS3() throws IOException {
        byte[] bFile = createBytes();

        when(amazonS3.putObject(eq("bucketName"), eq("test.png"), any(InputStream.class), any(ObjectMetadata.class)))
            .thenAnswer((Answer<PutObjectResult>) invocation -> {
                assertEquals("bucketName", invocation.getArguments()[0]);
                assertEquals("test.png", invocation.getArguments()[1]);

                assertEquals(bFile.length, ((InputStream) invocation.getArguments()[2]).read(bFile));
                return new PutObjectResult();
            });

        when(amazonS3.getRegion()).thenReturn(Region.EU_Ireland);

        Photo photo = Photo
            .builder()
            .file(bFile)
            .fileName("test.png")
            .thumbnailName("5a73963f4e40b344711440d2/5a73963f4e40b344711440d3/5a73963f4e40b344711440d4/test.png")
            .mediumName("5a73963f4e40b344711440cf/5a73963f4e40b344711440d0/5a73963f4e40b344711440d1/test.png")
            .originalName("5a73963f4e40b344711440cc/5a73963f4e40b344711440cd/5a73963f4e40b344711440ce/test.png")
            .processed(false)
            .build();

        CompletableFuture<ImageS3> photoFuture = amazonS3ImageService.createImage(photo);

        photoFuture.whenComplete((documentS3, throwable) -> {
            assertEquals("test.png", documentS3.getFileName());
            assertThat(documentS3.getMedium()).contains("bucketName");
        });
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateImageToS3_EmptyFileName() throws IOException {
        byte[] bFile = createBytes();

        when(amazonS3.putObject(eq("bucketName"), eq("test.png"), any(InputStream.class), any(ObjectMetadata.class)))
            .thenAnswer((Answer<PutObjectResult>) invocation -> {
                assertEquals("bucketName", invocation.getArguments()[0]);
                assertEquals("test.png", invocation.getArguments()[1]);

                assertEquals(bFile.length, ((InputStream) invocation.getArguments()[2]).read(bFile));
                return new PutObjectResult();
            });

        when(amazonS3.getRegion()).thenReturn(Region.EU_Ireland);

        Photo photo = Photo.builder().file(bFile).processed(false).build();

        CompletableFuture<ImageS3> photoFuture = amazonS3ImageService.createImage(photo);

        photoFuture.whenComplete((documentS3, throwable) -> {
            assertEquals("test.png", documentS3.getFileName());
            assertThat(documentS3.getMedium()).contains("bucketName");
        });
    }

    @Test
    public void testCreateImageToS3_NullFile() throws IOException {
        when(amazonS3.putObject(eq("bucketName"), eq("test.png"), any(InputStream.class), any(ObjectMetadata.class)))
            .thenAnswer((Answer<PutObjectResult>) invocation -> {
                assertEquals("bucketName", invocation.getArguments()[0]);
                assertEquals("test.png", invocation.getArguments()[1]);

                return new PutObjectResult();
            });

        when(amazonS3.getRegion()).thenReturn(Region.EU_Ireland);

        Photo photo = Photo
            .builder()
            .fileName("test.png")
            .thumbnailName("5a73963f4e40b344711440d2/5a73963f4e40b344711440d3/5a73963f4e40b344711440d4/test.png")
            .mediumName("5a73963f4e40b344711440cf/5a73963f4e40b344711440d0/5a73963f4e40b344711440d1/test.png")
            .originalName("5a73963f4e40b344711440cc/5a73963f4e40b344711440cd/5a73963f4e40b344711440ce/test.png")
            .processed(false)
            .build();

        CompletableFuture<ImageS3> photoFuture = amazonS3ImageService.createImage(photo);

        photoFuture.whenComplete((documentS3, throwable) -> assertThat(documentS3).isNull());
    }

    @Test
    public void testUpdateFileToS3() throws IOException {
        byte[] bFile = createBytes();

        when(amazonS3.putObject(eq("bucketName"), eq("test.png"), any(InputStream.class), any(ObjectMetadata.class))).thenAnswer((Answer<PutObjectResult>) invocation -> {
            assertEquals("bucketName", invocation.getArguments()[0]);
            assertEquals("test.png", invocation.getArguments()[1]);
            assertEquals(bFile.length, ((InputStream) invocation.getArguments()[2]).read(bFile));

            return new PutObjectResult();
        });

        when(amazonS3.getRegion()).thenReturn(Region.EU_Ireland);

        Photo photo = Photo.builder()
            .file(bFile)
            .fileName("test.png")
            .thumbnail("/bucketName/5a73963f4e40b344711440d2/5a73963f4e40b344711440d3/5a73963f4e40b344711440d4/test.png")
            .medium("/bucketName/5a73963f4e40b344711440cf/5a73963f4e40b344711440d0/5a73963f4e40b344711440d1/test.png")
            .original("/bucketName/5a73963f4e40b344711440cc/5a73963f4e40b344711440cd/5a73963f4e40b344711440ce/test.png")
            .thumbnailName("5a73963f4e40b344711440d2/5a73963f4e40b344711440d3/5a73963f4e40b344711440d4/test.png")
            .mediumName("5a73963f4e40b344711440cf/5a73963f4e40b344711440d0/5a73963f4e40b344711440d1/test.png")
            .originalName("5a73963f4e40b344711440cc/5a73963f4e40b344711440cd/5a73963f4e40b344711440ce/test.png")
            .processed(true)
            .build();

        CompletableFuture<ImageS3> photoFuture = amazonS3ImageService.updateImage(photo);

        photoFuture.whenComplete((documentS3, throwable) -> {
            assertEquals("test.png", documentS3.getFileName());
            assertEquals("/bucketName/5a73963f4e40b344711440d2/5a73963f4e40b344711440d3/5a73963f4e40b344711440d4/test.png", documentS3.getThumbnail());
            assertEquals("/bucketName/5a73963f4e40b344711440cf/5a73963f4e40b344711440d0/5a73963f4e40b344711440d1/test.png", documentS3.getMedium());
            assertEquals("/bucketName/5a73963f4e40b344711440cc/5a73963f4e40b344711440cd/5a73963f4e40b344711440ce/test.png", documentS3.getOriginal());
            assertEquals("5a73963f4e40b344711440d2/5a73963f4e40b344711440d3/5a73963f4e40b344711440d4/test.png", documentS3.getThumbnailName());
            assertEquals("5a73963f4e40b344711440cf/5a73963f4e40b344711440d0/5a73963f4e40b344711440d1/test.png", documentS3.getMediumName());
            assertEquals("5a73963f4e40b344711440cc/5a73963f4e40b344711440cd/5a73963f4e40b344711440ce/test.png", documentS3.getOriginalName());
        });
    }

    @Test
    public void testUpdateFileToS3_NullFile() throws IOException {
        when(amazonS3.putObject(eq("bucketName"), eq("test.png"), any(InputStream.class), any(ObjectMetadata.class))).thenAnswer((Answer<PutObjectResult>) invocation -> {
            assertEquals("bucketName", invocation.getArguments()[0]);
            assertEquals("test.png", invocation.getArguments()[1]);

            return new PutObjectResult();
        });

        when(amazonS3.getRegion()).thenReturn(Region.EU_Ireland);

        Photo photo = Photo.builder()
            .fileName("test.png")
            .thumbnail("/bucketName/5a73963f4e40b344711440d2/5a73963f4e40b344711440d3/5a73963f4e40b344711440d4/test.png")
            .medium("/bucketName/5a73963f4e40b344711440cf/5a73963f4e40b344711440d0/5a73963f4e40b344711440d1/test.png")
            .original("/bucketName/5a73963f4e40b344711440cc/5a73963f4e40b344711440cd/5a73963f4e40b344711440ce/test.png")
            .thumbnailName("5a73963f4e40b344711440d2/5a73963f4e40b344711440d3/5a73963f4e40b344711440d4/test.png")
            .mediumName("5a73963f4e40b344711440cf/5a73963f4e40b344711440d0/5a73963f4e40b344711440d1/test.png")
            .originalName("5a73963f4e40b344711440cc/5a73963f4e40b344711440cd/5a73963f4e40b344711440ce/test.png")
            .processed(true)
            .build();

        CompletableFuture<ImageS3> photoFuture = amazonS3ImageService.updateImage(photo);

        photoFuture.whenComplete((documentS3, throwable) -> assertThat(documentS3).isNull());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateFileToS3_MediumNameNull() throws IOException {
        byte[] bFile = createBytes();

        when(amazonS3.putObject(eq("bucketName"), eq("test.png"), any(InputStream.class), any(ObjectMetadata.class))).thenAnswer((Answer<PutObjectResult>) invocation -> {
            assertEquals("bucketName", invocation.getArguments()[0]);
            assertEquals("test.png", invocation.getArguments()[1]);
            assertEquals(bFile.length, ((InputStream) invocation.getArguments()[2]).read(bFile));

            return new PutObjectResult();
        });

        when(amazonS3.getRegion()).thenReturn(Region.EU_Ireland);

        Photo photo = Photo.builder()
            .file(bFile)
            .fileName("test.png")
            .thumbnail("/bucketName/5a73963f4e40b344711440d2/5a73963f4e40b344711440d3/5a73963f4e40b344711440d4/test.png")
            .medium("/bucketName/5a73963f4e40b344711440cf/5a73963f4e40b344711440d0/5a73963f4e40b344711440d1/test.png")
            .original("/bucketName/5a73963f4e40b344711440cc/5a73963f4e40b344711440cd/5a73963f4e40b344711440ce/test.png")
            .thumbnailName("5a73963f4e40b344711440d2/5a73963f4e40b344711440d3/5a73963f4e40b344711440d4/test.png")

            .originalName("5a73963f4e40b344711440cc/5a73963f4e40b344711440cd/5a73963f4e40b344711440ce/test.png")
            .processed(true)
            .build();

        CompletableFuture<ImageS3> photoFuture = amazonS3ImageService.updateImage(photo);
    }

    @Test
    public void testDeleteFileToS3() {
        File file = new File("src/test/resources/java.png");
        byte[] bFile = new byte[0];

        try {
            bFile = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Photo photo = Photo.builder()
            .file(bFile)
            .fileName("test.png")
            .thumbnail("/bucketName/5a73963f4e40b344711440d2/5a73963f4e40b344711440d3/5a73963f4e40b344711440d4/test.png")
            .medium("/bucketName/5a73963f4e40b344711440cf/5a73963f4e40b344711440d0/5a73963f4e40b344711440d1/test.png")
            .original("/bucketName/5a73963f4e40b344711440cc/5a73963f4e40b344711440cd/5a73963f4e40b344711440ce/test.png")
            .thumbnailName("5a73963f4e40b344711440d2/5a73963f4e40b344711440d3/5a73963f4e40b344711440d4/test.png")
            .mediumName("5a73963f4e40b344711440cf/5a73963f4e40b344711440d0/5a73963f4e40b344711440d1/test.png")
            .originalName("5a73963f4e40b344711440cc/5a73963f4e40b344711440cd/5a73963f4e40b344711440ce/test.png")
            .processed(true)
            .build();

        amazonS3ImageService.deleteImage(photo);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteFileToS3_MediumNull() {
        File file = new File("src/test/resources/java.png");
        byte[] bFile = new byte[0];

        try {
            bFile = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Photo photo = Photo.builder()
            .file(bFile)
            .fileName("test.png")
            .thumbnail("/bucketName/5a73963f4e40b344711440d2/5a73963f4e40b344711440d3/5a73963f4e40b344711440d4/test.png")
            .original("/bucketName/5a73963f4e40b344711440cc/5a73963f4e40b344711440cd/5a73963f4e40b344711440ce/test.png")
            .thumbnailName("5a73963f4e40b344711440d2/5a73963f4e40b344711440d3/5a73963f4e40b344711440d4/test.png")
            .originalName("5a73963f4e40b344711440cc/5a73963f4e40b344711440cd/5a73963f4e40b344711440ce/test.png")
            .processed(true)
            .build();

        amazonS3ImageService.deleteImage(photo);
    }

}
