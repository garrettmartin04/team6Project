package com.example;

public class FriendService {
    private UserRepository userRepository;
    private FriendRequestRepository friendRequestRepository;

    public FriendService(UserRepository userRepository, FriendRequestRepository friendRequestRepository) {
        this.userRepository = userRepository;
        this.friendRequestRepository = friendRequestRepository;
    }

    public void sendFriendRequest(String senderId, String receiverUsername) throws Exception {
        User sender = userRepository.findByUserID(senderId)
                .orElseThrow(() -> new Exception("Sender not found"));
        User receiver = userRepository.findByUsername(receiverUsername)
                .orElseThrow(() -> new Exception("Receiver not found"));

        FriendRequest request = new FriendRequest(sender, receiver);
        friendRequestRepository.save(request);

        sender.sendFriendRequest(request);
        receiver.receiveFriendRequest(request);

        userRepository.save(sender);
        userRepository.save(receiver);
    }

    public void acceptFriendRequest(String receiverId, String requestId) throws Exception {
        User receiver = userRepository.findByUserID(receiverId)
                .orElseThrow(() -> new Exception("Receiver not found"));
        FriendRequest request = friendRequestRepository.findByRequestId(requestId)
                .orElseThrow(() -> new Exception("Friend request not found"));

        if (!request.getReceiver().equals(receiver)) {
            throw new Exception("Unauthorized action");
        }

        request.accept();
        receiver.removeReceivedRequest(request);
        receiver.addFriend(request.getSender());

        User sender = request.getSender();
        sender.removeSentRequest(request);
        sender.addFriend(receiver);

        friendRequestRepository.save(request);
        userRepository.save(receiver);
        userRepository.save(sender);
    }

    public void deleteFriend(String userId, String friendId) throws Exception {
        User user = userRepository.findByUserID(userId)
                .orElseThrow(() -> new Exception("User not found"));
        User friend = userRepository.findByUserID(friendId)
                .orElseThrow(() -> new Exception("Friend not found"));

        user.removeFriend(friend);
        friend.removeFriend(user);

        userRepository.save(user);
        userRepository.save(friend);
    }
}

