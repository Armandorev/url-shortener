package benjamin.groehbiel.ch.shortener.db;

import javax.persistence.*;

@Entity
@Table(name = "dictionary")
public class DictionaryHash {

    @Id
    @Column(name = "hash_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hashId;

    private String language;
    private String hash;
    private String description;
    private boolean available;

    public DictionaryHash() {
    }

    public DictionaryHash(String hash, String language, String description, Boolean available) {
        this.language = language;
        this.hash = hash;
        this.description = description;
        this.available = available;
    }

    public Long getHashId() {
        return hashId;
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
}
