package com.buyerservice.service;

import java.util.List;
import java.util.Optional;

import com.buyerservice.dto.CartProductDTO;
import com.buyerservice.dto.OrderDTO;
import com.buyerservice.dto.ShoppingCartDTO;
import com.buyerservice.entity.Orders;
import com.buyerservice.entity.Products;
import com.buyerservice.entity.ShoppingCart;

public interface BuyerServiceInterface {

	List<CartProductDTO> viewCartProducts(Long customerId);

	List<Products> viewProducts();

	Optional<Products> viewProductDetails(long productId);

	List<Products> browseProductsByCategory(String productCategory);

	List<OrderDTO> viewOrderByHistory(long userId);


	List<String> getAllCategories();

	ShoppingCartDTO updateQuantityofCart(long cartId, long quantity);

}
