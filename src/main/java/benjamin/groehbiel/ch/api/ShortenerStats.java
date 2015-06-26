package benjamin.groehbiel.ch.api;

public class ShortenerStats {

    private Integer counter;

    protected ShortenerStats(Integer counter) {
        this.counter = counter;
    }

    public Integer getCounter() {
        return counter;
    }
}
