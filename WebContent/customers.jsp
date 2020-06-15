<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Customers</title>
<script type="text/javascript" src="js/actions.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
</head>

<body>
<h3>Customers List</h3>

<a href="index.html">Home</a> <br><br>
<input type="text" id="myInput" onkeyup="filterList()" placeholder="Search for names...">

<!-- todo: prova con ajax -->
<a href="customerDeletion?customerID=">
	<button id="deleteButton" disabled="disabled">Delete</button>
</a>

<ul id="myCustomers">
	<c:forEach items="${customers}" var="customer">
		<li class="customer">
			<div>
				<a href="#" id="${customer.id}">
					${customer.firstName} ${customer.lastName} (${customer.region},${customer.city})<br>
					${customer.phoneNumber}<br>
				</a>
			</div>
		</li>
	</c:forEach>
</ul>

<!-- script per abilitare il bottone quando si clicca su un customer da rimuovere -->
<script>
	$(document).ready(function(){
		var customerID = null;
		//Al click sull'ancora di un list item...
		$("li a").click(function(event){
			//prendo l'id del customer che � stato inserito dinamicamente
			customerID = $(this).attr("id");
			var deleteButton = $("#deleteButton");
			deleteButton.removeAttr("disabled"); //abilito il bottone
			
			//aggiungo al link predisposto sull'ancora del bottone l'ID del customer
			var anchor = deleteButton.parent("a");
			var completeLink = anchor.attr("href") + customerID;
			deleteButton.parent("a").attr("href", completeLink);
		});
	});
</script>


</body>

</html>