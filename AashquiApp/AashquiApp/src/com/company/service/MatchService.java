package com.company.service;

import com.company.data.Dao;
import com.company.exceptions.NoUserFoundException;
import com.company.model.ActionType;
import com.company.model.Location;
import com.company.model.Match;
import com.company.model.User;
import com.company.utils.IDgenerator;

import java.time.LocalDateTime;
import java.util.*;

public class MatchService {

    private static MatchService matchService = null;

    private Dao userDao=Dao.getInstance();

    private MatchService(){

    }
    private static Integer MAX_DISTANCE =  3;

    public static MatchService getInstance(){
        if(matchService==null){
            matchService=new MatchService();
        }
        return matchService;
    }

    public int distance(Location l1, Location l2) {
        return (int)Math.sqrt((l1.getX() - l2.getX())*(l1.getX() - l2.getX()) +
                (l1.getY() - l2.getY())*(l1.getY() - l2.getY()));
    }

    public List<User> findPotentialMatches(User user) throws NoUserFoundException {
        //firstly fetch all users
        List<User> potentialUser = fetchAllUsers(user);
        //Validation
        if(potentialUser==null || potentialUser.size()==0){
            throw new NoUserFoundException();
        }
        //sort them basis custom comparator logic and Print them.
        Collections.sort(potentialUser,new SortbyAge());
        System.out.println("Following are the potential matches for user "+user.getName()+"\n");
        int count=0;
        for (User user1:potentialUser){
            count++;
            System.out.println(count+": User Id: "+user1.getId()+": User Name: "+user1.getName()+" User Age: "+user1.getAge());
        }
        System.out.println("\n");
        return potentialUser;
    }


    public List<User> fetchAllUsers(User user){
        //if user is ACTIVE and Gender is Opposite, & distance is within range then its a potential user
        List<User> userList = new ArrayList<>();
        for(User potentialUser : userDao.getUserMap().values()){
            if(potentialUser.getActive() &&
                    !user.getGender().equals(potentialUser.getGender()) &&
                    distance(potentialUser.getLocation(),user.getLocation()) < MAX_DISTANCE ){
                userList.add(potentialUser);
            }
        }
        return userList;
    }

    //LIKE or IGNORE
    public Match actOnPotentialMatch(Integer id, Integer id2, ActionType actionType) throws NoUserFoundException {
        //If User does not exists
        if(!userDao.getUserMap().containsKey(id) || !userDao.getUserMap().containsKey(id2)){
            throw new NoUserFoundException();
        }
        //fetch the users
        User user1 = userDao.getUserMap().get(id);
        User user2 = userDao.getUserMap().get(id2);
        System.out.println("User "+user1.getName()+" has "+actionType+" User "+user2.getName()+"\n");

        //if u have LIKED a user, who already liked you, then its a match
        if(user1.getUsersWhoLikedYou().contains(user2) && actionType.equals(ActionType.Liked)){
            System.out.println("Match found between "+user1.getName()+" and "+user2.getName()+"\n");
            Match match = new Match();
            match.setId(IDgenerator.getId());
            match.setUser1(user1);
            match.setUser2(user2);
            match.setCreatedAt(new Date());
            user1.getMatches().add(match);
            user2.getMatches().add(match);
            //put in match map
            userDao.getMatchMap().put(match.getId(),match);
            return match;
        }
        //update user specific data for both users i.e. User1 and User2
        user1.getUserActionTypeMap().put(user2,actionType);
        if(actionType.equals(ActionType.Liked)) {
            user2.getUsersWhoLikedYou().add(user1);
        }

        return null;
    }

    public void showMatchesForUser(User user){
        //TODO: Improvise here

        //Just print from User Specific Data
        if(user.getMatches().size()==0){
            System.out.println("There are no matches for user "+user.getName()+" \n" );
            return;
        }

        System.out.println("List of all the matches for user "+user.getName()+" \n" );
        int count=1;
        for(Match match : user.getMatches()){

            System.out.println("Match number :"+count+" { "+match.getUser1().getName()+","+match.getUser2().getName()+"} ");
            count++;
        }
        System.out.println("\n");
    }
    public void showAllMatches(){
        //Just Print System Specific data
        if(userDao.getMatchMap().values().size()==0){
            System.out.println("There are no matches in system \n" );
            return;
        }
        System.out.println("List of all the matches present in system \n" );
        int count=1;
        for(Match match : userDao.getMatchMap().values()){

            System.out.println("Match number :"+count+" { "+match.getUser1().getName()+","+match.getUser2().getName()+"} ");
            count++;
        }
        System.out.println("\n");
    }

    class SortbyAge implements Comparator<User>
    {
        public int compare(User a, User b)
        {
            return a.getAge()-b.getAge();
        }
    }

}