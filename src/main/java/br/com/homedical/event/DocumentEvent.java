package br.com.homedical.event;

import br.com.homedical.domain.Document;
import org.springframework.context.ApplicationEvent;

public class DocumentEvent extends ApplicationEvent {

    public DocumentEvent(String id) {
        super(id);
    }

    public static class ProcessDocumentEvent {
        private Document document;
        private boolean update;

        public ProcessDocumentEvent(Document document, boolean update) {
            this.document = document;
            this.update = update;
        }

        public Document get() {
            return document;
        }

        public boolean isUpdate() {
            return update;
        }
    }
}
