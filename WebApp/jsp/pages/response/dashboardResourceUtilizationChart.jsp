<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>		
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="allUsersMap" value="${applicationScope.applicationConfiguraion.allUsersMap}"/>
<c:if test="${not empty dashboadResourceUtilization}">
	<div class="col-md-12">
		<c:forEach var="resourceUtilization" items="${dashboadResourceUtilization}">
			 <div class="row"  style="margin-top: 10px">
				<div class="col-md-1" style="text-align: right;padding: 0;margin: 0">
					<span class="legendtext">${allUsersMap[resourceUtilization.userId].firstName}</span>
				</div>
				<div class="col-md-10">
					<div class="progress" data-toggle="tooltip" data-placement="bottom"  title="Free : ${resourceUtilization.freeHrs} Hrs">
				        <div class="progress-bar billable" data-toggle="tooltip" data-placement="bottom" title="Billable : ${resourceUtilization.billableHrs} Hrs" style="width: ${resourceUtilization.resourceBillablePercentage}%">
				        </div>
				        <div class="progress-bar nonbillable" data-toggle="tooltip" data-placement="bottom" title="Non Billable : ${resourceUtilization.nonBillableHrs} Hrs" style="width: ${resourceUtilization.resourceNonBillablePercentage}%">
				        </div>
				    </div>
			    </div>
			    <div class="col-md-1">
					${resourceUtilization.grandResourceTotal}%
				</div>
		    </div>
		</c:forEach>
	</div>
</c:if>