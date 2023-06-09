package com.driver.services;


import com.driver.EntryDto.SubscriptionEntryDto;
import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.repository.SubscriptionRepository;
import com.driver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SubscriptionService {

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    UserRepository userRepository;

    public Integer buySubscription(SubscriptionEntryDto subscriptionEntryDto){

        //Save The subscription Object into the Db and return the total Amount that user has to pay
        User user=userRepository.findById(subscriptionEntryDto.getUserId()).get();
        Subscription subscription=new Subscription();
        subscription.setSubscriptionType(subscriptionEntryDto.getSubscriptionType());
        subscription.setNoOfScreensSubscribed(subscriptionEntryDto.getNoOfScreensRequired());
        subscription.setStartSubscriptionDate(new Date());

        //setting the amount on the basis of plan.
        int amount=0;
        int numberOfScreens=subscriptionEntryDto.getNoOfScreensRequired();
        if(subscriptionEntryDto.getSubscriptionType().equals(SubscriptionType.BASIC))
        {
            amount=500+(200*numberOfScreens);
        }
        if(subscriptionEntryDto.getSubscriptionType().equals(SubscriptionType.PRO))
        {
            amount=800+(250*numberOfScreens);
        }
        else
        {
            amount=1000+(350*numberOfScreens);
        }
        subscription.setTotalAmountPaid(amount);
        subscription.setUser(user);

        user.setSubscription(subscription);

        subscriptionRepository.save(subscription);

        return subscription.getTotalAmountPaid();
    }

    public Integer upgradeSubscription(Integer userId)throws Exception{

        //If you are already at an ElITE subscription : then throw Exception ("Already the best Subscription")
        //In all other cases just try to upgrade the subscription and tell the difference of price that user has to pay
        //update the subscription in the repository
        User user=userRepository.findById(userId).get();
        Subscription subscription=user.getSubscription();
        if(subscription.getSubscriptionType()==SubscriptionType.ELITE)
        {
            throw new Exception("Already the best Subscription");
        }
        int diffAmt=0;
        //int numberOfScreens=subscription.getNoOfScreensSubscribed();
        if(subscription.getSubscriptionType()==SubscriptionType.BASIC)
        {
            subscription.setSubscriptionType(SubscriptionType.PRO);
            diffAmt=(800+(250*subscription.getNoOfScreensSubscribed()))-subscription.getTotalAmountPaid();
            subscription.setTotalAmountPaid(800+(250*subscription.getNoOfScreensSubscribed()));
            subscription.setStartSubscriptionDate(new Date());
        }
        if(subscription.getSubscriptionType()==SubscriptionType.PRO)
        {
            subscription.setSubscriptionType(SubscriptionType.ELITE);
            diffAmt=(1000+(350*subscription.getNoOfScreensSubscribed()))-subscription.getTotalAmountPaid();
            subscription.setTotalAmountPaid(1000+(350*subscription.getNoOfScreensSubscribed()));
            subscription.setStartSubscriptionDate(new Date());
        }
        //subscription.setUser(user);

        //user.setSubscription(subscription);

        subscriptionRepository.save(subscription);

        return diffAmt;
    }

    public Integer calculateTotalRevenueOfHotstar(){

        //We need to find out total Revenue of hotstar : from all the subscriptions combined
        //Hint is to use findAll function from the SubscriptionDb
       List<Subscription>subscriptionList=subscriptionRepository.findAll();
       int totalRevenue=0;
       for(Subscription s:subscriptionList)
       {
           totalRevenue+=s.getTotalAmountPaid();
       }
        return totalRevenue;
    }

}
