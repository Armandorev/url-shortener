package benjamin.groehbiel.ch.api;

public class ShortenerStats {

    private Long counter;

    protected ShortenerStats(Long counter) {
        this.counter = counter;
    }

    public Long getCounter() {
        return counter;
    }
}
