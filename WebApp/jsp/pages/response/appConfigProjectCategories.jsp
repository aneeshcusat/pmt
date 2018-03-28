<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>		
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="currentUserGroupId" value="${applicationScope.applicationConfiguraion.currentUserGroupId}"/>
<c:set var="appConfigMap" value="${applicationScope.applicationConfiguraion.appConfigMap}"/>
<c:set var="projectCategory" value='projectCategory${currentUserGroupId}'/>  
<c:if test="${not empty appConfigMap[projectCategory] }">

        <c:forEach var="projectCategoryConf" items="${appConfigMap[projectCategory].appConfValueDetails}">
		<tr class="clickable">
			<td>${projectCategoryConf.name}</td>
			<td><a href="#" onclick="deleteApplicationConfigVal('${projectCategoryConf.name}',${projectCategoryConf.appConfValueId},'projectCateogry')" data-box="#confirmationbox" class="deleteAppConfValue mb-control" style="float:right"><i class="fa fa-trash-o fa-2x" style="color:red" aria-hidden="true"></i></a></td>
		</tr>
             </c:forEach>
    </c:if>
