<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="requestUri" value="${pageContext.request.requestURI}"/>
<c:set var="currentUser" value="${applicationScope.applicationConfiguraion.currentUser}"/>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:if test="${currentUser.userRole != 'SUPERADMIN' && currentUser.userRole != 'ADMIN' && currentUser.userRole != 'TEAMLEAD'}">
  <c:if test = "${fn:contains(requestUri, 'projectdashboard') || fn:contains(requestUri, 'employees') || fn:contains(requestUri, 'mileStones') || fn:contains(requestUri, 'taskAllocator') || fn:contains(requestUri, 'accounts') || fn:contains(requestUri, 'applicationConfig') || fn:contains(requestUri, 'projectreporting')}">
  		<c:redirect url="/dashboard/tasks"></c:redirect>
  </c:if>     
</c:if>
  <c:if test = "${fn:contains(requestUri, 'newindex')}">
  	<c:set var="expandedPage" value="false"></c:set>
  </c:if>