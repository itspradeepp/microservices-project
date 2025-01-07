package com.pradeep.user.service.services.impl;

import com.pradeep.user.service.entities.Hotel;
import com.pradeep.user.service.entities.Rating;
import com.pradeep.user.service.entities.User;
import com.pradeep.user.service.exceptions.ResourceNotFoundException;
import com.pradeep.user.service.external.services.HotelService;
import com.pradeep.user.service.repositories.UserRepository;
import com.pradeep.user.service.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HotelService hotelService;

    private Logger logger= LoggerFactory.getLogger(UserServiceImpl.class);
    @Override
    public User saveuser(User user) {
        //generate unique userid
        String randomUserId = UUID.randomUUID().toString();
        user.setUserId(randomUserId);
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    //get single user
    @Override
    public User getUser(String userId) {
        //get user from database with the help of user repository
        User user= userRepository.findById(userId).orElseThrow(() ->  new ResourceNotFoundException("User with given id is not found on server!! +"+userId));
        //fetch rating of the above user from the RATING SERVICE
        //http://localhost:8083/ratings/users/29a65440-7265-4915-a797-b84db169a5d0

        Rating[] ratingOfUser= restTemplate.getForObject("http://RATINGSERVICE/ratings/users/"+user.getUserId(), Rating[].class);
        logger.info("{}", ratingOfUser);

        List<Rating> ratings = Arrays.stream(ratingOfUser).toList();
        List<Rating> ratingList = ratings.stream().map(rating -> {
            //api call to hotel service to get the hotel
            //http://localhost:8082/hotels/62ca7c86-8f9d-4cf7-9683-8c1e42bb6684
            //ResponseEntity<Hotel> forEntity= restTemplate.getForEntity("http://HOTELSERVICE/hotels/"+rating.getHotelId(), Hotel.class);
            Hotel hotel=hotelService.getHotel(rating.getHotelId());
            //logger.info("response status code: {}", forEntity.getStatusCode());



            //set the hotel to rating
            rating.setHotel(hotel);
            //return the rating
            return rating;
        }).collect(Collectors.toList());

        user.setRatings(ratingList);

        return user;
    }
}
