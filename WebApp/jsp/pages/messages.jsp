<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@include file="includes/header.jsp" %>
<ul class="breadcrumb">
    <li><a href="${applicationHome}/index">Home</a></li>
    <li class="active">Messages</li>
</ul>
<style>
    #creategroupmodal .modal-dialog {
        width: 65%;
    }
    
    .margin10{
    	    margin-top: 10px;
    }
    
    .nav-list {
    padding-left: 15px;
    padding-right: 15px;
    margin-bottom: 0;
}

list .nav-header {
    margin-left: -15px;
    margin-right: -15px;
    text-shadow: 0 1px 0 rgba(255, 255, 255, 0.5);
}
.nav-header {
    display: block;
    padding: 3px 15px;
    font-size: 11px;
    font-weight: bold;
    line-height: 20px;
    color: #999999;
    text-transform: uppercase;
}


.messageGroupLink a:hover {
    background-color: lightblue;
    font-weight: bold;
    font-size: 1.1em;
}

.messageGroupLinkLightblue {
    background-color: lightblue;
    font-weight: bold;
    font-size: 1.1em;
}
</style>
<!-- START CONTENT FRAME -->
<div class="content-frame">            
    <!-- START CONTENT FRAME TOP -->
    <div class="content-frame-top">                        
        <div class="page-title">                    
            <h2><span class="fa fa-comments"></span>Messages</h2>
        </div>  
        <div class="pull-right">
          <c:if test="${currentUser.userRole == 'SUPERADMIN' || currentUser.userRole == 'ADMIN' || currentUser.userRole == 'MANAGER'}">
            <a data-toggle="modal" data-target="#creategroupmodal" class="btn btn-success btn-block" onclick="clearGroupForm();">
                <span class="fa fa-plus"></span> Create Group
            </a>
          </c:if>
        </div>                                                                               
    </div>
    <!-- END CONTENT FRAME TOP -->
    
    <!-- START CONTENT FRAME BODY -->
    <div class=" padding-bottom-0">
        
        <div class="row">
            
            <div class="sidebar-nav col-md-3 margin10">
			    <div class="well" style=" padding: 8px 0;">
					<ul class="nav nav-list" id="groupLeftNavHolder"> 
					  <li class="nav-header">Message Groups</li>        
					  <c:if test="${not empty modelViewMap.groupData}">
                <c:forEach var="group" items="${modelViewMap.groupData}" varStatus="status">
                <li
                <c:if test="${status.index==0}">
	            	class='active'
	            	</c:if>>
	            	<a href="#tab${group.groupId}" data-toggle="tab" onclick='resetScrollMessageDiv(${group.groupId}, this)' style="font-size: 14px" class="messageGroupLink"> <i class="icon-envelope"></i> ${group.name} 
	            	<span class="badge badge-info" style="border-radius:10px; float:right; display:none" id="newMessageCount${group.groupId}">0</span>  
	            	 </a>                           
	                 </li>
                </c:forEach>
                </c:if>
					</ul>
				</div>
			</div>
            <div class="col-md-9">
            <!-- message section -->
             <div class="tab-content">
              <c:if test="${not empty modelViewMap.groupData}">
              <c:forEach var="group" items="${modelViewMap.groupData}"  varStatus="status">
                <div class='row tab-pane
                <c:if test="${status.index==0}">
	            	active
	            	</c:if>
                ' id="tab${group.groupId}">
              		 <div class="col-md-9" style="box-shadow: 5px 5px 20px #888888;">
	              		 <div class="row col-md-12 margin10" style="border-bottom:1px solid lightgray">
		                 	<span style="font-size: 1.4em; font-weight: bold">Group :</span> <span style="font-size: 1.4em;">${group.name}</span>
		                 	<c:if test="${currentUser.userRole == 'SUPERADMIN' || currentUser.userRole == 'ADMIN' || currentUser.userRole == 'MANAGER'}">
		                 		<a href="#" data-box="#confirmationbox" class="mb-control" style="float:right" onclick="javascript:deleteGroup('${group.groupId}','${group.name}')"><i class="fa fa-trash-o fa-2x" style="color:red" aria-hidden="true"></i></a>
			                	<a data-toggle="modal" data-target="#creategroupmodal" onclick="editGroup(${group.groupId})" style="float:right"><i class="fa fa-pencil fa-2x" style="padding-right:5px;" aria-hidden="true"></i></a>
		               		</c:if>
		               	</div>
              		 	<div class="messages messages-img margin10"  style="overflow-y: scroll;min-height: 430px;max-height: 430px;" id="messageDiv${group.groupId}">
              		 	   <c:forEach var="groupMessage" items="${group.messages}">
                            <div class='item 
							<c:if test="${modelViewMap.currentUserId != groupMessage.user}">
							in
							</c:if>
							 item-visible'>
                                <div class="image">
                                    <img src="${applicationHome}/image/${groupMessage.user}" alt="${groupMessage.userFullName}" onerror="this.src='/bops/jsp/assets/images/users/no-image.jpg'">
                                </div>
                                <div class="text" 
                                <c:if test="${modelViewMap.currentUserId == groupMessage.user}">
							 style="background: #F6F6F6;"
								</c:if>
                               >
	                               <div class="heading">
	                                <a href="#">${groupMessage.userFullName}</a>
	                                <span class="date">${groupMessage.createdDateDisplay}</span>
	                            </div>
	                            ${groupMessage.description}
	                            </div>
	                            </div>
                             <c:set var="lastmsg" value="${groupMessage.messageId}" />
                            </c:forEach>
                              <input type="hidden" id="lastMessageId_${group.groupId}" value="${lastmsg}" />
                        </div>
                        
                         <div class="panel panel-default push-up-10">
				            <div class="panel-body panel-body-search">
				                <div class="input-group">
				                    <div class="input-group-btn">
				
				                    </div>
				                    <textArea id="${group.groupId}" class="form-control messageTextArea" style="height: 60px;" placeholder="Type in your message here and press enter"></textArea>
				                    <div class="input-group-btn">
				                        <button class="btn btn-info" onclick="sendMessage('${group.groupId}')" style="height: 60px;width: 80px;">Send</button>
				                    </div>
				                </div>
				            </div>
				        </div>
              		 </div>
              		 <div class="col-md-3 margin10">
              		  <h4>Group Members</h4>
              		 	<div class="sortDiv list-group list-group-contacts border-bottom margin10" style="box-shadow: -7px -4px 10px #888888">
                            <c:forEach var="subscriber" items="${group.subscribers}">
                            	 <div class="sortDivData" id="sortDivData${subscriber.id}" data-sort="${subscriber.checkUserStatus}">
                                 <a href="#" class="list-group-item">
                                      <div id="userOnline${subscriber.id}" class='list-group-status userOnline${subscriber.id} 
		                                <c:if test="${subscriber.checkUserStatus == 5}">
		                                status-online
		                                </c:if>
		                                <c:if test="${subscriber.checkUserStatus == 1}">
		                                status-away
		                                </c:if>
		                                 <c:if test="${subscriber.checkUserStatus == 0}">
		                                status-offline
		                                </c:if>
		                                '></div>
                                    <img src="${applicationHome}/image//${subscriber.id}" class="pull-left" alt="${subscriber.firstName}" onerror="this.src='${assets}/images/users/no-image.jpg'"/>
                                    <span class="contacts-title">${subscriber.firstName}&nbsp; ${subscriber.lastName}</span>
                                    <p>${subscriber.designation}</p>
                                </a>
                                </div>
                            </c:forEach>                           
                        </div>
              		 </div>
               </div>
               <!-- message section end-->
               </c:forEach>
               </c:if>
               </div>
            </div>
        </div>
        
    </div>                    
    <!-- END CONTENT FRAME BODY -->
    
