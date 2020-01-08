package br.com.homedical.service.mapper;

import br.com.homedical.domain.File;
import br.com.homedical.facade.dto.FileDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FileMapper extends EntityMapper<FileDTO, File> {

    @Override
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "s3Name", ignore = true)
    @Mapping(target = "processed", ignore = true)
    File toEntity(FileDTO dto);
}
