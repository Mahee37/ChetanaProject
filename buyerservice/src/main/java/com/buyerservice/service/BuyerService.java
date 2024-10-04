package com.buyerservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.buyerservice.dao.BuyerOrderDAOInterface;
import com.buyerservice.dao.BuyerProductDAOInterface;
import com.buyerservice.dao.BuyerShoppingDAOInterface;
import com.buyerservice.dto.CartProductDTO;
import com.buyerservice.dto.OrderDTO;
import com.buyerservice.dto.ProductDTO;
import com.buyerservice.dto.ProductDetailsProjection;
import com.buyerservice.dto.ProductDetailsResponse;
import com.buyerservice.dto.ShoppingCartDTO;
import com.buyerservice.entity.Orders;
import com.buyerservice.entity.Products;
import com.buyerservice.entity.ShoppingCart;

import jakarta.transaction.Transactional;


@Service
@Transactional
public class BuyerService implements BuyerServiceInterface{
	
	@Autowired
	BuyerShoppingDAOInterface buyerShoppingDAO;
	
	@Autowired
	BuyerProductDAOInterface buyerProductDAO;
	
	@Autowired
	BuyerOrderDAOInterface buyerOrderDAO;
	
	
	@Override
	public List<CartProductDTO> viewCartProducts(Long userId) {
		return buyerShoppingDAO.findByUserId(userId);
	}

	@Override
	public List<Products> viewProducts() {
		return buyerProductDAO.findAll();
	}
	@Override
	public List<Products> browseProductsByCategory(String productCategory) {
		List<Products> ll= buyerProductDAO.findAll();
		List<Products> ll1=new ArrayList<Products>();
		
		for(Products p1:ll) {
			if(p1.getProductCategory().equals(productCategory)) {
				ll1.add(p1);
			}
		}
		
		return ll1;
	}

	@Override
	public List<OrderDTO> viewOrderByHistory(long userId) {
	    List<Orders> allOrders = buyerOrderDAO.findAll();
	    List<OrderDTO> userOrdersList = new ArrayList<>();

	    for (Orders order : allOrders) {
	        if (order.getUser().getUserId() == userId) {
	            OrderDTO orderDTO = new OrderDTO();
	            orderDTO.setOrderId(order.getOrderId());
	            orderDTO.setTotalPrice(order.getTotalPrice());
	            orderDTO.setOrderDate(order.getOrderDate().toLocalDateTime().toLocalDate());
	            orderDTO.setPaymentMode(order.getPaymentMode());
	            orderDTO.setShoppingAddress(order.getShoppingAddress());
	            orderDTO.setCity(order.getCity());
	            orderDTO.setPincode(order.getPincode());
	            orderDTO.setPhoneNumber(order.getPhoneNumber());
	            orderDTO.setStatus(order.getStatus());

	            userOrdersList.add(orderDTO);
	        }
	    }

	    return userOrdersList;
	}

	@Override
	public List<String> getAllCategories() {
	    // Fetch distinct categories from the database
	    List<String> categories = buyerProductDAO.findDistinctCategories();

	  
	    if (categories != null && !categories.isEmpty()) {
	        System.out.println("Fetched Categories: " + categories);
	    } else {
	        System.out.println("No categories found.");
	    }

	    return categories;
	}

	@Override
	public Optional<Products> viewProductDetails(long productId) {
	    return Optional.ofNullable(buyerProductDAO.findProjectedById(productId));
	}

	@Override
	public ShoppingCartDTO updateQuantityofCart(long cartId, long quantity) {
		// TODO Auto-generated method stub
		 ShoppingCart shoppingCart = buyerShoppingDAO.findByCartId(cartId);
		    
		    if (shoppingCart != null) {
		        shoppingCart.setQuantity(quantity);
		        // Update totalPrice logic if needed
		        buyerShoppingDAO.save(shoppingCart);

		        // Map to DTO
		        ShoppingCartDTO shoppingCartDTO = new ShoppingCartDTO();
		        shoppingCartDTO.setCartId(shoppingCart.getCartId());
		        shoppingCartDTO.setQuantity(shoppingCart.getQuantity());
		        shoppingCartDTO.setTotalPrice(shoppingCart.getTotalPrice());
		        shoppingCartDTO.setProductName(shoppingCart.getProductName());
		        shoppingCartDTO.setProductDescription(shoppingCart.getProductDescription());

		        return shoppingCartDTO;
		    }

		    return null; // or throw an exception
		}

}
