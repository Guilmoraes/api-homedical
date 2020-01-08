package br.com.homedical.service;

import br.com.homedical.domain.File;

import java.util.List;

public interface FileService {

    void saveFiles(List<File> files);

}
