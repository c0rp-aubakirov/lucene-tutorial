package kz.kaznu.lucene.model;

import kz.kaznu.lucene.index.MessageToDocument;
import org.apache.lucene.document.Document;

public class Message {
    private String body;
    private String title;

    public Document convertToDocument() {
        return MessageToDocument.createWith(title, body);
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
