<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:set var="applicationHome" value="${contextPath}/dashboard"/>
<c:set var="currentUserGroupId" value="${applicationScope.applicationConfiguraion.currentUserGroupId}"/>
<c:set var="projectTaskActivitySelectId" value='projectTaskActivityDelete${currentUserGroupId}'/>
<c:set var="appConfigMap" value="${applicationScope.applicationConfiguraion.appConfigMap}"/>
<c:set var="projectTaskActivityDeleteEnabled" value='false'/>   
  <c:if test="${not empty appConfigMap[projectTaskActivitySelectId] && not empty appConfigMap[projectTaskActivitySelectId].appConfValueDetails}">
  <c:forEach var="projectTaskActivityDeleteConf" items="${appConfigMap[projectTaskActivitySelectId].appConfValueDetails}">
  <c:if test="${projectTaskActivityDeleteConf.value eq 'enabled'}">
	<c:set var="projectTaskActivityDeleteEnabled" value='true'/>   
  </c:if>
  </c:forEach>
  </c:if>

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