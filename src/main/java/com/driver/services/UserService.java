package com.driver.services;


import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.model.WebSeries;
import com.driver.repository.UserRepository;
import com.driver.repository.WebSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    WebSeriesRepository webSeriesRepository;


    public Integer addUser(User user){

        //Jut simply add the user to the Db and return the userId returned by the repository
        return userRepository.save(user).getId();
    }

    public Integer getAvailableCountOfWebSeriesViewable(Integer userId){

        //Return the count of all webSeries that a user can watch based on his ageLimit and subscriptionType
        //Hint: Take out all the Webseries from the WebRepository
        User user =userRepository.findById(userId).get();
        int count=0;
        int userAge=user.getAge();
        List<WebSeries>webSeriesList=webSeriesRepository.findAll();
        int countBasic=0;
        int countPro=0;
        for(WebSeries w:webSeriesList) {
            if (w.getSubscriptionType().equals(SubscriptionType.BASIC)) {
                countBasic++;
            }
            if (w.getSubscriptionType().equals(SubscriptionType.PRO)) {
                countBasic++;
                countPro++;
            }
        }
        if(user.getSubscription().getSubscriptionType().equals(SubscriptionType.BASIC))
        {
            count+=countBasic;
        }
        if(user.getSubscription().getSubscriptionType().equals(SubscriptionType.PRO) || user.getSubscription().getSubscriptionType().equals(SubscriptionType.ELITE))
        {
            count+=countBasic+countPro;
        }
        return count;
    }


}
