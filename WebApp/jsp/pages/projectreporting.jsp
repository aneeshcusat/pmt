<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@include file="includes/header.jsp" %>
<c:set var="userDetailsMap" value="${applicationScope.applicationConfiguraion.userMap}"/>

<style>
.dt-buttons a{
    width: 65px;
    height: 34px;
    font-size: 13px;
    font-weight: bold;
    background-color: red;
}
.dt-buttons{
    float: right;
    margin-left: 25px;
}
</style>
 <ul class="breadcrumb">
     <li><a href="${applicationHome}/index">Home</a></li>  
     <li class="active">Project Reporting</li>
 </ul>
<!-- START CONTENT FRAME -->
<div class="content-frame">            
    <!-- START CONTENT FRAME TOP -->
    <div class="content-frame-top">                        
        <div class="page-title">                    
            <h2><span class="fa fa-table"></span> Project Reporting - ${userGroupMap[currentUserGroupId].name}</h2>
        </div>  
        <div class="pull-right">
            <button class="btn btn-default content-frame-left-toggle"><span class="fa fa-bars"></span></button>
        </div>                                                                                
    </div>
    <!-- END CONTENT FRAME TOP -->
    <!-- body-->
    <div class="row">
	<div class="col-md-12" >
		<div class="row" id="assignTableId"  style="margin-top:10px">
		<!-- START DEFAULT TABLE EXPORT -->
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                
                                	<div class="col-md-6" >
                                 	</div>
                                 	<div class="col-md-2" >
					                 <span style="margin-top: 9px;margin-right:  10px;float:right"></>Select a date Range :  <i class="glyphicon glyphicon-calendar fa fa-calendar"></i>&nbsp;</span>
					                 <input type="text" name="daterange" id="daterangeText" style="display: none" value="${dateRange}" /> 
					                </div>
					             	
					             	<div class="col-md-3" >
					             	 <span id="reportrange" class="dtrange">                                            
            							<span>${dateRange}</span><b class="caret"></b>
        							</span>
        								<input style="margin-left:10px" class="btn btn-default" type="button" onclick="getProjectReportingData('${param.format}');" value="Search"></input>
        							</div>
        							<div class="col-md-1" >
        								<c:if test="${not empty param.format  && (param.format eq 'format1' || param.format eq 'format2')}">
        									<button onclick="exportReport('${param.format}')" class="btn btn-danger" aria-expanded="true"><i class="fa fa-bars"></i> Export Data</button>
        								</c:if>
        							</div>
                                </div>
                                <div class="panel-body panel-body-table" id="reportingBodyDiv">
                                    
                                </div>
                            </div>
                            <!-- END DEFAULT TABLE EXPORT -->
		</div>
	</div>
</div>
    <!-- body end -->
    
</div>               
<!-- END CONTENT FRAME -->                                
 <%@include file="includes/footer.jsp" %>
  <script type="text/javascript" src="${js}/plugins/datatables/jquery.dataTables.min_v1.js"></script> 
<script type="text/javascript" src="${js}/plugins/datatables/buttons.print.min.js"></script>    
<script type="text/javascript" src="${js}/plugins/datatables/buttons.html5.min.js"></script>    
<script type="text/javascript" src="${js}/plugins/datatables/dataTables.buttons.min.js"></script>    
<script type="text/javascript" src="${js}/plugins/datatables/jszip.min.js"></script>    
<script type="text/javascript" src="${js}/plugins/datatables/pdfmake.min.js"></script>  
<script type="text/javascript" src="${js}/plugins/datatables/vfs_fonts.js"></script>    

<script type="text/javascript">
        
        
var getProjectReportingData = function(format){
	 loadAllProjectDetails($('#daterangeText').val(), format);
}
        
var loadAllProjectDetails = function(daterange, format) {
	
	var dataString = {"daterange" : daterange, "format":format};
	doAjaxRequest("GET", "${applicationHome}/projectreportingResponse", dataString, function(data) {
        $("#reportingBodyDiv").html(data);
        document.title = "Export_" + daterange;
        $('#projectsTable').DataTable({
            dom: 'Bfrtip',
            buttons: [
                 'copy', 'csv', 'print'
            ],
            "pageLength": 100
        });
        
	}, function(e) {
        famstacklog("ERROR: ", e);
        alert(e);
    });
}
$(function() {
    $('input[name="daterange"]').daterangepicker();
    loadAllProjectDetails('${dateRange}','${param.format}');
});

$(document).ready(function(){
	
});
</script>