<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>		
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="staticNonBillableCategories" value="${applicationScope.applicationConfiguraion.staticNonBillableCategories}"/>
<c:set var="currentUserGroupId" value="${applicationScope.applicationConfiguraion.currentUserGroupId}"/>
<tr>
		<td>Leave</td><td></td>
</tr>
<c:if test="${not empty staticNonBillableCategories}">
    <c:forEach var="staticNonBillableCategoryConf" items="${staticNonBillableCategories}">
	<tr>
		<td>${staticNonBillableCategoryConf.name}</td>
	<td>
	<c:if test="${currentUserGroupId == '99999' }">
	<a href="#" onclick="deleteApplicationConfigVal('${staticNonBillableCategoryConf.name}',${staticNonBillableCategoryConf.appConfValueId},'staticNonBillableCategory')" data-box="#confirmationbox" class="deleteAppConfValue mb-control" style="float:right"><i class="fa fa-times fa-2x" style="color:red" aria-hidden="true"></i></a>
	</c:if>
	</td>
	</tr>
      </c:forEach>
 </c:if>
