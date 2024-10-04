<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:include page="header.jsp">
    <jsp:param name="pageTitle" value="Your Cart"/>
</jsp:include>

<!-- Main Content -->
<div class="container mt-5">
    <h1 class="mb-4">Shopping Cart</h1>

    <c:choose>
        <c:when test="${not empty cartItems}">
            <table class="table table-bordered">
                <thead>
                    <tr>
                        <th scope="col">Cart ID</th>
                        <th scope="col">Product</th>
                        <th scope="col">Description</th>
                        <th scope="col">Quantity</th>
                        <th scope="col">Total</th>
                        <th scope="col">Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:set var="totalPrice" value="0.0" />
                    <c:forEach var="item" items="${cartItems}">
                        <tr>
                            <td>${item.cartId}</td>
                            <td>${item.productName}</td>
                            <td>${item.productDescription}</td>
                            <td>
                                <form action="${pageContext.request.contextPath}/updateQuantity" method="post" class="d-flex">
                                    <input type="hidden" name="cartId" value="${item.cartId}"> 
                                    <input type="number" name="quantity" class="form-control me-2 quantity-input" value="${item.quantity}" min="1" required>
                                    <button type="submit" class="btn btn-primary">Update</button>
                                </form>
                            </td>
                            <td>
                                <fmt:formatNumber value="${item.totalPrice}" type="currency" currencySymbol="₹" />
                                <c:set var="totalPrice" value="${totalPrice + item.totalPrice}" />
                            </td>
                            <td>
                                <form action="${pageContext.request.contextPath}/buyer/removeProductFromCart" method="post">
                                    <input type="hidden" name="productId" value="${item.productId}">
                                    <input type="hidden" name="action" value="delete">
                                    <button type="submit" class="btn btn-danger">Remove</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

            <div class="text-end">
                <h4>Total Price: &#8377; <fmt:formatNumber value="${totalPrice}" type="currency" currencySymbol="₹" /></h4>
                <a href="${pageContext.request.contextPath}/checkout" class="btn btn-success mt-3">Proceed to Checkout</a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="alert alert-warning text-center" role="alert">
                Your cart is empty.
            </div>
            <a href="${pageContext.request.contextPath}/products" class="btn btn-primary">Continue Shopping</a>
        </c:otherwise>
    </c:choose>
</div>

<!-- Inline CSS for quantity input field -->
<style>
    .quantity-input {
        width: 100px; /* Adjust width as needed */
    }
</style>

<!-- Bootstrap JS and dependencies -->
<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.11.0/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
