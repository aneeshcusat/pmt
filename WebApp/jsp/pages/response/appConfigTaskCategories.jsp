<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>		
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="taskCategories" value="${applicationScope.applicationConfiguraion.taskCategories}"/>
		<c:if test="${not empty taskCategories}">

        <c:forEach var="taskCategoryConf" items="${taskCategories}">
		<tr>
			<td>${taskCategoryConf.name}</td>
			<td><a href="#" onclick="deleteApplicationConfigVal('${taskCategoryConf.name}',${taskCategoryConf.appConfValueId},'taskCateogry')" data-box="#confirmationbox" class="deleteAppConfValue mb-control" style="float:right"><i class="fa fa-times fa-2x" style="color:red" aria-hidden="true"></i></a></td>
		</tr>
             </c:forEach>
    </c:if>
