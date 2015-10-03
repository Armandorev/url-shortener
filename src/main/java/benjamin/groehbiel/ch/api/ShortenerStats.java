package benjamin.groehbiel.ch.api;

public class ShortenerStats {

    private Long shortenedCount;
    private Long remainingCount;

    public ShortenerStats() {
    }

    public ShortenerStats(Long counter, Long remainingWords) {
        this.setShortenedCount(counter);
        this.setRemainingCount(remainingWords);
    }

    public Long getShortenedCount() {
        return shortenedCount;
    }

    public Long getRemainingCount() {
        return remainingCount;
    }

    public void setShortenedCount(Long shortenedCount) {
        this.shortenedCount = shortenedCount;
    }

    public void setRemainingCount(Long remainingCount) {
        this.remainingCount = remainingCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ShortenerStats that = (ShortenerStats) o;

        if (shortenedCount != null ? !shortenedCount.equals(that.shortenedCount) : that.shortenedCount != null)
            return false;
        return !(remainingCount != null ? !remainingCount.equals(that.remainingCount) : that.remainingCount != null);
    }

}
