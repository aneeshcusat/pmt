<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>		
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:if test="${not empty dashboadAccountUtilization}">
	<div class="col-md-12 autilinnercontent" style="width: ${fn:length(dashboadAccountUtilization) * 75}px">
		<c:forEach var="accountUtilization" items="${dashboadAccountUtilization}">
			 <div class="myprogress" style="margin-top: ${110-accountUtilization.grandTotalPercentage}px ">
				  <p class="vprogresslabel">${accountUtilization.grandTotalPercentage}%</p>
				  <div class="progress progress-bar-vertical" data-toggle="tooltip" data-placement="right"  title="Non Billable : ${accountUtilization.nonBillableHrs} Hrs" style="height: ${accountUtilization.grandTotalPercentage}px;min-height:0px;">
				    <div class="progress-bar" role="progressbar" data-toggle="tooltip" data-placement="right"  title="Billable : ${accountUtilization.billableHrs} Hrs" aria-valuenow="${accountUtilization.billablePercentage}" aria-valuemin="0" aria-valuemax="100" style="height: ${accountUtilization.billablePercentage}%;">
				    </div>
				  </div> 
				  <p class="vprogresslabel legendtext">${accountUtilization.label}</p>
			  </div>
		</c:forEach>
	</div>
	
<script>
$(".accountUtilizationChartLabel").html("${dashboadAccountUtilization[0].type}");
</script>

</c:if>
