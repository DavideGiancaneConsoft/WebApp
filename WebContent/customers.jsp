<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="UTF-8"%>
    
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

<button id="deleteButton" disabled="disabled">Delete</button>

<ul id="myCustomers">
	<c:forEach items="${customers}" var="customer">
		<li class="customer">
			<div>
				<a href="#" id="${customer.id}">
					${customer.firstName} ${customer.lastName} (${customer.city.name})<br>
					${customer.phoneNumber}<br>
				</a>
			</div>
		</li>
	</c:forEach>
</ul>

<!-- script per abilitare il bottone quando si clicca su un customer da rimuovere -->
<script>
	$(document).ready(function(){
		var id = null;
		var deleteButton = null;
		//Al click sull'ancora di un list item...
		$("li a").click(function(event){
			//prendo l'id del customer che � stato inserito dinamicamente
			id = $(this).attr("id");
			deleteButton = $("#deleteButton");
			deleteButton.removeAttr("disabled"); //abilito il bottone

			//Salvo l'ID del customer come dato associato al bottone
			//(e lo recupero nella chiamata ajax)
			deleteButton.data("customerID", id);
		});
		

		//Chiamata ajax per eliminare la riga
		$("#deleteButton").click(function(event){
			event.preventDefault();
			$.ajax({
				url: "customerDeletion",
				data: {
					customerID: $(this).data("customerID")
					},
				type: "POST",
				async: "false"
			}).done(function(){
				liToRemove = $("li a#" + id).parents("li.customer");
				liToRemove.remove();
			}).fail(function(xhr, status, error){
				alert("Errore");
			});		
		});
	});
</script>


</body>

</html>