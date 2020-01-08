package br.com.homedical.facade.mapper;

import br.com.homedical.domain.DocumentType;
import br.com.homedical.facade.dto.documentType.DocumentTypeCreateDTO;
import br.com.homedical.facade.dto.documentType.DocumentTypeDTO;
import br.com.homedical.facade.dto.documentType.DocumentTypeSimpleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper for the entity DocumentType and its DTO DocumentTypeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DocumentTypeMapper extends EntityMapper <DocumentTypeDTO, DocumentType> {

    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "required", ignore = true)
    DocumentType toSimpleEntity(DocumentTypeSimpleDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    DocumentType fromDocumentTypeCreateDtoToEntity(DocumentTypeCreateDTO documentTypeCreateDTO);

    DocumentTypeSimpleDTO toSimpleDto(DocumentType entity);

    List<DocumentType> toSimpleEntityList(List<DocumentTypeSimpleDTO> dtoList);

    List<DocumentTypeDTO> toSimpleDtoList(List<DocumentType> entityList);

    List<DocumentTypeSimpleDTO> fromEntityListToSimpleList(List<DocumentType> documentTypes);

}
