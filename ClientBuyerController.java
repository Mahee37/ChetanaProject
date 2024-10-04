package com.revbookstoreclientapp.controller;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.revbookstoreclientapp.dto.CartProductDTO;
import com.revbookstoreclientapp.dto.OrderDTO;
import com.revbookstoreclientapp.dto.Orders;
import com.revbookstoreclientapp.dto.ProductDTO;
import com.revbookstoreclientapp.dto.ProductDetailsResponse;
import com.revbookstoreclientapp.dto.ProductDetailsViewModel;
import com.revbookstoreclientapp.dto.Products;
import com.revbookstoreclientapp.dto.Review;
import com.revbookstoreclientapp.dto.ShoppingCart;
import com.revbookstoreclientapp.dto.ShoppingCartDTO;
import com.revbookstoreclientapp.dto.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class ClientBuyerController {


		@Autowired
		private DiscoveryClient discoveryClient;
		
		
		
		@Autowired
		private RestTemplate restTemplate;
//------------------------------
		@GetMapping("/BuyerInventory")
		public ModelAndView viewProducts() {
		    ModelAndView mv = new ModelAndView();
		    
		    // Fetch BUYERSERVICE instances
		    List<ServiceInstance> instances = discoveryClient.getInstances("BUYERSERVICE");
		    if (instances.isEmpty()) {
		        throw new RuntimeException("No instances of BUYERSERVICE available");
		    }

		    ServiceInstance serviceInstance = instances.get(0);
		    String baseUrl = serviceInstance.getUri().toString() + "/buyer/viewProducts";
		    System.out.println("Base URL: " + baseUrl);

		    ResponseEntity<List<Products>> responseEntity;
		    
		    try {
		        responseEntity = restTemplate.exchange(baseUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<Products>>() {});
		        List<Products> products = responseEntity.getBody();

		        List<ProductDTO> productDTOs = products.stream()
		            .map(product -> {
		                ProductDTO productDTO = new ProductDTO();
		                productDTO.setProductId(product.getProductId());
		                productDTO.setProductName(product.getProductName());
		                productDTO.setProductDescription(product.getProductDescription());
		                productDTO.setImageUrl(product.getImageUrl());
		                productDTO.setProductCategory(product.getProductCategory());
		                productDTO.setPrice(product.getPrice());
		                productDTO.setDiscountPrice(product.getDiscountPrice());
		                productDTO.setQuantity(product.getQuantity());
		                return productDTO;
		            })
		            .collect(Collectors.toList());

		        mv.addObject("productresult", productDTOs);
		        
		        System.out.println("Product DTOs size: " + productDTOs.size());
		        
		    } catch (HttpClientErrorException e) {
		        System.err.println("Error calling BUYERSERVICE: " + e.getMessage());
		        return new ModelAndView("error"); // Redirect to an error JSP
		    }

		    mv.setViewName("products");
		    return mv;
		}

//---------------
		@GetMapping("/BuyerProductDetails")
		public ModelAndView viewProductDetails(@RequestParam("id") Long productId) {
		    ModelAndView mv = new ModelAndView();

		    List<ServiceInstance> instances = discoveryClient.getInstances("BUYERSERVICE");
		    if (instances.isEmpty()) {
		        mv.addObject("errorMessage", "Service unavailable.");
		        mv.setViewName("error"); 
		        return mv;
		    }

		    ServiceInstance serviceInstance = instances.get(0);
		    String baseUrl = serviceInstance.getUri().toString() + "/buyer/viewProductDetails/" + productId;
		    ProductDetailsResponse response = restTemplate.getForObject(baseUrl, ProductDetailsResponse.class);

		    if (response != null) {
		        ProductDetailsViewModel viewModel = new ProductDetailsViewModel();
		        viewModel.setProduct(response.getProduct());
		        viewModel.setSeller(response.getSeller());
		        viewModel.setReviews(response.getReviews());
		        mv.addObject("productdetails", viewModel);
		    } else {
		        mv.addObject("errorMessage", "Product not found.");
		    }

		    mv.setViewName("productinfo");
		    return mv;
		}



//--------------------------------
		//NOT WORKING
		// other imports
		@GetMapping("/browseProducts")
		public ModelAndView browseProducts(HttpServletRequest request, 
		                                    HttpServletResponse response,
		                                    @RequestParam(value = "category", required = false) String productCategory) {
		    ModelAndView mv = new ModelAndView();
		    HttpSession session = request.getSession();

		    // Get instances of BUYERSERVICE
		    List<ServiceInstance> instances = discoveryClient.getInstances("BUYERSERVICE");
		    ServiceInstance serviceInstance = instances.get(0);
		    String baseUrl = serviceInstance.getUri().toString();

		    RestTemplate restTemplate = new RestTemplate();

		    // Fetch the list of product categories from BUYERSERVICE
		    String categoryUrl = baseUrl + "/buyer/getCategories";
		    List<String> categoriesList = restTemplate.getForObject(categoryUrl, List.class);
		    
		    // Log the fetched categories
		    System.out.println("Fetched Categories: " + categoriesList);

		    // Prepare the products list
		    List<Products> productsList = new ArrayList<>();
		    
		    // Fetch products if a category is selected
		    if (productCategory != null && !productCategory.isEmpty()) {
		        String productUrl = baseUrl + "/buyer/browseProducts/" + productCategory;

		        // Use ParameterizedTypeReference to fetch the response
		        ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(
		            productUrl, 
		            HttpMethod.GET, 
		            null, 
		            new ParameterizedTypeReference<Map<String, Object>>() {}
		        );

		        if (responseEntity.getStatusCode() == HttpStatus.OK) {
		            // If the response is successful, get the products list
		            Map<String, Object> responseBody = responseEntity.getBody();
		            productsList = (List<Products>) responseBody.get("products");
		        } else {
		            // Handle case where products are not found
		            System.out.println("No products found for category '" + productCategory + "'");
		        }
		        
		        // Log the fetched products
		        System.out.println("Fetched Products for category '" + productCategory + "': " + productsList);
		    }

		    // Log the selected category
		    System.out.println("Selected Category: " + productCategory);

		    // Add the categories and products (if any) to the model
		    mv.addObject("categories", categoriesList);
		    mv.addObject("productresult", productsList); // Ensure this matches your JSP reference
		    mv.addObject("selectedCategory", productCategory);
		    mv.setViewName("products");

		    return mv;
		}
//---------------------------------
		@GetMapping("/BuyerCart")
		public ModelAndView viewCartProducts() {
		    ModelAndView mv = new ModelAndView();
		    
		    Long userId = 1L; // Replace with session handling if necessary

		    List<ServiceInstance> instances = discoveryClient.getInstances("BUYERSERVICE");
		    
		    if (instances.isEmpty()) {
		        throw new RuntimeException("No instances of BUYERSERVICE available");
		    }

		    ServiceInstance serviceInstance = instances.get(0);
		    String baseUrl = serviceInstance.getUri().toString() + "/buyer/cart/" + userId; 
		    System.out.println("Base URL: " + baseUrl);

		    ResponseEntity<List<CartProductDTO>> responseEntity;

		    try {
		        responseEntity = restTemplate.exchange(baseUrl, HttpMethod.GET, null, 
		            new ParameterizedTypeReference<List<CartProductDTO>>() {});

		        List<CartProductDTO> cartItems = responseEntity.getBody();
		        if (cartItems != null) {
		            List<CartProductDTO> cartProductDTOs = cartItems.stream()
		                .map(cartItem -> new CartProductDTO(
		                    cartItem.getCartId(),          // Cart ID
		                    cartItem.getProductId(),       // Product ID 
		                    cartItem.getQuantity(),        // Quantity
		                    cartItem.getTotalPrice(),      // Total Price
		                    cartItem.getProductDescription(), // Product Description
		                    cartItem.getProductName()      // Product Name
		                ))
		                .collect(Collectors.toList());

		            mv.addObject("cartItems", cartProductDTOs);
		            System.out.println("Cart Items size: " + cartProductDTOs.size());
		        } else {
		            mv.addObject("message", "No items found in the cart.");
		        }
		    } catch (HttpClientErrorException e) {
		        System.err.println("Error calling BUYERSERVICE: " + e.getMessage());
		        mv.setViewName("error");
		        mv.addObject("message", "Error fetching cart items.");
		        return mv;
		    } catch (Exception e) {
		        System.err.println("Unexpected error: " + e.getMessage());
		        mv.setViewName("error");
		        mv.addObject("message", "An unexpected error occurred.");
		        return mv;
		    }

		    mv.setViewName("cart"); 
		    return mv;
		}


//-------------------------------------------------
		
		// This method updates the quantity directly with a request parameter.
		@PostMapping("/updateQuantity")
		public ModelAndView updateQuantity(HttpServletRequest request, HttpServletResponse response,
		                                   @RequestParam("cartId") long cartId,
		                                   @RequestParam("quantity") long quantity) {
		    ModelAndView mv = new ModelAndView();
		    HttpSession session = request.getSession();

		    try {
		        // Discover the service instance for BUYERSERVICE
		        List<ServiceInstance> instances = discoveryClient.getInstances("BUYERSERVICE");
		        if (instances.isEmpty()) {
		            mv.setViewName("cart");
		            mv.addObject("errorMessage", "Service not available");
		            return mv;
		        }

		        ServiceInstance serviceInstance = instances.get(0);
		        String baseUrl = serviceInstance.getUri().toString();

		        // Prepare the URL for the PUT request
		        String url = baseUrl + "/buyer/updateQuantity/" + cartId;
		        RestTemplate restTemplate = new RestTemplate();
		        HttpHeaders headers = new HttpHeaders();
		        headers.setContentType(MediaType.APPLICATION_JSON);

		        // Prepare the ShoppingCartDTO with the updated quantity
		        ShoppingCartDTO shoppingCartDTO = new ShoppingCartDTO();
		        shoppingCartDTO.setQuantity(quantity);
		        HttpEntity<ShoppingCartDTO> entity = new HttpEntity<>(shoppingCartDTO, headers);
		        
		        // Call the BUYERSERVICE to update the quantity
		        ResponseEntity<ShoppingCartDTO> updatedCartResponse = restTemplate.exchange(url, HttpMethod.PUT, entity, ShoppingCartDTO.class);
		        
		        if (updatedCartResponse.getStatusCode() == HttpStatus.OK) {
		            ShoppingCartDTO updatedCartDTO = updatedCartResponse.getBody();
		            session.setAttribute("cartItems", updatedCartDTO); // Update session with the new cart
		            mv.addObject("cartItems", updatedCartDTO); // Pass updated cart to view
		            mv.setViewName("cart"); // Return to cart view
		        } else {
		            mv.addObject("errorMessage", "Failed to update the cart quantity.");
		            mv.setViewName("cart");
		        }
		    } catch (Exception e) {
		        e.printStackTrace();
		        mv.setViewName("error");
		        mv.addObject("errorMessage", "An error occurred while updating the cart.");
		    }

		    return mv;
		}



//-------------------------------
		@GetMapping("/OrderDetails")
		public ModelAndView viewOrderByHistory(HttpServletRequest request, HttpServletResponse response,
		        @RequestParam("userId") long userId) {
		    ModelAndView mv = new ModelAndView();
		    HttpSession session = request.getSession();

		    // Log the userId for debugging
		    System.out.println("Fetching order history for user ID: " + userId);

		    List<ServiceInstance> instances = discoveryClient.getInstances("BUYERSERVICE");
		    ServiceInstance serviceInstance = instances.isEmpty() ? null : instances.get(0);
		    String baseUrl = serviceInstance != null ? serviceInstance.getUri().toString() + "/buyer/viewOrderHistory/" + userId : "";

		    RestTemplate restTemplate = new RestTemplate();
		    List<OrderDTO> orderList = new ArrayList<>();

		    try {
		        // Fetch the order history directly as a list of OrderDTO
		        ResponseEntity<List<OrderDTO>> responseEntity = restTemplate.exchange(
		                baseUrl, HttpMethod.GET, null,
		                new ParameterizedTypeReference<List<OrderDTO>>() {}
		        );

		        // Check the response status
		        if (responseEntity.getStatusCode() == HttpStatus.OK) {
		            orderList = responseEntity.getBody();
		            // Log the retrieved order history for debugging
		            System.out.println("Retrieved order history: " + orderList);
		        } else {
		            System.err.println("No orders found for this user.");
		            mv.addObject("message", "No order history found for this user.");
		            mv.setViewName("orderHistory");
		            return mv;
		        }
		    } catch (Exception e) {
		        // Log the exception
		        System.err.println("Error fetching order history: " + e.getMessage());
		        mv.addObject("message", "Error fetching order history.");
		        mv.setViewName("orders");
		        return mv;
		    }

		    if (orderList != null && !orderList.isEmpty()) { 
		        mv.addObject("orders", orderList); 
		        mv.setViewName("orders"); 
		    } else {
		        mv.addObject("message", "No order history found for this user.");
		        mv.setViewName("orders"); 
		    }

		    return mv;
		}

}
