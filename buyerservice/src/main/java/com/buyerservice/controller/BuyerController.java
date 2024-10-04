package com.buyerservice.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.buyerservice.entity.Orders;
import com.buyerservice.entity.Products;
import com.buyerservice.entity.Review;
import com.buyerservice.entity.ShoppingCart;
import com.buyerservice.entity.User;
import com.buyerservice.service.BuyerReviewService;

import com.buyerservice.service.BuyerServiceInterface;
import com.buyerservice.dto.CartProductDTO;
import com.buyerservice.dto.OrderDTO;
import com.buyerservice.dto.ProductDTO;
import com.buyerservice.dto.ProductDetailsResponse;
import com.buyerservice.dto.ReviewDTO;
import com.buyerservice.dto.ShoppingCartDTO;
import com.buyerservice.dto.UserDTO;

@RestController
@RequestMapping("/buyer")
public class BuyerController {
	
	@Autowired
	private BuyerServiceInterface buyerService;
	
	@Autowired
	private BuyerReviewService reviewService;
	
	/*
	 * @Autowired private BuyerSellerService sellerService;
	 */
	
	
//-----------------------------
		@GetMapping("/viewProducts")
		public ResponseEntity<Object> viewProducts() {
		    // Retrieve the list of Products from the service
		    List<Products> productList = buyerService.viewProducts();
		    System.out.println("prodcuts list"+ productList.size());

		    // Create a list to hold the mapped ProductDTOs
		    List<ProductDTO> productDTOs = new ArrayList<>();
		    // Map Products to ProductDTO
		    for (Products product : productList) {
		        ProductDTO productDTO = new ProductDTO();
		        productDTO.setProductId(product.getProductId());
		        productDTO.setProductName(product.getProductName());
		        productDTO.setProductDescription(product.getProductDescription());
		        productDTO.setImageUrl(product.getImageUrl());
		        productDTO.setProductCategory(product.getProductCategory());
		        productDTO.setPrice(product.getPrice());
		        productDTO.setDiscountPrice(product.getDiscountPrice());
		        productDTO.setQuantity(product.getQuantity());

		        productDTOs.add(productDTO); // Add the mapped DTO to the list
		    }

		    // Check if the list of DTOs is empty
		    if (productDTOs.isEmpty()) {
		        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		    } else {
		        return new ResponseEntity<>(productDTOs, HttpStatus.OK);
		    }
		}
//---------------------------
		@GetMapping("viewProductDetails/{productId}")
		public ResponseEntity<ProductDetailsResponse> viewProductDetails(@PathVariable("productId") Long productId) {
		    Optional<Products> optionalProduct = buyerService.viewProductDetails(productId);

		    // Check if the product exists
		    if (!optionalProduct.isPresent()) {
		        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		    }

		    Products product = optionalProduct.get();

		    // Fetch the seller information from the product object
		    User seller = product.getUser();
		    if (seller == null) {
		        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		    }

		    // Fetch reviews for the product
		    List<Review> reviews = reviewService.getReviewsByProductId(productId);

		    // Construct the response object
		    ProductDetailsResponse response = new ProductDetailsResponse();

		    // Map product to ProductDTO
		    ProductDTO productDTO = new ProductDTO();
		    productDTO.setProductId(product.getProductId());
		    productDTO.setProductName(product.getProductName());
		    productDTO.setProductDescription(product.getProductDescription());
		    productDTO.setImageUrl(product.getImageUrl());
		    productDTO.setProductCategory(product.getProductCategory());
		    productDTO.setPrice(product.getPrice());
		    productDTO.setDiscountPrice(product.getDiscountPrice());
		    productDTO.setQuantity(product.getQuantity());

		    // Map seller to UserDTO
		    UserDTO sellerDTO = new UserDTO();
		    sellerDTO.setUserId(seller.getUserId());
		    sellerDTO.setName(seller.getName());
		    sellerDTO.setAddress(seller.getAddress());

		    // Map reviews to ReviewDTOs
		    List<ReviewDTO> reviewDTOs = reviews.stream().map(review -> {
		        ReviewDTO reviewDTO = new ReviewDTO();
		        reviewDTO.setReviewId(review.getReviewId());
		        reviewDTO.setReviewText(review.getReviewText());
		        reviewDTO.setRating(review.getRating());

		        // Map user who wrote the review to UserDTO
		        User reviewUser = review.getUser();
		        if (reviewUser != null) {
		            UserDTO reviewUserDTO = new UserDTO();
		            reviewUserDTO.setUserId(reviewUser.getUserId());
		            reviewUserDTO.setName(reviewUser.getName());
		            reviewUserDTO.setAddress(reviewUser.getAddress());
		            reviewDTO.setUser(reviewUserDTO);
		        }

		        return reviewDTO;
		    }).collect(Collectors.toList());

		    // Set product and reviews in the response
		    response.setProduct(productDTO);
		    response.setSeller(sellerDTO);
		    response.setReviews(reviewDTOs);

		    // Return the response with status OK
		    return new ResponseEntity<>(response, HttpStatus.OK);
		}

		
	
//------------------
//		NOT WORKING
		  @GetMapping("/getCategories") public ResponseEntity<List<String>>
		  getCategories() { // Fetch the distinct categories from the database
		  List<String> categories = buyerService.getAllCategories();
		  
		  if (categories != null && !categories.isEmpty())
		  { 
			  return new ResponseEntity<>(categories, HttpStatus.OK); 
			  }
		  else 
			  {
				  return new ResponseEntity<>(HttpStatus.NO_CONTENT); 
			} 
		  }
//		 		
		
