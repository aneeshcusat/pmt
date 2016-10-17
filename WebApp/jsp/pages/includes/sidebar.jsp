<div class="page-sidebar">
                <!-- START X-NAVIGATION -->
                <ul class="x-navigation">
                    <li class="xn-logo">
                        <a href="index.jsp">famstack</a>
                        <a href="#" class="x-navigation-control"></a>
                    </li>
                    <li class="xn-profile">
                        <a href="#" class="profile-mini">
                            <img src="" alt=""/>
                        </a>
                        <div class="profile" >
                            <div class="profile-image">
                               <img src="${applicationHome}/image/${currentUser.id}" alt="${currentUser.firstName}" onerror="this.src='${assets}/images/users/no-image.jpg'"/>
                            </div>
                            <div class="profile-data">
                                <div class="profile-data-name">${currentUser.firstName}</div>
                                <div class="profile-data-title">${currentUser.designation}</div>
                            </div>
                        </div>                                                                        
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
                            <li><a href="${applicationHome}/tasks"><span class="fa fa-tasks"></span> My Tasks</a></li>
                            <li><a href="${applicationHome}/projects"><span class="fa fa-edit"></span> Projects</a></li>
                            <li><a href="${applicationHome}/taskAllocator"><span class="fa fa-edit"></span> Task Scheduler</a></li>
                             <li><a href="${applicationHome}/projectreporting"><span class="fa fa-edit"></span> Project Reporting</a></li>
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
                            <li><a href="${applicationHome}/messages"><span class="fa fa-comments"></span> Messages</a></li>
                            <li><a href="${applicationHome}/calender"><span class="fa fa-calendar"></span> My Calendar</a></li>
                            <li><a href="${applicationHome}/projects"><span class="fa fa-edit"></span> Projects</a></li>
                        </ul>
                    </li>
                    </c:if>
                </ul>
                <!-- END X-NAVIGATION -->
            </div>