</div>                

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
                            data-dismiss="modal">
                        Cancel
                    </button>
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

function sendMessage(group) {
    var groupId = group;
    var message = $('#'+groupId).val();
    if (message == null || message == '') {
        return false;
    }
    var data = {"groupId": groupId , "message": message };
    doAjaxRequestWithGlobal('POST', '/bops/dashboard/sendMessage', data,
    function(response) {
        var responseJsonObj = JSON.parse(response);
        if (responseJsonObj.status == true) {
            var lastMessageId = $('#lastMessageId_'+ groupId).val();
           getMessageAfterId(groupId, lastMessageId);
            $('#'+groupId).val('')
        }
    }, function(e) {
        console.log(e)
    }, false);
}

function clearGroupForm() {
    $('#groupName').val('');
    $('#groupId').val('');
    $('#groupDescription').val('');
    $('.subCheckBox').prop("checked",false);
    $('#createOrUpdateGroupId span').html("Save");
    $('#createGroupFormId').prop("action", "createGroup");

}

function setGroupEditPage(responseJsonObj) {
    $('#groupName').val(responseJsonObj.name);
    $('#groupId').val(responseJsonObj.groupId);
    $('#groupDescription').val(responseJsonObj.description);

    var subscribers = responseJsonObj.subscriberIds;
    for (var i=0; i<subscribers.length; i++) {
    	console.log(subscribers[i]);
        $('#sub'+subscribers[i]).prop("checked",true);
    }
    $('#createGroupFormId').prop("action", "updateGroup");
  
}

