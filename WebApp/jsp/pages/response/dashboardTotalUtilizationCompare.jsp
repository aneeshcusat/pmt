<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>		
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:if test="${not empty dashboardOverAllutilization}">
	<div class="col-md-12 autilinnercontent"  style="width: ${fn:length(dashboardOverAllutilization) * 75}px">
		<c:forEach var="totalUtilization" items="${dashboardOverAllutilization}">
			 <div class="myprogress" data-style="margin-top: ${130-totalUtilization.grandTotalPercentage}px; margin-left:50px">
				  <p class="vprogresslabel">${totalUtilization.grandTotalPercentage}%</p>
				  <div class="progress progress-bar-vertical" data-toggle="tooltip" data-placement="right"  title="Non Billable : ${totalUtilization.nonBillableHrs} Hrs" data-style="height: ${totalUtilization.grandTotalPercentage+220}px;min-height:0px;">
				    <div class="progress-bar" role="progressbar" data-toggle="tooltip" data-placement="right"  title="Billable : ${totalUtilization.billableHrs} Hrs" aria-valuenow="${totalUtilization.billablePercentage}" aria-valuemin="0" aria-valuemax="100" style="height: ${totalUtilization.billablePercentage}%;">
				    </div>
				  </div> 
				  <p class="vprogresslabel legendtext">${totalUtilization.type}</p>
			  </div>
		</c:forEach>
	</div>
</c:if>
