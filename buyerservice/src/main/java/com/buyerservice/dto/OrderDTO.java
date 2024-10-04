package com.buyerservice.dto;

import java.time.LocalDate;

public class OrderDTO {
	    private Long orderId;
	    private Double totalPrice;
	    private LocalDate orderDate; // Use LocalDate if you want just the date; otherwise use LocalDateTime
	    private String paymentMode; // Updated from paymentMethod for consistency
	    private String shoppingAddress;
	    private String city;
	    private String pincode;
	    private String phoneNumber;
	    private String status;

	    // Getters and Setters
	    public Long getOrderId() {
	        return orderId;
	    }

	    public void setOrderId(Long orderId) {
	        this.orderId = orderId;
	    }

	    public Double getTotalPrice() {
	        return totalPrice;
	    }

	    public void setTotalPrice(Double totalPrice) {
	        this.totalPrice = totalPrice;
	    }

	    public LocalDate getOrderDate() {
	        return orderDate;
	    }

	    public void setOrderDate(LocalDate orderDate) {
	        this.orderDate = orderDate;
	    }

	    public String getPaymentMode() {
	        return paymentMode;
	    }

	    public void setPaymentMode(String paymentMode) {
	        this.paymentMode = paymentMode;
	    }

	    public String getShoppingAddress() {
	        return shoppingAddress;
	    }

	    public void setShoppingAddress(String shoppingAddress) {
	        this.shoppingAddress = shoppingAddress;
	    }

	    public String getCity() {
	        return city;
	    }

	    public void setCity(String city) {
	        this.city = city;
	    }

	    public String getPincode() {
	        return pincode;
	    }

	    public void setPincode(String pincode) {
	        this.pincode = pincode;
	    }

	    public String getPhoneNumber() {
	        return phoneNumber;
	    }

	    public void setPhoneNumber(String phoneNumber) {
	        this.phoneNumber = phoneNumber;
	    }

	    public String getStatus() {
	        return status;
	    }

	    public void setStatus(String status) {
	        this.status = status;
	    }
	}


