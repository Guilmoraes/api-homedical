package br.com.homedical.service.impl;

import br.com.homedical.domain.File;
import br.com.homedical.event.FileEvent;
import br.com.homedical.repository.FileRepository;
import br.com.homedical.service.FileService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class FileServiceImpl implements FileService {

    private final FileRepository repository;

    private final ApplicationEventPublisher publisher;

    public FileServiceImpl(FileRepository repository, ApplicationEventPublisher publisher) {
        this.repository = repository;
        this.publisher = publisher;
    }

    @Override
    public void saveFiles(List<File> files) {
        List<File> saveFiles = repository.save(files);
        if (saveFiles.stream().anyMatch(file -> file.getFile() != null)) {
            publisher.publishEvent(new FileEvent.StartFileUploadProcess());
        }
    }
}
