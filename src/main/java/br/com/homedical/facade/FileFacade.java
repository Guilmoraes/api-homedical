package br.com.homedical.facade;

import br.com.homedical.domain.File;
import br.com.homedical.facade.dto.FileDTO;
import br.com.homedical.service.FileService;
import br.com.homedical.service.mapper.FileMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class FileFacade {

    private final Logger log = LoggerFactory.getLogger(FileFacade.class);

    private final FileMapper mapper;

    private final FileService service;

    public FileFacade(FileMapper mapper, FileService service) {
        this.mapper = mapper;
        this.service = service;
    }

    @Transactional
    public void syncFiles(List<FileDTO> dtos) {
        List<File> files = mapper.toEntity(dtos);
        service.saveFiles(files);
    }

}
