<%@include file="includes/header.jsp" %>

<style>
.backgroundColor{
background-color: lightblue;
}

tr.clickable:hover {
    background-color: lightblue;
}
 #createaccountmodal .modal-dialog {
     width: 55%;
 }
</style>
 <ul class="breadcrumb">
     <li><a href="${applicationHome}/index">Home</a></li>  
     <li class="active">Account</li>
 </ul>

<div class="content-frame">            
    <!-- START CONTENT FRAME TOP -->
    <div class="content-frame-top">                        
        <div class="page-title">                    
            <h2><span class="fa fa-calendar"></span> Account</h2>
        </div>  
    </div>
    <!-- END CONTENT FRAME TOP -->
    
    <!-- START CONTENT FRAME BODY -->
    <div class=" padding-bottom-0">
        
        <div class="row">
            <div class="col-md-5" id="accountDiv">
                 <table class="table table table-bordered data-table">
                 <thead>
				   <tr style="font-weight:bold">
				        <th >Account Name</th>
				        <th >Type</th>
				        <th >Holder</th>
				        <th width="70px" style="text-align:center"> <a data-toggle="modal" data-target="#createaccountmodal" parentid="0" onclick="initializeWidget('account', this);"><i class="fa fa-plus fa-2x" aria-hidden="true" style="color:#95b75d"></i></a></th>
				   </tr>
				   <thead>
				    <c:if test="${not empty accountData}">  
				   <tbody>
                      <c:forEach var="account" items="${accountData}"  varStatus="accountStatus">
                       <tr class="clickable" accountId="${account.accountId}">
                      	<td>${account.name}</td>
                      	<td>${account.type}</td>
                      	<td>${account.holder}</td>
                      	<td><a href="#"><i class="fa fa-pencil-square-o fa-2x" aria-hidden="true" style="color:blue"></i></a>&nbsp;&nbsp;<a href="#"><i class="fa fa-trash-o fa-2x" style="color:red" aria-hidden="true"></i></a></td>
							<span class="hide accountInfo" accountId="${account.accountId}">
							<span>${account.name}</span>
							<span>${account.type}</span>
							<span>${account.holder}</span>
							</span>
                      	
                      	</tr>
         			  </c:forEach>
         			  </tbody>
         			</c:if>
				 </table>
            </div>
            
             <div class="col-md-3" id="teamDiv">
             <h3>Team</h3>
              <div  style="overflow-y: scroll; overflow-x:hidden; max-height:450px">
                <table class="table table table-bordered">
				   <tr>
				        <th >Team Name</th>
				        <th >POC</th>
				        <th width="70px" style="text-align:center"><a onclick="initializeWidget('team', this);" data-toggle="modal" data-target="#createaccountmodal" id="teamAddLink" style="display:none"><i class="fa fa-plus fa-2x" aria-hidden="true" style="color:#95b75d"></i></a></th>
				   </tr>
				   <c:if test="${not empty accountData}">  
                     <c:forEach var="account" items="${accountData}">
                     <c:if test="${not empty account.projectTeams}">  
                       <c:forEach var="projectTeam" items="${account.projectTeams}">
                         <tr account="${account.accountId}" class="clickable" teamId="${projectTeam.teamId}">
                    		<td>${projectTeam.name}</td>
                   			<td>${projectTeam.poc}</td>
                   				<td><a href="#"><i class="fa fa-pencil-square-o fa-2x" aria-hidden="true" style="color:blue"></i></a>&nbsp;&nbsp;<a href="#"><i class="fa fa-trash-o fa-2x" style="color:red" aria-hidden="true"></i></a></td>
                   		 </tr>
                       </c:forEach>
                     
                     </c:if>
        			  </c:forEach>
        			</c:if>
				 </table>
				 </div>
            </div>
            
             <div class="col-md-4" id="subTeamDiv">
              <h3> Sub Team</h3>
               <div  style="overflow-y: scroll; overflow-x:hidden; max-height:450px">
                <table class="table table table-bordered">
				   <tr>
				        <th >Sub Team Name</th>
				        <th >PO ID</th>
				        <th width="70px" style="text-align:center"><a onclick="initializeWidget('subTeam', this);" data-toggle="modal" data-target="#createaccountmodal" id="subTeamAddLink" style="display:none"><i class="fa fa-plus fa-2x" aria-hidden="true" style="color:#95b75d"></i></a></th>
				   </tr>
					<c:if test="${not empty accountData}">  
                     <c:forEach var="account" items="${accountData}">
                     <c:if test="${not empty account.projectTeams}">  
                       <c:forEach var="projectTeam" items="${account.projectTeams}">
                        <c:if test="${not empty projectTeam.projectSubTeams}">  
                         <c:forEach var="projectSubTeam" items="${projectTeam.projectSubTeams}">
                         <tr account="${account.accountId}" team="${projectTeam.teamId}" subTeamId="${projectSubTeam.subTeamId}" class="clickable">
                         		<td>${projectSubTeam.name}</td>
                      			<td>${projectSubTeam.poId}</td>
                      				<td><a href="#"><i class="fa fa-pencil-square-o fa-2x" aria-hidden="true" style="color:blue"></i></a>&nbsp;&nbsp;<a href="#"><i class="fa fa-trash-o fa-2x" style="color:red" aria-hidden="true"></i></a></td>
                      			</tr>
                         </c:forEach>
                        </c:if>
                       </c:forEach>
                     </c:if>
        			  </c:forEach>
        			</c:if>
				 </table>
				 </div>
            </div>
              </div>
            <div class="row">
             <div class="col-md-10">
             <h3>Clients contacts</h3>
              <div  style="overflow-y: scroll; overflow-x:hidden; max-height:450px">
                <table class="table table table-striped table-bordered table-hover">
				   <tr>
				        <th >Client Name</th>
				         <th >Client Email</th>
				        <th width="70px" style="text-align:center"><a onclick="initializeWidget('client', this);" data-toggle="modal" data-target="#createaccountmodal" id="clientAddLink" style="display:none"><i class="fa fa-plus fa-2x" aria-hidden="true" style="color:#95b75d"></i></a></th>
				   </tr>
				   
				   <c:if test="${not empty accountData}">  
                     <c:forEach var="account" items="${accountData}">
                     <c:if test="${not empty account.projectTeams}">  
                       <c:forEach var="projectTeam" items="${account.projectTeams}">
                        <c:if test="${not empty projectTeam.projectSubTeams}">  
                         <c:forEach var="projectSubTeam" items="${projectTeam.projectSubTeams}">
                         		<c:if test="${not empty projectSubTeam.clientItems}">  
                         			<c:forEach var="client" items="${projectSubTeam.clientItems}">
                         			<tr account="${account.accountId}" team="${projectTeam.teamId}" subteam="${projectSubTeam.subTeamId}">
                         				<td>${client.name}</td>
                         				<td>${client.email}</td>
                         				<td><a href="#"><i class="fa fa-pencil-square-o fa-2x" aria-hidden="true" style="color:blue"></i></a>&nbsp;&nbsp;<a href="#"><i class="fa fa-trash-o fa-2x" style="color:red" aria-hidden="true"></i></a></td>
                         			</tr>
                         			</c:forEach>
                          </c:if>
                         </c:forEach>
                        </c:if>
                       </c:forEach>
                       </c:if>
                       </c:forEach>
                     </c:if>
				 </table>
				 </div>
            </div>
        </div>
        
    </div>                    
    <!-- END CONTENT FRAME BODY -->
