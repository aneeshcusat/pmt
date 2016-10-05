<%@include file="includes/header.jsp" %>
 <ul class="breadcrumb">
     <li><a href="${applicationHome}/index">Home</a></li>  
     <li class="active">Messages</li>
 </ul>
<!-- START CONTENT FRAME -->
<div class="content-frame">                                    
    <!-- START CONTENT FRAME TOP -->
    <div class="content-frame-top">                        
        <div class="page-title">                    
            <h2><span class="fa fa-comments"></span> Messages</h2>
        </div>                                                    
        <div class="pull-right">                            
            <button class="btn btn-danger"><span class="fa fa-book"></span> Contacts</button>
            <button class="btn btn-default content-frame-right-toggle"><span class="fa fa-bars"></span></button>
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
        
        <div class="block">
            <h4>Status</h4>
            <div class="list-group list-group-simple">                                
                <a href="#" class="list-group-item"><span class="fa fa-circle text-success"></span> Online</a>
                <a href="#" class="list-group-item"><span class="fa fa-circle text-warning"></span> Away</a>
                <a href="#" class="list-group-item"><span class="fa fa-circle text-muted"></span> Offline</a>                                
            </div>
        </div>
        
    </div>
    <!-- END CONTENT FRAME RIGHT -->

    <!-- START CONTENT FRAME BODY -->
    <div class="content-frame-body content-frame-body-left">
        <!-- START VERTICAL TABS -->
     <div class="panel panel-default nav-tabs-vertical">                   
         <ul class="nav nav-tabs">
             <li class="active"><a href="#tab22" data-toggle="tab">First</a></li>
             <li><a href="#tab23" data-toggle="tab">Second</a></li>
             <li><a href="#tab24" data-toggle="tab">Third</a></li>
         </ul>                    
         <div class="panel-body tab-content">
             <div class="tab-pane active" id="tab22">
			                <div class="messages messages-img">
			            <div class="item in">
			                <div class="image">
			                    <img src="${assets}/images/users/user2.jpg" alt="John Doe">
			                </div>
			                <div class="text">
			                    <div class="heading">
			                        <a href="#">John Doe</a>
			                        <span class="date">08:33</span>
			                    </div>
			                    Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed facilisis suscipit eros vitae iaculis.
			                </div>
			            </div>
			            <div class="item">
			                <div class="image">
			                    <img src="${assets}/images/users/user.jpg" alt="Dmitry Ivaniuk">
			                </div>                                
			                <div class="text">
			                    <div class="heading">
			                        <a href="#">Dmitry Ivaniuk</a>
			                        <span class="date">08:39</span>
			                    </div>                                    
			                    Integer et ipsum vitae urna mattis dictum. Sed eu sollicitudin nibh, in luctus velit.
			                </div>
			            </div>
			            <div class="item">
			                <div class="image">
			                    <img src="${assets}/images/users/user.jpg" alt="Dmitry Ivaniuk">
			                </div>                                
			                <div class="text">
			                    <div class="heading">
			                        <a href="#">Dmitry Ivaniuk</a>
			                        <span class="date">08:42</span>
			                    </div>                                    
			                    In dapibus ex ut nisl laoreet aliquam. Donec in mollis leo. Aenean nec suscipit neque, non iaculis justo. Quisque eget odio efficitur, porta risus vitae, sagittis neque.
			                </div>
			            </div>
			            <div class="item in">
			                <div class="image">
			                    <img src="${assets}/images/users/user2.jpg" alt="John Doe">
			                </div>
			                <div class="text">
			                    <div class="heading">
			                        <a href="#">John Doe</a>
			                        <span class="date">08:58</span>
			                    </div>
			                    Curabitur et euismod urna?
			                </div>
			            </div>
			            <div class="item">
			                <div class="image">
			                    <img src="${assets}/images/users/user.jpg" alt="Dmitry Ivaniuk">
			                </div>                                
			                <div class="text">
			                    <div class="heading">
			                        <a href="#">Dmitry Ivaniuk</a>
			                        <span class="date">09:11</span>
			                    </div>                                    
			                    Fusce ultricies erat quis massa interdum, eu elementum urna iaculis
			                </div>
			            </div>
			            <div class="item in">
			                <div class="image">
			                    <img src="${assets}/images/users/user2.jpg" alt="John Doe">
			                </div>
			                <div class="text">
			                    <div class="heading">
			                        <a href="#">John Doe</a>
			                        <span class="date">09:22</span>
			                    </div>
			                    Vestibulum cursus ipsum ut dolor vulputate dapibus. Donec elementum est vel vulputate malesuada?
			                </div>
			            </div>
			        </div>                        
			        
			        <div class="panel panel-default push-up-10">
			            <div class="panel-body panel-body-search">
			                <div class="input-group">
			                    <div class="input-group-btn">
			                        <button class="btn btn-default"><span class="fa fa-camera"></span></button>
			                        <button class="btn btn-default"><span class="fa fa-chain"></span></button>
			                    </div>
			                    <input type="text" class="form-control" placeholder="Your message..."/>
			                    <div class="input-group-btn">
			                        <button class="btn btn-default">Send</button>
			                    </div>
			                </div>
			            </div>
			        </div>             
			        </div>
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
</div>
<!-- END PAGE CONTENT FRAME -->
<%@include file="includes/footer.jsp" %>     