 <!-- START PRELOADS -->
        <%@page import="com.fasterxml.jackson.annotation.JsonInclude.Include"%>
        <c:set var="fsVersionNumber" value="${applicationScope.applicationConfiguraion.fsVersionNumber}"/>
<audio id="audio-alert" src="${audio}/alert.mp3" preload="auto"></audio>
        <audio id="audio-fail" src="${audio}/fail.mp3" preload="auto"></audio>
        <!-- END PRELOADS -->                  
        
    <!-- START SCRIPTS -->
        <!-- START PLUGINS -->
        <script type="text/javascript" src="${js}/plugins/jquery/jquery.min.js?v=${fsVersionNumber}"></script>
        <script type="text/javascript" src="${js}/plugins/jquery/jquery-ui.min.js?v=${fsVersionNumber}"></script>
        <script type="text/javascript" src="${js}/plugins/bootstrap/bootstrap.min.js?v=${fsVersionNumber}"></script>  
              
        <!-- END PLUGINS -->

        <!-- START THIS PAGE PLUGINS-->        
        <script type='text/javascript' src="${js}/plugins/icheck/icheck.min.js?v=${fsVersionNumber}"></script>        
        <script type="text/javascript" src="${js}/plugins/mcustomscrollbar/jquery.mCustomScrollbar.min.js?v=${fsVersionNumber}"></script>
        <script type="text/javascript" src="${js}/plugins/scrolltotop/scrolltopcontrol.js?v=${fsVersionNumber}"></script>
        <script type="text/javascript" src="${js}/jquery.cropit.js?v=${fsVersionNumber}"></script>  
        
        <script type='text/javascript' src="${js}/plugins/jvectormap/jquery-jvectormap-1.2.2.min.js?v=${fsVersionNumber}"></script>
        <script type='text/javascript' src="${js}/plugins/jvectormap/jquery-jvectormap-world-mill-en.js?v=${fsVersionNumber}"></script>                
                        
        <script type="text/javascript" src="${js}/plugins/owl/owl.carousel.min.js?v=${fsVersionNumber}"></script>                 
        
        <script type="text/javascript" src="${js}/plugins/moment.min.js?v=${fsVersionNumber}"></script>
        <script type="text/javascript" src="${js}/plugins/daterangepicker/daterangepicker.js?v=${fsVersionNumber}"></script>
        <script type="text/javascript" src="${js}/plugins/bootstrap/bootstrap-timepicker.min.js?v=${fsVersionNumber}"></script>
        <script type="text/javascript" src="${js}/plugins/jquery/jquery.form.min.js?v=${fsVersionNumber}"></script>
        <script type="text/javascript" src="${js}/plugins/blockui/jquery.blockUI.js?v=${fsVersionNumber}"></script>
        <script type="text/javascript" src="${js}/plugins/jeditable/jquery.jeditable.js?v=${fsVersionNumber}"></script>
        <script type="text/javascript" src="${js}/plugins/datepicker/jquery.datetimepicker.full.min.js?v=${fsVersionNumber}"></script>
        <script type="text/javascript" src=" ${js}/plugins/waitme/waitMe.min.js?ver=3.0&v=${fsVersionNumber}"></script>
        <script type="text/javascript" src=" ${js}/plugins/desktopnotification/push_notification.min.js?v=${fsVersionNumber}"></script>
        <!-- END THIS PAGE PLUGINS-->        


<%-- <%@include file="autologout.jsp"%> --%>

