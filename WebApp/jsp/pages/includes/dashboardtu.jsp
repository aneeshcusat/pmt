<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>		
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!-- PAGE CONTENT WRAPPER -->
<div class="dashboard col-md-12 dashboadtu" style="padding-right: 5px;display: none">
<div class="col-md-12 dbwidget">
	<div class="row">
		<div class="col-md-12 dbwidget">
			<div class="panel panel-default">
				<div class="panel-heading">
					<div class="panel-title-box">
						<span style="float: left;margin-top: 3px"><a href="javascript:showHome();"><span class="fa fa-chevron-left fa-3x"></span></a></span>
						<span class="dbheading">Total Utilization</span>
					</div>
				</div>
				<div class="panel-body padding-0">
					<div class="dbcontentbox" style="height: 200px;">
						<div class="col-md-12 center-block" style="height: 10px;text-align: center;">
							<span style="background-color: #0BB4C1;" class="legend"></span><span class="legendtext">Billable</span>
							<span style="background-color: #E3E3E3;" class="legend"></span><span class="legendtext">Non Billable</span>
							<span style="float: right;">
								<a class="legenddateview tuviewday"  href="javascript:refreshTotalUtilizationDiv('day');">Day</a>
								<a class="legenddateview tuviewmonth"  href="javascript:refreshTotalUtilizationDiv('week');">Week</a>
								<a class="legenddateview tuviewyear"  href="javascript:refreshTotalUtilizationDiv('month');">Month</a>
							</span>
						</div>
						<div class="col-md-12 tatalutilizationChart autiloutercontent" style="height: 190px">
							
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="row">
		<div class="col-md-12 dbwidget">
			<div class="panel panel-default">
				<div class="panel-body padding-0">
					<div class="dbcontentbox" style="height: 480px;">
						<div class="calendar fullcaledartu">                                
                    		<div id="fullcaledartu"></div>                            
                		</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
</div>