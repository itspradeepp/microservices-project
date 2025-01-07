package com.pradeep.hotel.services;

import com.pradeep.hotel.HotelServiceApplication;
import com.pradeep.hotel.entities.Hotel;

import java.util.List;

public interface HotelService {


    //create
    Hotel create(Hotel hotel);

    //getall
    List<Hotel> getAll();


    //getsingle
    Hotel get(String id);

}
