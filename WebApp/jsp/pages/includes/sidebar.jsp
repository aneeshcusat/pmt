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
                        <a href="javascript:void(0)" class="profile-mini">
                          <img src="${applicationHome}/image/${currentUser.id}" alt="${currentUser.firstName}" onerror="this.src='${assets}/images/users/no-image.jpg'"/>
                        </a>
                        <div class="profile" >
                            <div class="profile-image">
                               <img src="${applicationHome}/image/${currentUser.id}" alt="${currentUser.firstName}" onerror="this.src='${assets}/images/users/no-image.jpg'"/>
                            </div>
						 <a href="${applicationHome}/profile/${currentUser.id}">
                            <div class="profile-data">
                                <div class="profile-data-name">${currentUser.firstName}</div>
                                <div class="profile-data-title">${currentUser.designation}</div>
                                <c:set var="userGroupId" value="${currentUser.userGroupId}"/>
                      			<div class="profile-data-title">${userGroupMap[userGroupId].name}</div>
                            </div>
                        </a>
                        
                        </div>      
                    </li>
                    <li class="xn-title">Navigation</li>
                     <li class="">
                        <a href="${applicationHome}/index"><span class="fa fa-list-alt"></span> <span class="xn-text">Home Page</span></a>                        
                    </li>
                    
                    <c:if test="${currentUser.userRole == 'SUPERADMIN' || currentUser.userRole == 'ADMIN' || currentUser.userRole == 'TEAMLEAD'}">
                    <li>
                        <a href="${applicationHome}/dashboard"><span class="fa fa-desktop"></span> <span class="xn-text">Dashboard</span></a>                        
                    </li>
                    <li>
                    	<a href="${applicationHome}/tasks"><span class="fa fa-tasks"></span><span class="xn-text">Manage Tasks</span></a>
                    </li>
                          
					 <c:choose>
                    	<c:when test="${currentUserGroupId == '1001'}">
                    	 <li><a href="${applicationHome}/projectreporting?format=format1"><span class="fa fa-table"></span><span class="xn-text">Project Reporting</span></a></li>
                    	</c:when>
                    	
                    	<c:when test="${currentUserGroupId == '1004'}">
                    	<li><a href="${applicationHome}/projectreporting?format=format2"><span class="fa fa-table"></span><span class="xn-text">Project Reporting</span></a></li>
                    	</c:when>
                    	<c:otherwise>
                    	 <li><a href="${applicationHome}/projectreporting?format=default"><span class="fa fa-table"></span><span class="xn-text">Project Reporting</span></a></li>
                    	</c:otherwise>
                    </c:choose>	                    
                    
                    <li class="xn-openable<c:if test="${expandedPage}"></c:if>">
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
                            <li><a href="${applicationHome}/faq"><span class="fa fa-arrow-circle-o-left"></span>FAQ</a></li>
                        </ul>
                    </li>
                    </c:if>
                    
                     <c:if test="${!(currentUser.userRole == 'SUPERADMIN' || currentUser.userRole == 'ADMIN' || currentUser.userRole == 'TEAMLEAD')}">
                    <li class="active">
                        <a href="${applicationHome}/tasks"><span class="fa fa-tasks"></span><span class="xn-text">Manage Tasks</span></a>                    
                    </li>                    
                    
                     <li class="">
                        <a href="${applicationHome}/index"><span class="fa fa-list-alt"></span> <span class="xn-text">Home Page</span></a>                        
                    </li>
                    
                    
                    <li class="xn-openable active">
                        <a href="#"><span class="fa fa-files-o"></span> <span class="xn-text">Additional Features</span></a>
                        <ul>
 							<li><a href="${applicationHome}/myTaskList"><span class="fa fa-th-list"></span>Task Activities</a></li>
                            <li><a href="${applicationHome}/messages"><span class="fa fa-comments"></span> Messages</a></li>
                            <li><a href="${applicationHome}/calender"><span class="fa fa-calendar"></span> My Calendar</a></li>
                            <li><a href="${applicationHome}/faq"><span class="fa fa-arrow-circle-o-left"></span>FAQ</a></li>
                        </ul>
                    </li>
                    </c:if>
                </ul>
                <!-- END X-NAVIGATION -->
            </div>