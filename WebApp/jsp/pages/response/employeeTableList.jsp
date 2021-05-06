<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="employeeMap" value="${applicationScope.applicationConfiguraion.userMap}"/>
<c:set var="currentUser" value="${applicationScope.applicationConfiguraion.currentUser}"/>
<c:set var="userGroupMap" value="${applicationScope.applicationConfiguraion.userGroupMap}"/>
<c:set var="allUsersMap" value="${applicationScope.applicationConfiguraion.allUsersMap}"/>
<c:set var="userMap" value="${applicationScope.applicationConfiguraion.userList}"/>
<c:set var="allSortedUsers" value="${applicationScope.applicationConfiguraion.allSortedUsers}"/>

<c:if test="${currentUser.userGroupId == '1012' || currentUser.userRole == 'SUPERADMIN' || currentUser.userRole == 'ADMIN'}">
<c:if test="${not empty allUsersMap}">
<table class="table table-hover p-table employeeDataTable ">
		<thead>
			<tr>
				<th>Id</th>
				<th>Code</th>
				<th>Name</th>
				<th>Email</th>
				<th>Reporting Manager email</th>
				<th>Mobile</th>
				<th>Team</th>
				<th>Gender</th>
				<th>Qualification</th>
				<th>Designation</th>
				<th>Role</th>
				<th>Group</th>
				<th>Date of Joining</th>
				<th>Location</th>
				<th>Country</th>
				<th>Division</th>
				<th>Department</th>
				<th>Sub-department</th>
				<th>Band</th>
				<th>Grade</th>
				<th>Employment Type</th>	
				
				<th>IsFunded</th>
				<th>Exit Date</th>
				<th>IsDeleted</th>
				<th>LoggedIn?</th>
				<th></th>
			</tr>
		</thead> 
		
		<tbody>
		
		 <c:if test="${currentUser.userGroupId != '1012' && currentUser.userRole != 'SUPERADMIN'}">
	    <c:if test="${not empty userMap}">
	    <c:forEach var="user" items="${userMap}">
	        	<%@include file="../fagments/employeeTableDetails.jspf" %>
	    </c:forEach>
	    </c:if>
    </c:if> 
    
     <c:if test="${currentUser.userGroupId == '1012' || currentUser.userRole == 'SUPERADMIN'}">
	    <c:if test="${not empty allSortedUsers}">
	    <c:forEach var="user" items="${allSortedUsers}">
	        	<%@include file="../fagments/employeeTableDetails.jspf" %>
	    </c:forEach>
	    </c:if>
    </c:if> 
		
		
		</tbody>
		</table>
 </c:if>
</c:if> 