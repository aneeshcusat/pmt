<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!-- PAGE CONTENT WRAPPER -->
<div class="dashboard col-md-12 dashboadhome" style="padding-right: 5px;">
<div class="col-md-10 dbwidget">
	<div class="row">
		<div class="col-md-2 dbwidget">
			<div class="panel panel-default">
				<div class="panel-heading">
					<div class="panel-title-box">
						<h3>Utilization</h3>
					</div>
				</div>
				<div class="panel-body padding-0">
					<div class="dbcontentbox" style="height: 175px;">
					<div id="utilizationChart" class="utilizationChart" style="height: 150px">
					</div>
					
					<div class="col-md-12 center-block"" style="height: 10px;">
						<span style="background-color: #0BB4C1;" class="legend dot"></span><span class="legendtext">Billable</span>
						<span style="background-color: #E3E3E3;" class="legend dot"></span><span class="legendtext">Non Billable</span>
					</div>
					</div>
					
				</div>
			</div>
		</div>
		<div class="col-md-4 dbwidget accountDivWidget">
			<div class="panel panel-default">
				<div class="panel-heading">
					<div class="panel-title-box">
						<h3 class="accountUtilizationChartLabel">Accounts</h3>
					</div>
				</div>
				<div class="panel-body padding-0">
					<div class="dbcontentbox accountsdiv" style="height: 175px;">
					<div class="col-md-12 center-block" style="height: 10px;text-align: center;">
						<span style="background-color: #0BB4C1;" class="legend"></span><span class="legendtext">Billable</span>
						<span style="background-color: #E3E3E3;" class="legend"></span><span class="legendtext">Non Billable</span>
					</div>
					<div class="col-md-12 accountUtilizationChart autiloutercontent" style="height: 165px">
					</div>
					</div>
				</div>
			</div>
		</div>
		<div class="col-md-6 dbwidget resourceUtilDivWidget">
			<div class="panel panel-default">
				<div class="panel-heading">
					<div class="panel-title-box">
						<h3>Resource Utilization</h3>
					</div>
				</div>
				<div class="panel-body padding-0">
					<div class="dbcontentbox resutildiv"  style="height: 175px;overflow: auto;overflow-x: hidden;">
						
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="row">
		<div class="col-md-12">
			<a href="javascript:showTu();"><span class="viewprjsummary">View summary</span></a>
		</div>
	</div>	
		
	
	<div class="row">
		<div class="col-md-12 dbwidget">
			<div class="panel panel-default">
				<div class="panel-heading">
					<div class="panel-title-box" style="width: 100%">
						<span class="dbheading" style="float: left">Project Summary</span>
						<span style="float: right;"><span class="fa fa-search fa-3x"></span>
						<input style="min-width: 225px;margin-right: 5px;" id="projectSearchInput" class="searchbox" type="text" placeholder="Search for projects by Id, team, PO etc.."/>
						<a href="#" class="panel-fullscreen"><img  style="width: 17px;margin-right: 5px;" alt="image" src="${assets}/images/fullscreenimg.png"></a>
						</span>
						
					</div>
				</div>
				<div class="panel-body padding-0" style="overflow: hidden;">
					<div class="dbcontentbox projectdetails">
						<div class="col-md-12" style="height: 20px;background-color: #e3e3e3;padding-left: 0px;">
							<a href="javascript:showMyProjects()" class="projectfilter myprojectslink active" style="">My Projects</a>
							<a href="javascript:showTotalProjects()" class="projectfilter totalprojectslink"style="">Total Projects</a>
						</div>
						<div class="col-md-12 projecttitleblock">
						<div class="col-md-2">
							<div class="projectsummary totalprojectcount active" data-type="ALL">
								<span class="total">Total Projects</span>
								<span class="numberofproject">0</span>
								<span class="hours"></span>
							</div>
						</div>
						<div class="col-md-2">
							<div class="projectsummary backlogcount"  data-type="BACKLOG">
								<span class="total">Backlogs</span>
								<span class="numberofproject">0</span>
								<span class="hours"></span>
							</div>
						</div>
						<div class="col-md-2">
							<div class="projectsummary unassignedcount" data-type="UNASSIGNED">
								<span class="total">Unassigned</span>
								<span class="numberofproject">0</span>
								<span class="hours"></span>
							</div>
						</div>
						<div class="col-md-2">
							<div class="projectsummary inprogress"  data-type="INPROGRESS">
								<span class="total">In Progress</span>
								<span class="numberofproject">0</span>
								<span class="hours"></span>
							</div>
						</div>
						<div class="col-md-2">
							<div class="projectsummary completed"  data-type="COMPLETED">
								<span class="total">Completed</span>
								<span class="numberofproject">0</span>
								<span class="hours"></span>
							</div>
						</div>
						<div class="col-md-2">
							<div class="projectsummary upcoming" data-type="UPCOMING">
								<span class="total">Upcomming</span>
								<span class="numberofproject">0</span>
								<span class="hours"></span>
							</div>
						</div>
					</div>
					<div class="col-md-12 fixedheadertable">
							<table class="table ">
						    <thead>
						    <tr>
						    	<th class="rtborder">Projects</th>
						    	<th class="panelHideTD" style="display: none">PO Id</th>
						        <th>Team</th>
						        <th>Sub Team</th>
						        <th class="panelHideTD" style="display: none">Account</th>
						        <th class="panelHideTD" style="display: none">Client</th>
						        <th>Project Lead</th>
						        <th>Assigned To</th>
		    				    <th class="panelHideTD" style="display: none">Type</th>
		    				    <th class="panelHideTD" style="display: none">Category</th>
						        <th>Time</th>
						        <th>Status</th>
						    </tr>
						    </thead>
						    <tbody class="dashboardProjDetailsDiv">
						     
						    </tbody>
						</table>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	
</div>
<div class="col-md-2 dbwidget" style="padding-left:  0px;padding-right:  0px;">
	<div class="col-md-12 dbwidget">
		<div class="panel panel-default">
			<div class="panel-heading">
				<div class="panel-title-box">
					<span class="dbheading">Bandwidth</span><span style="float: right;font-size: 9px;margin-top: 0px;"><a href="javascript:showBandWidth();">View summary</a></span>
				</div>
			</div>
			<div class="panel-body padding-0">
				<div class="dbcontentbox" style="height: 560px;">
					<div class="fixedheadertable">
					<table class="table">
				    <thead>
				    <tr>
				    	<th style="width: 50%;text-align: center;border-bottom: 0;"><span style="font-size: 11px;color: #57BF9E;font-weight: bold;">All <span class="allUserCount">(0)</span></span></th>
				        <th style="width: 25%; text-align: center;border-bottom: 0;"><span style="font-size: 8px">Last Day</span></th>
				        <th style="width: 25%; text-align: center;border-bottom: 0;"><span style="font-size: 8px">Today</span></th>
				    </tr>
				    </thead>
				    <tbody style="height: 210px" class="dashboardEmpBWDiv">
			
				    </tbody>
				    </table>
					</div>
					<div class="fixedheadertable">
					<table class="table">
				    <thead>
				    <tr>
				    	<th style="width: 100%;border-bottom:0"><span style="font-size: 13px;color: red;font-weight: 500;">On Leave <span class="allLeaveUserCount">(0)</span></span></th>
				    </tr>
				    </thead>
				    <tbody style="height: 80px" class="dashboardEmpLeaveDiv">
				     
				    </tbody>
				    </table>				
				    </div>
				    <div class="calenderdiv" style="width: 200px">
				    </div>
				</div>
			</div>
		</div>
	</div>
</div>
</div>