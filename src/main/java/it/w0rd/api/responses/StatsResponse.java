package it.w0rd.api.responses;

public class StatsResponse {

    private Long shortenedCount;
    private Long remainingCount;

    public StatsResponse() {
    }

    public StatsResponse(Long counter, Long remainingWords) {
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

        StatsResponse that = (StatsResponse) o;

        if (shortenedCount != null ? !shortenedCount.equals(that.shortenedCount) : that.shortenedCount != null)
            return false;
        return !(remainingCount != null ? !remainingCount.equals(that.remainingCount) : that.remainingCount != null);
    }

}
