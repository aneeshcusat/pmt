 <!-- START PRELOADS -->
        <audio id="audio-alert" src="${audio}/alert.mp3" preload="auto"></audio>
        <audio id="audio-fail" src="${audio}/fail.mp3" preload="auto"></audio>
        <!-- END PRELOADS -->                  
        
    <!-- START SCRIPTS -->
        <!-- START PLUGINS -->
        <script type="text/javascript" src="${js}/plugins/jquery/jquery.min.js"></script>
        <script type="text/javascript" src="${js}/plugins/jquery/jquery-ui.min.js"></script>
        <script type="text/javascript" src="${js}/plugins/bootstrap/bootstrap.min.js"></script>  
              
        <!-- END PLUGINS -->

        <!-- START THIS PAGE PLUGINS-->        
        <script type='text/javascript' src="${js}/plugins/icheck/icheck.min.js"></script>        
        <script type="text/javascript" src="${js}/plugins/mcustomscrollbar/jquery.mCustomScrollbar.min.js"></script>
        <script type="text/javascript" src="${js}/plugins/scrolltotop/scrolltopcontrol.js"></script>
        <script type="text/javascript" src="${js}/jquery.cropit.js"></script>  
        
        <script type="text/javascript" src="${js}/plugins/morris/raphael-min.js"></script>
        <script type="text/javascript" src="${js}/plugins/morris/morris.min.js"></script>       
        <script type="text/javascript" src="${js}/plugins/rickshaw/d3.v3.js"></script>
        <script type="text/javascript" src="${js}/plugins/rickshaw/rickshaw.min.js"></script>
        <script type='text/javascript' src="${js}/plugins/jvectormap/jquery-jvectormap-1.2.2.min.js"></script>
        <script type='text/javascript' src="${js}/plugins/jvectormap/jquery-jvectormap-world-mill-en.js"></script>                
                        
        <script type="text/javascript" src="${js}/plugins/owl/owl.carousel.min.js"></script>                 
        
        <script type="text/javascript" src="${js}/plugins/moment.min.js"></script>
        <script type="text/javascript" src="${js}/plugins/daterangepicker/daterangepicker.js"></script>
        <script type="text/javascript" src="${js}/plugins/bootstrap/bootstrap-timepicker.min.js"></script>
        <script type="text/javascript" src="${js}/plugins/jquery/jquery.form.min.js"></script>
        <script type="text/javascript" src="${js}/plugins/blockui/jquery.blockUI.js"></script>
        <script type="text/javascript" src="${js}/plugins/jeditable/jquery.jeditable.js"></script>
        <script type="text/javascript" src="${js}/plugins/datepicker/jquery.datetimepicker.full.js"></script>
        <script type="text/javascript" src=" ${js}/plugins/waitme/waitMe.min.js"></script>
        <script type="text/javascript" src=" ${js}/plugins/desktopnotification/push_notification.min.js"></script>
        <!-- END THIS PAGE PLUGINS-->        

<div id="settingsId" class="hide">
	<div class="ts-button">
        <span class="fa fa-cogs fa-spin"></span>
    </div>
    <div class="ts-body">
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
        <div class="ts-title">Options</div>
        <div class="ts-row">
            <label class="check"><input type="checkbox" class="icheckbox" name="autoRefresh"
               <c:if test="${applicationScope.applicationConfiguraion.autoRefresh == true}">
             	checked
             </c:if> />
             Auto refresh</label>
        </div>
        <div class="ts-row">
            <label class="check"><input type="checkbox" class="icheckbox" name="enableEmail" id="enableEmail"
               <c:if test="${applicationScope.applicationConfiguraion.emailEnabled == true}">
             	checked
             </c:if> />
            Enable Email</label>
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
    </div>
</div>
<script>
var site_settings = "";
<c:if test="${(currentUser.userRole == 'SUPERADMIN' || currentUser.userRole == 'ADMIN' || currentUser.userRole == 'MANAGER')}">
site_settings = $("#settingsId").html();
</c:if>

    </script>

        <!-- START TEMPLATE -->
        <script type="text/javascript" src="${js}/settings.js"></script>
        <script type="text/javascript" src="${js}/plugins.js"></script>        
        <script type="text/javascript" src="${js}/actions.js"></script>
        <!--
        <script type="text/javascript" src="${js}/demo_dashboard.js"></script>
         END TEMPLATE -->
         
         <!--  famstack scripts -->
         <script type="text/javascript" src="${js}/famstack.ajax.js"></script>
         <script type="text/javascript" src="${js}/famstack.plugin.js"></script>
         <!--  famstack scripts ends -->
    <!-- END SCRIPTS --> 
    <script>
    
    $('input[type="checkbox"].icheckbox').change(function () {
        var name = $(this).prop('name');
        var check = $(this).prop('checked');
        doAjaxEnableSettings(name,check);
    });
    
    function doAjaxEnableSettings(name,value) {
	    doAjaxRequest("POST", "${applicationHome}/setConfiguration",  {propertyName:name,propertyValue:value},function(data) {
	    	 
			console.log("data: ", data);
	    },function(error) {
	    	console.log("ERROR: ", error);
	    },false);
	 }
    
    $("#userGroupSelection").change(function(){
    	console.log($(this).prop("selectedIndex"));
    	if ($(this).prop("selectedIndex") > 0) {
	    	 var id = $(this).val();
	    	 console.log("userGroupId: ", id);
	    	 doAjaxChangeUserGroup(id);
    	}
    });
    
    function doAjaxChangeUserGroup(id) {
	    doAjaxRequest("POST", "${applicationHome}/changeUserGroup",  {groupId:id},function(data) {
	    	window.location.reload(true);
	    },function(error) {
	    	console.log("ERROR: ", error);
	    },false);
	 }
    </script>