<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>		
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="projectCategories" value="${applicationScope.applicationConfiguraion.projectCategories}"/>
<c:if test="${not empty projectCategories}">

        <c:forEach var="projectCategoryConf" items="${projectCategories}">
		<tr class="clickable">
			<td>${projectCategoryConf.name}</td>
			<td><a href="#" onclick="deleteApplicationConfigVal('${projectCategoryConf.name}',${projectCategoryConf.appConfValueId},'projectCateogry')" data-box="#confirmationbox" class="deleteAppConfValue mb-control" style="float:right"><i class="fa fa-times fa-2x" style="color:red" aria-hidden="true"></i></a></td>
		</tr>
             </c:forEach>
    </c:if>
