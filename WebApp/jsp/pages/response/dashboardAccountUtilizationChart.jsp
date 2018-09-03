<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>		
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:if test="${not empty dashboadAccountUtilization}">
	<div class="col-md-12 autilinnercontent" style="width: ${fn:length(dashboadAccountUtilization) * 85}px">
		<c:forEach var="accountUtilization" items="${dashboadAccountUtilization}">
			 <div class="myprogress" data-style="margin-top: ${100-accountUtilization.grandTotalPercentage}px ">
				  <p class="vprogresslabel">${accountUtilization.grandTotalPercentage}%</p>
				  <div class="progress progress-bar-vertical" title="Non Billable Mins - ${accountUtilization.nonBillableMins}" data-style="min-height: ${accountUtilization.grandTotalPercentage}px">
				    <div class="progress-bar" role="progressbar" title="Billable Mins - ${accountUtilization.billableMins}" aria-valuenow="${accountUtilization.billablePercentage}" aria-valuemin="0" aria-valuemax="100" style="height: ${accountUtilization.billablePercentage}%;">
				    </div>
				  </div> 
				  <p class="vprogresslabel">${accountUtilization.label}</p>
			  </div>
		</c:forEach>
	</div>
	
<script>
$(".accountUtilizationChartLabel").html("${dashboadAccountUtilization[0].type}");
</script>

</c:if>
