package br.com.homedical.facade.mapper;

import br.com.homedical.domain.Document;
import br.com.homedical.facade.dto.document.DocumentDTO;
import br.com.homedical.facade.dto.document.DocumentSimpleDTO;
import br.com.homedical.facade.dto.document.DocumentUploadDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper for the entity Document and its DTO DocumentDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DocumentMapper extends EntityMapper<DocumentDTO, Document> {

    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "url", ignore = true)
    @Mapping(target = "fileName", ignore = true)
    @Mapping(target = "s3Name", ignore = true)
    @Mapping(target = "file", ignore = true)
    @Mapping(target = "fileContentType", ignore = true)
    @Mapping(target = "processed", ignore = true)
    @Mapping(target = "type", ignore = true)
    @Mapping(target = "professional", ignore = true)
    @Mapping(target = "status", ignore = true)
    Document toSimpleEntity(DocumentSimpleDTO dto);

    DocumentSimpleDTO toSimpleDto(Document entity);

    List<Document> toSimpleEntityList(List<DocumentSimpleDTO> dtoList);

    List<DocumentDTO> toSimpleDtoList(List<Document> entityList);

    @Mapping(target = "professional.createdBy", ignore = true)
    @Mapping(target = "professional.createdDate", ignore = true)
    @Mapping(target = "professional.lastModifiedBy", ignore = true)
    @Mapping(target = "professional.lastModifiedDate", ignore = true)
    @Mapping(target = "professional.user", ignore = true)
    @Mapping(target = "professional.specialties", ignore = true)
    @Mapping(target = "professional.phone", ignore = true)
    @Mapping(target = "professional.address", ignore = true)
    @Mapping(target = "professional.cpf", ignore = true)
    @Mapping(target = "professional.patients", ignore = true)
    @Mapping(target = "professional.duties", ignore = true)
    @Mapping(target = "type.createdBy", ignore = true)
    @Mapping(target = "type.createdDate", ignore = true)
    @Mapping(target = "type.lastModifiedBy", ignore = true)
    @Mapping(target = "type.lastModifiedDate", ignore = true)
    @Mapping(target = "type.required", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "url", ignore = true)
    @Mapping(target = "s3Name", ignore = true)
    @Mapping(target = "processed", ignore = true)
    Document fromDocumentUploadDtoToEntity(DocumentUploadDTO documentUploadDTO);

}
