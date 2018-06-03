<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="employeeMap" value="${applicationScope.applicationConfiguraion.userMap}"/>
<c:set var="currentUser" value="${applicationScope.applicationConfiguraion.currentUser}"/>
<c:set var="userGroupMap" value="${applicationScope.applicationConfiguraion.userGroupMap}"/>

<c:if test="${currentUser.userGroupId == '1012'}">
<c:if test="${not empty employeeMap}">
<table class="table table-hover p-table employeeDataTable ">
		<thead>
			<tr>
				<th>Id</th>
				<th>Code</th>
				<th>Name</th>
				<th>Email</th>
				<th>Mobile</th>
				<th>Team</th>
				<th>Gender</th>
				<th>Qualification</th>
				<th>Designation</th>
				<th>Manager</th>
				<th>Role</th>
				<th>Group</th>
				<th>IsTemp</th>
				<th>LoggedIn?</th>
				<th></th>
			</tr>
		</thead> 
		
		<tbody>
		<c:forEach var="employeeItem" items="${employeeMap}">
		<c:set var="user" value="${employeeItem.value}"></c:set>
		<tr class="contact-name userDetails${user.id}">
				<td>${user.id}</td>
				<td>${user.empCode}</td>
				<td>${user.firstName}</td>
				<td>${user.email}</td>
				<td>${user.mobileNumber}</td>
				<td>${user.team}</td>
				<td>${user.gender}</td>
				<td>${user.qualification}</td>
				<td>${user.designation}</td>
				<td>${employeeMap[user.reportingManger].firstName}</td>
				<td>${user.role}</td>
				<td>${userGroupMap[user.userGroupId].name}</td>
				<td><c:if test="${user.temporaryEmployee}">Yes<span class="hide">Temporary</span></c:if><c:if test="${!user.temporaryEmployee}">No</c:if></td>
				<td><c:if test="${user.needPasswordReset}">No</c:if><c:if test="${!user.needPasswordReset}">Yes<span class="hide">LoggedIn</span></c:if></td>
				<td style="width: 50px">
					<a data-toggle="modal" class="profile-control-left" data-target="#registerusermodal" onclick="javascript:loadUser('${user.id}')"><span class="fa fa-edit fa-2x"></span></a>
				</td>
		</tr>
		
		 </c:forEach>
		</tbody>
		</table>
 </c:if>
</c:if> 