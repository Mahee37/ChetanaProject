package com.buyerservice.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.buyerservice.dto.CartProductDTO;
import com.buyerservice.entity.ShoppingCart;

@Repository
public interface BuyerShoppingDAOInterface extends JpaRepository<ShoppingCart, Long> {
	 @Query("SELECT new com.buyerservice.dto.CartProductDTO(s.cartId, s.product.productId, s.quantity, s.totalPrice, s.product.productDescription, s.product.productName) " +
	           "FROM ShoppingCart s WHERE s.user.userId = :userId")
	    List<CartProductDTO> findByUserId(@Param("userId") Long userId);
	 
	 
	 @Query("SELECT sc FROM ShoppingCart sc WHERE sc.cartId = :cartId")
	    ShoppingCart findByCartId(@Param("cartId") long cartId);
}

