 <c:if test="${currentUser.userGroupId != '1012' && currentUser.userRole != 'SUPERADMIN'}">
	    <c:if test="${not empty userMap}">
	    <c:forEach var="user" items="${userMap}">
	        	<%@include file="../fagments/employeeDetails.jspf" %>
	    </c:forEach>
	    </c:if>
    </c:if> 
    
     <c:if test="${currentUser.userGroupId == '1012' || currentUser.userRole == 'SUPERADMIN'}">
	    <c:if test="${not empty allSortedUsers}">
	    <c:forEach var="user" items="${allSortedUsers}">
	        	<%@include file="../fagments/employeeDetails.jspf" %>
	    </c:forEach>
	    </c:if>
    </c:if> 