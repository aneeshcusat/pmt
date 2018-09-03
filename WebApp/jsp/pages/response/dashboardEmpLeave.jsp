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
    <tr style="padding-bottom: 0px;margin-top: 0px; border-bottom: 1px solid #dddddd">
        <td class="filterable-cell" style="width: 60%;padding: 0px;border-top:0">
        <span class="userdetails">
        	<img  style="width: 30px;border-radius: 20px;" alt="image" src="${applicationHome}/image/${user.userId}"  onerror="this.src='${assets}/images/users/user-offline.png'"></a>
        </span><b class="wraptext">${user.firstName}</b></td>
    </tr>
</c:forEach>
</c:if>

<script>
$(".allLeaveUserCount").html("("+${fn:length(usersList)}+")");
</script>