function editGroup(groupId) {
    clearGroupForm();
    $('#createOrUpdateGroupId span').html("Update");
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
    doAjaxRequestWithGlobal('GET', '/bops/dashboard/messageAfter?messageId='+messageId+'&groupId='+groupId, null,
        function(response) {
            var responseJsonObj = JSON.parse(response);
            processMessageAfterSave(responseJsonObj, groupId);
        }, function(e) {
            console.log(e)
        }, false);
}

function processMessageAfterSave(jsonResponse, groupId) {
    var htmlString = '';
    var lastMessageId = '';
    var currentUser = ${currentUser.id};
    var messageCount = $("#newMessageCount"+groupId).html();
    
    for (var i = 0; i < jsonResponse.length; i++) {
        var messageObject = jsonResponse[i];
        if (currentUser == messageObject.user) {
            htmlString += '<div class="item item-visible">';
        } else {
            htmlString += '<div class="item in item-visible">';
            
            if (messageCount != "") {
            	messageCount = parseInt(messageCount) + 1;
            }
        }

        htmlString += '<div class="image">';

        htmlString += '<img src="/bops/dashboard/image/' + messageObject.user+'" alt="'+ messageObject.userFullName +'" onerror="this.src=\'/bops/jsp/assets/images/users/no-image.jpg\'">';
        htmlString += '</div>';
        htmlString += '<div class="text" ';
        if (currentUser == messageObject.user) {
       	 htmlString += 'style="background: #F6F6F6;"';
        }
        htmlString += '>';
        htmlString += '<div class="heading">';
        htmlString += '<a href="#">'+ messageObject.userFullName +'</a>';
        htmlString += '<span class="date">'+ messageObject.createdDateDisplay +'</span>';
        htmlString += '</div>';
        htmlString += messageObject.description;
        htmlString += '</div>';
        htmlString += '</div>';
        lastMessageId = messageObject.messageId;
    }

    if (messageCount > 0) {
        $("#newMessageCount"+groupId).html(messageCount);
    	$("#newMessageCount"+groupId).show();
    } else {
    	$("#newMessageCount"+groupId).hide();
    }
    if (htmlString != '') {
        $('#messageDiv'+groupId).append(htmlString);
        scrollMessageDiv(groupId);
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

function resetScrollMessageDiv(groupId, thisVar) {
	$("#newMessageCount"+groupId).hide();
	$("#newMessageCount"+groupId).html("");
	$(".messageGroupLink").each(function(){
		$(this).removeClass("messageGroupLinkLightblue");
	});
	$(thisVar).addClass("messageGroupLinkLightblue");
	scrollMessageDiv(groupId);
}
function scrollMessageDiv(groupId) {
    $('#messageDiv'+groupId).scrollTop($('#messageDiv'+groupId)[0].scrollHeight);
}

$("#groupLeftNavHolder a").first().click();

function deleteGroup(groupId, groupName){
	$(".msgConfirmText").html("Delete group");
	$(".msgConfirmText1").html(groupName);
	$("#confirmYesId").prop("href","javascript:doAjaxDeleteGroup('"+groupId+"')");
}

function doAjaxDeleteGroup(groupId) {
	  doAjaxRequest('GET', '/bops/dashboard/deleteGroup?groupId='+groupId, null,
	    function(response) {
	        var responseJsonObj = JSON.parse(response);
	        if (responseJsonObj.status){
	            window.location.reload(true);
	        }
	    }, function(e) {
	        console.log(e)
	    });
}

setInterval('refreshMessages()', 7000);

</script>