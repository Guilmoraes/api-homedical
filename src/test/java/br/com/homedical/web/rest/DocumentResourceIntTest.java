package br.com.homedical.web.rest;

import br.com.homedical.HomedicalApp;
import br.com.homedical.domain.DocumentType;
import br.com.homedical.domain.enumeration.DocumentStatus;
import br.com.homedical.facade.dto.document.DocumentDTO;
import br.com.homedical.facade.mapper.DocumentMapper;
import br.com.homedical.repository.DocumentRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static br.com.homedical.web.rest.util.AuthenticationUtil.getAccessToken;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the DocumentResource REST controller.
 *
 * @see DocumentResource
 */
@SuppressWarnings("Duplicates")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HomedicalApp.class)
@Sql(scripts = {"classpath:sql/homedical_specialty_professional_test.sql", "classpath:sql/homedical_user_professional_test.sql", "classpath:sql/homedical_user_authority_professional_test.sql", "classpath:sql/homedical_phone_professional_test.sql", "classpath:sql/homedical_address_professional_test.sql", "classpath:sql/homedical_patient_professional_test.sql", "classpath:sql/homedical_professional_professional_test.sql", "classpath:sql/homedical_professional_patients_professional_test.sql", "classpath:sql/homedical_professional_specialties_professional_test.sql", "classpath:sql/homedical_document_test.sql"})
public class DocumentResourceIntTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private DocumentMapper documentMapper;

    private MockMvc restDocumentMockMvc;


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        this.restDocumentMockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .apply(SecurityMockMvcConfigurers.springSecurity()).build();
    }

    @Test
    @Transactional
    public void createDocument() throws Exception {
        DocumentDTO documentDTO = createDocumentDto();

        MvcResult mvcResult = restDocumentMockMvc.perform(post("/api/documents")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restDocumentMockMvc, "admin", "admin"))
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documentDTO)))
            .andExpect(status().isCreated())
            .andReturn();

    }

    @Test
    @Transactional
    public void createDocumentWithoutAuth() throws Exception {
        DocumentDTO documentDTO = createDocumentDto();

        MvcResult mvcResult = restDocumentMockMvc.perform(post("/api/documents")
            .with(csrf())
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documentDTO)))
            .andExpect(status().isUnauthorized())
            .andReturn();
    }

    @Test
    @Transactional
    public void createDocumentWithoutDocumentType() throws Exception {
        DocumentDTO documentDTO = createDocumentDto();
        documentDTO.setType(null);

        MvcResult mvcResult = restDocumentMockMvc.perform(post("/api/documents")
            .with(csrf())
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documentDTO)))
            .andExpect(status().isUnauthorized())
            .andReturn();
    }

    @Test
    @Transactional
    public void createDocumentWithoutInvalidDocumentType() throws Exception {
        DocumentDTO documentDTO = createDocumentDto();
        documentDTO.getType().setId("11111-11-1-1-1");

        MvcResult mvcResult = restDocumentMockMvc.perform(post("/api/documents")
            .with(csrf())
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documentDTO)))
            .andExpect(status().isUnauthorized())
            .andReturn();
    }

    @Test
    @Transactional
    public void getOneDocument() throws Exception {

        MvcResult mvcResult = restDocumentMockMvc.perform(get("/api/documents/{id}", "b62bc22f-621d-4089-b294-bd9a08268000")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restDocumentMockMvc, "admin", "admin")))
            .andExpect(status().isOk())
            .andReturn();
    }

    @Test
    @Transactional
    public void getOneDocumentWithoutAuth() throws Exception {

        MvcResult mvcResult = restDocumentMockMvc.perform(get("/api/documents/{id}", "b62bc22f-621d-4089-b294-bd9a08268000")
            .with(csrf()))
            .andExpect(status().isUnauthorized())
            .andReturn();
    }

    @Test
    @Transactional
    public void getDocuments() throws Exception {

        MvcResult mvcResult = restDocumentMockMvc.perform(get("/api/documents")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restDocumentMockMvc, "admin", "admin")))
            .andExpect(status().isOk())
            .andReturn();
    }

    @Test
    @Transactional
    public void getProfessionalDocuments() throws Exception {

        MvcResult mvcResult = restDocumentMockMvc.perform(get("/api/documents/{id}/professionals", "179a277e-b7a8-4e86-90f5-d5cb4b94465d")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restDocumentMockMvc, "admin", "admin")))
            .andExpect(status().isOk())
            .andReturn();
    }

    @Test
    @Transactional
    public void getProfessionalDocumentsWithoutAuth() throws Exception {

        MvcResult mvcResult = restDocumentMockMvc.perform(get("/api/documents/{id}/professionals", "179a277e-b7a8-4e86-90f5-d5cb4b94465d")
            .with(csrf()))
            .andExpect(status().isUnauthorized())
            .andReturn();
    }

    @Test
    @Transactional
    public void getDocumentsWithoutAuth() throws Exception {

        MvcResult mvcResult = restDocumentMockMvc.perform(get("/api/documents")
            .with(csrf()))
            .andExpect(status().isUnauthorized())
            .andReturn();
    }

    @Test
    @Transactional
    public void getOneDocumentWithWrongId() throws Exception {

        MvcResult mvcResult = restDocumentMockMvc.perform(get("/api/documents/{id}", "iodjiosaj-621d-4089-b294-bd9a08268000")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restDocumentMockMvc, "admin", "admin")))
            .andExpect(status().isBadRequest())
            .andReturn();
    }

    @Test
    @Transactional
    public void updateDocument() throws Exception {
        DocumentDTO documentDTO = documentMapper.toDto(documentRepository.findOne("b62bc22f-621d-4089-b294-bd9a08268000"));
        documentDTO.setFileName("teste.png");

        MvcResult mvcResult = restDocumentMockMvc.perform(put("/api/documents/{id}", "b62bc22f-621d-4089-b294-bd9a08268000")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restDocumentMockMvc, "admin", "admin"))
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documentDTO)))
            .andExpect(status().isOk())
            .andReturn();
    }

    @Test
    @Transactional
    public void updateDocumentWithoutAuth() throws Exception {
        DocumentDTO documentDTO = documentMapper.toDto(documentRepository.findOne("b62bc22f-621d-4089-b294-bd9a08268000"));
        documentDTO.setFileName("teste.png");

        MvcResult mvcResult = restDocumentMockMvc.perform(put("/api/documents/{id}", "b62bc22f-621d-4089-b294-bd9a08268000")
            .with(csrf())
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documentDTO)))
            .andExpect(status().isUnauthorized())
            .andReturn();
    }

    @Test
    @Transactional
    public void updateDocumentStatus() throws Exception {
        DocumentDTO documentDTO = documentMapper.toDto(documentRepository.findOne("b62bc22f-621d-4089-b294-bd9a08268000"));
        documentDTO.setStatus(DocumentStatus.APPROVED);

        MvcResult mvcResult = restDocumentMockMvc.perform(put("/api/documents/status", "b62bc22f-621d-4089-b294-bd9a08268000")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restDocumentMockMvc, "admin", "admin"))
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documentDTO)))
            .andExpect(status().isOk())
            .andReturn();
    }

    @Test
    @Transactional
    public void updateDocumentStatusWithWrongRole() throws Exception {
        DocumentDTO documentDTO = documentMapper.toDto(documentRepository.findOne("b62bc22f-621d-4089-b294-bd9a08268000"));
        documentDTO.setStatus(DocumentStatus.APPROVED);

        MvcResult mvcResult = restDocumentMockMvc.perform(put("/api/documents/status", "b62bc22f-621d-4089-b294-bd9a08268000")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restDocumentMockMvc, "user", "user"))
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documentDTO)))
            .andExpect(status().isForbidden())
            .andReturn();
    }

    @Test
    @Transactional
    public void updateDocumentStatusWithoutAuth() throws Exception {
        DocumentDTO documentDTO = documentMapper.toDto(documentRepository.findOne("b62bc22f-621d-4089-b294-bd9a08268000"));
        documentDTO.setStatus(DocumentStatus.APPROVED);

        MvcResult mvcResult = restDocumentMockMvc.perform(put("/api/documents/status", "b62bc22f-621d-4089-b294-bd9a08268000")
            .with(csrf())
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documentDTO)))
            .andExpect(status().isUnauthorized())
            .andReturn();
    }

    @Test
    @Transactional
    public void updateDocumentStatusWithWrongId() throws Exception {
        DocumentDTO documentDTO = documentMapper.toDto(documentRepository.findOne("b62bc22f-621d-4089-b294-bd9a08268000"));
        documentDTO.setStatus(DocumentStatus.APPROVED);

        MvcResult mvcResult = restDocumentMockMvc.perform(put("/api/documents/status", "testeId")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restDocumentMockMvc, "user", "user"))
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documentDTO)))
            .andExpect(status().isForbidden())
            .andReturn();
    }

    @Test
    @Transactional
    public void deleteOneDocument() throws Exception {
        MvcResult mvcResult = restDocumentMockMvc.perform(delete("/api/documents/{id}", "b62bc22f-621d-4089-b294-bd9a08268000")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restDocumentMockMvc, "admin", "admin")))
            .andExpect(status().isOk())
            .andReturn();
    }

    @Test
    @Transactional
    public void deleteOneDocumentWithoutAuth() throws Exception {
        MvcResult mvcResult = restDocumentMockMvc.perform(delete("/api/documents/{id}", "b62bc22f-621d-4089-b294-bd9a08268000")
            .with(csrf()))
            .andExpect(status().isUnauthorized())
            .andReturn();
    }

    @Test
    @Transactional
    public void getProfessionalDocumentsCorrectly() throws Exception {

        MvcResult mvcResult = restDocumentMockMvc.perform(get("/api/documents/professional")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restDocumentMockMvc, "rafael@esparta.io", "homedical123")))
            .andExpect(status().isOk())
            .andReturn();
    }

    @Test
    @Transactional
    public void getProfessionalDocumentsCorrectlyWithWrongRole() throws Exception {

        MvcResult mvcResult = restDocumentMockMvc.perform(get("/api/documents/professional")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restDocumentMockMvc, "user", "user")))
            .andExpect(status().isForbidden())
            .andReturn();
    }

    @Test
    @Transactional
    public void getProfessionalDocumentsCorrectlyWithoutAuth() throws Exception {

        MvcResult mvcResult = restDocumentMockMvc.perform(get("/api/documents/professional")
            .with(csrf()))
            .andExpect(status().isUnauthorized())
            .andReturn();
    }

    private DocumentDTO createDocumentDto(){
        DocumentDTO documentDTO = new DocumentDTO();
        documentDTO.setFileContentType("image/jpg");
        documentDTO.setUrl("https://s3.amazonaws.com/homedical/documents/990FA79334B78203263B93835ECD4A42A2BB2D19E0DB787667BEFC8882D38EEE/5ab94efcfcaede56b372028c/lPh7AfvOXSHk9mi118Hn4/homedical.png");
        documentDTO.setFile(TestUtil.createByteArray(1, "0"));
        documentDTO.setFileName("homedical.png");
        documentDTO.setProcessed(false);
        documentDTO.setType(createDocumentType());
        documentDTO.setS3Name("990FA79334B78203263B93835ECD4A42A2BB2D19E0DB787667BEFC8882D38EEE/5ab94efcfcaede56b372028c/lPh7AfvOXSHk9mi118Hn4/homedical.png");
        documentDTO.setStatus(DocumentStatus.WAITING_APPROVEMENT);

        return  documentDTO;
    }

    private DocumentType createDocumentType(){
        DocumentType documentType = new DocumentType();
        documentType.setId("b62bc22f-621d-4089-b294-bd9a08268e02");
        documentType.setName("CPF");
        documentType.setRequired(true);

        return documentType;
    }

}
