<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="UTF-8"%>

<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Insert new Customer</title>
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
	<script type="text/javascript" src="js/actions.js"></script>
</head>
<body>

<h3>New customer form</h3>

<!-- Form di inserimento dati di un customer -->
<form id="registrationForm" action="formProcessing" method="post">
<label>First name: </label>
<input type="text" name="firstName">
<br>

<label>Last name: </label>
<input type="text" name="lastName">
<br>

<label>Phone number: </label>
<input type="text" name="phoneNumber">
<br>	

<label for="region">Region: </label>
<select id="region" name="region" form="registrationForm">
	<option disabled="disabled" selected="selected">--- Select region ---</option>
	<c:forEach items="${regions}" var="region">
		<option value="${region.regionID}">${region.regionName}</option>
	</c:forEach>
</select> <br>

<label for=city>City: </label>
<select id="city" name="city" form="registrationForm">
	<option disabled="disabled" selected="selected">--- No city ---</option>
</select><br>

<input type="submit" value="Register">
</form>

 <!-- script per elaborare le select-->
	<script>
		$(document).ready(function(){
			$("#region").change(function(event){
				//Cancello le citt� precedentemente avvalorate e prendo la regione selezionata
				$("#city").empty();
				var targetRegion = event.target.value;
				
				//Chiamata AJAX per prelevare la lista delle citt� afferenti alla regione
				$.ajax({
					url: "registration",
					data: {
						regionID: targetRegion,
						requestAction: "CitiesRequest"
					},
					type: "GET",
					dataType: "json",
					async: "false"
				}).done(function(jsonCities){
					$.each(jsonCities, function(index, city){
						var initials = city.initials;
						var optHTML = "<option value='" + initials[0]+initials[1] + "'></option>";
						var option = $(optHTML).text(city.name)
						$("#city").append(option);
					})
				}).fail(function(){
					alert("Something went wrong!");
				});
			});
			
			$("input, select").click(function(){
				$(this).css("background-color", "yellow");
			});

			$("input, select").blur(function(){
				$(this).css("background-color", "white");
			});
		});
	</script>
</body>
</html>