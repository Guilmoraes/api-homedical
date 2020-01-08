package br.com.homedical.service;

import br.com.homedical.HomedicalApp;
import br.com.homedical.config.ApplicationProperties;
import br.com.homedical.domain.Document;
import br.com.homedical.domain.DocumentS3;
import br.com.homedical.service.impl.AmazonS3DocumentServiceImpl;
import br.com.homedical.util.GenerateS3NameUtil;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.Region;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.InputStream;
import java.net.URL;
import java.util.Date;
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
public class DocumentUploadServiceInitTest {

    private AmazonS3DocumentService amazonS3DocumentService;

    private AmazonS3Client amazonS3;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private ResourceLoader resourceLoader;

    @Before
    public void setup() {
        amazonS3 = mock(AmazonS3Client.class);
        MockitoAnnotations.initMocks(this);
        amazonS3DocumentService = new AmazonS3DocumentServiceImpl(resourceLoader, amazonS3, applicationProperties);

    }

    @Test
    public void testName() {
        String fileName = GenerateS3NameUtil.generateName("file.exe");
        assertThat(fileName).isEqualTo("6D96A888BF1AAD63EC6BEF7E4C9132CD03C948D43EBBFDC76D60C7851FA100C4");
    }

    @Test
    public void testCreateFileToS3() {
        String message = "src/test/resources/import-test-data.sql";

        when(amazonS3.putObject(eq("bucketName"), eq("test.sql"), any(InputStream.class), any(ObjectMetadata.class)))
            .thenAnswer((Answer<PutObjectResult>) invocation -> {
                assertEquals("bucketName", invocation.getArguments()[0]);
                assertEquals("test.sql", invocation.getArguments()[1]);
                byte[] content = new byte[message.length()];
                assertEquals(content.length, ((InputStream) invocation.getArguments()[2]).read(content));
                assertEquals(message, new String(content));
                return new PutObjectResult();
            });

        when(amazonS3.getRegion()).thenReturn(Region.EU_Ireland);

        Document document = Document
            .builder()
            .file(message.getBytes())
            .fileName("test.sql")
            .s3Name("5a73778c1d058716a75604b8/5a73778c1d058716a75604b9/5a73778c1d058716a75604ba/test.sql")
            .processed(false)
            .build();

        CompletableFuture<DocumentS3> documentFuture = amazonS3DocumentService.createDocument(document, document.getFileContentType());

        documentFuture.whenComplete((documentS3, throwable) -> {
            assertEquals("test.sql", documentS3.getFileName());
            assertThat(documentS3.getUrl()).contains("bucketName");
        });
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateFileToS3_EmptyFileName() {
        String message = "src/test/resources/import-test-data.sql";

        when(amazonS3.putObject(eq("bucketName"), eq("test.sql"), any(InputStream.class), any(ObjectMetadata.class))).thenAnswer((Answer<PutObjectResult>) invocation -> {
            assertEquals("bucketName", invocation.getArguments()[0]);
            assertEquals("test.sql", invocation.getArguments()[1]);
            byte[] content = new byte[message.length()];
            assertEquals(content.length, ((InputStream) invocation.getArguments()[2]).read(content));
            assertEquals(message, new String(content));
            return new PutObjectResult();
        });

        when(amazonS3.getRegion()).thenReturn(Region.EU_Ireland);

        Document document = Document
            .builder()
            .file(message.getBytes())
            .fileName("")
            .processed(false)
            .build();

        amazonS3DocumentService.createDocument(document, document.getFileContentType());
    }

    @Test
    public void testCreateFileToS3_NullFile() {
        when(amazonS3.putObject(eq("bucketName"), eq("test.sql"), any(InputStream.class), any(ObjectMetadata.class))).thenAnswer((Answer<PutObjectResult>) invocation -> {
            assertEquals("bucketName", invocation.getArguments()[0]);
            assertEquals("test.sql", invocation.getArguments()[1]);

            return new PutObjectResult();
        });

        when(amazonS3.getRegion()).thenReturn(Region.EU_Ireland);

        Document document = Document
            .builder()
            .processed(false)
            .s3Name("5a73778c1d058716a75604b8/5a73778c1d058716a75604b9/5a73778c1d058716a75604ba/test.sql")
            .fileName("test.sql")
            .build();

        CompletableFuture<DocumentS3> documentFuture = amazonS3DocumentService.createDocument(document, document.getFileContentType());
        documentFuture.whenComplete((documentS3, throwable) -> {
            assertThat(documentS3).isNull();
        });
    }

    @Test
    public void testUpdateFileToS3() {
        String message = "src/test/resources/import-test-data.sql";

        when(amazonS3.putObject(eq("bucketName"), eq("test.sql"), any(InputStream.class), any(ObjectMetadata.class))).thenAnswer((Answer<PutObjectResult>) invocation -> {
            assertEquals("bucketName", invocation.getArguments()[0]);
            assertEquals("test.sql", invocation.getArguments()[1]);
            byte[] content = new byte[message.length()];
            assertEquals(content.length, ((InputStream) invocation.getArguments()[2]).read(content));
            assertEquals(message, new String(content));
            return new PutObjectResult();
        });

        when(amazonS3.getRegion()).thenReturn(Region.EU_Ireland);

        Document document = Document
            .builder()
            .file(message.getBytes())
            .fileName("test.sql")
            .url("/bucketName/5a7379824e40b31adea64a53/5a7379824e40b31adea64a54/5a7379824e40b31adea64a55/test.sql")
            .s3Name("5a73778c1d058716a75604b8/5a73778c1d058716a75604b9/5a73778c1d058716a75604ba/test.sql")
            .processed(false)
            .build();

        CompletableFuture<DocumentS3> documentFuture = amazonS3DocumentService.createDocument(document, document.getFileContentType());

        documentFuture.whenComplete((documentS3, throwable) -> {
            assertEquals("test.sql", documentS3.getFileName());
            assertEquals("/bucketName/5a7379824e40b31adea64a53/5a7379824e40b31adea64a54/5a7379824e40b31adea64a55/test.sql", documentS3.getUrl());
        });
    }

    @Test
    public void testUpdateFileToS3_EmptyS3Name() {
        String message = "src/test/resources/import-test-data.sql";

        when(amazonS3.putObject(eq("bucketName"), eq("test.sql"), any(InputStream.class), any(ObjectMetadata.class))).thenAnswer((Answer<PutObjectResult>) invocation -> {
            assertEquals("bucketName", invocation.getArguments()[0]);
            assertEquals("test.sql", invocation.getArguments()[1]);
            byte[] content = new byte[message.length()];
            assertEquals(content.length, ((InputStream) invocation.getArguments()[2]).read(content));
            assertEquals(message, new String(content));
            return new PutObjectResult();
        });

        when(amazonS3.getRegion()).thenReturn(Region.EU_Ireland);

        Document document = Document
            .builder()
            .file(message.getBytes())
            .fileName("test.sql")
            .s3Name("5a73778c1d058716a75604b8/5a73778c1d058716a75604b9/5a73778c1d058716a75604ba/test.sql")
            .url("/bucketName/5a7379824e40b31adea64a53/5a7379824e40b31adea64a54/5a7379824e40b31adea64a55/test.sql")
            .processed(false)
            .build();

        CompletableFuture<DocumentS3> documentFuture = amazonS3DocumentService.updateDocument(document, document.getFileContentType());

        documentFuture.whenComplete((documentS3, throwable) -> {
            assertEquals("test.sql", documentS3.getFileName());
            assertThat(documentS3.getUrl()).contains("/bucketName/");
        });
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateFileToS3_EmptyS3NameAndFileName() {
        String message = "src/test/resources/import-test-data.sql";

        when(amazonS3.putObject(eq("bucketName"), eq("test.sql"), any(InputStream.class), any(ObjectMetadata.class))).thenAnswer((Answer<PutObjectResult>) invocation -> {
            assertEquals("bucketName", invocation.getArguments()[0]);
            assertEquals("test.sql", invocation.getArguments()[1]);
            byte[] content = new byte[message.length()];
            assertEquals(content.length, ((InputStream) invocation.getArguments()[2]).read(content));
            assertEquals(message, new String(content));
            return new PutObjectResult();
        });

        when(amazonS3.getRegion()).thenReturn(Region.EU_Ireland);

        Document document = Document
            .builder()
            .file(message.getBytes())
            .url("/bucketName/5a7379824e40b31adea64a53/5a7379824e40b31adea64a54/5a7379824e40b31adea64a55/test.sql")
            .processed(false)
            .build();

        amazonS3DocumentService.updateDocument(document, document.getFileContentType());
    }

    @Test
    public void testUpdateFileToS3_NullFile() {
        when(amazonS3.putObject(eq("bucketName"), eq("test.sql"), any(InputStream.class), any(ObjectMetadata.class))).thenAnswer((Answer<PutObjectResult>) invocation -> {
            assertEquals("bucketName", invocation.getArguments()[0]);
            assertEquals("test.sql", invocation.getArguments()[1]);

            return new PutObjectResult();
        });

        when(amazonS3.getRegion()).thenReturn(Region.EU_Ireland);

        Document document = Document
            .builder()
            .fileName("test.sql")
            .url("/bucketName/5a7379824e40b31adea64a53/5a7379824e40b31adea64a54/5a7379824e40b31adea64a55/test.sql")
            .s3Name("5a73778c1d058716a75604b8/5a73778c1d058716a75604b9/5a73778c1d058716a75604ba/test.sql")
            .processed(false)
            .build();

        CompletableFuture<DocumentS3> documentFuture = amazonS3DocumentService.updateDocument(document, document.getFileContentType());
        documentFuture.whenComplete((documentS3, throwable) -> assertThat(documentS3).isNull());
    }

    @Test
    public void testGeneratePresignedUrlFileToS3() {
        String message = "src/test/resources/import-test-data.sql";

        when(amazonS3.generatePresignedUrl(eq("bucketName"), eq("5a73778c1d058716a75604b8/5a73778c1d058716a75604b9/5a73778c1d058716a75604ba/test.sql"), any(Date.class)))
            .thenAnswer((Answer<URL>) invocation -> {
                assertEquals("bucketName", invocation.getArguments()[0]);
                assertEquals("5a73778c1d058716a75604b8/5a73778c1d058716a75604b9/5a73778c1d058716a75604ba/test.sql", invocation.getArguments()[1]);
                assertEquals(Date.class, invocation.getArguments()[2].getClass());

                return new URL("http://amazon.s3/bucketName/5a73778c1d058716a75604ba/test.sql");
            });

        Document document = Document
            .builder()
            .file(message.getBytes())
            .fileName("test.sql")
            .url("/bucketName/5a7379824e40b31adea64a53/5a7379824e40b31adea64a54/5a7379824e40b31adea64a55/test.sql")
            .s3Name("5a73778c1d058716a75604b8/5a73778c1d058716a75604b9/5a73778c1d058716a75604ba/test.sql")
            .processed(false)
            .build();

        String url = amazonS3DocumentService.generatePresignedUrl(document);

        assertEquals("http://amazon.s3/bucketName/5a73778c1d058716a75604ba/test.sql", url);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGeneratePresignedUrlFileToS3_NullS3Name() {
        String message = "src/test/resources/import-test-data.sql";

        when(amazonS3.generatePresignedUrl(eq("bucketName"), eq("5a73778c1d058716a75604b8/5a73778c1d058716a75604b9/5a73778c1d058716a75604ba/test.sql"), any(Date.class)))
            .thenAnswer((Answer<URL>) invocation -> {
                assertEquals("bucketName", invocation.getArguments()[0]);
                assertEquals("5a73778c1d058716a75604b8/5a73778c1d058716a75604b9/5a73778c1d058716a75604ba/test.sql", invocation.getArguments()[1]);
                assertEquals(Date.class, invocation.getArguments()[2].getClass());

                return new URL("http://amazon.s3/bucketName/5a73778c1d058716a75604ba/test.sql");
            });

        Document document = Document
            .builder()
            .file(message.getBytes())
            .fileName("test.sql")
            .url("/bucketName/5a7379824e40b31adea64a53/5a7379824e40b31adea64a54/5a7379824e40b31adea64a55/test.sql")
            .processed(false)
            .build();

        amazonS3DocumentService.generatePresignedUrl(document);
    }

    @Test
    public void testDeleteFileToS3() {
        String message = "src/test/resources/import-test-data.sql";

        Document document = Document
            .builder()
            .file(message.getBytes())
            .fileName("test.sql")
            .url("/bucketName/5a7379824e40b31adea64a53/5a7379824e40b31adea64a54/5a7379824e40b31adea64a55/test.sql")
            .s3Name("5a73778c1d058716a75604b8/5a73778c1d058716a75604b9/5a73778c1d058716a75604ba/test.sql")
            .processed(false)
            .build();

        amazonS3DocumentService.deleteDocument(document);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteFileToS3_NullS3Name() {
        String message = "src/test/resources/import-test-data.sql";

        Document document = Document
            .builder()
            .file(message.getBytes())
            .fileName("test.sql")
            .url("/bucketName/5a7379824e40b31adea64a53/5a7379824e40b31adea64a54/5a7379824e40b31adea64a55/test.sql")
            .processed(false)
            .build();

        amazonS3DocumentService.deleteDocument(document);
    }

}
