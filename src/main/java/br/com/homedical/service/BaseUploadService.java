package br.com.homedical.service;

import br.com.homedical.domain.DocumentS3;
import br.com.homedical.util.GenerateS3NameUtil;
import br.com.homedical.util.ObjectId;
import org.apache.commons.lang3.RandomStringUtils;

public interface BaseUploadService {

    default DocumentS3 generateName(DocumentS3 documentS3) {
        documentS3.setS3Name(generateName(documentS3.getFileName()));
        return documentS3;
    }

    default String generateName(String name) {
        return String.format("%s/%s/%s/%s",
            GenerateS3NameUtil.generateName(name),
            ObjectId.get().toString(),
            RandomStringUtils.randomAlphanumeric(21),
            name
        );
    }

}
