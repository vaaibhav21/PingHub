package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Friendship;
import com.example.entity.User;
import com.example.repository.FriendshipRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FriendshipService {

    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    private NotificationService notificationService;

    public void sendRequest(User requester, User addressee) {
        Optional<Friendship> existing = friendshipRepository.findFriendshipBetween(requester, addressee);
        if (existing.isPresent()) return;

        Friendship friendship = new Friendship();
        friendship.setRequester(requester);
        friendship.setAddressee(addressee);
        friendshipRepository.save(friendship);

        notificationService.createNotification(addressee, requester.getUsername() + " sent you a friend request.");
    }

    public void acceptRequest(Long friendshipId, User loggedInUser) {
        Friendship f = friendshipRepository.findById(friendshipId).orElse(null);
        if (f != null && f.getAddressee().getId().equals(loggedInUser.getId())) {
            f.setStatus("ACCEPTED");
            friendshipRepository.save(f);
            notificationService.createNotification(f.getRequester(), loggedInUser.getUsername() + " accepted your friend request.");
        }
    }

    public List<User> getAcceptedFriends(User user) {
        List<Friendship> friendships = friendshipRepository.findAcceptedFriendships(user);
        List<User> friends = new ArrayList<>();
        for (Friendship f : friendships) {
            if (f.getRequester().getId().equals(user.getId())) {
                friends.add(f.getAddressee());
            } else {
                friends.add(f.getRequester());
            }
        }
        return friends;
    }

    public List<Friendship> getPendingRequests(User user) {
        return friendshipRepository.findByAddresseeAndStatus(user, "PENDING");
    }
}
