<%@include file="includes/header.jsp" %>
<c:set var="currentUserGroupId" value="${applicationScope.applicationConfiguraion.currentUserGroupId}"/>

<style>
.backgroundColor{
background-color: lightblue;
}

tr.clickable:hover {
    background-color: lightblue;
}
 #createAppModel .modal-dialog {
     width: 45%;
 }
 
 .nav-header {
    display: block;
    padding: 3px 15px;
    font-size: 11px;
    font-weight: bold;
    line-height: 20px;
    color: #999999;
    text-transform: uppercase;
    border-bottom: 0px !important;
}

.nav-header a {
    position: relative;
    float: right;
    padding: 0px 0px !important;
}

#applicationConfigDiv .tab-pane{
	margin-top:15px;
}

.table-borderless td,
.table-borderless th {
    border: 0;
}


.autoreporting ul {
  list-style-type: none;
  margin: 0;
  padding: 0;
  overflow: hidden;
}

.autoreporting li a {
  text-align: center;
  padding: 2px;
  text-decoration: none;
}

.error {
	border: 1px solid red;
}

</style>
 <ul class="breadcrumb">
     <li><a href="${applicationHome}/index">Application Config</a></li>  
     <li class="active">Account</li>
 </ul>

<div class="content-frame">            
    <!-- START CONTENT FRAME TOP -->
    <div class="content-frame-top">                        
        <div class="page-title">                    
            <h2><span class="fa fa-cog"></span> Application Configuration</h2>
        </div>  
    </div>
    <!-- END CONTENT FRAME TOP -->
    
    <!-- START CONTENT FRAME BODY -->
    <div class=" padding-bottom-0">
        
        <div class="row">
         
            <div class="col-md-2 margin10">
            <c:if test="${currentUser.userRole == 'SUPERADMIN'}">
    		<select class="form-control" data-live-search="true" id="userGroupSelection">
			    <option value="">- select -</option>
			    <c:forEach var="userGroup" items="${userGroupMap}" varStatus="userGroupIndex"> 
			      <option <c:if test="${currentUserGroupId == userGroup.value.userGroupId}">selected="selected"</c:if> value="${userGroup.value.userGroupId}">${userGroup.value.name}</option>
			    </c:forEach>
			</select>
    		</c:if>
			    <div class="well" style=" padding: 8px 0;">
			     <table class="table table data-table table-borderless">
			       <thead>
				   <tr style="font-weight:bold">
				        <th class="nav-header">Application config type</th>
				        <th><a data-toggle="modal" data-target="#createTypeModel" style="float:right;display: none"><i class="fa fa-plus fa-2x" aria-hidden="true" style="color:#95b75d"></i></a></th>
				    </tr>
				    </thead>
				   <tbody>
			   	 	<tr class="clickable hide">
			   	 		<td colspan="2"><a href="#tab1" data-toggle="tab" style="font-size: 14px" class="applicationTypeLink active"> <i class="icon-envelope"></i>Company Division</a></td>
				   	</tr>
				   	 <tr>
				   		<td colspan="2"><a href="#tab2" onclick="refreshProjectCategory()" data-toggle="tab" style="font-size: 14px" class="applicationTypeLink"> <i class="icon-envelope"></i>Project Categories</a></td>
				   	</tr>
				   	<tr>
				   		<td colspan="2"><a href="#tab21" onclick="refreshNewProjectCategory()" data-toggle="tab" style="font-size: 14px" class="applicationTypeLink"> <i class="icon-envelope"></i>New Project Categories</a></td>
				   	</tr>
				   	 <tr>
				   		<td colspan="2"><a href="#tab3" onclick="refreshNonBillableCategory()" data-toggle="tab" style="font-size: 14px" class="applicationTypeLink"> <i class="icon-envelope"></i>Non-billable Categories</a></td>
				   	</tr>
				   	<tr>
				   		<td colspan="2"><a href="#tab4" data-toggle="tab" style="font-size: 14px" class="applicationTypeLink"> <i class="icon-envelope"></i>Reports</a></td>
				   	</tr>
				   	<tr>
				   		<td colspan="2"><a href="#tab5" data-toggle="tab" style="font-size: 14px" class="applicationTypeLink"> <i class="icon-envelope"></i>Dashboard View</a></td>
				   	</tr>
				   	<tr>
				   		<td colspan="2"><a href="#tab6" data-toggle="tab" style="font-size: 14px" class="applicationTypeLink"> <i class="icon-envelope"></i>Weekly Time Log</a></td>
				   	</tr>
				   	<tr>
				   		<td colspan="2"><a href="#tab7" data-toggle="tab" style="font-size: 14px" class="applicationTypeLink"> <i class="icon-envelope"></i>Project Config</a></td>
				   	</tr>
				   	
				   	 <tr class="">
				   		<td colspan="2"><a href="#tab8" onclick="refreshAutoReportingConfig();" data-toggle="tab" style="font-size: 14px" class="applicationTypeLink"> <i class="icon-envelope"></i>Automated Reporting config</a></td>
				   	</tr>
			   	 	</tbody>
			     </table>
				</div>
			</div>

			<div class="col-md-10" id="applicationConfigDiv"
				style="box-shadow: 5px 5px 20px #888888; margin-top: 10px">
				<div class="tab-content">
					<div class='row tab-pane ' id="tab1">
						<table class="table table table-bordered data-table">
							<thead>
								<tr style="font-weight: bold">
									<th colspan="2">Company Division</th>
									<th width="70px" style="text-align: center"><a
										data-toggle="modal" data-target="#createaccountmodal"
										parentid="0" onclick="initializeWidget('account', this);"><i
											class="fa fa-plus fa-2x" aria-hidden="true"
											style="color: #95b75d;float:right"></i></a></th>
								</tr>
							<thead>
							<tbody>
								<tr>
									<td>Name</td>
									<td>Value</td>
									<td><a href="#" data-box="#confirmationbox" class="mb-control" style="float:right"><i class="fa fa-trash-o fa-2x" style="color:red" aria-hidden="true"></i></a></td>
								</tr>
							</tbody>
						</table>
					</div>
					<div class='row tab-pane active' id="tab2">
						<table class="table table table-bordered data-table">
							<thead>
								<tr style="font-weight: bold">
									<th>Project Categories</th>
									<th width="70px" style="text-align: center"><a
										data-toggle="modal" data-target="#createAppModel"  onclick="setAppConfigAction('createProjectCategory();')"
										parentid="0"><i
											class="fa fa-plus fa-2x" aria-hidden="true"
											style="color: #95b75d;float:right"></i></a></th>
								</tr>
							<thead>
								<tbody id="projectCategoryDiv">
									<%@include file="response/appConfigProjectCategories.jsp"%>
							    </tbody>
						</table>
					</div>
					
					<div class='row tab-pane' id="tab21">
						<table class="table table table-bordered data-table">
							<thead>
								<tr style="font-weight: bold">
									<th>New Project Categories</th>
									<th width="70px" style="text-align: center"><a
										data-toggle="modal" data-target="#createAppModel"  onclick="setAppConfigAction('createNewProjectCategory();')"
										parentid="0"><i
											class="fa fa-plus fa-2x" aria-hidden="true"
											style="color: #95b75d;float:right"></i></a></th>
								</tr>
							<thead>
								<tbody id="newProjectCategoryDiv">
									<%@include file="response/appConfigNewProjectCategories.jsp"%>
							    </tbody>
						</table>
					</div>
					
					<div class='row tab-pane' id="tab3">
						<table class="table table table-bordered data-table nonBillableCategoryTypeDiv">
							<thead>
								<tr style="font-weight: bold">
									<th>Non-Billable Type</th>
									<th width="70px" style="text-align: center"><a
										data-toggle="modal" data-target="#createAppModel" onclick="setAppConfigAction('createNonBillableCategory();')"
										parentid="0"><i
											class="fa fa-plus fa-2x" aria-hidden="true"
											style="color: #95b75d;float:right"></i></a></th>
								</tr>
							<thead>
								<tbody id="nonBillableCategoryDiv">
									<%@include file="response/appConfigNonBillableCategories.jsp"%>
							    </tbody>
						</table>
						<table class="table table table-bordered data-table">
							<thead>
								<tr style="font-weight: bold">
									<th colspan="2">Static Non-Billable categories</th>
								</tr>
							<thead>
							<tbody>
								<tr>
									<td>
									<select class="form-control select" id="staticNonBillableCategorySelected">
										<option value="disabled">Disabled</option>
										<option value="enabled">Enabled</opiton>
									</select>
									</td>
									<td width="70px"><a href="#" onclick="staticNonBillableCategoryEnabled();" style="float:right"><i class="fa fa-save fa-2x" style="color:blue" aria-hidden="true"></i></a></td>
								</tr>
							</tbody>
						</table>
						<table class="table table table-bordered data-table staticNonBillableCategoryTypeDiv">
							<thead>
								<tr style="font-weight: bold">
									<th>Static Non-Billable Type</th>
									<th width="70px" style="text-align: center">
									<c:if test="${currentUserGroupId == '99999' }">
									<a
										data-toggle="modal" data-target="#createAppModel" onclick="setAppConfigAction('createStaticNonBillableCategory();')"
										parentid="0"><i
											class="fa fa-plus fa-2x" aria-hidden="true"
											style="color: #95b75d;float:right"></i></a>
											</c:if>
											</th>
								</tr>
							<thead>
								<tbody id="staticNonBillableCategoryDiv">
									<%@include file="response/appConfigStaticNonBillableCategories.jsp"%>
							    </tbody>
						</table>
			
					</div>
					
					<div class='row tab-pane ' id="tab4">
						<table class="table table table-bordered data-table">
							<thead>
								<tr style="font-weight: bold">
									<th colspan="2">Report</th>
								</tr>
							<thead>
							<tbody>
								<tr>
									<td>
									<select class="form-control select" id="reportingSelectId">
										<option value="default">Default</option>
										<option value="format1">Project Reporting - Format 1</opiton>
										<option value="format2">Project Reporting - Format 2</opiton>
										<option value="format3">Project Reporting - Format 3</opiton>
									</select>
									</td>
									<td width="70px"><a href="#" onclick="createReportingApplicationConfig()" style="float:right"><i class="fa fa-save fa-2x" style="color:blue" aria-hidden="true"></i></a></td>
								</tr>
							</tbody>
						</table>
					</div>
					<div class='row tab-pane ' id="tab5">
						<table class="table table table-bordered data-table">
							<thead>
								<tr style="font-weight: bold">
									<th colspan="2">Dashboard View</th>
								</tr>
							<thead>
							<tbody>
								<tr>
									<td>
									<select class="form-control select" id="dashboardSelectId">
										<option value="dashboard">Default</option>
										<option value="newindex">Dashboard 1</opiton>
										<!-- <option value="newdashboard">Dashboard 2</opiton> -->
									</select>
									</td>
									<td width="70px"><a href="#" onclick="createDashboardApplicationConfig()" style="float:right"><i class="fa fa-save fa-2x" style="color:blue" aria-hidden="true"></i></a></td>
								</tr>
							</tbody>
						</table>
					</div>
					<div class='row tab-pane ' id="tab6">
						<table class="table table table-bordered data-table">
							<thead>
								<tr style="font-weight: bold">
									<th colspan="2">Weekly Time Log</th>
								</tr>
							<thead>
							<tbody>
								<tr>
									<td>
									<select class="form-control select" id="weeklyTimeLogSelectId">
										<option value="disabled">Disabled</option>
										<option value="enabled">Enabled</opiton>
									</select>
									</td>
									<td width="70px"><a href="#" onclick="createWeeklyTimeLog();" style="float:right"><i class="fa fa-save fa-2x" style="color:blue" aria-hidden="true"></i></a></td>
								</tr>
							</tbody>
						</table>
						<table class="table table table-bordered data-table">
							<thead>
								<tr style="font-weight: bold">
									<th colspan="2">New Task create</th>
								</tr>
							<thead>
							<tbody>
								<tr>
									<td>
									<select class="form-control select" id="weeklyTimeLogNewTaskSelectId">
										<option value="enabled">Enabled</option>
										<option value="disabled">Disabled</opiton>
									</select>
									</td>
									<td width="70px"><a href="#" onclick="createWeeklyTimeLogNewTask();" style="float:right"><i class="fa fa-save fa-2x" style="color:blue" aria-hidden="true"></i></a></td>
								</tr>
							</tbody>
						</table>
						<table class="table table table-bordered data-table">
							<thead>
								<tr style="font-weight: bold">
									<th colspan="2">Disable month after last working day</th>
								</tr>
							<thead>
							<tbody>
								<tr>
									<td>
									<select class="form-control select" id="weekTLDisableMonthSelectId">
										<option value="disabled">Disabled</option>
										<option value="enabled">Enabled</opiton>
									</select>
									</td>
									<td width="70px"><a href="#" onclick="createweekTLDisableMonth();" style="float:right"><i class="fa fa-save fa-2x" style="color:blue" aria-hidden="true"></i></a></td>
								</tr>
							</tbody>
						</table>
					</div>
					<div class='row tab-pane ' id="tab7">
						<table class="table table table-bordered data-table">
							<thead>
								<tr style="font-weight: bold">
									<th colspan="2">Project Task Activity Delete</th>
								</tr>
							<thead>
							<tbody>
								<tr>
									<td>
									<select class="form-control select" id="projectTaskActivitySelectId">
										<option value="disabled">Disabled</option>
										<option value="enabled">Enabled</opiton>
									</select>
									</td>
									<td width="70px"><a href="#" onclick="projectTaskActivityDelete();" style="float:right"><i class="fa fa-save fa-2x" style="color:blue" aria-hidden="true"></i></a></td>
								</tr>
							</tbody>
						</table>
						<table class="table table table-bordered data-table">
							<thead>
								<tr style="font-weight: bold">
									<th colspan="2">Recurring Project From, Default</th>
								</tr>
							<thead>
							<tbody>
								<tr>
									<td>
									<select class="form-control select" id="projectRecurringByCodeSelectId">
										<option value="disabled">Original Project</option>
										<option value="enabled">Latest Project</opiton>
									</select>
									</td>
									<td width="70px"><a href="#" onclick="projectRecurringByCode();" style="float:right"><i class="fa fa-save fa-2x" style="color:blue" aria-hidden="true"></i></a></td>
								</tr>
							</tbody>
						</table>
						
					     <table class="table table table-bordered data-table">
							<thead>
								<tr style="font-weight: bold">
									<th colspan="2">Assignee for Project Quick cloning</th>
								</tr>
							<thead>
							<tbody>
								<tr>
									<td>
									<select class="form-control select" id="assignManForQckCloneSelectId">
										<option value="enabled">Mandatory</option>
										<option value="disabled">Optional</opiton>
									</select>
									</td>
									<td width="70px"><a href="#" onclick="assignManForQckClone();" style="float:right"><i class="fa fa-save fa-2x" style="color:blue" aria-hidden="true"></i></a></td>
								</tr>
							</tbody>
						</table>
						
						<table class="table table table-bordered data-table">
							<thead>
								<tr style="font-weight: bold">
									<th colspan="2">Project Task Create Restriction for Managers and below roles</th>
								</tr>
							<thead>
							<tbody>
								<tr>
									<td>
									<select class="form-control select" id="sameDayOnlyTaskEnabledSelectId">
										<option value="disabled">No restriction</opiton>
										<option value="enabled">Same day only</option>
									</select>
									</td>
									<td width="70px"><a href="#" onclick="sameDayOnlyTask();" style="float:right"><i class="fa fa-save fa-2x" style="color:blue" aria-hidden="true"></i></a></td>
								</tr>
							</tbody>
						</table>
						
					</div>
					
					<div class='row tab-pane' id="tab8">
						<table class="table table table-bordered data-table">
							<thead>
								<tr style="font-weight: bold">
									<th colspan="2">Automatic Reporting config</th>
								</tr>
							<thead>
							<tbody>
								<tr>
									<td>
										<%@include file="response/autoReportingConfig.jsp"%>
									</td>
								</tr>
								<tr>
									<td class="autoReportingSavedDataDiv">
										<%-- <%@include file="response/autoReportingConfigSaved.jsp"%> --%>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>                    
    <!-- END CONTENT FRAME BODY -->
