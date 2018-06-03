 <c:if test="${currentUser.userGroupId != '1012'}">
	    <c:if test="${not empty userMap}">
	    <c:forEach var="user" items="${userMap}">
	        	<%@include file="../fagments/employeeDetails.jspf" %>
	    </c:forEach>
	    </c:if>
    </c:if> 
    
     <c:if test="${currentUser.userGroupId == '1012'}">
	    <c:if test="${not empty employeeMap}">
	    <c:forEach var="employeeItem" items="${employeeMap}">
	    		<c:set var="user" value="${employeeItem.value}"></c:set>
	        	<%@include file="../fagments/employeeDetails.jspf" %>
	    </c:forEach>
	    </c:if>
    </c:if> 