package com.buyerservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.buyerservice.dao.BuyerReviewRepositry;
import com.buyerservice.entity.Review;


@Service
@Transactional
public class BuyerReviewServiceImpl implements BuyerReviewService {
	
	@Autowired
	private BuyerReviewRepositry reviewrepo;

	@Override
    public List<Review> getReviewsByProductId(Long productId) {
		 List<Review> reviews = reviewrepo.findByProductId(productId);
		    
		    
		    if (reviews.isEmpty()) {
		       
		        System.out.println("No reviews found for product ID: " + productId);
		    } else {
		        System.out.println("Found " + reviews.size() + " reviews for product ID: " + productId);
		    }
		    
		    return reviews; 
	}
}
