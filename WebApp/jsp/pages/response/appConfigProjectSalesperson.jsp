<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>		
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="currentUserGroupId" value="${applicationScope.applicationConfiguraion.currentUserGroupId}"/>

<c:set var="projectSalesPersons" value="${applicationScope.applicationConfiguraion.projectSalesPersons}"/>
		<c:if test="${not empty projectSalesPersons}">

        <c:forEach var="projectSalesPersonConf" items="${projectSalesPersons}">
		<tr>
			<td>${projectSalesPersonConf.name}</td>
			<td>
			<c:if test="${currentUserGroupId == '99999' }">
			<a href="#" onclick="deleteApplicationConfigVal('${projectSalesPersonConf.name}',${projectSalesPersonConf.appConfValueId},'projectSalesPerson')" data-box="#confirmationbox" class="deleteAppConfValue mb-control" style="float:right"><i class="fa fa-times fa-2x" style="color:red" aria-hidden="true"></i></a>
			</c:if>
			</td>
		</tr>
             </c:forEach>
    </c:if>
