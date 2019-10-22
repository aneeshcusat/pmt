<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>		
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="taskProjectCategories" value="${applicationScope.applicationConfiguraion.taskProjectCategories}"/>
<c:set var="currentUserGroupId" value="${applicationScope.applicationConfiguraion.currentUserGroupId}"/>
<c:if test="${not empty taskProjectCategories}">
    <c:forEach var="taskProjectCategoryMap" items="${taskProjectCategories}">
	<tr>
		<td>${taskProjectCategoryMap.catTaskValue}</td>
		<td>${taskProjectCategoryMap.catProjectValue}</td>
	<td>
	<a href="#" onclick="deleteApplicationConfigVal('${taskProjectCategoryMap.name}',${taskProjectCategoryMap.appConfValueId},'taskProjectCategoryMappings')" data-box="#confirmationbox" class="deleteAppConfValue mb-control" style="float:right"><i class="fa fa-times fa-2x" style="color:red" aria-hidden="true"></i></a>
	</td>
	</tr>
      </c:forEach>
 </c:if>
