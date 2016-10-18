<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@include file="includes/header.jsp" %>
 <ul class="breadcrumb">
     <li><a href="${applicationHome}/index">Home</a></li>  
     <li class="active">Messages</li>
 </ul>
<style>
#creategroupmodal .modal-dialog  {width:65%;}
::-webkit-scrollbar {
    width: 12px;
}
::-webkit-scrollbar-track {
    -webkit-box-shadow: inset 0 0 6px rgba(0,0,0,0.3); 
    -webkit-border-radius: 10px;
    border-radius: 10px;
}
::-webkit-scrollbar-thumb {
    -webkit-border-radius: 10px;
    border-radius: 10px;
    background: rgba(0, 0, 0, 0.02); 
    -webkit-box-shadow: inset 0 0 6px rgba(0,0,0,0.5); 
}
::-webkit-scrollbar-thumb:window-inactive {
    background: rgba(0, 0, 0, 0.02); 
}
</style>
<script type="text/javascript">
function setLastMessageId(lastMessageIdField, messageId) {
    $('#'+lastMessageIdField).val(messageId);
}

</script>
<!-- START CONTENT FRAME -->
<div class="content-frame">                                    
    <!-- START CONTENT FRAME TOP -->
    <div class="content-frame-top">                        
        <div class="page-title">                    
            <h2><span class="fa fa-comments"></span> Messages</h2>
        </div>                                                    
        <div class="pull-right">                            
            <a data-toggle="modal" data-target="#creategroupmodal" class="btn btn-success btn-block" onclick="clearGroupForm();">
              <span class="fa fa-plus"></span> Create Group</a>
        </div>                           
    </div>
    <!-- END CONTENT FRAME TOP -->
    
    <!-- START CONTENT FRAME RIGHT -->
    <div class="content-frame-right">
        
        <div class="list-group list-group-contacts border-bottom push-down-10">
            <a href="#" class="list-group-item">                                 
                <div class="list-group-status status-online"></div>
                <img src="${assets}/images/users/user.jpg" class="pull-left" alt="Dmitry Ivaniuk">
                <span class="contacts-title">Dmitry Ivaniuk</span>
                <p>Hello my friend, how are...</p>
            </a>                                
            <a href="#" class="list-group-item">                                    
                <div class="list-group-status status-online"></div>
                <img src="${assets}/images/users/user3.jpg" class="pull-left" alt="Nadia Ali">
                <span class="contacts-title">Nadia Ali</span>
                <p>Wanna se my photos?</p>
            </a>                                                                
            <a href="#" class="list-group-item active">         
                <div class="list-group-status status-online"></div>
                <img src="${assets}/images/users/user2.jpg" class="pull-left" alt="John Doe">
                <div class="contacts-title">John Doe <span class="label label-danger">5</span></div>
                <p>This project is awesome</p>                                       
            </a>
            <a href="#" class="list-group-item">         
                <div class="list-group-status status-away"></div>
                <img src="${assets}/images/users/user4.jpg" class="pull-left" alt="Brad Pitt">
                <span class="contacts-title">Brad Pitt</span>
                <p>ok</p>                     
            </a>
            <a href="#" class="list-group-item">         
                <div class="list-group-status status-offline"></div>
                <img src="${assets}/images/users/no-image.jpg" class="pull-left" alt="Darth Vader">
                <span class="contacts-title">Darth Vader</span>
                <p>We should win this war!!!1</p>
            </a>
            <a href="#" class="list-group-item">         
                <div class="list-group-status status-offline"></div>
                <img src="${assets}/images/users/no-image.jpg" class="pull-left" alt="Kim Kardashian">
                <span class="contacts-title">Kim Kardashian</span>
                <p>You received a letter from Darth?</p>
            </a>
            <a href="#" class="list-group-item">         
                <div class="list-group-status status-offline"></div>
                <img src="${assets}/images/users/no-image.jpg" class="pull-left" alt="Jason Statham">
                <span class="contacts-title">Jason Statham</span>
                <p>Lets play chess...</p>
            </a>                            
        </div>
        
       
        
    </div>
    <!-- END CONTENT FRAME RIGHT -->
    <input type="hidden" id="currentUserId" value="${modelViewMap.currentUserId}">

    <!-- START CONTENT FRAME BODY -->
    <div class="content-frame-body content-frame-body-left">
        <!-- START VERTICAL TABS -->
     <div class="panel panel-default nav-tabs-vertical">                   
         <ul class="nav nav-tabs">
         <c:if test="${not empty modelViewMap.groupData}">
         <c:forEach var="group" items="${modelViewMap.groupData}">
            <li style="border-bottom: 1px dashed; border-color: #DCDCDC;"><a href="#tabGroup${group.groupId}" data-toggle="tab"><span onclick="editGroup('${group.groupId}');" data-toggle="modal" data-target="#creategroupmodal" class="fa fa-edit" style="margin-right: 5px;"></span>${group.name}</a></li>
         </c:forEach>
             
         </c:if>
         </ul>                    
         <div class="panel-body tab-content" style="background-color: #F5F5F5;">
            <c:forEach var="group" items="${modelViewMap.groupData}">
             <div class="tab-pane" id="tabGroup${group.groupId}">
			                <div class="messages messages-img messageContainer" style=" overflow-y: scroll; min-height: 300px; max-height: 300px;">
			                
			                <c:forEach var="groupMessage" items="${group.messages}">
				                <c:if test="${modelViewMap.currentUserId == groupMessage.user}">
				                    <div class="item in item-visible" >
				                    
				                </c:if>
				                <c:if test="${modelViewMap.currentUserId != groupMessage.user}">
	                                <div class="item item-visible">
	                            </c:if>
		                            <div class="image">
		                                <img src="${applicationHome}/image/${groupMessage.user}" alt="${groupMessage.userFullName}" onerror="this.src='/bops/jsp/assets/images/users/no-image.jpg'">
		                            </div>
		                            <c:if test="${modelViewMap.currentUserId == groupMessage.user}">
		                            <div class="text" style="background: #F6F6F6;">
		                            </c:if>
		                            <c:if test="${modelViewMap.currentUserId != groupMessage.user}">
		                            <div class="text">
		                            </c:if>
		                            <c:set var="lastmsg" value="${groupMessage.messageId}"/>
		                            
		                                <div class="heading">
		                                    <a href="#">${groupMessage.userFullName}</a>
		                                    <span class="date">${groupMessage.createdDateDisplay}</span>
		                                </div>
		                                ${groupMessage.description}
		                            </div>
		                        </div>
			                </c:forEach>
			               <input type="hidden" id="lastMessageId_${group.groupId}" value="${lastmsg}"/>
			                
			        </div>                        
			        <div class="panel panel-default push-up-10">
                        <div class="panel-body panel-body-search">
                            <div class="input-group">
                                <div class="input-group-btn">
                                    
                                </div>
                                <input type="text" id="${group.groupId}" class="form-control messageTextArea" placeholder="type in your message here and press enter"/>
                                <div class="input-group-btn">
                                    <button class="btn btn-default" onclick="sendMessage('${group.groupId}')">Send</button>
                                </div>
                            </div>
                        </div>
                    </div>
			        </div>
			        </c:forEach>
         </div>
     </div>                        
     <!-- END VERTICAL TABS -->
        
        
    </div>
    <!-- END CONTENT FRAME BODY -->      
