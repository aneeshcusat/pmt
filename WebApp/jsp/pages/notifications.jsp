<%@include file="includes/header.jsp" %>
  <!-- START CONTENT FRAME -->
   <div class="content-frame">                                    
       <!-- START CONTENT FRAME TOP -->
       <div class="content-frame-top">                        
           <div class="page-title">                    
               <h2><span class="fa fa-comments"></span> Notifications</h2>
           </div>                                                    
       </div>
       <!-- END CONTENT FRAME TOP -->
       
       <!-- START CONTENT FRAME BODY -->
       <div class="content-frame-body content-frame-body-left">
           <div class="messages messages-img" id="notificationPageDiv">
           </div>                        
       </div>
       <!-- END CONTENT FRAME BODY -->      
   </div>
<!-- END PAGE CONTENT FRAME -->
<%@include file="includes/footer.jsp" %>     
<script>
function fillNotificationPage(elem) {
	var noficationType = elem.notificationType;
	var message = elem.data.name + "has been created";
	var notificationDate = getTodayDateTime(new Date(elem.createdTme));
	
	var notificationMessage = '<div class="item item-visible"><div class="text"><div class="heading"><a target="_new" href="${applicationHome}/project/'+elem.data.id+'">'+noficationType+'</a><span class="date">'+notificationDate+'</span></div>'+message+'</div></div>';
	$("#notificationPageDiv").prepend(notificationMessage);
}
</script>