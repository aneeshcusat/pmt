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
                    <li class="xn-logo">
                        <a href="index">famstack</a>
                        <a href="#" class="x-navigation-control"></a>
                    </li>
                    <li class="xn-profile">
                        <a href="${applicationHome}/profile/${currentUser.id}" class="profile-mini">
                          <img src="${applicationHome}/image/${currentUser.id}" alt="${currentUser.firstName}" onerror="this.src='${assets}/images/users/no-image.jpg'"/>
                        </a>
                        <a href="${applicationHome}/profile/${currentUser.id}">
                        <div class="profile" >
                            <div class="profile-image">
                               <img src="${applicationHome}/image/${currentUser.id}" alt="${currentUser.firstName}" onerror="this.src='${assets}/images/users/no-image.jpg'"/>
                            </div>
                            <div class="profile-data">
                                <div class="profile-data-name">${currentUser.firstName}</div>
                                <div class="profile-data-title">${currentUser.designation}</div>
                                <c:set var="userGroupId" value="${currentUser.userGroupId}"/>
                      			<div class="profile-data-title">${userGroupMap[userGroupId].name}</div>
                            </div>
                        </div>      
                        </a>                                                                  
                    </li>
                    <li class="xn-title">Navigation</li>
                    <c:if test="${currentUser.userRole == 'SUPERADMIN' || currentUser.userRole == 'ADMIN' || currentUser.userRole == 'MANAGER'}">
                    <li class="active">
                        <a href="${applicationHome}/index"><span class="fa fa-desktop"></span> <span class="xn-text">Dashboard</span></a>                        
                    </li>                    
                    <li class="xn-openable active">
                        <a href="#"><span class="fa fa-files-o"></span> <span class="xn-text">Pages</span></a>
                        <ul>
                            <li><a href="${applicationHome}/employees"><span class="fa fa-users"></span> Employees</a></li>
                            <li><a href="${applicationHome}/messages"><span class="fa fa-comments"></span> Messages</a></li>
                            <li><a href="${applicationHome}/calender"><span class="fa fa-calendar"></span> My Calendar</a></li>
                            <li><a href="${applicationHome}/tasks"><span class="fa fa-tasks"></span>Manage Tasks</a></li>
                            <li><a href="${applicationHome}/myTaskList"><span class="fa fa-th-list"></span>Task Activities</a></li>
                            <li><a href="${applicationHome}/projects"><span class="fa fa-list-alt"></span> Projects</a></li>
                            <li><a href="${applicationHome}/mileStones"><span class="fa fa-trophy"></span> Milestones</a></li>
                            <li><a href="${applicationHome}/taskAllocator"><span class="fa fa-list"></span> Task Scheduler</a></li>
                            <li><a href="${applicationHome}/projectreporting"><span class="fa fa-table"></span> Project Reporting</a></li>
                            <li><a href="${applicationHome}/accounts"><span class="fa fa-table"></span>Account config</a></li>
                        </ul>
                    </li>
                    </c:if>
                    
                     <c:if test="${!(currentUser.userRole == 'SUPERADMIN' || currentUser.userRole == 'ADMIN' || currentUser.userRole == 'MANAGER')}">
                    <li class="active">
                        <li><a href="${applicationHome}/tasks"><span class="fa fa-tasks"></span> My Tasks</a></li>                        
                    </li>                    
                    <li class="xn-openable active">
                        <a href="#"><span class="fa fa-files-o"></span> <span class="xn-text">Pages</span></a>
                        <ul>
 							<li><a href="${applicationHome}/myTaskList"><span class="fa fa-th-list"></span>Task Activities</a></li>
                            <li><a href="${applicationHome}/messages"><span class="fa fa-comments"></span> Messages</a></li>
                            <li><a href="${applicationHome}/calender"><span class="fa fa-calendar"></span> My Calendar</a></li>
                            <li><a href="${applicationHome}/projects"><span class="fa fa-edit"></span> Projects</a></li>
                        </ul>
                    </li>
                    </c:if>
                </ul>
                <!-- END X-NAVIGATION -->
            </div>