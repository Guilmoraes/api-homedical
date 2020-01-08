package br.com.homedical.service;

import br.com.homedical.domain.ImageS3;

import java.util.concurrent.CompletableFuture;

public interface AmazonS3ImageService {

    CompletableFuture<ImageS3> updateImage(ImageS3 imageS3);

    CompletableFuture<ImageS3> createImage(ImageS3 imageS3);

    void deleteImage(ImageS3 imageS3);

}
