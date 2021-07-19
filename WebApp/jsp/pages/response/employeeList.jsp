 <c:if test="${currentUser.userGroupId != '1012' && currentUser.userRole != 'SUPERADMIN'}">
	    <c:if test="${not empty userMap}">
	    <c:forEach var="user" items="${userMap}">
	    <c:if test="${currentUserGroupId == '99999' || !user.deleted}">
	        	<%@include file="../fagments/employeeDetails.jspf" %>
	    </c:if>
	    </c:forEach>
	    </c:if>
    </c:if> 
    
     <c:if test="${currentUserGroupId == '1012' || currentUser.userRole == 'SUPERADMIN'}">
	    <c:if test="${not empty allSortedUsers}">
	    <c:forEach var="user" items="${allSortedUsers}">
	    <c:if test="${currentUserGroupId == '99999' || !user.deleted}">
	        	<%@include file="../fagments/employeeDetails.jspf" %>
	       </c:if>
	    </c:forEach>
	    </c:if>
    </c:if> 