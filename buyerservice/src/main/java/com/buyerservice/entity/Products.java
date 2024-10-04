package com.buyerservice.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Products {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long productId;
	
	private String productName;
	private String productDescription;
	private String imageUrl;
	private String productCategory;
	private double price;
	private double discountPrice;
	private long quantity;
	
	@ManyToOne
	@JoinColumn(name = "sellerId", referencedColumnName = "userId") 
	@JsonBackReference
	private User user;
	

	@OneToMany(mappedBy = "product",cascade=CascadeType.ALL)
	@JsonBackReference
	private List<ShoppingCart> shoppingcart;
	
	
	
	@OneToMany(mappedBy = "product",cascade=CascadeType.ALL)
	 @JsonManagedReference
	private List<Orders> orders;
	
	
	public Products() {
    }

	public void setUser(User user) {
		this.user = user;
	}
	
	public User getUser() {
		return user;
	}

	
	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getDiscountPrice() {
		return discountPrice;
	}

	public void setDiscountPrice(double discountPrice) {
		this.discountPrice = discountPrice;
	}

	public long getQuantity() {
		return quantity;
	}

	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}

	

	public List<ShoppingCart> getShoppingcart() {
		return shoppingcart;
	}

	public void setShoppingcart(List<ShoppingCart> shoppingcart) {
		this.shoppingcart = shoppingcart;
	}

	

	public List<Orders> getOrders() {
		return orders;
	}

	public void setOrders(List<Orders> orders) {
		this.orders = orders;
	}
	 public Products(Long productId, String productName, String productDescription, String imageUrl,
             String productCategory, double price, double discountPrice, long quantity) {
 this.productId = productId;
 this.productName = productName;
 this.productDescription = productDescription;
 this.imageUrl = imageUrl;
 this.productCategory = productCategory;
 this.price = price;
 this.discountPrice = discountPrice;
 this.quantity = quantity;
}
	

	

}
