package br.com.homedical.domain;

public interface ImageS3 {

    String getFileName();

    byte[] getFile();

    void setThumbnail(String thumbnail);

    void setMedium(String medium);

    void setOriginal(String original);

    String getThumbnail();

    String getMedium();

    String getOriginal();

    void setThumbnailName(String thumbnailName);

    void setMediumName(String mediumName);

    void setOriginalName(String originalName);

    String getThumbnailName();

    String getMediumName();

    String getOriginalName();

}
