<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@include file="includes/header.jsp" %>           
 <!-- START BREADCRUMB -->
 <ul class="breadcrumb">
     <li><a href="${applicationHome}/index">Home</a></li>  
     <li class="active">Employees</li>
 </ul>
 <!-- END BREADCRUMB -->  
<!-- PAGE TITLE -->
<div class="page-title">                    
    <h2><span class="fa fa-arrow-circle-o-left"></span> Frequently Asked Questions</h2>
</div>
<!-- END PAGE TITLE -->                
<!-- Start PAGE CONTENT WRAPPER --> <div class="page-content-wrap">
                    
<div class="row">
    <div class="col-md-8">
        
        <div class="panel panel-default">
            <div class="panel-body">
                <h3 class="push-down-0">General Questions</h3>
            </div>
            <div class="panel-body faq">
                
                <div class="faq-item">
                    <div class="faq-title"><span class="fa fa-angle-down"></span>How do I reset my password?</div>
                    <div class="faq-text">
                        <h5>Password reset can be done from Login page</h5>
                        <p>Open the login page and click on forgot password, you will be receiving a mail with temporary password.</p>
                        <p>Login with the temporary password, that will prompt you to change the password. Please update the password and Login back again to use the system. </p>
                    </div>
                </div>
                
                <div class="faq-item">
                    <div class="faq-title"><span class="fa fa-angle-down"></span>How do I update my personal information?</div>
                    <div class="faq-text">
                        <h5>Personal information can be updated from profile page</h5>
                        <p>Login to the system click on the Profile Pic that will take you to profile page.</p>
                        <p>Locate the edit button and click on it, that will open a new window for you to update your details.</p>
                    </div>
                </div>
            </div>
        </div>
        
        <div class="panel panel-default">
            <div class="panel-body">
                <h3 class="push-down-0">Projects</h3>
            </div>
            <div class="panel-body faq">
                
                <div class="faq-item">
                    <div class="faq-title"><span class="fa fa-angle-down"></span>How do I create a new project?</div>
                    <div class="faq-text">
                        <h5>Project can be created from the Project dashboard</h5>
                        <p>Locate the project create button from project dashboard.</p>
                        <p>Click on the create project option, will open a new window and fill the required details to create a new project</p>
                    </div>
                </div>
            </div>
        </div>
        
    </div>                        
    <div class="col-md-4">
        
        <div class="panel panel-primary">
            <div class="panel-body">
                <h3>Search</h3>
                <form id="faqForm">
                    <div class="input-group">
                        <input type="text" class="form-control" id="faqSearchKeyword" placeholder="Search...">
                        <div class="input-group-btn">
                            <button class="btn btn-primary" id="faqSearch">Search</button>
                        </div>
                    </div>
                </form>
                    <div class="push-up-10"><strong>Search Result:</strong> <span id="faqSearchResult">Please fill keyword field</span></div>
                    <div class="push-up-10">
                        <button class="btn btn-primary" id="faqRemoveHighlights">Remove Highlights</button>
                        <div class="pull-right">
                            <button class="btn btn-default" id="faqOpenAll"><span class="fa fa-angle-down"></span> Open All</button>
                            <button class="btn btn-default" id="faqCloseAll"><span class="fa fa-angle-up"></span> Close All</button>
                        </div>                                       
                    </div>                                    
                </div>
            </div>
            
            <div class="panel panel-primary">
                <div class="panel-body">
                    <h3>Contact</h3>
                    <p>Feel free to contact us for any issues you might have with application.</p>
                    <div class="form-group">
                        <label>E-mail</label>
                        <input type="email" class="form-control" placeholder="youremail@blueoceanmi.com">
                    </div>
                    <div class="form-group">
                        <label>Subject</label>
                        <input type="email" class="form-control" placeholder="Message subject">
                    </div>                                
                    <div class="form-group">
                        <label>Message</label>
                        <textarea class="form-control" placeholder="Your message" rows="3"></textarea>
                    </div>                                
                </div>
                <div class="panel-footer">
                    <button class="btn btn-default hide"><span class="fa fa-paperclip"></span> Add attachment</button>
                    <button class="btn btn-success pull-right"><span class="fa fa-envelope-o"></span> Send</button>
                </div>
            </div>
            
        </div>
    </div>
                                            
</div>
<!-- END PAGE CONTENT WRAPPER -->                                                 
<%@include file="includes/footer.jsp" %>
 <!-- START THIS PAGE PLUGINS-->        
 <script type='text/javascript' src='${js}/plugins/icheck/icheck.min.js'></script>
 <script type="text/javascript" src="${js}/plugins/mcustomscrollbar/jquery.mCustomScrollbar.min.js?v=${fsVersionNumber}"></script>
 
 <script type="text/javascript" src="${js}/plugins/highlight/jquery.highlight-4.js?v=${fsVersionNumber}"></script>
 <!-- END THIS PAGE PLUGINS-->        
  <script type="text/javascript" src="${js}/faq.js?v=${fsVersionNumber}"></script>