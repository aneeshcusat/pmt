<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@include file="includes/header.jsp" %>
 <ul class="breadcrumb">
     <li><a href="${applicationHome}/index">Home</a></li>  
     <li class="active">Project Reporting</li>
 </ul>
<!-- START CONTENT FRAME -->
<div class="content-frame">            
    <!-- START CONTENT FRAME TOP -->
    <div class="content-frame-top">                        
        <div class="page-title">                    
            <h2><span class="fa fa-table"></span> Project Reporting</h2>
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
                                <div class="col-md-8" >
                                   <form action="" method="post">
					                 Select a date Range :  <i class="glyphicon glyphicon-calendar fa fa-calendar"></i>&nbsp;
					                 <input type="text" name="daterange" style="width: 175px;height: 33px;margin-left: 5px;padding-left: 7px;" value="${dateRange}" /> 
					                 <input class="btn btn-default" type="submit" value="Search"></button>
					             	</form>
					             	</div>
                                    <div class="pull-right" class="col-md-4">
                                        <button class="btn btn-danger toggle" data-toggle="exportTable"><i class="fa fa-bars"></i> Export Data</button>
                                    </div>
                                </div>
                                <div class="panel-body" id="exportTable" style="display: none;">
                                    <div class="row">
                                        <div class="col-md-3">
                                            <div class="list-group border-bottom">
                                                <a href="#" class="list-group-item" onClick ="$('#projectsTable').tableExport({type:'json',escape:'false'});"><img src='${image}/icons/json.png' width="24"/> JSON</a>
                                                <a href="#" class="list-group-item" onClick ="$('#projectsTable').tableExport({type:'json',escape:'false',ignoreColumn:'[2,3]'});"><img src='${image}/icons/json.png' width="24"/> JSON (ignoreColumn)</a>
                                                <a href="#" class="list-group-item" onClick ="$('#projectsTable').tableExport({type:'json',escape:'true'});"><img src='${image}/icons/json.png' width="24"/> JSON (with Escape)</a>
                                            </div>
                                        </div>
                                        <div class="col-md-2">
                                            <div class="list-group border-bottom">
                                                <a href="#" class="list-group-item" onClick ="$('#projectsTable').tableExport({type:'xml',escape:'false'});"><img src='${image}/icons/xml.png' width="24"/> XML</a>
                                                <a href="#" class="list-group-item" onClick ="$('#projectsTable').tableExport({type:'sql'});"><img src='${image}/icons/sql.png' width="24"/> SQL</a>
                                            </div>
                                        </div>
                                        <div class="col-md-2">
                                            <div class="list-group border-bottom">
                                                <a href="#" class="list-group-item" onClick ="$('#projectsTable').tableExport({type:'csv',escape:'false'});"><img src='${image}/icons/csv.png' width="24"/> CSV</a>
                                                <a href="#" class="list-group-item" onClick ="$('#projectsTable').tableExport({type:'txt',escape:'false'});"><img src='${image}/icons/txt.png' width="24"/> TXT</a>
                                            </div>
                                        </div>
                                        <div class="col-md-3">
                                            <div class="list-group border-bottom">
                                                <a href="#" class="list-group-item" onClick ="$('#projectsTable').tableExport({type:'excel',escape:'false'});"><img src='${image}/icons/xls.png' width="24"/> XLS</a>
                                                <a href="#" class="list-group-item" onClick ="$('#projectsTable').tableExport({type:'doc',escape:'false'});"><img src='${image}/icons/word.png' width="24"/> Word</a>
                                                <a href="#" class="list-group-item" onClick ="$('#projectsTable').tableExport({type:'powerpoint',escape:'false'});"><img src='${image}/icons/ppt.png' width="24"/> PowerPoint</a>
                                            </div>
                                        </div>
                                        <div class="col-md-2">
                                            <div class="list-group border-bottom">
                                                <a href="#" class="list-group-item" onClick ="$('#projectsTable').tableExport({type:'png',escape:'false'});"><img src='${image}/icons/png.png' width="24"/> PNG</a>
                                                <a href="#" class="list-group-item" onClick ="$('#projectsTable').tableExport({type:'pdf',escape:'false'});"><img src='${image}/icons/pdf.png' width="24"/> PDF</a>
                                            </div>
                                        </div>
                                    </div>                               
                                </div>
                                <div class="panel-body panel-body-table">
                                    <table id="projectsTable" class="table table-striped">
                                        <thead>			
                                            <tr>
                                                <th>Project code</th>
                                                <th>PO ID</th>
                                                <th>Project Name</th>
                                                <th>Type</th>
                                                 <th>Category</th>
                                                 <th>Team</th>
                                                 <th>Sub Team</th>
                                                <th>Client</th>
                                                <th>Est Duration (Hrs)</th>
                                                <th>Actual Duration (Hrs)</th>
                                                <th>Status</th>
                                            </tr>
                                        </thead>
                                        <c:if test="${not empty projectData}">
										<tbody>
        								<c:forEach var="project" items="${projectData}">
                                            <tr>
                                                <td>${project.code}</td>
                                                <td>${project.PONumber}</td>
                                                <td>${project.name}</td>
                                                <td>${project.type}</td>
                                                <td>${project.category}</td>
                                                <td>${project.teamName}</td>
                                                <td>${project.subTeamName}</td>
                                                <td>${project.clientName}</td>
                                                <td>${project.duration}</td>
                                                <td>${project.actualDuration}</td>
                                                <td>${project.status}</td>
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
 <script type="text/javascript" src="${js}/plugins/tableexport/tableExport.js"></script>            
 <script type="text/javascript" src="${js}/plugins/tableexport/jquery.base64.js"></script>
<script type="text/javascript" src="${js}/plugins/tableexport/html2canvas.js"></script>
<script type="text/javascript" src="${js}/plugins/tableexport/jspdf/libs/sprintf.js"></script>
<script type="text/javascript" src="${js}/plugins/tableexport/jspdf/jspdf.js"></script>
<script type="text/javascript" src="${js}/plugins/tableexport/jspdf/libs/base64.js"></script>   
<script type="text/javascript">
        
$(function() {
    $('input[name="daterange"]').daterangepicker();
   
});

$('#projectsTable').DataTable({ 
	responsive: true,
    "lengthMenu": [[50, 100, -1], [50, 100, "All"]],
    "ordering": true,

});
</script>