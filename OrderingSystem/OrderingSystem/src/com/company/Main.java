package com.company;

import com.company.constants.Gender;
import com.company.model.Order;
import com.company.model.Restaurant;
import com.company.model.Review;
import com.company.model.User;
import com.company.service.OrderService;
import com.company.service.RestaurantService;
import com.company.service.UserService;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("Execution started");
        UserService userService = UserService.getInstance();
        RestaurantService restaurantService = RestaurantService.getInstance();
        OrderService orderService = OrderService.getInstance();

        // TODO: Change variable names
        User user1 = userService.registerUser(9898989891L,"Pralove",1L, Gender.MALE);
        User user2 = userService.registerUser(9898989892L,"Nitesh",2L,Gender.MALE);
        User user3 = userService.registerUser(9898989893L,"Vatsal",3L,Gender.MALE);

        User logIn = userService.login(user1.getPhone());
        Restaurant r1 = restaurantService.registerRestaurant("FoodCourt1","1,2,4","pizza",100,10);
        Restaurant r2 = restaurantService.registerRestaurant("FoodCourt2","1,4","burger",50,15);
        r1=restaurantService.updateQuantity("FoodCourt1",5);
        logIn=userService.login(user2.getPhone());
        Restaurant r3 = restaurantService.registerRestaurant("FoodCourt3","1,2,4","burger",50,15);

        logIn=userService.login(user3.getPhone());

        Restaurant r4 = restaurantService.registerRestaurant("FoodCourt4","1,3,4","burger",50,15);

        logIn=userService.login(user1.getPhone());
        restaurantService.showRestaurant("rating");
        Order order = orderService.placeOrder("FoodCourt1",5);
        Review review = restaurantService.rateRestaurant("FoodCourt1",5,"good");
        List<Order> orderList=orderService.listOrders();
        restaurantService.showRestaurant("rating");

        System.out.println("Execution ended");
    }
}
