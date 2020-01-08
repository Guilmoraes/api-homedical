package br.com.homedical.web.rest;

import br.com.homedical.HomedicalApp;
import br.com.homedical.domain.DocumentType;
import br.com.homedical.facade.dto.documentType.DocumentTypeCreateDTO;
import br.com.homedical.facade.dto.documentType.DocumentTypeDTO;
import br.com.homedical.facade.mapper.DocumentTypeMapper;
import br.com.homedical.repository.DocumentTypeRepository;
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
 * Test class for the DocumentTypeResource REST controller.
 *
 * @see DocumentTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HomedicalApp.class)
@Sql(scripts = {"classpath:sql/homedical_document_type_test.sql", "classpath:/sql/documenttype/homedical_document_type_test_phone.sql", "classpath:/sql/documenttype/homedical_document_type_test_address.sql", "classpath:/sql/documenttype/homedical_document_type_test_user.sql", "classpath:sql/documenttype/homedical_document_type_test_user_authority.sql", "classpath:/sql/documenttype/homedical_document_type_test_professional.sql"})
public class DocumentTypeResourceIntTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc restDocumentTypeMockMvc;

    private DocumentType documentType;

    @Autowired
    private DocumentTypeRepository documentTypeRepository;

    @Autowired
    private DocumentTypeMapper documentTypeMapper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        this.restDocumentTypeMockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .apply(SecurityMockMvcConfigurers.springSecurity()).build();
    }

    @Test
    @Transactional
    public void createDocumentType() throws Exception {
        DocumentTypeCreateDTO documentTypeCreateDTO = createDocumentTypeCreateDto();

        MvcResult mvcResult = restDocumentTypeMockMvc.perform(post("/api/document-types")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restDocumentTypeMockMvc, "admin", "admin"))
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documentTypeCreateDTO)))
            .andExpect(status().isCreated())
            .andReturn();

    }

    @Test
    @Transactional
    public void createDocumentTypeWithoutAuth() throws Exception {
        DocumentTypeCreateDTO documentTypeCreateDTO = createDocumentTypeCreateDto();

        MvcResult mvcResult = restDocumentTypeMockMvc.perform(post("/api/document-types")
            .with(csrf())
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documentTypeCreateDTO)))
            .andExpect(status().isUnauthorized())
            .andReturn();
    }

    @Test
    @Transactional
    public void createDocumentTypeWithWrongRole() throws Exception {
        DocumentTypeCreateDTO documentTypeCreateDTO = createDocumentTypeCreateDto();

        MvcResult mvcResult = restDocumentTypeMockMvc.perform(post("/api/document-types")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restDocumentTypeMockMvc, "user", "user"))
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documentTypeCreateDTO)))
            .andExpect(status().isForbidden())
            .andReturn();
    }

    @Test
    @Transactional
    public void getOneDocumentType() throws Exception {

        MvcResult mvcResult = restDocumentTypeMockMvc.perform(get("/api/document-types/{id}", "b62bc22f-621d-4089-b294-bd9a08268e02")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restDocumentTypeMockMvc, "admin", "admin")))
            .andExpect(status().isOk())
            .andReturn();
    }

    @Test
    @Transactional
    public void getOneDocumentTypeWithoutAuth() throws Exception {

        MvcResult mvcResult = restDocumentTypeMockMvc.perform(get("/api/document-types/{id}", "b62bc22f-621d-4089-b294-bd9a08268e02")
            .with(csrf()))
            .andExpect(status().isUnauthorized())
            .andReturn();
    }

    @Test
    @Transactional
    public void getDocumentTypes() throws Exception {

        MvcResult mvcResult = restDocumentTypeMockMvc.perform(get("/api/document-types")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restDocumentTypeMockMvc, "admin", "admin")))
            .andExpect(status().isOk())
            .andReturn();
    }

    @Test
    @Transactional
    public void getDocumentTypesWithoutAuth() throws Exception {

        MvcResult mvcResult = restDocumentTypeMockMvc.perform(get("/api/document-types")
            .with(csrf()))
            .andExpect(status().isUnauthorized())
            .andReturn();
    }

    @Test
    @Transactional
    public void getOneDocumentTypeWithWrongId() throws Exception {

        MvcResult mvcResult = restDocumentTypeMockMvc.perform(get("/api/document-types/{id}", "lokdmdf2-621d-4089-b294-bd9a08268e01")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restDocumentTypeMockMvc, "admin", "admin")))
            .andExpect(status().isNotFound())
            .andReturn();
    }

    @Test
    @Transactional
    public void updateDocumentType() throws Exception {
        DocumentTypeDTO documentTypeDTO = documentTypeMapper.toDto(documentTypeRepository.findOne("b62bc22f-621d-4089-b294-bd9a08268e02"));
        documentTypeDTO.setName("RG");

        MvcResult mvcResult = restDocumentTypeMockMvc.perform(put("/api/document-types/{id}", "b62bc22f-621d-4089-b294-bd9a08268e02")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restDocumentTypeMockMvc, "admin", "admin"))
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documentTypeDTO)))
            .andExpect(status().isOk())
            .andReturn();
    }

    @Test
    @Transactional
    public void updateDocumentWithWrongRole() throws Exception {
        DocumentTypeDTO documentTypeDTO = documentTypeMapper.toDto(documentTypeRepository.findOne("b62bc22f-621d-4089-b294-bd9a08268e02"));
        documentTypeDTO.setName("RG");

        MvcResult mvcResult = restDocumentTypeMockMvc.perform(put("/api/document-types/{id}", "b62bc22f-621d-4089-b294-bd9a08268e02")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restDocumentTypeMockMvc, "user", "user"))
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documentTypeDTO)))
            .andExpect(status().isForbidden())
            .andReturn();
    }

    @Test
    @Transactional
    public void updateDocumentTypeWithoutAuth() throws Exception {
        DocumentTypeDTO documentTypeDTO = documentTypeMapper.toDto(documentTypeRepository.findOne("b62bc22f-621d-4089-b294-bd9a08268e02"));
        documentTypeDTO.setName("RG");

        MvcResult mvcResult = restDocumentTypeMockMvc.perform(put("/api/document-types/{id}", "b62bc22f-621d-4089-b294-bd9a08268e02")
            .with(csrf())
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documentTypeDTO)))
            .andExpect(status().isUnauthorized())
            .andReturn();
    }

    @Test
    @Transactional
    public void deleteOneDocumentType() throws Exception {
        MvcResult mvcResult = restDocumentTypeMockMvc.perform(delete("/api/document-types/{id}", "b62bc22f-621d-4089-b294-bd9a08268e02")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restDocumentTypeMockMvc, "admin", "admin")))
            .andExpect(status().isOk())
            .andReturn();
    }

    @Test
    @Transactional
    public void deleteOneDocumentTypeWithWrongRole() throws Exception {
        MvcResult mvcResult = restDocumentTypeMockMvc.perform(delete("/api/document-types/{id}", "b62bc22f-621d-4089-b294-bd9a08268e02")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restDocumentTypeMockMvc, "user", "user")))
            .andExpect(status().isForbidden())
            .andReturn();
    }

    @Test
    @Transactional
    public void deleteOneDocumentTypeWithoutAuth() throws Exception {
        MvcResult mvcResult = restDocumentTypeMockMvc.perform(delete("/api/document-types/{id}", "b62bc22f-621d-4089-b294-bd9a08268e02")
            .with(csrf()))
            .andExpect(status().isUnauthorized())
            .andReturn();
    }

    @Test
    @Transactional
    public void getPendingDocumentTypes() throws Exception {

        MvcResult mvcResult = restDocumentTypeMockMvc.perform(get("/api/document-types/pending")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restDocumentTypeMockMvc, "rafael@esparta.io", "homedical123")))
            .andExpect(status().isOk())
            .andReturn();
    }

    @Test
    @Transactional
    public void getPendingDocumentTypesWithWrongRole() throws Exception {

        MvcResult mvcResult = restDocumentTypeMockMvc.perform(get("/api/document-types/pending")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restDocumentTypeMockMvc, "user", "user")))
            .andExpect(status().isForbidden())
            .andReturn();
    }

    @Test
    @Transactional
    public void getPendingDocumentTypesWithoutAuth() throws Exception {

        MvcResult mvcResult = restDocumentTypeMockMvc.perform(get("/api/document-types/pending")
            .with(csrf()))
            .andExpect(status().isUnauthorized())
            .andReturn();
    }


    private DocumentTypeCreateDTO createDocumentTypeCreateDto() {
        DocumentTypeCreateDTO documentTypeCreateDTO = new DocumentTypeCreateDTO();
        documentTypeCreateDTO.setName("CPF");
        documentTypeCreateDTO.setRequired(true);

        return documentTypeCreateDTO;
    }

}
