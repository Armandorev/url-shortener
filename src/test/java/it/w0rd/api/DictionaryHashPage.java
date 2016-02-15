package it.w0rd.api;

import it.w0rd.persistence.db.DictionaryHash;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties({"last", "totalElements", "totalPages", "size", "number", "first", "sort", "numberOfElements"})
public class DictionaryHashPage {

    private List<DictionaryHash> content;

    public List<DictionaryHash> getContent() {
        return content;
    }

    public void setContent(List<DictionaryHash> content) {
        this.content = content;
    }

}
