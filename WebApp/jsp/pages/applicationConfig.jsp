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
         
            <div class="col-md-3 margin10">
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
				   	 <tr class="clickable">
				   		<td colspan="2"><a href="#tab2" onclick="refreshProjectCategory()" data-toggle="tab" style="font-size: 14px" class="applicationTypeLink"> <i class="icon-envelope"></i>Project Categories</a></td>
				   	</tr>
				   	<tr class="clickable">
				   		<td colspan="2"><a href="#tab3" data-toggle="tab" style="font-size: 14px" class="applicationTypeLink"> <i class="icon-envelope"></i>Reports</a></td>
				   	</tr>
			   	 	</tbody>
			     </table>
				</div>
			</div>

			<div class="col-md-7" id="applicationConfigDiv"
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
								<tr class="clickable">
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
									<th>Project Type</th>
									<th width="70px" style="text-align: center"><a
										data-toggle="modal" data-target="#createAppModel"
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
					<div class='row tab-pane ' id="tab3">
						<table class="table table table-bordered data-table">
							<thead>
								<tr style="font-weight: bold">
									<th colspan="2">Report</th>
								</tr>
							<thead>
							<tbody>
								<tr class="clickable">
									<td>
									<select class="form-control select" id="reportingSelectId">
										<option value="default">Default</option>
										<option value="format1">Format 1</opiton>
									</select>
									</td>
									<td width="70px"><a href="#" onclick="createReportingApplicationConfig()" style="float:right"><i class="fa fa-save fa-2x" style="color:blue" aria-hidden="true"></i></a></td>
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
                 <h4 class="modal-title" id="myModalLabel">Project Category</h4>
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
	src="${js}/plugins/bootstrap/bootstrap-select.js"></script>
 
 <c:set var="projectReporting" value='reporting${currentUserGroupId}'/>   
  <c:if test="${not empty appConfigMap[projectReporting] && not empty appConfigMap[projectReporting].appConfValueDetails}">
  <c:forEach var="projectReportingConf" items="${appConfigMap[projectReporting].appConfValueDetails}">
  	 <script type="text/javascript">
  	 	$("#reportingSelectId").val('${projectReportingConf.value}');
  	 	$("#reportingSelectId").refresh();
  	 </script>
  </c:forEach>
  </c:if>
 
 <script type="text/javascript">
 
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
		 refreshProjectCategory();
		 $('#createAppModel').modal('hide');
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
 </script>