		@GetMapping("/browseProducts/{productCategory}")
		public ResponseEntity<Map<String, Object>> browseProductsByCategory(
		        @PathVariable("productCategory") String productCategory) {

		  //categories and products
		    Map<String, Object> response = new HashMap<>();

		    
		    List<String> categories = buyerService.getAllCategories();

	
		    response.put("categories", categories);

	
		    List<Products> products = buyerService.browseProductsByCategory(productCategory);

		  
		    if (products != null && !products.isEmpty()) {
		        response.put("products", products); 
		        return new ResponseEntity<>(response, HttpStatus.OK);
		    } else {
		        response.put("products", Collections.emptyList()); 
		        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); 
		    }
		}
//--------------------------
		@GetMapping("/cart/{userId}")
		public ResponseEntity<List<CartProductDTO>> viewCartProducts(@PathVariable("userId") Long userId) {
		    List<CartProductDTO> cartItems = buyerService.viewCartProducts(userId);
		    
		    if (cartItems.isEmpty()) {
		        return new ResponseEntity<>(HttpStatus.NO_CONTENT); 
		    }
		    
		    return new ResponseEntity<>(cartItems, HttpStatus.OK); 
		}

		
		
//----------------------------------------
		//completed by other team mates
		@PutMapping("/updateQuantity/{cartId}")
		public ResponseEntity<ShoppingCartDTO> updateQuantity(
		        @PathVariable("cartId") long cartId,
		        @RequestBody Map<String, Integer> requestBody) {
		    
		   
		    int quantity = requestBody.get("quantity");

		    
		    ShoppingCartDTO updatedCartDTO = buyerService.updateQuantityofCart(cartId, quantity);

		   
		    if (updatedCartDTO != null) {
		        return new ResponseEntity<>(updatedCartDTO, HttpStatus.OK);
		    } else {
		        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		    }
		}

//------------------------
		@GetMapping("/viewOrderHistory/{userId}")
		public ResponseEntity<List<OrderDTO>> viewOrderByHistory(@PathVariable("userId") long userId) {
		    List<OrderDTO> orderDTOs = buyerService.viewOrderByHistory(userId); // This now returns a List<OrderDTO>

		    if (orderDTOs != null && !orderDTOs.isEmpty()) {
		        return new ResponseEntity<>(orderDTOs, HttpStatus.OK); 
		    } else {
		        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		    }
		}


}