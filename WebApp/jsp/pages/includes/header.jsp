<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<spring:url value="/jsp/css" var="css" htmlEscape="true"/>
<spring:url value="/jsp/assets" var="assets" htmlEscape="true"/>
<spring:url value="/jsp/image" var="image" htmlEscape="true"/>
<spring:url value="/jsp/js" var="js" htmlEscape="true"/>
<spring:url value="/jsp/audio" var="audio" htmlEscape="true"/>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:set var="applicationHome" value="${contextPath}/dashboard"/>
<c:set var="userMap" value="${applicationScope.applicationConfiguraion.userList}"/>
<c:set var="employeeMap" value="${applicationScope.applicationConfiguraion.userMap}"/>
<c:set var="currentUser" value="${applicationScope.applicationConfiguraion.currentUser}"/>
<c:set var="userGroupMap" value="${applicationScope.applicationConfiguraion.userGroupMap}"/>
<c:set var="currentUserGroupId" value="${applicationScope.applicationConfiguraion.currentUserGroupId}"/>


<!DOCTYPE html>
<html lang="en">
    <head>        
        <!-- META SECTION -->
        <title>Famstack - Project scheduler</title>            
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />

        <link rel="icon" href="${fn:escapeXml(image)}/favicon.ico" type="image/x-icon" />
        <!-- END META SECTION -->
        
        <!-- CSS INCLUDE -->        
        <link rel="stylesheet" type="text/css" id="theme" href="${fn:escapeXml(css)}/theme-default.css"/>
        <link rel="stylesheet" type="text/css" id="theme" href="${fn:escapeXml(css)}/famstack.css"/>
        <link rel="stylesheet" type="text/css" id="theme" href="${fn:escapeXml(css)}/datepicker/daterangepicker.css"/>
        <link rel="stylesheet" type="text/css" id="theme" href="${fn:escapeXml(css)}/datepicker/jquery.datetimerangepicker.min.css"/>
        <link rel="stylesheet" type="text/css" id="theme" href="${fn:escapeXml(css)}/taginput/mab-jquery-taginput.css"/>
         <link rel="stylesheet" type="text/css" id="theme" href="${fn:escapeXml(css)}/waitme/waitMe.min.css"/>
         <link rel="stylesheet" type="text/css" id="theme" href="${fn:escapeXml(css)}/datepicker/bootstrap-datetimepicker.css"/>
         <link rel="stylesheet" type="text/css" id="theme" href="${fn:escapeXml(css)}/datatable/buttons.dataTables.min.css"/>
        <!-- EOF CSS INCLUDE -->                                    
    </head>
    <body>
    <c:set var="contextPath" value="${pageContext.request.contextPath}"/>
    <!-- START PAGE CONTAINER -->
        <div class="page-container page-navigation-top-fixed">
            
            <!-- START PAGE SIDEBAR -->
            <%@include file="sidebar.jsp" %>
            <!-- END PAGE SIDEBAR -->
            
            <!-- PAGE CONTENT -->
            <div class="page-content">
                
                <!-- START X-NAVIGATION VERTICAL -->
                <ul class="x-navigation x-navigation-horizontal x-navigation-panel">
                    <!-- TOGGLE NAVIGATION -->
                    <li class="xn-icon-button">
                        <a href="#" class="x-navigation-minimize"><span class="fa fa-dedent"></span></a>
                    </li>
                    <!-- END TOGGLE NAVIGATION -->
                    <!-- SEARCH -->
                    <li class="xn-search">
                        <form role="form">
                            <input type="text" name="search" placeholder="Search..."/>
                        </form>
                    </li>   
                    <!-- END SEARCH -->
                    <!-- SIGN OUT -->
                    <li class="xn-icon-button pull-right">
                        <a href="#" class="mb-control" data-box="#mb-signout"><span class="fa fa-sign-out"></span></a>                        
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
                    <!-- END TASKS -->
                </ul>
                <!-- END X-NAVIGATION VERTICAL -->                     