<div id="settingsId" class="hide">
	<div class="ts-button">
        <span class="fa fa-cogs fa-spin"></span>
    </div>
    <div class="ts-body">
    	<c:if test="${(currentUser.userRole == 'SUPERADMIN')}">
	    <div class="ts-title">Themes</div>
        <div class="ts-themes">
            <a href="#" class="active" data-theme="${css}/theme-default.css"><img src="${image}/themes/default.jpg"/></a>            
        </div>
		<div class="ts-title">Layout</div>
        <div class="ts-row">
            <label class="check"><input type="radio" class="iradio" name="st_layout_boxed" value="0" checked/> Full Width</label>
        </div>
        <div class="ts-row">
            <label class="check"><input type="radio" class="iradio" name="st_layout_boxed" value="1"/> Boxed</label>
        </div>
        </c:if>
        <div class="ts-title">Options</div>
         <div class="ts-row">
            <label class="check"><input type="checkbox" class="icheckbox" name="${currentUser.userId}_disableEmail" id="enableEmail"
               <c:if test="${applicationScope.applicationConfiguraion.emailUserDisabled == true}">
             	checked
             </c:if> />
            Disable Emails</label>
        </div>
        <c:if test="${(currentUser.userRole == 'SUPERADMIN')}">
        <div class="ts-row">
            <label class="check"><input type="checkbox" class="icheckbox" name="enableEmail" id="enableAllEmail"
               <c:if test="${applicationScope.applicationConfiguraion.emailAllEnabled == true}">
             	checked
             </c:if> />
            Enable All Emails</label>
        </div>
        
        <div class="ts-row">
            <label class="check"><input type="checkbox" class="icheckbox" name="autoRefresh"
               <c:if test="${applicationScope.applicationConfiguraion.autoRefresh == true}">
             	checked
             </c:if> />
             Auto refresh</label>
        </div>
          <div class="ts-row">
            <label class="check"><input type="checkbox" class="icheckbox" name="logDebug"  id="logDebug"
            <c:if test="${applicationScope.applicationConfiguraion.logDebug == true}">
             	checked
             </c:if> />
            Enable Logs</label>
        </div>
         <div class="ts-row">
            <label class="check"><input type="checkbox" class="icheckbox" name="desktopNotification"  id="desktopNotification"
            <c:if test="${applicationScope.applicationConfiguraion.desktopNotificationEnabled == true}">
             	checked
             </c:if> />
            Enable Desktop Notification</label>
        </div>
        </c:if>
    </div>
