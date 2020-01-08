package br.com.homedical.domain;

public interface DocumentS3 {

    String getUrl();

    void setUrl(String url);

    String getFileName();

    void setFileName(String fileName);

    String getS3Name();

    void setS3Name(String s3Name);

    byte[] getFile();

    void setFile(byte[] file);
}

