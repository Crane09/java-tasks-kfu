package repo;

public class CacheStats {
    private volatile long lastRefreshTimeMillis;
    private volatile long refreshCount;
    private volatile int cacheSize;

    public long getLastRefreshTimeMillis() { return lastRefreshTimeMillis; }
    public long getRefreshCount() { return refreshCount; }
    public int getCacheSize() { return cacheSize; }

    void setLastRefreshTimeMillis(long v) { this.lastRefreshTimeMillis = v; }
    void incRefreshCount() { this.refreshCount++; }
    void setCacheSize(int v) { this.cacheSize = v; }

    public String toJson() {
        return "{"
                + "\"lastRefreshTimeMillis\":" + lastRefreshTimeMillis + ","
                + "\"refreshCount\":" + refreshCount + ","
                + "\"cacheSize\":" + cacheSize
                + "}";
    }
}