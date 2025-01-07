package com.pradeep.user.service.services;

import com.pradeep.user.service.entities.User;

import java.util.List;

public interface UserService {

    //user operations

    //create
    User saveuser(User user);

    //get all user
    List<User> getAllUser();

    //get single user of  given userId
    User getUser(String userId);

    //TODO: delete
    //TODO:update
}
