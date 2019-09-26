<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>		
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="nonBillableCategories" value="${applicationScope.applicationConfiguraion.nonBillableCategories}"/>
		<tr>
			<td>Leave</td>
			<td></td>
		</tr>
		<tr>
			<td>Meeting</td>
			<td></td>
		</tr>
		<c:if test="${not empty nonBillableCategories}">
        <c:forEach var="nonBillableCategoryConf" items="${nonBillableCategories}">
		<tr>
			<td>${nonBillableCategoryConf.name}</td>
			<td><a href="#" onclick="deleteApplicationConfigVal('${nonBillableCategoryConf.name}',${nonBillableCategoryConf.appConfValueId},'nonBillableCategory')" data-box="#confirmationbox" class="deleteAppConfValue mb-control" style="float:right"><i class="fa fa-times fa-2x" style="color:red" aria-hidden="true"></i></a></td>
		</tr>
        </c:forEach>
     </c:if>
