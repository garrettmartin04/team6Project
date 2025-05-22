package com.example;

import java.util.List;
import java.util.Optional;

public interface FriendRequestRepository {
    void save(FriendRequest request);
    Optional<FriendRequest> findByRequestId(String requestId);
    List<FriendRequest> findByReceiver(User receiver);
    List<FriendRequest> findBySender(User sender);
}

