package com.buyerservice.service;

import java.util.List;
import com.buyerservice.entity.Review;

public interface BuyerReviewService {

	List<Review> getReviewsByProductId(Long productId);

}
