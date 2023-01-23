package com.company.data;

import com.company.constants.Gender;
import com.company.model.Order;
import com.company.model.Restaurant;
import com.company.model.Review;
import com.company.model.User;
import com.company.utils.IDGenerator;

import java.util.*;

public class UserDao {
    private static UserDao userDao = null;

    private UserDao() {

    }

    public static UserDao getInstance() {
        if (userDao == null) {
            userDao = new UserDao();
        }
        return userDao;
    }

    private HashMap<Integer, User> userHashMap = new HashMap<>();
    private HashMap<Long, Integer> phoneNumberMap = new HashMap<>();
    private HashMap<String, Restaurant> restaurantNameMap = new HashMap<>();

    private User loggedInUser = null; //keeping logged in user, initially no user is there

    public User registerUser(Long phone, String name, Long pinCode, Gender gender) {

        if (phoneNumberMap.containsKey(phone)) {
            User user = userHashMap.get(phoneNumberMap.get(phone));
            System.out.println("User already exist with phone number " + phone + " with user id : " + user.getId()+"\n");
            return user;
        }
        //if a new User
        User user = new User(IDGenerator.getId(), phone, name, pinCode, gender);
        phoneNumberMap.put(phone, user.getId());
        userHashMap.put(user.getId(),user);
        System.out.println("Successfully created user with user id:" + user.getId()+"\n");

        return user;
    }

    public User login(Long  phone) {
        if(!phoneNumberMap.containsKey(phone)){
            System.out.println("No user exists with phone " + phone);
            return null;
            //TODO: Exception throw with message
        }
        //if the User is a registered user

        User user = userHashMap.get(phoneNumberMap.get(phone));

        loggedInUser = user;
        System.out.println("Successfully logged in  user with user id" + user.getId()+"\n");

        return user;
    }

    public Restaurant registerRestaurant(String name, String pinCodes, String item, int price, int quantity) {
        if (loggedInUser == null) {
            System.out.println("No logged in user found,request can't be served\n");
            return null;
        }
        if (restaurantNameMap.containsKey(name)) {
            System.out.println("Restaurant already exist with given name, please give unique name\n");
            return null;
        }
        List<String> pinCodeList = Arrays.asList(pinCodes.split(","));
        List<Long> pins = new ArrayList<>();
        if (!pinCodes.isEmpty()) {

            for (String s : pinCodeList) {
                if (!s.chars().allMatch(Character::isDigit)) {
                    System.out.println("Invalid pincode provided\n");
                    return null;
                }
                pins.add(Long.parseLong(s));
            }
        }
        //TODO: check the feasibility of Builder Pattern here
        Restaurant restaurant = new Restaurant();
        restaurant.setId(IDGenerator.getId());
        restaurant.setName(name);
        restaurant.setItem(item);
        restaurant.setQuantity(quantity);
        restaurant.setPrice(price);
        restaurant.setServiceablePincode(pins);
        restaurant.setCreatedBy(loggedInUser.getId());
        //restaurant map update krdiya
        restaurantNameMap.put(name,restaurant);
        // adding restaurant in user's list
        // yaha kuch gadbad lag rhi h
        loggedInUser.getRestaurants().add(restaurant);
        System.out.println("Successfully registered restaurant  id" + restaurant.getId()+"\n");

        return restaurant;
    }

    //TODO: please correct the variable names with intuitive ones
    public Review rateRestaurant(String name, Integer rating, String comment) {
        Restaurant restaurant = restaurantNameMap.get(name);
        if (restaurant == null) {
            System.out.println("No restaurant found with given name " + name);
            return null;
        }

        Review review = new Review();
        review.setId(IDGenerator.getId());
        review.setComment(comment);
        review.setScore(rating);

        //if first review
        if (restaurant.getReviews() == null || restaurant.getReviews().size() == 0) {
            restaurant.setRating(Float.valueOf(rating));
        } else {
            //updating overall rating
            float currentScore =
                    (restaurant.getRating() * restaurant.getReviews().size() + rating) / (restaurant.getReviews().size() + 1);
            restaurant.setRating(currentScore);
        }
        restaurant.getReviews().add(review);
        return review;
    }

    public Restaurant updateQuantity(String name, int quantity) {
        Restaurant restaurant = restaurantNameMap.get(name);
        if (restaurant == null) {
            System.out.println("No restaurant found with given name " + name);
            return null;
        }

        restaurant.setQuantity(restaurant.getQuantity() + quantity);
        return restaurant;
    }

    public List<Restaurant> showRestaurant(String sortBy) {
        //not a good name
        List<Restaurant> r= loggedInUser.getRestaurants();
        List<Restaurant> restaurants= new ArrayList<>();
        for (Restaurant restaurant: r){
            if(restaurant.getServiceablePincode().contains(loggedInUser.getPinCode())
                    && restaurant.getQuantity()>0){
                restaurants.add(restaurant);
            }
        }
        if(sortBy.equalsIgnoreCase("rating")){
            Collections.sort(restaurants,new SortByRating());
            for(Restaurant restaurant: restaurants){
                System.out.println("Restaurant id :"+restaurant.getId()+ ": name-> "+
                        restaurant.getName()+": price-> "+restaurant.getPrice()+": rating -> "+restaurant.getRating());
            }
            return restaurants;
        }
        //by default price
        Collections.sort(restaurants,new SortByPrice());
        for(Restaurant restaurant: restaurants){
            System.out.println("Restaurant id :"+restaurant.getId()+ ": name-> "+
                    restaurant.getName()+": price-> "+restaurant.getPrice()+": rating -> "+restaurant.getRating());
        }
        return restaurants;

    }

    public Order placeOrder(String name, Integer quantity) {
        Restaurant restaurant = restaurantNameMap.get(name);
        if (restaurant == null) {
            System.out.println("No restaurant found with given name " + name);
            return null;
        }

        if (restaurant.getQuantity()==0) {
            System.out.println("restaurant is out of stock, please try later");
            return null;
        }

        if(restaurant.getQuantity()<quantity){
            System.out.println("Restaurant has only" + restaurant.getQuantity()+" Items, please check your cart");
            return null;
        }

        Order order =new Order();
        order.setId(IDGenerator.getId());
        order.setItem(restaurant.getItem());
        order.setQuantity(quantity);
        order.setUserId(loggedInUser.getId());
        order.setTimestamp(System.currentTimeMillis());
        order.setCost((long) (quantity*restaurant.getPrice()));
        //update quantity
        restaurant.setQuantity(restaurant.getQuantity()-quantity);
        //add to user order history
        loggedInUser.getOrders().add(order);
        return order;
    }

    public List<Order> listOrders() {
        for(Order order: loggedInUser.getOrders()){
            System.out.println("Order id :"+order.getId()+" item:"+
                    order.getItem()+" quantity:"+order.getQuantity()+" cost:"+order.getCost());
        }
        return loggedInUser.getOrders();
    }




    class SortByRating implements Comparator<Restaurant> {
        @Override
        public int compare(Restaurant o1, Restaurant o2) {
            if(o1.getRating()==null || o2.getRating()==null) return 0;
            if(o1.getRating()>o2.getRating()) {
                return 1;
            }
            else if(o1.getRating()==o2.getRating()){
                return 0;
            }
            else {
                return -1;
            }
        }
    }
    class SortByPrice implements Comparator<Restaurant>{
        @Override
        public int compare(Restaurant o1, Restaurant o2) {
            return o1.getPrice()-o2.getPrice();
        }
    }
}