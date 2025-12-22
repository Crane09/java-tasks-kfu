package repo;

import domain.User;
import infra.UserApiClient;
import infra.UserStorage;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class UserRepository implements AutoCloseable {
    private final UserApiClient apiClient;
    private final UserStorage storage;

    private final ConcurrentHashMap<Integer, User> cache = new ConcurrentHashMap<>();
    private final ReentrantReadWriteLock rw = new ReentrantReadWriteLock();

    private final AtomicBoolean isRefreshing = new AtomicBoolean(false);
    private final ExecutorService refreshExecutor;

    private final CacheStats stats = new CacheStats();

    public UserRepository(UserApiClient apiClient, UserStorage storage) {
        this.apiClient = apiClient;
        this.storage = storage;

        this.refreshExecutor = Executors.newSingleThreadExecutor(r -> {
            Thread t = new Thread(r, "user-refresh-thread");
            t.setDaemon(true);
            return t;
        });

        try {
            List<User> fromFile = storage.load();
            putAllToCache(fromFile);
            stats.setCacheSize(cache.size());
        } catch (IOException e) {
            System.out.println("Warning: failed to load from file: " + e.getMessage());
        }
    }

    public List<User> getAll() {
        rw.readLock().lock();
        try {
            return new ArrayList<>(cache.values());
        } finally {
            rw.readLock().unlock();
        }
    }

    public User getById(int id) {

        return cache.get(id);
    }

    /**
     * Запускает обновление в отдельном потоке.
     * @return true если обновление реально запущено, false если уже идёт
     */
    public boolean refresh() {
        if (!isRefreshing.compareAndSet(false, true)) {
            return false;
        }

        refreshExecutor.submit(() -> {
            try {
                List<User> users = apiClient.fetchUsers();


                rw.writeLock().lock();
                try {
                    cache.clear();
                    for (User u : users) cache.put(u.getId(), u);
                    stats.setCacheSize(cache.size());
                } finally {
                    rw.writeLock().unlock();
                }

                storage.save(users);

                stats.incRefreshCount();
                stats.setLastRefreshTimeMillis(System.currentTimeMillis());
            } catch (Exception e) {
                System.out.println("Refresh error: " + e.getMessage());
            } finally {
                isRefreshing.set(false);
            }
        });

        return true;
    }

    public boolean isRefreshingNow() {
        return isRefreshing.get();
    }

    public CacheStats getStats() {
        return stats;
    }

    private void putAllToCache(List<User> users) {
        rw.writeLock().lock();
        try {
            cache.clear();
            for (User u : users) cache.put(u.getId(), u);
        } finally {
            rw.writeLock().unlock();
        }
    }

    @Override
    public void close() {
        refreshExecutor.shutdown();
        try {
            refreshExecutor.awaitTermination(2, TimeUnit.SECONDS);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }
}