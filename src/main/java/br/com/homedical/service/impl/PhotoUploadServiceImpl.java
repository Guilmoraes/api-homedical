package br.com.homedical.service.impl;

import br.com.homedical.domain.ImageS3;
import br.com.homedical.domain.Photo;
import br.com.homedical.event.ProcessPhotoEvent;
import br.com.homedical.repository.PhotoRepository;
import br.com.homedical.service.AmazonS3ImageService;
import br.com.homedical.service.PhotoUploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.concurrent.CompletableFuture;

@Service
public class PhotoUploadServiceImpl implements PhotoUploadService {

    private final Logger log = LoggerFactory.getLogger(PhotoUploadServiceImpl.class);

    private final PhotoRepository photoRepository;

    private final AmazonS3ImageService amazonS3ImageService;

    public PhotoUploadServiceImpl(PhotoRepository repository,
                                  AmazonS3ImageService amazonS3ImageService) {
        this.photoRepository = repository;
        this.amazonS3ImageService = amazonS3ImageService;
    }

    @Scheduled(cron = "0 1 * * * *")
    @Override
    public void uploadPhotos() {
        log.info("Start upload photos");
        photoRepository
            .findByFileIsNotNull()
            .forEach(it -> {
                if (it.getMedium().isEmpty() || it.getOriginal().isEmpty() || it.getThumbnail().isEmpty()) {
                    generatePhotosNames(it);
                    amazonS3ImageService.createImage(it);
                } else {
                    amazonS3ImageService.updateImage(it);
                }
            });
    }

    @Scheduled(cron = "0 3 * * * *")
    @Override
    public void unprocessed() {
        log.info("Start upload photos");
        photoRepository
            .findByProcessedFalse()
            .forEach(it -> {
                if (it.getMedium().isEmpty() || it.getOriginal().isEmpty() || it.getThumbnail().isEmpty()) {
                    generatePhotosNames(it);
                    amazonS3ImageService.createImage(it);
                } else {
                    amazonS3ImageService.updateImage(it);
                }
            });
    }

    @Async
    @TransactionalEventListener(fallbackExecution = true)
    @Override
    public void processPhotoEvent(ProcessPhotoEvent event) {
        log.info("Start upload photos");
        Photo photo = event.get();
        if (event.isUpdate()) {
            update(
                amazonS3ImageService.updateImage(photo)
            );
        } else {
            generatePhotosNames(photo);
            update(
                amazonS3ImageService.createImage(photo)
            );
        }
    }

    private void update(CompletableFuture<ImageS3> photo) {
        photo.whenComplete((imageS3, throwable) -> {
            if (imageS3 != null) {
                Photo p = (Photo) imageS3;
                p.setFile(null);
                p.setProcessed(true);
                photoRepository.save(p);
            }
        });
    }

    private void generatePhotosNames(Photo photo) {
        photo.setOriginalName(generateName(photo.getFileName()));
        photo.setMediumName(generateName(photo.getFileName()));
        photo.setThumbnailName(generateName(photo.getFileName()));
    }

}