</div>       
<div class="modal fade" id="createaccountmodal" tabindex="-1"
     role="dialog" aria-labelledby="reprocessConfirmation"
     aria-hidden="true">
     <div class="modal-dialog" role="document">
         <div class="modal-content">
             <div class="modal-header">
                 <button type="button" class="close" data-dismiss="modal"
                         aria-label="Close">
                     <span aria-hidden="true">&times;</span>
                 </button>
                 <h4 class="modal-title" id="myModalLabel"></h4>
             </div>
             <div class="modal-body">
                 <%@include file="fagments/createAccountModal.jspf" %>
             </div>
             <div class="modal-footer">
                 <button type="button" class="btn btn-secondary"
                         data-dismiss="modal">
                     Cancel
                 </button>
                 <a id="createOrUpdateId" href="#" onclick="doAjaxCreateForm(this)" class="btn btn-primary"><span id="userButton">Save</span></a>
             </div>
         </div>
     </div>
</div>
        
<!-- END CONTENT FRAME -->                                
 <%@include file="includes/footer.jsp" %> 
 
 <script type="text/javascript">
 
 function initializeWidget(type, thisVar){
	 $("#thirdInputDivId").hide();
	 var parentid = $(thisVar).attr("parentid");
	 if(type == 'account') {
		 $("#myModalLabel").html("Create Account");
		 $("#createOrUpdateId").attr("action", "ACCOUNT");
		 $("#firstInputLabelId").html("Account Name");
		 $("#secondInputLabelId").html("Account Holder");
		 $("#thirdInputDivId").show();
	 } else if (type == 'team') {
		 $("#myModalLabel").html("Create Team");
		 $("#firstInputLabelId").html("Team Name");
		 $("#secondInputLabelId").html("POC");
		 $("#createOrUpdateId").attr("action", "TEAM");
	 }else if (type == 'subTeam') {
		 $("#myModalLabel").html("Create Sub Team");
		 $("#firstInputLabelId").html("Sub Team Name");
		 $("#secondInputLabelId").html("PO Number");
		 $("#createOrUpdateId").attr("action", "SUBTEAM");
	 }else if (type == 'client') {
		 $("#myModalLabel").html("Create Client");
		 $("#firstInputLabelId").html("Client Name");
		 $("#secondInputLabelId").html("Client Email");
		 $("#createOrUpdateId").attr("action", "CLIENT");
	 }
	 
	 $("#createOrUpdateId").attr("parentid", parentid);
 }
 
 function clickBillableType(){
	 $("#billableType").val("BILLABLE");
 }
 
 function clickNonBillableType(){
	 $("#billableType").val("NONBILLABLE");
 }
 
 function doAjaxCreateForm(thisVar){
	 var action = $(thisVar).attr("action");
	 var parentid = parseInt($(thisVar).attr("parentid"));
	 var input1 = $("#firstInputId").val();
	 var input2 = $("#secondInputId").val();
	 var type = $("#billableType").val();
	 
	 doAjaxRequestWithGlobal("POST", "${applicationHome}/accountConfig",  {input1: input1, input2: input2,type: type, action:action, parentId:parentid},function(data) {
		 window.location.reload(true);
	    },function(error) {
	    	console.log("ERROR: ", error);
	    },false);
 }
 
 $(document).ready(function() {
     $('.data-table').dataTable({
             responsive: true,
             "lengthMenu": [[10,20,40,-1], [10,20,40,"All"]]
     });
     

     $("input[type=search]").keydown(function(){
    	 console.log($(this).val());
    	 filterAccountData(this);
     });
     
     $("input[type=search]").keyup(function(){
    	 console.log($(this).val());
    	 filterAccountData(this);
     });
 });
 
 
 function filterAccountData(thisVar){
		var serarchText = $(thisVar).val();
		if (serarchText != "") {
			$('table tr[account]').each(function () {
				$(this).hide();
			});
		    $('span.accountInfo').each(function(){
		       if($(this).text().toUpperCase().indexOf(serarchText.toUpperCase()) != -1){
		          var accountId =  $(this).attr("accountId");
		          console.log(accountId);
		          $('table tr[account="'+accountId+'"]').each(function () {
						$(this).show();
					});
		       }
		    });
		} else {
			$('table tr[account]').each(function () {
				$(this).show();
			});
		}
 }
 $("tr.clickable").click(function(){
	 var teamAttr = $(this).attr("teamId");
	 var subTteamAttr = $(this).attr("subTeamId");
	 if( $(this).hasClass("backgroundColor")) { // unselect
		
		 $(this).removeClass("backgroundColor");
		 
		 if (typeof subTteamAttr !== typeof undefined) {
			 var teamId = $(this).attr("team");
			 $('table tr[subteam]').each(function () {
				 if(teamId == $(this).attr("team")){
					$(this).show();
				 }
			 });
			 $("#teamDiv").show(500);
			 $("#clientAddLink").hide();
		 } else if (typeof teamAttr !== typeof undefined) {
			 var accountId = $(this).attr("account");
			 $('table tr[team]').each(function () {
				 if(accountId == $(this).attr("account")){
					$(this).show();
				 }
			 });
			 $("#accountDiv").show(500);
			 $("#subTeamAddLink").hide();
		 }else  {
			 $('table tr[account]').each(function () {
					$(this).show();
			 });
			 $("#teamAddLink").hide();
		 }
		 
		 
	 } else { // select
		 
		 if (typeof subTteamAttr !== typeof undefined) {
			 var subTeamId = $(this).attr("subTeamId");
			 $('table tr[subteam]').each(function () {
					$(this).hide();
			});
			$('table tr[subteam="'+subTeamId+'"]').each(function () {
					$(this).show();
			});
			 $("#clientAddLink").show();
			 $("#clientAddLink").attr("parentid", subTeamId);
			 $("#teamDiv").hide(500);
			 $("#accountDiv").hide(500);
			 $("tr[subTeamId].clickable").removeClass("backgroundColor");
		 } else if (typeof teamAttr !== typeof undefined) {
 			 var teamId = $(this).attr("teamId");
			 $('table tr[team]').each(function () {
					$(this).hide();
			});
			$('table tr[team="'+teamId+'"]').each(function () {
					$(this).show();
			});
			$("#subTeamAddLink").show();
			$("#subTeamAddLink").attr("parentid", teamId);
			$("#accountDiv").hide(500);
			$("tr[teamId].clickable").removeClass("backgroundColor");
		 } else {
			 var accountId = $(this).attr("accountId");
			 $('table tr[account]').each(function () {
					$(this).hide();
			});
			 $('table tr[account="'+accountId+'"]').each(function () {
					$(this).show();
				});
			 $("#teamAddLink").show();
			 $("#teamAddLink").attr("parentid", accountId);
			 $("tr[accountId].clickable").removeClass("backgroundColor");
			 $("tr[teamId].clickable").removeClass("backgroundColor");
		 }
		 $(this).addClass("backgroundColor");
	 }
 });
</script>           