<!-- END PAGE CONTENT FRAME -->

<div class="modal fade" id="creategroupmodal" tabindex="-1"
    role="dialog" aria-labelledby="reprocessConfirmation"
    aria-hidden="true">
    <form:form id="createGroupFormId" action="createGroup" method="POST" role="form" class="form-horizontal">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                    aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="myModalLabel">Create Group</h4>
            </div>
            <div class="modal-body">
                <%@include file="fagments/createGroupModal.jspf" %>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary"
                    data-dismiss="modal">Cancel</button>
                <a id="createOrUpdateGroupId" href="#" onclick="doAjaxCreateGroupForm()" class="btn btn-primary"><span id="userButton">Save</span></a>
            </div>
        </div>
    </div>
    
    </form:form>
</div>
<%@include file="includes/footer.jsp" %>     

<script>
function doAjaxCreateGroupForm(){
    $('#createGroupFormId').submit();
}

$('#createGroupFormId').ajaxForm(function(response) { 
    var responseJson = JSON.parse(response);
    if (responseJson.status){
        window.location.reload(true);
    }
}); 

$('.nav-tabs li:first-child a').trigger( "click" );

function sendMessage(group) {
	var groupId = group;
	var message = $('#'+groupId).val();
	if (message == null || message == '') {
		return false;
	}
	var data = {"groupId": groupId , "message": message };
	doAjaxRequest('POST', '/bops/dashboard/sendMessage', data, 
	function(response) {
        var responseJsonObj = JSON.parse(response);
        if (responseJsonObj.status == true) {
        	var lastMessageId = $('#lastMessageId_'+ groupId).val();
        	getMessageAfterId(groupId, lastMessageId);
        	$('#'+groupId).val('')
        } 
    }, function(e) {
    	console.log(e)
    });
}

