package benjamin.groehbiel.ch.shortener.db;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "dictionary")
public class DictionaryHash {

    @Id
    private String hash;

    private String language;
    private String description;
    private boolean available;
    private Timestamp indexed;
    private Timestamp registered;

    public DictionaryHash() {
    }

    public DictionaryHash(String hash, String language, String description, Boolean available) {
        this.language = language;
        this.hash = hash;
        this.description = description;
        this.available = available;
    }

    public String getLanguage() {
        return language;
    }

    public String getHash() {
        return hash.trim();
    }

    public String getDescription() {
        return description.trim();
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Timestamp getIndexed() {
        return indexed;
    }

    public void setIndexed(Timestamp indexed) {
        this.indexed = indexed;
    }

    public Timestamp getRegistered() {
        return registered;
    }

    public void setRegistered(Timestamp registered) {
        this.registered = registered;
    }
}
