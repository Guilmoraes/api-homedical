package br.com.homedical.facade.mapper;

import br.com.homedical.domain.Signature;
import br.com.homedical.facade.dto.SignatureDTO;
import br.com.homedical.service.mapper.FileMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {FileMapper.class})
public interface SignatureMapper extends EntityMapper<SignatureDTO, Signature> {
}
