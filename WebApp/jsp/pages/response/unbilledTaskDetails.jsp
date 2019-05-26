<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="task-list-container">
	<c:set var="hasTaskActivityValue" value="false"/>
	<c:if test="${not empty taskActivitiesMap['TODAY']}">
	<c:set var="hasTaskActivityValue" value="true"/>
		<div class="title-separator">
			<h2 class="separator-heading">Todays</h2>
		</div>
		<ul class="list-group task-list" id="todaysTask">
			<c:forEach var="taskActivites" items="${taskActivitiesMap['TODAY']}">
				<%@include file="../fagments/unbilledItem.jspf" %>
			</c:forEach>
		</ul>
	</c:if>
	<c:if test="${not empty taskActivitiesMap['UPCOMING']}">
	<c:set var="hasTaskActivityValue" value="true"/>
	<div class="title-separator">
		<h2 class="separator-heading">Upcoming</h2>
	</div>
	<ul class="list-group task-list" id="upcomingTask">
		<c:forEach var="taskActivites" items="${taskActivitiesMap['UPCOMING']}">
			<%@include file="../fagments/unbilledItem.jspf" %>
		</c:forEach>
	</ul>
	</c:if>
	<c:if test="${not empty taskActivitiesMap['PAST']}">
	<c:set var="hasTaskActivityValue" value="true"/>
	<div class="title-separator">
		<h2 class="separator-heading">Last months</h2>
	</div>
	<ul class="list-group task-list" id="oldTask">
		<c:forEach var="taskActivites" items="${taskActivitiesMap['PAST']}">
			<%@include file="../fagments/unbilledItem.jspf" %>
		</c:forEach>
	</ul>	
	</c:if>
	<c:if test="${!hasTaskActivityValue}">
		No Task Activities found!
	</c:if>
</div>