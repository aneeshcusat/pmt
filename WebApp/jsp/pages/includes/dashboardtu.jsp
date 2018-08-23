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
					<div class="dbcontentbox" style="height: 150px;">
					<div class="col-md-12 center-block" style="height: 10px;text-align: center;">
						<span style="background-color: #0BB4C1;" class="legend"></span><span class="legendtext">Billable</span>
						<span style="background-color: #E3E3E3;" class="legend"></span><span class="legendtext">Non Billable</span>
						<span style="float: right;">
							<span class="legenddateview">Day</span>
							<span class="legenddateview">Week</span>
							<span class="legenddateview">Month</span>
						</span>
					</div>
					<div class="tatalutilizationChart" style="height: 140px">
						<div class="col-md-12">
						<div class="row">
							  <c:forEach var="i" begin="0" end="22" step="1">
							  <div class="myprogress">
								  <p class="vprogresslabel">${i}%</p>
								  <div class="progress progress-bar-vertical">
								    <div class="progress-bar" role="progressbar" aria-valuenow="30" aria-valuemin="0" aria-valuemax="100" style="height: 30%;">
								      <span class="sr-only">30% Complete</span>
								    </div>
								  </div> 
								  <p class="vprogresslabel">Name${1}</p>
							  </div>
							  </c:forEach>
							   <!-- <div class="col-xs-1">
						            <p class="progressTitle">Probe</p>
						            <div class="progress progress-bar-vertical">
						                <div class="progress-bar" role="progressbar" aria-valuenow="30" aria-valuemin="0" aria-valuemax="100" style="height: 32%;">
						                </div>
						            </div>
						            <p class="valSpan">51</p>
						        </div> -->
							   </div>
					 	 </div>
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
					<div class="dbcontentbox" style="height: 380px;">
						<div class="calendar">                                
                    		<div id="fullcaledartu"></div>                            
                		</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
</div>