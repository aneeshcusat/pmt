<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

		<c:if test="${expandedPage}">
        	<div class="page-sidebar">
        </c:if>
		<c:if test="${expandedPage == false}">
        	<div class="page-sidebar page-sidebar-fixed scroll mCustomScrollbar _mCS_1 mCS-autoHide mCS_disabled">
        </c:if>
                <!-- START X-NAVIGATION -->
                <c:if test="${expandedPage}">
		        	<ul class="x-navigation">
		        </c:if>
		         <c:if test="${expandedPage == false}">
		        	<ul class="x-navigation x-navigation-custom x-navigation-minimized">
		        </c:if>
		       	 	<c:if test="${tracopusConfigEnabled == false}">
                    <li class="xn-logo">
                        <a href="index">famstack</a>
                        <a href="#" class="x-navigation-control"></a>
                    </li>
                    </c:if>
                    <c:if test="${tracopusConfigEnabled == true}">
                      <li class="xn-logo">
                        <a href="index"> <img src="${image}/tracopuslogo.png" style="height: 35px"/></a>
                        <a href="#" class="x-navigation-control"></a>
                    </li>
                    </c:if>
                    <li class="xn-profile">
                        <a href="javascript:void(0)" class="profile-mini">
                          <img src="${applicationHome}/image/${currentUser.id}" alt="${currentUser.firstName}" onerror="this.src='${assets}/images/users/no-image.jpg'"/>
                        </a>
                        <div class="profile" >
                            <div class="profile-image" style="float: left;text-align: left;width: 25%;padding-right: 0px;">
                               <img style="border-radius: 20%; width: 50px;" src="${applicationHome}/image/${currentUser.id}" alt="${currentUser.firstName}" onerror="this.src='${assets}/images/users/no-image.jpg'"/>
                            </div>
						 <a href="${applicationHome}/profile/${currentUser.id}">
                            <div class="profile-data" style="width: 75%;text-align: left;">
                                <div class="profile-data-name" style="margin-left: 10px;word-break: break-word;">${currentUser.firstName}</div>
                                <div class="profile-data-title" style="margin-left: 10px;">${currentUser.designation}</div>
                                <c:set var="userGroupId" value="${currentUser.userGroupId}"/>
                      			<div class="profile-data-title" style="margin-left: 10px;">${userGroupMap[userGroupId].name}</div>
                            </div>
                        </a>
                        </div>    
                    </li>
                    <li class="xn-title">Navigation</li>
                    <c:if test="${currentUser.userRole == 'SUPERADMIN' || currentUser.userRole == 'ADMIN' || currentUser.userRole == 'TEAMLEAD'}">
                     <li class="">
                        <a href="${applicationHome}/index"><span class="fa fa-list-alt"></span> <span class="xn-text">Home Page</span></a>                        
                    </li>
                    <li>
                     <c:set var="dashboardView" value='dashboardview${currentUserGroupId}'/> 
                     <c:set var="dashboardViewName" value='dashboard'/>   
					  <c:if test="${not empty appConfigMap[dashboardView] && not empty appConfigMap[dashboardView].appConfValueDetails}">
					  <c:forEach var="dashboardViewConf" items="${appConfigMap[dashboardView].appConfValueDetails}">
					  		<c:set var="dashboardViewName" value='${dashboardViewConf.value}'/>  
					  </c:forEach>
					  </c:if>
                        <a href="${applicationHome}/${dashboardViewName}"><span class="fa fa-desktop"></span> <span class="xn-text">Dashboard</span></a>                        
                    </li>
                    <li>
                    	<a href="${applicationHome}/tasks"><span class="fa fa-tasks"></span><span class="xn-text">Manage Tasks</span></a>
                    </li>
                    
                    <c:set var="projectReporting" value='reporting${currentUserGroupId}'/>   
                    <c:if test="${not empty appConfigMap[projectReporting] && not empty appConfigMap[projectReporting].appConfValueDetails}">
                    <c:forEach var="projectReportingConf" items="${appConfigMap[projectReporting].appConfValueDetails}">
                    	 <li><a href="${applicationHome}/projectreporting?format=${projectReportingConf.value}"><span class="fa fa-table"></span><span class="xn-text">Project Reporting</span></a></li>
                    </c:forEach>
                    </c:if>
                    
                    <c:if test="${empty appConfigMap[projectReporting] || empty appConfigMap[projectReporting].appConfValueDetails}">
	                    <li><a href="${applicationHome}/projectreporting?format=default"><span class="fa fa-table"></span><span class="xn-text">Project Reporting</span></a></li>
                    </c:if>
                    
                    <li  class="menuExpandLink xn-openable<c:if test="${expandedPage}"></c:if>">
                        <a href="#"><span class="fa fa-files-o"></span> <span class="xn-text">Additional Features</span></a>
                        <ul>
                            <li><a href="${applicationHome}/employees"><span class="fa fa-users"></span> Employees</a></li>
                            <li><a href="${applicationHome}/messages"><span class="fa fa-comments"></span> Messages</a></li>
                            <li><a href="${applicationHome}/calender"><span class="fa fa-calendar"></span> My Calendar</a></li>
                            <li><a href="${applicationHome}/myTaskList"><span class="fa fa-th-list"></span>Task Activities</a></li>
                            <li><a href="${applicationHome}/mileStones"><span class="fa fa-trophy"></span> Milestones</a></li>
                            <li><a href="${applicationHome}/taskAllocator"><span class="fa fa-list"></span> Task Scheduler</a></li>
                            <li><a href="${applicationHome}/accounts"><span class="fa fa-table"></span>Account config</a></li>
                            <li><a href="${applicationHome}/applicationConfig"><span class="fa fa-cog"></span>Application config</a></li>
                            <li><a href="${applicationHome}/userSiteActivity"><span class="fa fa-calendar"></span>User Site Activity</a></li>
                            <li><a href="${applicationHome}/faq"><span class="fa fa-arrow-circle-o-left"></span>FAQ</a></li>
                        </ul>
                    </li>
                    </c:if>
                    
                     <c:if test="${!(currentUser.userRole == 'SUPERADMIN' || currentUser.userRole == 'ADMIN' || currentUser.userRole == 'TEAMLEAD')}">
                    <li class="active">
                        <a href="${applicationHome}/tasks"><span class="fa fa-tasks"></span><span class="xn-text">Manage Tasks</span></a>                    
                    </li>                    
                    
                    <li class="menuExpandLink xn-openable active">
                        <a href="#"><span class="fa fa-files-o"></span> <span class="xn-text">Additional Features</span></a>
                        <ul>
 							<li><a href="${applicationHome}/myTaskList"><span class="fa fa-th-list"></span>Task Activities</a></li>
                            <li><a href="${applicationHome}/messages"><span class="fa fa-comments"></span> Messages</a></li>
                            <li><a href="${applicationHome}/calender"><span class="fa fa-calendar"></span> My Calendar</a></li>
                            <li><a href="${applicationHome}/userSiteActivity"><span class="fa fa-calendar"></span>User Site Activity</a></li>
                            <li><a href="${applicationHome}/faq"><span class="fa fa-arrow-circle-o-left"></span>FAQ</a></li>
                        </ul>
                    </li>
                    </c:if>
                </ul>
                <!-- END X-NAVIGATION -->
            </div>