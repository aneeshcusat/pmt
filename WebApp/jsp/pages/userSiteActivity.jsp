<%@include file="includes/header.jsp" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<c:set var="currentUser" value="${applicationScope.applicationConfiguraion.currentUser}"/>
 <ul class="breadcrumb">
     <li><a href="${applicationHome}/index">Home</a></li>  
     <li class="active">User Site Activity</li>
 </ul>
<style>

.colorRed {
	background-color: red !important;
}
.calenderEventPopOver{
	font-size: 10pt;
}
.disabled .fc-day-content {
    background-color: #123959;
    color: #FFFFFF;
    cursor: default;
}
#unbillableTaskCreationModal .modal-dialog {
	width: 50%;
}

.marksiteactivity {
position:absolute;bottom:0;left:0;right:0;font-size:12px;color:#000;text-align:center;cursor:pointer;
}
.autocomplete-suggestions { border: 1px solid #999; background: #FFF; overflow: auto; }
.autocomplete-suggestion { padding: 2px 5px; white-space: nowrap; overflow: hidden; }
.autocomplete-selected { background: #F0F0F0; }
.autocomplete-suggestions strong { font-weight: normal; color: #3399FF; }
.autocomplete-group { padding: 2px 5px; }
.autocomplete-group strong { display: block; border-bottom: 1px solid #000; }
</style>
<!-- START CONTENT FRAME -->
<div class="content-frame">            
    <!-- START CONTENT FRAME TOP -->
    <div class="content-frame-top">                        
        <div class="page-title">                    
            <h2><span class="fa fa-calendar"></span> User Site Activity</h2>
        </div>  
        <div class="pull-right col-md-10">
        <div class="col-md-6">
        </div>
         <div class="col-md-3">
							  <c:if test="${(currentUser.userRole == 'SUPERADMIN' || currentUser.userRole == 'ADMIN')}">
								<select id="userSiteActivityAssigneeId" name="userSiteActivityAssigneeId" class="form-control select" data-live-search="true">
										 <c:if test="${not empty userMap}">
										 	<option value="-1">select</option>
											<c:forEach var="user" items="${userMap}">
												 <c:if test="${currentUser.id eq user.id}">
												 	<option selected="selected" value="${user.id}">${user.firstName}</option>
												 </c:if>
												  <c:if test="${currentUser.id ne user.id}">
												  <option value="${user.id}">${user.firstName}</option>
												  </c:if>
							  				</c:forEach>
							  			</c:if>
									</select>
								</c:if>
								<c:if test="${not (currentUser.userRole == 'SUPERADMIN' || currentUser.userRole == 'ADMIN')}">
									<input id="userSiteActivityAssigneeId" type="hidden" name="userSiteActivityAssigneeId" value="${currentUser.id}"/>
								</c:if>
								
							</div>
						<div class="col-md-3">
			</div>

        </div>                                                                                
    </div>
    <!-- END CONTENT FRAME TOP -->
    
    <!-- START CONTENT FRAME BODY -->
    <div class=" padding-bottom-0">
        
        <div class="row">
            <div class="col-md-12">
                <div id="alert_holder"></div>
                <div class="fullcaledarusersiteactivity">                                
                    <div id="fullcaledarusersiteactivity"></div>                            
                </div>
            </div>
        </div>
        
    </div>                    
    <!-- END CONTENT FRAME BODY -->
    
</div>               
<!-- END CONTENT FRAME -->                                
 <%@include file="includes/footer.jsp" %>            
 <script type="text/javascript"
	src="${js}/plugins/bootstrap/bootstrap-select.js?v=${fsVersionNumber}"></script>
 
<script type="text/javascript" src="${js}/plugins/fullcalendar/fullcalendar.min.js?v=${fsVersionNumber}"></script>
<script type='text/javascript' src="${js}/plugins/datepicker/bootstrap-datetimepicker_new.js?v=${fsVersionNumber}"></script>
<script type="text/javascript" src="${js}/usersiteactivity.js?version=2.1&v=${fsVersionNumber}"></script>
