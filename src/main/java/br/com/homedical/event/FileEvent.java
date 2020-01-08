package br.com.homedical.event;

import br.com.homedical.domain.Document;
import org.springframework.context.ApplicationEvent;

import java.util.List;

public class FileEvent extends ApplicationEvent {

    public FileEvent(String id) {
        super(id);
    }

    public static class StartFileUploadProcess {

        public StartFileUploadProcess() {
        }
    }

    public static class StartFilesUploadByIds {
        private List<String> ids;

        public StartFilesUploadByIds(List<String> ids) {
            this.ids = ids;
        }

        public List<String> getIds() {
            return ids;
        }
    }
}

