package br.com.homedical.web.rest;

import br.com.homedical.facade.FileFacade;
import br.com.homedical.facade.dto.FileDTO;
import br.com.homedical.security.AuthoritiesConstants;
import com.codahale.metrics.annotation.Timed;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class FileResource {

    private final FileFacade facade;

    public FileResource(FileFacade facade) {
        this.facade = facade;
    }

    @PostMapping("/files/sync")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.PROFESSIONAL})
    public ResponseEntity<Void> syncFiles(@Valid @RequestBody List<FileDTO> dtos) {
        facade.syncFiles(dtos);
        return ResponseEntity.ok().build();
    }
}
