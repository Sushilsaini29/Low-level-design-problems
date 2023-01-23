package com.company.service;

import com.company.data.UserDao;
import com.company.model.Restaurant;
import com.company.model.Review;

import java.util.List;

public class RestaurantService {
    private static RestaurantService restaurantService=null;
    private RestaurantService(){

    }
    public static RestaurantService getInstance(){
        if(restaurantService==null){
            restaurantService=new RestaurantService();
        }
        return restaurantService;
    }
    UserDao userDao=UserDao.getInstance();

    public Restaurant registerRestaurant(String name, String pinCodes, String item, int price, int quantity){
        if(price<=0 || quantity<0){
            System.out.println("Invalid value for mandatory fields");
            //BAD Practice
            //TODO: remove all null returns with Exception
            return null;
        }
        else if(name.isEmpty()){
            System.out.println("invalid value for name");
            return null;
        }
        return userDao.registerRestaurant(name, pinCodes, item, price, quantity);
    }

    public Review rateRestaurant(String name, Integer rating , String comment){
        if(rating==null || rating<=0 || rating>5 ){
            System.out.println("Invalid value for mandatory fields");
            return null;
        }
        return userDao.rateRestaurant(name,rating,comment);
    }

    public Restaurant updateQuantity(String name, int quantity){
        if(quantity<=0){
            System.out.println("Invalid value for mandatory fields");
            return null;
        }
        return userDao.updateQuantity(name,quantity);
    }
    public List<Restaurant> showRestaurant(String sortBy){
        //TODO: apply validation on sortBy string
        return userDao.showRestaurant(sortBy);
    }
}