package com.pradeep.user.service.controllers;

import com.pradeep.user.service.entities.User;
import com.pradeep.user.service.services.UserService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/Users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    //create
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {

        User user1= userService.saveuser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user1);

    }
    int retryCount=1;
    //single user get
    @GetMapping("/{userId}")
//    @CircuitBreaker(name="ratingHotelBreaker", fallbackMethod = "ratingHotelFallback")
//    @Retry(name="ratingHotelService", fallbackMethod = "ratingHotelFallback")

    @RateLimiter(name="userRateLimiter", fallbackMethod = "ratingHotelFallback")
    public ResponseEntity<User> getSingleUser(@PathVariable String userId) {
        logger.info("Get Single User handler: UserController");
        logger.info("Retry count: {}", retryCount);
        retryCount++;
        User user =  userService.getUser(userId);
        return ResponseEntity.ok(user);

    }

    //creating fallback method for circuitbreaker

    public ResponseEntity<User> ratingHotelFallback(String userId, Exception ex) {
//        logger.info("Fallback is excuted because service id down:", ex.getMessage());
        User user = User.builder()
                .email("dummy@gmail.com")
                .name("Dummy")
                .about("This user is created because some service is down")
                .userId("141234")
                .build();
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    //all user get
    @GetMapping
    public ResponseEntity<List<User>> getAllUser() {
        List<User> allUser=userService.getAllUser();
        return ResponseEntity.ok(allUser);
    }

}