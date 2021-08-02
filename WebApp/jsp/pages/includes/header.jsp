<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="staticFilesLocation" value="${applicationScope.applicationConfiguraion.staticFilesLocation}"/>
<c:set value="${staticFilesLocation}/css" var="css"/>
<c:set value="${staticFilesLocation}/assets" var="assets"/>
<c:set value="${staticFilesLocation}/image" var="image"/>
<c:set value="${staticFilesLocation}/js" var="js"/>
<c:set value="${staticFilesLocation}/audio" var="audio"/>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:set var="applicationHome" value="${contextPath}/dashboard"/>
<c:set var="fsVersionNumber" value="${applicationScope.applicationConfiguraion.fsVersionNumber}"/>
<c:set var="userMap" value="${applicationScope.applicationConfiguraion.userList}"/>
<c:set var="employeeMap" value="${applicationScope.applicationConfiguraion.userMap}"/>
<c:set var="allUsersMap" value="${applicationScope.applicationConfiguraion.allUsersMap}"/>
<c:set var="currentUser" value="${applicationScope.applicationConfiguraion.currentUser}"/>
<c:set var="userGroupMap" value="${applicationScope.applicationConfiguraion.userGroupMap}"/>
<c:set var="appConfigMap" value="${applicationScope.applicationConfiguraion.appConfigMap}"/>
<c:set var="currentUserGroupId" value="${applicationScope.applicationConfiguraion.currentUserGroupId}"/>
<c:set var="expandedPage" value="${applicationScope.applicationConfiguraion.expandedPage}"/>
<c:set var="serverInstanceName" value="${applicationScope.applicationConfiguraion.instanceName}"/>
<c:set var="tracopusConfigEnabled" value="${applicationScope.applicationConfiguraion.tracopusConfigEnabled}"/>
<%@include file="accessCheck.jsp" %>
<!DOCTYPE html>
<html lang="en">
    <head>       
    <!-- Global site tag (gtag.js) - Google Analytics -->
	<script async src="https://www.googletagmanager.com/gtag/js?id=UA-147513705-2"></script>
	<script>
	  window.dataLayer = window.dataLayer || [];
	  function gtag(){dataLayer.push(arguments);}
	  gtag('js', new Date());
	
	  gtag('config', 'UA-147513705-2');
	</script>

        <!-- META SECTION -->
        <title><c:if test="${tracopusConfigEnabled == false}">Famstack&trade;</c:if><c:if test="${tracopusConfigEnabled == true}">Tracopus</c:if> - Project scheduler</title>            
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />

        <link rel="icon" href="${fn:escapeXml(image)}/favicon.ico" type="image/x-icon" />
        <link rel="shortcut icon" href="${fn:escapeXml(image)}/favicon.ico" type="image/x-icon" />
        <!-- END META SECTION -->
        
        <!-- CSS INCLUDE -->        
        <link rel="stylesheet" type="text/css" id="theme" href="${fn:escapeXml(css)}/theme-default.css?version=3.2&v=${fsVersionNumber}"/>
        <link rel="stylesheet" type="text/css" id="theme" href="${fn:escapeXml(css)}/famstack.css?v=200"/>
        <link rel="stylesheet" type="text/css" id="theme" href="${fn:escapeXml(css)}/datepicker/daterangepicker.css"/>
        <link rel="stylesheet" type="text/css" id="theme" href="${fn:escapeXml(css)}/datepicker/jquery.datetimerangepicker.min.css"/>
        <link rel="stylesheet" type="text/css" id="theme" href="${fn:escapeXml(css)}/taginput/mab-jquery-taginput.css"/>
         <link rel="stylesheet" type="text/css" id="theme" href="${fn:escapeXml(css)}/waitme/waitMe.min.css?ver=3.0"/>
         <link rel="stylesheet" type="text/css" id="theme" href="${fn:escapeXml(css)}/datepicker/bootstrap-datetimepicker.css"/>
         <link rel="stylesheet" type="text/css" id="theme" href="${fn:escapeXml(css)}/datatable/buttons.dataTables.min.css"/>
        <!-- EOF CSS INCLUDE -->                                    
    
    <script type="text/javascript">
		var fsApplicationHome = '${applicationHome}';
		</script>
    </head>
    <body>
    <c:set var="contextPath" value="${pageContext.request.contextPath}"/>
    <!-- START PAGE CONTAINER -->
    	<c:if test="${expandedPage}">
        	<div class="page-container page-navigation-top-fixed">
        </c:if>
        <c:if test="${expandedPage == false}">
        	<div class="page-container page-navigation-top-fixed page-navigation-toggled page-container-wide">
        </c:if>   
            <!-- START PAGE SIDEBAR -->
            <%@include file="sidebar.jsp" %>
            <!-- END PAGE SIDEBAR -->
            
            <!-- PAGE CONTENT -->
            <div class="page-content">
                
                <!-- START X-NAVIGATION VERTICAL -->
                <ul class="x-navigation x-navigation-horizontal x-navigation-panel" style="box-shadow: 0px 5px 5px rgba(0, 0, 0, .5);">
                    <!-- TOGGLE NAVIGATION -->
                    <li class="xn-icon-button">
                        <a href="#" class="x-navigation-minimize"><span class="fa fa-dedent"></span></a>
                    </li>
                    <!-- END TOGGLE NAVIGATION -->
                    <!-- SEARCH -->
                    <li class="dashboardtitle">
                       DASHBOARD
                    </li>   
                    <!-- END SEARCH -->
                    <!-- SIGN OUT -->
                    <li class="xn-icon-button pull-right">
                        <a href="#" class="mb-control" data-box="#mb-signout"><span class="fa fa-sign-out"></span></a>                        
                    </li>
                    
                    <li class="xn-icon-button pull-right">
                        <a href="${applicationHome}/faq"><span class="fa fa-question-circle"></span></a>                        
                    </li>
                     
                    <!-- END SIGN OUT -->
                    
                    <!-- MESSAGES -->
                    <li class="xn-icon-button pull-right">
                        <a href="#" onclick="$('.newMessageNotification').html(0);"><span class="fa fa-comments"></span></a>
                        <div class="informer informer-info"><span class="newMessageNotification" id="newMessageNotification">0</span></div>
                        <div class="panel panel-primary animated zoomIn xn-drop-left xn-panel-dragging">
                            <div class="panel-heading">
                                <h3 class="panel-title"><span class="fa fa-comments"></span> Messages</h3>                                
                                <div class="pull-right">
                                    <span class="label label-danger"><span class="newMessageNotification">0</span> new</span>
                                </div>
                            </div>
                            <div class="panel-body list-group list-group-contacts scroll" style="height: 200px;" id="notificationsPanel">
                            </div>     
                            <div class="panel-footer text-center">
                                <a href="messages">Show all messages</a>
                            </div>                            
                        </div>                        
                    </li>
                    <!-- END MESSAGES -->
                    
                    <!-- Notificaiton -->
                    <li class="xn-icon-button pull-right">
                        <a href="#" onclick="$('.newNotification').html(0);"><span class="fa fa-bell-o"></span></a>
                        <div class="informer informer-danger"><span class="newNotification" id="newNotification">0</span></div>
                        <div class="panel panel-primary animated zoomIn xn-drop-left xn-panel-dragging">
                            <div class="panel-heading">
                                <h3 class="panel-title"><span class="fa fa-bell-o"></span> Notifications</h3>                                
                                <div class="pull-right">
                                    <span class="label label-danger"><span class="newNotification">0</span> new</span>
                                </div>
                            </div>
                            <div class="panel-body list-group list-group-contacts scroll" style="height: 200px;" id="notificationsPanel">
                            </div>     
                            <div class="panel-footer text-center">
                                <a href="notifications">Show all notifications</a>
                            </div>                            
                        </div>                        
                    </li>
                    <!-- END Notification -->
                    <!-- TASKS -->
                    <li class="xn-icon-button pull-right">
                        <a href="#"><span class="fa fa-tasks"></span></a>
                        <div class="informer informer-warning"><span class="taskNotification" id="taskNotification">0</span></div>
                        <div class="panel panel-primary animated zoomIn xn-drop-left xn-panel-dragging">
                            <div class="panel-heading">
                                <h3 class="panel-title"><span class="fa fa-tasks"></span> Tasks</h3>                                
                                <div class="pull-right">
                                    <span class="label label-warning"><span class="taskNotification">0</span> active</span>
                                </div>
                            </div>
                            <div class="panel-body list-group scroll" style="height: 200px;">                                
                            </div>     
                            <div class="panel-footer text-center">
                                <a href="tasks">Show all tasks</a>
                            </div>                            
                        </div>                        
                    </li>
                     <li class="xn-icon-button pull-right">
                        <a href="${applicationHome}/messages"><span class="fa fa-envelope"></span></a>                        
                    </li>
                    <!-- END TASKS -->
                </ul>
                <!-- END X-NAVIGATION VERTICAL -->                     
<script type="text/javascript">
var ajaxStartLabelDisabled=false;
</script>