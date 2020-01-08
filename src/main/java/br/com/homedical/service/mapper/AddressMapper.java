package br.com.homedical.service.mapper;

import br.com.homedical.domain.Address;
import br.com.homedical.facade.dto.adddress.AddressPatientDTO;
import br.com.homedical.service.dto.AddressDTO;
import br.com.homedical.service.dto.AddressSimpleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper for the entity Address and its DTO AddressDTO.
 */
@Mapper(componentModel = "spring", uses = {CityMapper.class,})
public interface AddressMapper extends EntityMapper<AddressDTO, Address> {

    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "street", ignore = true)
    @Mapping(target = "number", ignore = true)
    @Mapping(target = "zipcode", ignore = true)
    @Mapping(target = "district", ignore = true)
    @Mapping(target = "complement", ignore = true)
    @Mapping(target = "city", ignore = true)
    @Mapping(target = "lat", ignore = true)
    @Mapping(target = "lng", ignore = true)
    @Mapping(target = "isValidAddress", ignore = true)
    Address toSimpleEntity(AddressSimpleDTO dto);

    AddressSimpleDTO toSimpleDto(Address entity);

    List<Address> toSimpleEntityList(List<AddressSimpleDTO> dtoList);

    List<AddressDTO> toSimpleDtoList(List<Address> entityList);

    @Override
    @Mapping(target = "valid", ignore = true)
    @Mapping(target = "placeId", ignore = true)
    @Mapping(target = "placeName", ignore = true)
    @Mapping(target = "placeNameSecondary", ignore = true)
    @Mapping(target = "stateName", ignore = true)
    @Mapping(target = "cityName", ignore = true)
    @Mapping(target = "route", ignore = true)
    AddressDTO toDto(Address entity);

    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    Address fromAddressPatientDtoToEntity(AddressPatientDTO addressPatientDTO);
}
