<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>		
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="newProjectCategories" value="${applicationScope.applicationConfiguraion.newProjectCategories}"/>
		<tr class="clickable">
			<td>Reporting and Descriptive Analytics</td>
			<td></td>
		</tr>
		<tr class="clickable">
			<td>Insightful Analytics</td>
			<td></td>
		</tr>
		<tr class="clickable">
			<td>Internal</td>
			<td></td>
		</tr>
		<tr class="clickable">
			<td>Project Management</td>
			<td></td>
		</tr>
		<tr class="clickable">
			<td>Advanced Analytics</td>
			<td></td>
		</tr>
		<tr class="clickable">
			<td>System Integration and Implementation</td>
			<td></td>
		</tr>
		<tr class="clickable">
			<td>Business &amp; Analytics Consulting</td>
			<td></td>
		</tr>

<c:if test="${not empty newProjectCategories}">

        <c:forEach var="projectCategoryConf" items="${newProjectCategories}">
		<tr class="clickable">
			<td>${projectCategoryConf.name}</td>
			<td><a href="#" onclick="deleteApplicationConfigVal('${projectCategoryConf.name}',${projectCategoryConf.appConfValueId},'projectCateogry')" data-box="#confirmationbox" class="deleteAppConfValue mb-control" style="float:right"><i class="fa fa-times fa-2x" style="color:red" aria-hidden="true"></i></a></td>
		</tr>
             </c:forEach>
    </c:if>