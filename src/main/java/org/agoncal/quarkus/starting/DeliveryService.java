package org.agoncal.quarkus.starting;

import jakarta.inject.Singleton;

import java.util.HashSet;
import java.util.Set;

@Singleton
public class DeliveryService {
    private final Set<Integer> deliveredBooks = new HashSet<>();

    public boolean processDelivery(int bookId, String username, String address) {
        if (deliveredBooks.contains((bookId))) {
            return false;
        }
        System.out.println("Delivering book ID " + bookId  + " to " + username  + " at " +  address);
        deliveredBooks.add(bookId);
        return true;
    }
}
