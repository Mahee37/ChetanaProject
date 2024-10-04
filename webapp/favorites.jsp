<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:include page="header.jsp">
	<jsp:param name="pageTitle" value="Favorites" />
</jsp:include>

<!-- Main Content -->
<div class="container mt-5">
	<h1 class="mb-4">Your Favorite Books</h1>

	<c:choose>
		<c:when test="${not empty fav_list}">
			<table class="table table-striped">
				<thead>
					<tr>
						<th scope="col">Product Name</th>
						<th scope="col">Description</th>
						<th scope="col">Price</th>
						<th scope="col">Action</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="product" items="${fav_list}">
						<tr>
							<td>${product.productName}</td>
							<td class="product-description">${product.price}</td>
							<td>${product.discounted_price}</td>
							<td>
								<form action="${pageContext.request.contextPath}/buyer/removeFromFavorite" method="post">
									<input type="hidden" name="productId" value="${product.id}">
									<button type="submit">Remove</button>
								</form>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</c:when>

		<c:otherwise>
			<div class="alert alert-warning text-center" role="alert">No
				favorite books</div>
		</c:otherwise>
	</c:choose>

	<!-- Bootstrap JS and dependencies -->
	<script
		src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"></script>
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.min.js"></script>
	</body>
	</html>