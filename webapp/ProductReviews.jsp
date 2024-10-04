<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Product Reviews</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
    <div class="container mt-5">
        <h2>Product Reviews</h2>

        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>
        
        <c:if test="${not empty productsWithReviews}">
            <div class="list-group">
                <c:forEach items="${productsWithReviews}" var="product">
                    <div class="list-group-item">
                        <h5 class="mb-1">${product.productName}</h5>
                        <p class="mb-1">Category: ${product.productCategory}</p>
                        <p class="mb-1">Description: ${product.productDescription}</p>
                        <c:if test="${not empty product.reviews}">
                            <h6>Reviews:</h6>
                            <ul class="list-group">
                                <c:forEach items="${product.reviews}" var="review">
                                    <li class="list-group-item">
                                        <p><strong>Rating:</strong> ${review.rating}</p>
                                        <p><strong>Review:</strong> ${review.reviewText}</p>
                                    </li>
                                </c:forEach>
                            </ul>
                        </c:if>
                        <c:if test="${empty product.reviews}">
                            <p>No reviews available for this product.</p>
                        </c:if>
                    </div>
                </c:forEach>
            </div>
        </c:if>

        <c:if test="${empty productsWithReviews}">
            <p>No products available.</p>
        </c:if>
    </div>

    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.0.7/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
