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
                                 	<form action="" method="post">
                                 	<div class="col-md-2" >
					                 <span style="margin-top: 9px;margin-right:  10px;float:right"></>Select a date Range :  <i class="glyphicon glyphicon-calendar fa fa-calendar"></i>&nbsp;</span>
					                 <input type="text" name="daterange" id="daterangeText" style="display: none" value="${dateRange}" /> 
					             	</div>
					             	
					             	<div class="col-md-3" >
					             	 <span id="reportrange" class="dtrange">                                            
            							<span>${dateRange}</span><b class="caret"></b>
        							</span>
        								<input style="margin-left:10px" class="btn btn-default" type="submit" value="Search"></button>
        							</div>
									</form>
        							<div class="col-md-1" >
        							<button onclick="exportReport('visualServices')" class="btn btn-danger" aria-expanded="true"><i class="fa fa-bars"></i> Export Data</button>
        							</div>
                                </div>
                                <div class="panel-body panel-body-table">
                                    <table id="projectsTable" class="table table-striped">
                                        <thead>			
                                            <tr>
                                            	<th>Date</th>
                                                <th>Project code</th>
                                                <th>ID</th>
                                                <th>PO ID</th>
                                                <th>Project Name</th>
                                                <th>Type</th>
                                                <th>Category</th>
                                                <th>Team</th>
                                                <th>Sub Team</th>
                                                <th>Client</th>
                                                <th>Status</th>
                                                 <th>Project Duration</th>
                                            </tr>
                                        </thead>
                                        <c:if test="${not empty projectData}">
										<tbody>
        								<c:forEach var="project" items="${projectData}">
	        								 <c:set var="projectState" value="info"/>
								             <c:if test="${project.status == 'COMPLETED' }">
								             	<c:set var="projectState" value="success"/>
								              </c:if>
								              <c:if test="${project.projectMissedTimeLine == true }">
								              	<c:set var="projectState" value="danger"/>
								              </c:if>
        									   <tr>
				                               	<td>${project.startTime}</td>
                                                <td><a href="${applicationHome}/project/${project.id}" target="_new">${project.code}</a></td>
                                                <td>${project.id}</td>
                                                <td>${project.PONumber}</td>
                                                <td>${project.name}</td>
                                                <td>${project.type}</td>
                                                <td>${project.category}</td>
                                                <td>${project.teamName}</td>
                                                <td>${project.subTeamName}</td>
                                                <td>${project.clientName}</td>
				                              	<td> <span class="label label-${projectState}">${project.status}</span></td>
				                              	 <td>${project.actualDurationInHrs}</td>
                                            </tr>  
                                           </c:forEach>
                                        </tbody>
                                        </c:if>
                                    </table>                                    
                                    
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
        
$(function() {
    $('input[name="daterange"]').daterangepicker();
   
});

$('#projectsTable').DataTable({
    dom: 'Bfrtip',
    buttons: [
         'copy', 'csv', 'pdf', 'print'
    ],
    "pageLength": 100
});
$(document).ready(function(){
	document.title = "Export_" + $("#daterangeText").val();
});

</script>