</div>
<script>
var site_settings = "";
	site_settings = $("#settingsId").html();
    </script>

        <!-- START TEMPLATE -->
        <script type="text/javascript" src="${js}/settings.js?v=${fsVersionNumber}"></script>
        <script type="text/javascript" src="${js}/plugins.js?version=2.1&v=${fsVersionNumber}"></script>        
        <script type="text/javascript" src="${js}/actions.js?v=${fsVersionNumber}"></script>
        <!--
        <script type="text/javascript" src="${js}/demo_dashboard.js?v=${fsVersionNumber}"></script>
         END TEMPLATE -->
         
         <!--  famstack scripts -->
         <script type="text/javascript" src="${js}/famstack.ajax.js?version=3.2&v=${fsVersionNumber}"></script>
         <script type="text/javascript" src="${js}/famstack.plugin.js?v=${fsVersionNumber}"></script>
         <script type="text/javascript" src="${js}/plugins/notify/bootstrap-notify.js?v=${fsVersionNumber}"></script>
        
         <!--  famstack scripts ends -->
    <!-- END SCRIPTS --> 
    <script>

    $( document ).ajaxStart(function() {
    	if(!ajaxStartLabelDisabled) {
   		 $(".page-container").waitMe({
   			effect : "img",
   			text : "",
   			bg : 'rgba(255,255,255,0.7)',
   			color : "#000",
   			source : "${image}/tracopus.gif"
   			});
    	}
   	}).ajaxStop(function() {
   		if(!ajaxStartLabelDisabled) {
   			$(".page-container").waitMe('hide');
   		}
   	});

    
    
    $(".menuExpandLink").on("click", function(){
		if(!$(this).hasClass("expanded")) {
			setTimeout( function () {
				   $("#mcs_container").mCustomScrollbar('scrollTo','last');
			}, 100);
			$(".menuExpandLink").addClass("expanded");
		} else {
			//$('.scroll-y').mCustomScrollbar('scrollTo', 'top');
			$(".menuExpandLink").removeClass("expanded");
		}    	
    });
    
    var famstackLogEnabled = false;
    function famstacklog(message){
    	famstacklog("INFO", message);
    }
    
    function famstackalert(error){
    	if(error.status == 401) {
    		window.location.reload(true);
    	}
    }
    function triggerClientErrorEmail(error, exception){
    	if (error.status == 0) {
    		window.location.reload(true);
    	}
    	if(error.status != 401 && error.status != 0) {
	       	var subject = "[${serverInstanceName}]["+ error.url + "][" +error.status +"] : ["+ exception +"] :";
	    	var message = "Error has occured - user name [${currentUser.userId}] Page URL ["+window.location.href+"]" ;
	    	doAjaxRequestWithGlobal("GET", "${applicationHome}/triggerEmail",  {subject:subject,body:message},function(data) {
				famstacklog("data: ", data);
		    },function(error) {
		    	famstacklog("ERROR: ", error);
		    },false);
	    }
    }
    
	function famstacklog(code, message){
    	if (famstackLogEnabled) {
    		console.log(code, message);
    	}
    }
	
	function famstackLoginCheck(code, message){
    	try{
	    	if (code.indexOf("ERROR") != -1) {
	    		if (message.status == 401) {
	    			window.location = "${applicationHome}/login";
	    		}
	    	}
		}catch(err){
			
		}
    }
    
    $('input[type="checkbox"].icheckbox').change(function () {
        var name = $(this).prop('name');
        var check = $(this).prop('checked');
        doAjaxEnableSettings(name,check);
    });
    
    function doAjaxEnableSettings(name,value) {
    	famstacklog("name: ", name);
    	famstacklog("value: ", value);
	    doAjaxRequestWithGlobal("POST", "${applicationHome}/setConfiguration",  {propertyName:name,propertyValue:value},function(data) {
			famstacklog("data: ", data);
	    },function(error) {
	    	famstacklog("ERROR: ", error);
	    },false);
	 }
    
    $("#userGroupSelection").change(function(){
    	famstacklog($(this).prop("selectedIndex"));
    	if ($(this).prop("selectedIndex") > 0) {
	    	 var id = $(this).val();
	    	 famstacklog("userGroupId: ", id);
	    	 doAjaxChangeUserGroup(id);
    	}
    });
    
    function doAjaxChangeUserGroup(id) {
	    doAjaxRequest("POST", "${applicationHome}/changeUserGroup",  {groupId:id},function(data) {
	    	window.location.reload(true);
	    },function(error) {
	    	famstacklog("ERROR: ", error);
	    },false);
	 }
    
    var filterDateMap = {'Today': [moment(), moment()],
            'Yesterday': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
            'Last 7 Days': [moment().subtract(6, 'days'), moment()],
            'This Week': [moment().startOf('week'), moment().endOf('week')],
            'Last Week': [moment().subtract(1, 'week').startOf('week'), moment().subtract(1, 'week').endOf('week')],
            'Last 30 Days': [moment().subtract(29, 'days'), moment()],
            'This Month': [moment().startOf('month'), moment().endOf('month')],
            'Last Month': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')],
    		'Last 3 Month': [moment().subtract(3, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')],
    		'Last 6 Month': [moment().subtract(6, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]};

    

    $("a.x-navigation-minimize").on("click", function(){
    	var name = ${currentUser.id} + "_dashboardToggle";
    	var value = false;
    	if ($(".page-sidebar .x-navigation.x-navigation-minimized").length > 0) {
    		value = true;
    	}
    	doAjaxEnableSettings(name,value);
    });
    
        /* reportrange */
        if($("#reportrange").length > 0){   
            $("#reportrange").daterangepicker({                    
                ranges: filterDateMap,
                opens: 'left',
                buttonClasses: ['btn btn-default'],
                applyClass: 'btn-small btn-primary',
                cancelClass: 'btn-small',
                format: 'MM.DD.YYYY',
                separator: ' to ',
                <c:if test="${not empty dateRangeLabel}">
                	startDate: filterDateMap['${dateRangeLabel}'][0],
               	 	endDate: filterDateMap['${dateRangeLabel}'][1]  
                </c:if>
                <c:if test="${empty dateRangeLabel}">
	            	startDate: moment().startOf('month'),
	           	 	endDate:  moment().endOf('month').format('MM/DD/YYYY')  
	            </c:if>
              },function(start, end) {
            	  $("#daterangeText").val(start.format('MM/DD/YYYY') + ' - ' + end.format('MM/DD/YYYY'));
            	  $("#reportrange span").html($("#daterangeText").val());
            });
            
            <c:if test="${not empty dateRange}">
        		$("#daterangeText").val("${dateRange}");
        		$("#reportrange span").html("${dateRange}");
        	</c:if>
        	<c:if test="${empty dateRange}">
            	$("#daterangeText").val(moment().startOf('month').format('MM/DD/YYYY') + ' - ' + moment().endOf('month').format('MM/DD/YYYY'));
         		$("#reportrange span").html($("#daterangeText").val());
         	</c:if>
        }
        

        var exportReport = function(reportName) {
        	var dateRange = $("#daterangeText").val();
        	window.location.href = "${applicationHome}/export/"+reportName+"?daterange=" + dateRange;
        }
     
    </script>