function clearGroupForm() {
    $('#groupName').val('');
    $('#groupId').val('');
    $('#groupDescription').val('');
    $('.subscriberCheckBox').removeAttr('checked');
    $('.icheckbox_minimal-grey').removeClass('checked');
    $('#createOrUpdateGroupId').click(function () {
    	$('#createGroupFormId').prop("action", "createGroup");
    });
    
}

function setGroupEditPage(responseJsonObj) {
    $('#groupName').val(responseJsonObj.name);
    $('#groupId').val(responseJsonObj.groupId);
    $('#groupDescription').val(responseJsonObj.description);
    
    var subscribers = responseJsonObj.subscriberIds;
    for (var i=0; i<subscribers.length; i++) {
    	$('#'+subscribers[i]).next('ins').click();
    }
    $('#createGroupFormId').prop("action", "updateGroup");
}

function editGroup(groupId) {
	clearGroupForm();
	doAjaxRequest('GET', '/bops/dashboard/editGroup?groupId='+groupId, null, 
    function(response) {
        var responseJsonObj = JSON.parse(response);
        setGroupEditPage(responseJsonObj);
    }, function(e) {
        console.log(e)
    });
}

function getMessageAfterId(groupId, messageId) {
	if (messageId == 'undefined' || messageId == null || messageId == '') {
		messageId = 0;
	}
	   doAjaxRequest('GET', '/bops/dashboard/messageAfter?messageId='+messageId+'&groupId='+groupId, null, 
	    function(response) {
	        var responseJsonObj = JSON.parse(response);
	        processMessageAfterSave(responseJsonObj, groupId);
	    }, function(e) {
	    	console.log(e)
	    });
}

function processMessageAfterSave(jsonResponse, groupId) {
    var htmlString = '';
    var lastMessageId = '';
    var currentUser = $('#currentUserId').val();
    for (var i = 0; i < jsonResponse.length; i++) {
        var messageObject = jsonResponse[i];
        if (currentUser == messageObject.user) {
        	htmlString += '<div class="item in item-visible">';
        } else {
        	htmlString += '<div class="item item-visible">';
        }
        
        htmlString += '<div class="image">';
        
        htmlString += '<img src="/bops/dashboard/image/' + messageObject.user+'" alt="'+ messageObject.userFullName +'" onerror="this.src=\'/bops/jsp/assets/images/users/no-image.jpg\'">';
        htmlString += '</div>';
        htmlString += '<div class="text" style="background: #F6F6F6;">';
        htmlString += '<div class="heading">';
        htmlString += '<a href="#">'+ messageObject.userFullName +'</a>';
        htmlString += '<span class="date">'+ messageObject.createdDateDisplay +'</span>';
        htmlString += '</div>';
        htmlString += messageObject.description;
        htmlString += '</div>';
        htmlString += '</div>';
        lastMessageId = messageObject.messageId;
    }
    if (htmlString != '') {
	    var messageDiv = $('#lastMessageId_'+groupId).parent();
	    var currentHTML = $('#lastMessageId_'+groupId).parent().html();
	    $('#lastMessageId_'+groupId).parent().html(currentHTML + htmlString);
	    messageDiv.scrollTop(messageDiv[0].scrollHeight);
	    $('#lastMessageId_'+groupId).val(lastMessageId);
    }
}

$('.messageTextArea').keypress(function (e) {
	 var key = e.which;
	 if(key == 13)  // the enter key code
	  {
		 sendMessage(this.id);
	    return false;  
	  }
	}); 
$('.messageContainer').each(function(){
	$(this).scrollTop($(this)[0].scrollHeight);
});

function refreshMessages() {
	$('.messageTextArea').each(function(){
        var groupId = $(this).attr('id');
        var messageId = $('#lastMessageId_'+ groupId ).val();
        getMessageAfterId(groupId, messageId);
    });
}
setInterval('refreshMessages()', 3000);
</script>