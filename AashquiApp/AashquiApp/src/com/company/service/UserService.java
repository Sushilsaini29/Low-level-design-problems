package com.company.service;

import com.company.data.Dao;
import com.company.model.Gender;
import com.company.model.Location;
import com.company.model.Match;
import com.company.model.User;

public class UserService {
    private static UserService userService = null;

    private UserService(){

    }
    public static UserService getInstance(){
        if(userService==null){
            userService=new UserService();
        }
        return userService;
    }

    private Dao userDao = Dao.getInstance();

    public User registerUser(int age, String name, String no, Gender gender, int x, int y){
        //something is missing
        return userDao.createUser(age,name,no,gender,new Location(x,y));
    }

    public User deactivateAccount(User user){


        //set active to false
        user.setActive(false);

        // clear all actions, as data is not required now
        user.getUserActionTypeMap().clear();

        //TODO: Improvise here
        //remove user from the match map data
        for(Match match : user.getMatches()){
            if(match.getUser1().equals(user) || match.getUser2().equals(user)){
                userDao.getMatchMap().remove(match.getId());
            }
        }

        //user k match ki list clear kardo
        user.getMatches().clear();

        //if someone has LIKED or IGNORED, remove that from data too.
        for(User user1: user.getUsersWhoLikedYou()){
            user1.getUserActionTypeMap().remove(user);
        }

        //Remove from overall Match map too
        for(Match match : userDao.getMatchMap().values()){
            if(match.getUser1().equals(user) || match.getUser2().equals(user)){
                userDao.getMatchMap().remove(match.getId());
            }
        }
        return user;
    }

}