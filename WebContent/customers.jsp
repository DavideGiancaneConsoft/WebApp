<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Customers</title>
<script type="text/javascript" src="js/actions.js"></script>
</head>

<body>
<h3>Customers List</h3>

<input type="text" id="myInput" onkeyup="filterList()" placeholder="Search for names...">

<ul id="myCustomers">
	<c:forEach items="${customers}" var="customer">
		<li id="${customer.id}">
			<div>
				<a href="#">
					${customer.firstName} ${customer.lastName} (${customer.region},${customer.city})<br>
					${customer.phoneNumber}<br>
				</a>
			</div>
			<div>
				<!-- todo: prova con ajax -->
				<a href="customerDeletion?customerID=${customer.id}">
					<button>Delete</button>
				</a>
			</div>
		</li>
	</c:forEach>
</ul>
</body>

</html>