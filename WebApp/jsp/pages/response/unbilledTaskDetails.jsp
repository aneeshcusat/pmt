<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="task-list-container">
	<div class="title-separator">
		<h2 class="separator-heading">Todays</h2>
	</div>
	<ul class="list-group task-list" id="todaysTask">
		<c:forEach var="taskActivites" items="${taskActivitiesMap['TODAY']}">
			<%@include file="../fagments/unbilledItem.jspf" %>
		</c:forEach>
	</ul>
	
	<div class="title-separator">
		<h2 class="separator-heading">Upcoming</h2>
	</div>
	<ul class="list-group task-list" id="upcomingTask">
		<c:forEach var="taskActivites" items="${taskActivitiesMap['UPCOMING']}">
			<%@include file="../fagments/unbilledItem.jspf" %>
		</c:forEach>
	</ul>
	
	<div class="title-separator">
		<h2 class="separator-heading">Last months</h2>
	</div>
	<ul class="list-group task-list" id="oldTask">
		<c:forEach var="taskActivites" items="${taskActivitiesMap['PAST']}">
			<%@include file="../fagments/unbilledItem.jspf" %>
		</c:forEach>
	</ul>	

</div>