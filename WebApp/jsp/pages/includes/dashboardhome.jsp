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
					<div id="utilizationChart" style="height: 150px">
					</div>
					
					<div class="col-md-12 center-block"" style="height: 10px;">
						<span style="background-color: #0BB4C1;" class="legend dot"></span><span class="legendtext">Billable</span>
						<span style="background-color: #E3E3E3;" class="legend dot"></span><span class="legendtext">Non Billable</span>
					</div>
					</div>
					
				</div>
			</div>
		</div>
		<div class="col-md-4 dbwidget">
			<div class="panel panel-default">
				<div class="panel-heading">
					<div class="panel-title-box">
						<h3>Accounts</h3>
					</div>
				</div>
				<div class="panel-body padding-0">
					<div class="dbcontentbox" style="height: 175px;">
					<div class="col-md-12 center-block" style="height: 10px;text-align: center;">
						<span style="background-color: #0BB4C1;" class="legend"></span><span class="legendtext">Billable</span>
						<span style="background-color: #E3E3E3;" class="legend"></span><span class="legendtext">Non Billable</span>
					</div>
					<div class="utilizationChart" style="height: 150px">
					
					</div>
					</div>
				</div>
			</div>
		</div>
		<div class="col-md-6 dbwidget">
			<div class="panel panel-default">
				<div class="panel-heading">
					<div class="panel-title-box">
						<h3>Resource Utilization</h3>
					</div>
				</div>
				<div class="panel-body padding-0">
					<div class="dbcontentbox" style="height: 175px;"></div>
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
						<input style="min-width: 225px;margin-right: 5px;" class="searchbox" type="text" placeholder="Search for projects by Id, team, PO etc.."/>
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
							<div class="projectsummary totalprojectcount active">
								<span class="total">Total Projects</span>
								<span class="numberofproject">25</span>
								<span class="hours">(125 Hrs)</span>
							</div>
						</div>
						<div class="col-md-2">
							<div class="projectsummary backlogcount" style="">
								<span class="total">Backlogs</span>
								<span class="numberofproject">05</span>
								<span class="hours">(125 Hrs)</span>
							</div>
						</div>
						<div class="col-md-2">
							<div class="projectsummary unassignedcount">
								<span class="total">Unassigned</span>
								<span class="numberofproject">06</span>
								<span class="hours">(125 Hrs)</span>
							</div>
						</div>
						<div class="col-md-2">
							<div class="projectsummary inprogress">
								<span class="total">In Progress</span>
								<span class="numberofproject">10</span>
								<span class="hours">(125 Hrs)</span>
							</div>
						</div>
						<div class="col-md-2">
							<div class="projectsummary completed">
								<span class="total">Completed</span>
								<span class="numberofproject">14</span>
								<span class="hours">(125 Hrs)</span>
							</div>
						</div>
						<div class="col-md-2">
							<div class="projectsummary upcoming">
								<span class="total">Upcomming</span>
								<span class="numberofproject">02</span>
								<span class="hours">(125 Hrs)</span>
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
						    <tbody>
						      <c:forEach var="i" begin="0" end="30" step="1">
						    <tr>
						        <td class="filterable-cell rtborder">Projects</td>
						        <td class="panelHideTD" style="display: none">PO Id</td>
						        <td class="filterable-cell">Team</td>
						        <td class="filterable-cell">Sub Team</td>
						        <td class="panelHideTD" style="display: none">Account</td>
		    				    <td class="panelHideTD" style="display: none">Client</td>
						        <td class="filterable-cell">Project Lead</td>
						        <td class="filterable-cell">Assigned To</td>
		    				    <td class="panelHideTD" style="display: none">Type</td>
								<td class="panelHideTD" style="display: none">Category</td>
						        <td class="filterable-cell">10:00</td>
						        <td class="filterable-cell">INPROGRESS</td>
						    </tr>
						    </c:forEach>
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
					<span class="dbheading">Bandwidth</span><span style="float: right"><a href="javascript:showBandWidth();">View summary</a></span>
				</div>
			</div>
			<div class="panel-body padding-0">
				<div class="dbcontentbox" style="height: 540px;">
					<div class="fixedheadertable">
					<table class="table">
				    <thead>
				    <tr>
				    	<th style="width: 50%;text-align: center;border-bottom: 0;"><span style="font-size: 11px;color: #57BF9E;font-weight: bold;">All (45)</span></th>
				        <th style="width: 25%; text-align: center;border-bottom: 0;"><span style="font-size: 8px">Last Day</span></th>
				        <th style="width: 25%; text-align: center;border-bottom: 0;"><span style="font-size: 8px">Today</span></th>
				    </tr>
				    </thead>
				    <tbody style="height: 210px" >
				    <c:forEach var="i" begin="0" end="30" step="1">
					    <tr style="padding-bottom: 0px;margin-top: 0px; border-bottom: 1px solid #dddddd">
					        <td class="filterable-cell" style="width: 60%;padding: 0px;border-top:0">
					        <span class="userdetails">
					        	<img  style="width: 30px;border-radius: 20px;" alt="image" src="${assets}/images/users/user-online.png"  onerror="this.src='${assets}/images/users/no-image.jpg'"></a>
					        </span><b>test user</b></td>
					        <td class="filterable-cell emputilization" style="background-color: #ececec;border-top:0"><span class="percentage">${i} %</span></td>
					        <td class="filterable-cell emputilization" style="background-color: #d7d7d7;border-top:0"><span class="percentage">--%</span></td>
					    </tr>
					</c:forEach>
				    </tbody>
				    </table>
					</div>
					<div class="fixedheadertable">
					<table class="table">
				    <thead>
				    <tr>
				    	<th style="width: 100%;border-bottom:0"><span style="font-size: 13px;color: red;font-weight: 500;">On Leave (45)</span></th>
				    </tr>
				    </thead>
				    <tbody style="height: 80px" >
				    <c:forEach var="i" begin="0" end="30" step="1">
					    <tr style="padding-bottom: 0px;margin-top: 0px; border-bottom: 1px solid #dddddd">
					        <td class="filterable-cell" style="width: 60%;padding: 0px;;border-top:0">
					        <span class="userdetails" style="margin-bottom: 5px">
					        <img  style="width: 30px;border-radius: 20px;" alt="image" src="${assets}/images/users/user-offline.png"  onerror="this.src='${assets}/images/users/no-image.jpg'"></a>
					        </span><b>test user</b></td>
					    </tr>
					</c:forEach>
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