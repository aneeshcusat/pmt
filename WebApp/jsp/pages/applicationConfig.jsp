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
 
 .nav-header {
    display: block;
    padding: 3px 15px;
    font-size: 11px;
    font-weight: bold;
    line-height: 20px;
    color: #999999;
    text-transform: uppercase;
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
				        <th><a data-toggle="modal" data-target="#createaccountmodal" style="float:right"><i class="fa fa-plus fa-2x" aria-hidden="true" style="color:#95b75d"></i></a></th>
				    </tr>
				    </thead>
				   <tbody>
				   	<tr class="clickable">
				   		<td><a href="#tab1" data-toggle="tab" style="font-size: 14px" class="applicationTypeLink"> <i class="icon-envelope"></i>Designation</a></td>
				   		<td><a href="#" data-box="#confirmationbox" class="mb-control" style="float:right"><i class="fa fa-trash-o fa-2x" style="color:red" aria-hidden="true"></i></a></td>
				   	</tr>
			   	 	<tr class="clickable">
			   	 		<td><a href="#tab2" data-toggle="tab" style="font-size: 14px" class="applicationTypeLink"> <i class="icon-envelope"></i>Designation</a></td>
				   		<td><a href="#" data-box="#confirmationbox" class="mb-control" style="float:right"><i class="fa fa-trash-o fa-2x" style="color:red" aria-hidden="true"></i></a></td>
				   	</tr>
			   	 	</tbody>
			     </table>
				</div>
			</div>

			<div class="col-md-7" id="applicationConfigDiv"
				style="box-shadow: 5px 5px 20px #888888; margin-top: 10px">
				<div class="tab-content">
					<div class='row tab-pane' id="tab1">
						<table class="table table table-bordered data-table">
							<thead>
								<tr style="font-weight: bold">
									<th colspan="2">Designation</th>
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
					<div class='row tab-pane' id="tab2">
						<table class="table table table-bordered data-table">
							<thead>
								<tr style="font-weight: bold">
									<th colspan="2">Project Type</th>
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
                 <a id="createOrUpdateId" href="#" onclick="doAjaxCreateForm(this)" class="btn btn-primary"><span id="saveButton">Save</span></a>
             </div>
         </div>
     </div>
</div>
        
<!-- END CONTENT FRAME -->                                
 <%@include file="includes/footer.jsp" %> 
