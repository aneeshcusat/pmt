<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>		
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<spring:url value="/jsp/assets" var="assets" htmlEscape="true"/>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:set var="applicationHome" value="${contextPath}/dashboard"/>
<c:if test="${not empty usersList}">

			   <c:forEach var="user" items="${usersList}">
    <tr style="padding-bottom: 0px;margin-top: 0px; border-bottom: 1px solid #909090">
        <td class="filterable-cell" style="width: 60%;padding: 0px;border-top:0">
	    <div class="img-header">
	        <img  class="img" style="width: 30px;border-radius: 20px;" alt="image" src="${applicationHome}/image/${user.userId}"  onerror="this.src='${assets}/images/users/user-online.png'">
	        <span class="onlinestatus onlineStatus${user.userId}"></span>
	        <span class="availabilityStatus availabilityStatus${user.userId}"></span>
	    </div>
	    <span class="bwwraptext legendtext">${user.onlyFirstName}</span>
        </td>
        <td class="filterable-cell emputilization" style="background-color: #3d3d3d;border:0"><span class="percentagetxt">${user.yesterdayUtilization}<span class="percentage">%</span></span></td>
        <td class="filterable-cell emputilization" style="background-color: #3d3d3d;border:0"><span class="percentagetxt">${user.todayUtilization}<span class="percentage">%</span></span></td>
    </tr>
</c:forEach>
</c:if>

<script>
$(".allUserCount").html("("+${fn:length(usersList)}+")");
</script>