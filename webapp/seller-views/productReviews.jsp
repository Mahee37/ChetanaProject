<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>Product Reviews</title>
</head>
<body>
    <h1>Product Reviews</h1>

    <c:if test="${empty products}">
        <p>No products available.</p>
    </c:if>

    <c:forEach var="product" items="${products}">
        <h2>${product.name}</h2>
        <c:forEach var="review" items="${reviews}">
            <c:if test="${review.product.id == product.id}">
                <p>Rating: ${review.rating}</p>
                <p>Review: ${review.reviewText}</p>
                <p>Reviewed by User ID: ${review.userId}</p>
            </c:if>
        </c:forEach>
    </c:forEach>
</body>
</html>