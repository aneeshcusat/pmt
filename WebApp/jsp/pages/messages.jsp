<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@include file="includes/header.jsp" %>
 <ul class="breadcrumb">
     <li><a href="${applicationHome}/index">Home</a></li>  
     <li class="active">Messages</li>
 </ul>
<style>
    #creategroupmodal .modal-dialog  {width:65%;}
   
    
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
			                <div class="messages messages-img" style=" overflow: scroll; min-height: 300px; max-height: 300px;" >
			                 <input type="hidden" id="lastMessageId_${group.groupId}" value=""/>
			                <c:forEach var="groupMessage" items="${group.messages}">
				                <c:if test="${modelViewMap.currentUserId == groupMessage.user}">
				                    <div class="item in">
				                    
				                </c:if>
				                <c:if test="${modelViewMap.currentUserId != groupMessage.user}">
	                                <div class="item">
	                            </c:if>
		                            <div class="image">
		                                <img src="" alt="${groupMessage.userFullName}" onerror="this.src='/bops/jsp/assets/images/users/no-image.jpg'">
		                            </div>
		                            <c:if test="${modelViewMap.currentUserId == groupMessage.user}">
		                            <div class="text" style="background: #F6F6F6;">
		                            </c:if>
		                            <c:if test="${modelViewMap.currentUserId != groupMessage.user}">
		                            <div class="text">
		                            </c:if>
		                            <script type="text/javascript">
		                            setLastMessageId('lastMessageId_'+${groupMessage.group},'${groupMessage.messageId}' );
		                            </script>
		                            
		                                <div class="heading">
		                                    <a href="#">${groupMessage.userFullName}</a>
		                                    <span class="date">${groupMessage.createdDate}</span>
		                                </div>
		                                ${groupMessage.description}
		                            </div>
		                        </div>
			                </c:forEach>
			               
			                
			        </div>                        
			        <div class="panel panel-default push-up-10">
                        <div class="panel-body panel-body-search">
                            <div class="input-group">
                                <div class="input-group-btn">
                                    
                                </div>
                                <input type="text" id="messageTextAreaField_${group.groupId}" class="form-control" placeholder="Your message..."/>
                                <div class="input-group-btn">
                                    <button class="btn btn-default" onclick="sendMessage('${group.groupId}')">Send</button>
                                </div>
                            </div>
                        </div>
                    </div>
			                     
			        </div>
			        </c:forEach>
             <div class="tab-pane" id="tab23">
                 <p>Donec tristique eu sem et aliquam. Proin sodales elementum urna et euismod. Quisque nisl nisl, venenatis eget dignissim et, adipiscing eu tellus. Sed nulla massa, luctus id orci sed, elementum consequat est. Proin dictum odio quis diam gravida facilisis. Sed pharetra dolor a tempor tristique. Sed semper sed urna ac dignissim. Aenean fermentum leo at posuere mattis. Etiam vitae quam in magna viverra dictum. Curabitur feugiat ligula in dui luctus, sed aliquet neque posuere.</p>
                 <p>Nam a nisi et nisi tristique lacinia non sit amet orci. Duis blandit leo odio, eu varius nulla fringilla adipiscing. Praesent posuere blandit diam, sit amet suscipit justo consequat sed. Duis cursus volutpat ante at convallis. Integer posuere a enim eget imperdiet. Nulla consequat dui quis purus molestie fermentum. Donec faucibus sapien eu nisl placerat auctor. Pellentesque quis justo lobortis, tempor sapien vitae, dictum orci.</p>
             </div>
             <div class="tab-pane" id="tab24">
                 <p>Vestibulum cursus augue sed leo tempor, at aliquam orci dictum. Sed mattis metus id velit aliquet, et interdum nulla porta. Etiam euismod pellentesque purus, in fermentum eros venenatis ut. Praesent vitae nibh ac augue gravida lacinia non a ipsum. Aenean vestibulum eu turpis eu posuere. Sed eget lacus lacinia, mollis urna et, interdum dui. Donec sed diam ut metus imperdiet malesuada. Maecenas tincidunt ultricies ipsum, lobortis pretium dolor sodales ut. Donec nec fringilla nulla. In mattis sapien lorem, nec tincidunt elit scelerisque tempus.</p>
                 <p>Nam a nisi et nisi tristique lacinia non sit amet orci. Duis blandit leo odio, eu varius nulla fringilla adipiscing. Praesent posuere blandit diam, sit amet suscipit justo consequat sed. Duis cursus volutpat ante at convallis. Integer posuere a enim eget imperdiet. Nulla consequat dui quis purus molestie fermentum. Donec faucibus sapien eu nisl placerat auctor. Pellentesque quis justo lobortis, tempor sapien vitae, dictum orci.</p>
             </div>                        
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
    console.log(response);
    var responseJson = JSON.parse(response);
    if (responseJson.status){
        window.location.reload(true);
    }
}); 

$('.nav-tabs li:first-child a').trigger( "click" );

function sendMessage(group) {
	var groupId = group;
	var message = $('#messageTextAreaField_'+groupId).val();
	var data = {"groupId": groupId , "message": message };
	doAjaxRequest('POST', '/bops/dashboard/sendMessage', data, 
	function(response) {
        var responseJsonObj = JSON.parse(response);
        if (responseJsonObj.status == true) {
        	var lastMessageId = $('#lastMessageId_'+ groupId).val();
        	getMessageAfterId(groupId, lastMessageId);
        } 
    }, function(e) {
        alert(e)
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
        alert(e)
    });
}

function getMessageAfterId(groupId, messageId) {
	if (messageId == 'undefined' || messageId == null || messageId == '') {
		messageId = 0;
	}
	   doAjaxRequest('GET', '/bops/dashboard/messageAfter?messageId='+messageId+'&groupId='+groupId, null, 
	    function(response) {
	        console.log(response);
	        var responseJsonObj = JSON.parse(response);
	        processMessageAfterSave(responseJsonObj, groupId);
	    }, function(e) {
	        alert(e)
	    });
}

function processMessageAfterSave(jsonResponse, groupId) {
    var htmlString = '';
    var lastMessageId = '';
    for (var i = 0; i < jsonResponse.length; i++) {
        var messageObject = jsonResponse[i];
        htmlString += '<div class="item in item-visible">';
        htmlString += '<div class="image">';
        htmlString += '<img src="'+ messageObject.user+'" alt="'+ messageObject.userFullName +'" onerror="this.src=\'/bops/jsp/assets/images/users/no-image.jpg\'">';
        htmlString += '</div>';
        htmlString += '<div class="text" style="background: #F6F6F6;">';
        htmlString += '<div class="heading">';
        htmlString += '<a href="#">'+ messageObject.userFullName +'</a>';
        htmlString += '<span class="date">'+ messageObject.createdDate +'</span>';
        htmlString += '</div>';
        htmlString += messageObject.description;
        htmlString += '</div>';
        htmlString += '</div>';
        lastMessageId = messageObject.messageId;
    }
    var currentHTML = $('#lastMessageId_'+groupId).parent().html();
    $('#lastMessageId_'+groupId).parent().html(currentHTML + htmlString);
    $('#lastMessageId_'+groupId).val(lastMessageId);
}

$(".messages .item").each(function(index){
    var elm = $(this);
    setInterval(function(){
        elm.addClass("item-visible");
    },index*100);              
});
</script>