</div>       
<div class="modal fade" id="createAppModel" tabindex="-1"
     role="dialog" aria-labelledby="reprocessConfirmation"
     aria-hidden="true">
     <div class="modal-dialog" role="document">
         <div class="modal-content">
             <div class="modal-header">
                 <button type="button" class="close" data-dismiss="modal"
                         aria-label="Close">
                     <span aria-hidden="true">&times;</span>
                 </button>
                 <h4 class="modal-title" id="myModalLabel">Categories</h4>
             </div>
             <div class="modal-body">
                 <%@include file="fagments/createAppConfModal.jspf" %>
             </div>
             <div class="modal-footer">
                 <button type="button" class="btn btn-secondary"
                         data-dismiss="modal">
                     Cancel
                 </button>
                 <a id="createOrUpdateId" href="#" onclick="createApplicationConfig('projectCategory');" class="btn btn-primary"><span id="saveButton">Save</span></a>
             </div>
         </div>
     </div>
</div>
        
<!-- END CONTENT FRAME -->                                
 <%@include file="includes/footer.jsp" %> 
 <script type="text/javascript"
	src="${js}/plugins/bootstrap/bootstrap-select.js?v=${fsVersionNumber}"></script>
 
 <c:set var="projectReporting" value='reporting${currentUserGroupId}'/>   
  <c:if test="${not empty appConfigMap[projectReporting] && not empty appConfigMap[projectReporting].appConfValueDetails}">
  <c:forEach var="projectReportingConf" items="${appConfigMap[projectReporting].appConfValueDetails}">
  	 <script type="text/javascript">
  	 	$("#reportingSelectId").val('${projectReportingConf.value}');
  	 	//$("#reportingSelectId").refresh();
  	 </script>
  </c:forEach>
  </c:if>
  
  <c:set var="dashboardView" value='dashboardview${currentUserGroupId}'/>   
  <c:if test="${not empty appConfigMap[dashboardView] && not empty appConfigMap[dashboardView].appConfValueDetails}">
  <c:forEach var="dashboardViewConf" items="${appConfigMap[dashboardView].appConfValueDetails}">
  	 <script type="text/javascript">
  	 	$("#dashboardSelectId").val('${dashboardViewConf.value}');
  	 	//$("#reportingSelectId").refresh();
  	 </script>
  </c:forEach>
  </c:if>
  
   <c:set var="weeklyTimeLog" value='weeklyTimeLog${currentUserGroupId}'/>   
  <c:if test="${not empty appConfigMap[weeklyTimeLog] && not empty appConfigMap[weeklyTimeLog].appConfValueDetails}">
  <c:forEach var="weeklyTimeLogConf" items="${appConfigMap[weeklyTimeLog].appConfValueDetails}">
  	 <script type="text/javascript">
  	 	$("#weeklyTimeLogSelectId").val('${weeklyTimeLogConf.value}');
  	 </script>
  </c:forEach>
  </c:if>
  
     <c:set var="weeklyTimeLogNewTask" value='weeklyTimeLogNewTask${currentUserGroupId}'/>   
  <c:if test="${not empty appConfigMap[weeklyTimeLogNewTask] && not empty appConfigMap[weeklyTimeLogNewTask].appConfValueDetails}">
  <c:forEach var="weeklyTimeLogNewTaskConf" items="${appConfigMap[weeklyTimeLogNewTask].appConfValueDetails}">
  	 <script type="text/javascript">
  	 	$("#weeklyTimeLogNewTaskSelectId").val('${weeklyTimeLogNewTaskConf.value}');
  	 </script>
  </c:forEach>
  </c:if>
  
     <c:set var="projectRecurringByCode" value='projectRecurringByCode${currentUserGroupId}'/>   
  <c:if test="${not empty appConfigMap[projectRecurringByCode] && not empty appConfigMap[projectRecurringByCode].appConfValueDetails}">
  <c:forEach var="projectRecurringByCodeConf" items="${appConfigMap[projectRecurringByCode].appConfValueDetails}">
  	 <script type="text/javascript">
  	 	$("#projectRecurringByCodeSelectId").val('${projectRecurringByCodeConf.value}');
  	 </script>
  </c:forEach>
  </c:if>
  
       <c:set var="assignManForQckClone" value='assignManForQckClone${currentUserGroupId}'/>   
  <c:if test="${not empty appConfigMap[assignManForQckClone] && not empty appConfigMap[assignManForQckClone].appConfValueDetails}">
  <c:forEach var="assignManForQckCloneConf" items="${appConfigMap[assignManForQckClone].appConfValueDetails}">
  	 <script type="text/javascript">
  	 	$("#assignManForQckCloneSelectId").val('${assignManForQckCloneConf.value}');
  	 </script>
  </c:forEach>
  </c:if>

   <c:set var="projectTaskActivitySelectId" value='projectTaskActivityDelete${currentUserGroupId}'/>   
  <c:if test="${not empty appConfigMap[projectTaskActivitySelectId] && not empty appConfigMap[projectTaskActivitySelectId].appConfValueDetails}">
  <c:forEach var="projectTaskActivityDeleteConf" items="${appConfigMap[projectTaskActivitySelectId].appConfValueDetails}">
  	 <script type="text/javascript">
  	 	$("#projectTaskActivitySelectId").val('${projectTaskActivityDeleteConf.value}');
  	 </script>
  </c:forEach>
  </c:if>
  
   <c:set var="sameDayOnlyTaskEnabledSelectId" value='sameDayOnlyTask${currentUserGroupId}'/>   
  <c:if test="${not empty appConfigMap[sameDayOnlyTaskEnabledSelectId] && not empty appConfigMap[sameDayOnlyTaskEnabledSelectId].appConfValueDetails}">
  <c:forEach var="sameDayOnlyTaskConf" items="${appConfigMap[sameDayOnlyTaskEnabledSelectId].appConfValueDetails}">
  	 <script type="text/javascript">
  	 	$("#sameDayOnlyTaskEnabledSelectId").val('${sameDayOnlyTaskConf.value}');
  	 </script>
  </c:forEach>
  </c:if>
    <c:set var="weekTLDisableMonthSelectId" value='weekTLDisableMonth${currentUserGroupId}'/>   
  <c:if test="${not empty appConfigMap[weekTLDisableMonthSelectId] && not empty appConfigMap[weekTLDisableMonthSelectId].appConfValueDetails}">
  <c:forEach var="weekTLDisableMonthConf" items="${appConfigMap[weekTLDisableMonthSelectId].appConfValueDetails}">
  	 <script type="text/javascript">
  	 	$("#weekTLDisableMonthSelectId").val('${weekTLDisableMonthConf.value}');
  	 </script>
  </c:forEach>
  </c:if>
  <c:set var="staticNonBillableCategorySelected" value='staticNonBillableCategoryEnabled${currentUserGroupId}'/>   
  <c:if test="${not empty appConfigMap[staticNonBillableCategorySelected] && not empty appConfigMap[staticNonBillableCategorySelected].appConfValueDetails}">
  <c:forEach var="staticNonBillableCategoryConf" items="${appConfigMap[staticNonBillableCategorySelected].appConfValueDetails}">
  	 <script type="text/javascript">
  	 	$("#staticNonBillableCategorySelected").val('${staticNonBillableCategoryConf.value}');
  	 </script>
  	 <c:if test="${staticNonBillableCategoryConf.value == 'enabled'}">
  	 <script type="text/javascript">
  	 	$(".nonBillableCategoryTypeDiv").addClass("hide");
  	 	$(".statiNonBillableCategoryTypeDiv").removeClass("hide");
  	 </script>
  	 </c:if>
	 <c:if test="${staticNonBillableCategoryConf.value == 'disabled'}">
  	 <script type="text/javascript">
  	 	$(".nonBillableCategoryTypeDiv").removeClass("hide");
  	 	$(".staticNonBillableCategoryTypeDiv").addClass("hide");
  	 </script>
  	 </c:if>  	 
  	 
  </c:forEach>
  </c:if>
 
 
 
 
 <script type="text/javascript">
 
 function setAppConfigAction(callbackFunctionName){
	 $("#createOrUpdateId").attr("onclick",callbackFunctionName);
 }
 
 function createProjectCategory(){
	 createApplicationConfig('projectCategory');
 }
 
 function createNewProjectCategory(){
	 createApplicationConfig('newProjectCategory');
 }

 function createNonBillableCategory(){
	 createApplicationConfig('nonBillableCategory');
 }
 
 function createStaticNonBillableCategory(){
	 createApplicationConfig('staticNonBillableCategory');
 }
 
 function deleteApplicationConfigVal(name, id, type){
		$(".msgConfirmText").html("Delete " + type);
		$(".msgConfirmText1").html(name);
		$("#confirmYesId").prop("href","javascript:doAjaxDeleteAppConfigVal("+id+")");
 }
 
 function createApplicationConfig(type){
	 var input1 = $("#firstInputId").val();
	 var input2 = $("#secondInputId").val();
	 var dataString = {input1: input1, input2: input1,type: type};
	 doAjaxRequest("POST", "${applicationHome}/createAppConfValue", dataString ,function(data) {
		 refreshProjectCategory();
		 refreshNewProjectCategory();
		 refreshNonBillableCategory();
		 $('#createAppModel').modal('hide');
	    },function(error) {
	    	famstacklog("ERROR: ", error);
	    });
 }
 
 function createReportingApplicationConfig(){
	 var input1 = $("#reportingSelectId").val();
	 var type = "reporting";
	 var dataString = {input1: input1, input2: input1,type: type};
	 doAjaxRequest("POST", "${applicationHome}/updateAppConfValue", dataString ,function(data) {
	    },function(error) {
	    	famstacklog("ERROR: ", error);
	    });
 }
 
 function createDashboardApplicationConfig(){
	 var input1 = $("#dashboardSelectId").val();
	 var type = "dashboardview";
	 var dataString = {input1: input1, input2: input1,type: type};
	 doAjaxRequest("POST", "${applicationHome}/updateAppConfValue", dataString ,function(data) {
	    },function(error) {
	    	famstacklog("ERROR: ", error);
	    });
 }
 
 
 function createWeeklyTimeLog(){
	 var input1 = $("#weeklyTimeLogSelectId").val();
	 var type = "weeklyTimeLog";
	 var dataString = {input1: input1, input2: input1,type: type};
	 doAjaxRequest("POST", "${applicationHome}/updateAppConfValue", dataString ,function(data) {
	    },function(error) {
	    	famstacklog("ERROR: ", error);
	    });
 }

 function createWeeklyTimeLogNewTask(){
	 var input1 = $("#weeklyTimeLogNewTaskSelectId").val();
	 var type = "weeklyTimeLogNewTask";
	 var dataString = {input1: input1, input2: input1,type: type};
	 doAjaxRequest("POST", "${applicationHome}/updateAppConfValue", dataString ,function(data) {
	    },function(error) {
	    	famstacklog("ERROR: ", error);
	    });
 }
 
 function createweekTLDisableMonth(){
	 var input1 = $("#weekTLDisableMonthSelectId").val();
	 var type = "weekTLDisableMonth";
	 var dataString = {input1: input1, input2: input1,type: type};
	 doAjaxRequest("POST", "${applicationHome}/updateAppConfValue", dataString ,function(data) {
	    },function(error) {
	    	famstacklog("ERROR: ", error);
	    });
 }

 function projectRecurringByCode(){
	 var input1 = $("#projectRecurringByCodeSelectId").val();
	 var type = "projectRecurringByCode";
	 var dataString = {input1: input1, input2: input1,type: type};
	 doAjaxRequest("POST", "${applicationHome}/updateAppConfValue", dataString ,function(data) {
	    },function(error) {
	    	famstacklog("ERROR: ", error);
	    });
 } 
 
 
 function staticNonBillableCategoryEnabled(){
	 var input1 = $("#staticNonBillableCategorySelected").val();
	 var type = "staticNonBillableCategoryEnabled";
	 var dataString = {input1: input1, input2: input1,type: type};
	 doAjaxRequest("POST", "${applicationHome}/updateAppConfValue", dataString ,function(data) {
		 
		 if(input1 == 'disabled') {
			 $(".nonBillableCategoryTypeDiv").removeClass("hide");
		  	 $(".staticNonBillableCategoryTypeDiv").addClass("hide");
		 } else {
			 $(".nonBillableCategoryTypeDiv").addClass("hide");
		  	 $(".staticNonBillableCategoryTypeDiv").removeClass("hide");
		 }
		 
	    },function(error) {
	    	famstacklog("ERROR: ", error);
	    });
 }
 

 function assignManForQckClone(){
	 var input1 = $("#assignManForQckCloneSelectId").val();
	 var type = "assignManForQckClone";
	 var dataString = {input1: input1, input2: input1,type: type};
	 doAjaxRequest("POST", "${applicationHome}/updateAppConfValue", dataString ,function(data) {
	    },function(error) {
	    	famstacklog("ERROR: ", error);
	    });
 } 

 function projectTaskActivityDelete(){
	 var input1 = $("#projectTaskActivitySelectId").val();
	 var type = "projectTaskActivityDelete";
	 var dataString = {input1: input1, input2: input1,type: type};
	 doAjaxRequest("POST", "${applicationHome}/updateAppConfValue", dataString ,function(data) {
	    },function(error) {
	    	famstacklog("ERROR: ", error);
	    });
 }
  
 function sameDayOnlyTask(){
	 var input1 = $("#sameDayOnlyTaskEnabledSelectId").val();
	 var type = "sameDayOnlyTask";
	 var dataString = {input1: input1, input2: input1,type: type};
	 doAjaxRequest("POST", "${applicationHome}/updateAppConfValue", dataString ,function(data) {
	    },function(error) {
	    	famstacklog("ERROR: ", error);
	    });
 }
 
 
 function doAjaxDeleteAppConfigVal(id) {
	 var dataString = {"id" : id};
		doAjaxRequest("POST", "${applicationHome}/deleteAppConfValue", dataString, function(data) {
         famstacklog("SUCCESS: ", data);
         var responseJson = JSON.parse(data);
         if (responseJson.status){
        	 refreshProjectCategory();
        	 refreshNewProjectCategory();
        	 refreshNonBillableCategory();
         }
         $(".message-box").removeClass("open");
     }, function(e) {
         famstacklog("ERROR: ", e);
     });
 }
 
 function refreshProjectCategory(){
	 doAjaxRequestWithGlobal("GET", "${applicationHome}/appConfigProjectCategories", {}, function(data) {
	        $("#projectCategoryDiv").html(data);
	    }, function(e) {
	        famstacklog("ERROR: ", e);
	    }, false);
 }
 
 function refreshNewProjectCategory(){
	 doAjaxRequestWithGlobal("GET", "${applicationHome}/appConfigNewProjectCategories", {}, function(data) {
	        $("#newProjectCategoryDiv").html(data);
	    }, function(e) {
	        famstacklog("ERROR: ", e);
	    }, false);
 }
 
 function refreshNonBillableCategory(){
	 doAjaxRequestWithGlobal("GET", "${applicationHome}/appConfigNonBillableCategories", {}, function(data) {
	        $("#nonBillableCategoryDiv").html(data);
    }, function(e) {
        famstacklog("ERROR: ", e);
    }, false);
	 
	 doAjaxRequestWithGlobal("GET", "${applicationHome}/appConfigStaticNonBillableCategories", {}, function(data) {
	        $("#staticNonBillableCategoryDiv").html(data);
 }, function(e) {
     famstacklog("ERROR: ", e);
 }, false);
 }
 
 
 
 $(document).ready(function(){
		/* MESSAGE BOX */
		$(document).on("click",".deleteAppConfValue",function(){
		    var box = $($(this).data("box"));
		    if(box.length > 0){
		        box.toggleClass("open");
		        
		        var sound = box.data("sound");
		        
		        if(sound === 'alert')
		            playAudio('alert');
		        
		        if(sound === 'fail')
		            playAudio('fail');
		        
		    }        
		    return false;
		});
		$(document).on("click",".mb-control-close",function(){
		   $(this).parents(".message-box").removeClass("open");
		   return false;
		});    
		/* END MESSAGE BOX */
	});

 function refreshAutoReportingConfig(){
	 doAjaxRequestWithGlobal("GET", "${applicationHome}/refreshAutoReportingConfig", {}, function(data) {
	        $(".autoReportingSavedDataDiv").html(data);
	    }, function(e) {
	        famstacklog("ERROR: ", e);
	    }, false);
 }

 function deleteAutoReportingConf(configId){
	 doAjaxRequestWithGlobal("POST", "${applicationHome}/deleteAutoReportingConfig", {id:configId}, function(data) {
	        $(".autoReportConf"+configId).remove();
	    }, function(e) {
	        famstacklog("ERROR: ", e);
	    }, false);
 }

 $(document).on("change",".autoReportEnabled",function(){
	 var configId = $(this).attr("data-configid");
	 var enabled = $(this).is(":checked");
	 
	 enableOrDisableAutoReportConfig(configId, enabled);
 });
 
 $(document).on("change",".autoReportNotifyDefaulters",function(){
	 var configId = $(this).attr("data-configid");
	 var enabled = $(this).is(":checked");
	 
	 enableOrDisableNotifyDefaulters(configId, enabled);
 });
 
 function enableOrDisableNotifyDefaulters(configId, enabled){
	 doAjaxRequestWithGlobal("POST", "${applicationHome}/enableOrDisableNotifyDefaulters", {id:configId,enable:enabled}, function(data) {
     }, function(e) {
        famstacklog("ERROR: ", e);
    }, false);
 }
 
 function enableOrDisableAutoReportConfig(configId, enabled){
	 doAjaxRequestWithGlobal("POST", "${applicationHome}/enableOrDisableAutoReportingConfig", {id:configId,enable:enabled}, function(data) {
     }, function(e) {
        famstacklog("ERROR: ", e);
    }, false);
 }
 
 function addEmailAutoReportingConfig(configId, type){
	var email =  $(".email"+type+configId).val();
	doAjaxRequestWithGlobal("POST", "${applicationHome}/addEmailAutoReportingConfig", {id:configId,email:email, type:type}, function(data) {
		var emaildatalist = "";
		if (data != null && data != "") {
			var emailArray = data.split(",");
			
			$.each( emailArray, function( index, value ) {
				emaildatalist += '<li>'+value+'<a href="javascript:removeEmailAutoReportingConfig(\''+value+'\','+configId+',\''+type+'\')"><i class="fa fa-times" style="color: red" aria-hidden="true"></i></a></li>';
			});
		}
		$(".email"+type+"list"+configId).html(emaildatalist);
     }, function(e) {
        famstacklog("ERROR: ", e);
    }, false);
 }
 
 function removeEmailAutoReportingConfig(email, configId, type){
	doAjaxRequestWithGlobal("POST", "${applicationHome}/removeEmailAutoReportingConfig", {id:configId,email:email, type:type}, function(data) {
		var emaildatalist = "";
		if (data != null && data != "") {
			var emailArray = data.split(",");
			$.each( emailArray, function( index, value ) {
				emaildatalist += '<li>'+value+'<a href="javascript:removeEmailAutoReportingConfig(\''+value+'\','+configId+',\''+type+'\')"><i class="fa fa-times" style="color: red" aria-hidden="true"></i></a></li>';
			});
		}
		$(".email"+type+"list"+configId).html(emaildatalist);
     }, function(e) {
        famstacklog("ERROR: ", e);
    }, false);
 }

 function createAutoReportingConfig(){
	
	 var reportName = $(".autoReportName").val();
	 var reportType = $(".autoReportType").val();
	 var startDate = $(".autoReportStartDay").val();
	 var howmanyPreDay = $(".autoReportHowmany").val();
	 var cronDays = $(".autoReportDaySelection").val();
	 var cronTimes = $(".autoReportTimeSelection").val();
	 var configId = $(".autoReportingConfigId").val();
	 var autoReportSubject = $(".autoReportSubject").val();
	 var endDate =$(".autoReportingEndDate").val();; 
     var cron ="0 0 "+cronTimes+" ? * "+cronDays+" *";
     var error = false;
     $(".autoReportName").removeClass("error");
     $(".removeReportTimeSelection").addClass("error");
     if (reportName == "") {
    	 $(".autoReportName").addClass("error");
    	 error = true;
     }
 	 if (cronTimes == "") {
 		 $(".autoReportTimeSelection").addClass("error");
 		error = true;
     }
 	 if (!error) {
	 var dataString = {name: reportName, type: reportType,startDate: startDate,startDate: startDate,previousDate: howmanyPreDay,subject: autoReportSubject,cron: cron,endDate:endDate,configId:configId};
	 doAjaxRequest("POST", "${applicationHome}/createAutoReportingConfig", dataString ,function(data) {
		 refreshAutoReportingConfig();
		 cleareditautoreporting();
	    },function(error) {
	    	famstacklog("ERROR: ", error);
	    });
 	}
 }
 
 $(".autoReportStartDay").on("change",function(){
	 var selectedIndex = $(this).prop("selectedIndex");
	 $(".autoReportHowmany").prop("selectedIndex",selectedIndex);
	 var index = 0;
	 $('.autoReportHowmany option').each(function() {
		 if (index< selectedIndex) {
			 $(this).prop('disabled', true);
		 } else {
			 $(this).prop('disabled', false);
		 }
		 index ++;
		});
 });
 
 $('.autoReportingEndDate').datetimepicker({
		timepicker:false,
		formatDate:'Y/m/d',
		format:'Y/m/d',
		minDate:new Date()
	});
 
 function editautoreporting(configId){
	 $(".autoReportName").val($(".reportingName"+configId).val());
	 $(".autoReportType").val( $(".reportingType"+configId).val());
	 $(".autoReportStartDay").val( $(".reportingStartDay"+configId).val());
	 $(".autoReportHowmany").val( $(".reportingPreDay"+configId).val());
	 $(".autoReportDaySelection").val( $(".reportingScheduleDay"+configId).val());
	 $(".autoReportTimeSelection").val( $(".reportingScheduleTime"+configId).val());
	 $(".autoReportingConfigId").val(configId);
	 $(".autoReportSubject").val( $(".reportingSubject"+configId).val());
	 $(".autoReportingEndDate").val( $(".reportingEndDate"+configId).val());
	 
	 $(".clearAutoReporting").removeClass("hide");
	 $(".saveAutoReporting").html("Update");
 }
 
 function cleareditautoreporting(){
	 $(".autoReportName").val("");
	 $(".autoReportType").prop("selectedIndex", 0);
	 $(".autoReportStartDay").prop("selectedIndex", 0);
	 $(".autoReportHowmany").prop("selectedIndex", 0);
	 $(".autoReportDaySelection").prop("selectedIndex", 0);
	 $(".autoReportTimeSelection").val("");
	 $(".autoReportingConfigId").val("");
	 $(".autoReportSubject").val("");
	 $(".autoReportingEndDate").val("");
	 $(".clearAutoReporting").addClass("hide");
	 $(".saveAutoReporting").html("Save");
 }

 function triggerautoreporting(reportId){
	 doAjaxRequestWithGlobal("GET", "${applicationHome}/sendAutoReportEmail", {reportId:reportId}, function(data) {
	    }, function(e) {
	        famstacklog("ERROR: ", e);
	    }, false);
 }

 
 $(".autoReportTimeSelection").on("keypress keyup", function(){
	 var value = $(this).val();
	 value = value.replace(/[^0-9,]/g, '');
	 $(this).val(value);
 })
 </script>
