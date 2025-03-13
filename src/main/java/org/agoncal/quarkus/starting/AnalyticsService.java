package org.agoncal.quarkus.starting;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class AnalyticsService {
    private final Map<Integer, Integer> bookBorrowCount = new HashMap<>();
    private final Map<String, Integer> userActivity = new HashMap<>();

    public void recordBorrowing(int bookId, String username) {
        bookBorrowCount.put(bookId, bookBorrowCount.getOrDefault(bookId, 0) + 1);
        userActivity.put(username, userActivity.getOrDefault(username, 0) + 1);

    }

    private final Map<Integer, Integer> bookReturnCount = new HashMap<>();
    private final Map<String, Integer> userReturns = new HashMap<>();
    private final Map<String, Long> lastReturnTime = new HashMap<>();
    public Map<Integer, Integer> getBookReturnCount() {
        return bookReturnCount;
    }
    public Map<String, Integer> getUserReturns() {
        return userReturns;
    }
    public Map<String, Long> getLastReturnTime() {
        return lastReturnTime;
    }

    public void recordReturning(int bookId, String username) {
        bookReturnCount.put(bookId, bookReturnCount.getOrDefault(bookId, 0) + 1);
        userReturns.put(username, userReturns.getOrDefault(username, 0) + 1);
        lastReturnTime.put(username, System.currentTimeMillis());
    }

    public List<Integer> getMostBorrowedBooks(int topN) {
        return bookBorrowCount.entrySet().stream().sorted((a, b) -> b.getValue().compareTo(a.getValue())).limit(topN).map(Map.Entry::getKey).collect(Collectors.toList());
    }

    public List<String> getMostActiveUsers(int topN) {
        return userActivity.entrySet().stream().sorted((a, b) -> b.getValue().compareTo(a.getValue())).limit(topN).map(Map.Entry::getKey).collect(Collectors.toList());
    }

}
