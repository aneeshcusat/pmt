<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@include file="includes/header.jsp" %>    
                
                <!-- PAGE CONTENT WRAPPER -->
                <div class="page-content-wrap">
                
                    <div class="row">
                        <div class="col-md-12">
                            
                           <form:form id="jvalidate" action="createEmployee" method="POST" role="form" class="form-horizontal">     
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <h3 class="panel-title"><strong>Register</strong> a new employee</h3>
                                    <ul class="panel-controls">
                                        <li><a href="employees" class=""><span class="fa fa-times"></span></a></li>
                                    </ul>
                                </div>
                                <div class="panel-body">  
                                                                                                  
                                    <div class="row">
                                        <div class="col-md-6">
                                          <!-- START JQUERY VALIDATION PLUGIN -->    
				                                <div class="panel-body">   
				                                	<div class="form-group">
				                                        <label class="col-md-3 control-label">First Name</label>
				                                        <div class="col-md-9">
				                                            <form:input path="firstName" type="text" value="" name="firstName" class="form-control" placeholder="eg:Myname" tabindex="1"/>                                        
				                                            <span class="help-block">required name</span>
				                                        </div>
				                                    </div>    
				                                    
				                                    <div class="form-group">
				                                        <label class="col-md-3 control-label">E-mail:</label>
				                                        <div class="col-md-9">
				                                            <form:input path="email" type="text" value="" name="email" class="form-control" placeholder="eg:name@email.com"/>                                        
				                                            <span class="help-block">required email</span>
				                                        </div>
				                                    </div>
				                                    <div class="form-group">
				                                        <label class="col-md-3 control-label">Password:</label>                                        
				                                        <div class="col-md-9">
				                                            <form:input path="password" type="password" class="form-control" name="password" id="password2"/>                                        
				                                            <span class="help-block">min size = 5, max size = 10</span>
				                                        </div>
				                                    </div>                    
				                                    <div class="form-group">
				                                        <label class="col-md-3 control-label">Confirm Password:</label>                                       
				                                        <div class="col-md-9">
				                                            <form:input path="confirmPassword" type="password" class="form-control" name="confirmPassword"/>
				                                            <span class="help-block">required same value as Password</span>
				                                        </div>
				                                    </div>               
				                                     <div class="form-group">
				                                        <label class="col-md-3 control-label">Mobile Number:</label>
				                                        <div class="col-md-9">
				                                            <form:input path="mobileNumber" type="text" value="" name="mobile" class="form-control" placeholder="eg:+919686301304"/>                                        
				                                            <span class="help-block">required email</span>
				                                        </div>
				                                    </div>                                           
				                                    <div class="form-group">
				                                        <label class="col-md-3 control-label">Date of birth:</label>
			                                       <div class="col-md-9">
                                                    <div class="input-group">
                                                        <span class="input-group-addon"><span class="fa fa-calendar"></span></span>
                                                        <input type="text" class="form-control datepicker" data-date-format="dd-mm-yyyy" placeholder="eg:05-07-2014">                                            
                                                    </div>
                                                    <span class="help-block">required date</span>
                                                	</div>
				                                    </div>
				                                       <div class="form-group">
												        <label class="col-xs-3 control-label">Gender</label>
												        <div class="col-xs-9">
												            <div class="btn-group" data-toggle="buttons">
												                <label class="btn btn-default">
												                    <input type="radio" name="gender" value="male" /> Male
												                </label>
												                <label class="btn btn-default">
												                    <input type="radio" name="gender" value="female" /> Female
												                </label>
												                <label class="btn btn-default">
												                    <input type="radio" name="gender" value="other" /> Other
												                </label>
												            </div>
												        </div>
												    </div>
				                                     <div class="form-group">
			                                            <div class="col-md-12">
			                                                <label>Photo</label><br/>
			                                                <input type="file" multiple id="filephoto" accept="image/*"/>
			                                            </div>                                            
			                                        </div>
				                                                                                                                      
				                            </div>
				                            <!-- END JQUERY VALIDATION PLUGIN -->                
                                        </div>
                                        <div class="col-md-6">
                                             <!-- From fields start-->
                                               <div class="panel-body">   
				                                	<div class="form-group">
				                                        <label class="col-md-3 control-label">Last Name</label>
				                                        <div class="col-md-9">
				                                            <input type="text" value="" name="lastName" class="form-control" placeholder="eg:Lastname" tabindex="1"/>                                        
				                                            <span class="help-block">required name</span>
				                                        </div>
				                                    </div>    
				                                    <div class="form-group">
				                                        <label class="col-md-3 control-label">Group</label>
					                                   <div class="col-md-9">                                                                                
				                                            <select class="form-control select" data-live-search="true">
				                                            	<option>Select a group</option>
				                                                <option>Group 1</option>
				                                                <option>Group 2</option>
				                                                <option>Group 3</option>
				                                                <option>Group 4</option>
				                                                <option>Group 5</option>
				                                            </select>
				                                            <span class="help-block">required group</span>
					                                   </div>
				                                   </div>
				                                   
				                                   <div class="form-group">
				                                        <label class="col-md-3 control-label">Qualification</label>
					                                   <div class="col-md-9">                                                                                
				                                            <select class="form-control select" data-live-search="true">
				                                            	<option>Select a qualification</option>
				                                                <option>Qualification 1</option>
				                                                <option>Qualification 2</option>
				                                                <option>Qualification 3</option>
				                                                <option>Qualification 4</option>
				                                                <option>Qualification 5</option>
				                                            </select>
				                                            <span class="help-block">required qualification</span>
					                                   </div>
				                                   </div>
				                                   
				                                    <div class="form-group">
				                                        <label class="col-md-3 control-label">Designation</label>
					                                   <div class="col-md-9">                                                                                
				                                            <select class="form-control select" data-live-search="true">
				                                            	<option>Select a designation</option>
				                                                <option>Designation 1</option>
				                                                <option>Designation 2</option>
				                                                <option>Designation 3</option>
				                                                <option>Designation 4</option>
				                                                <option>Designation 5</option>
				                                            </select>
				                                            <span class="help-block">required designation</span>
					                                   </div>
				                                   </div>
				                                   
				                                    <div class="form-group">
				                                        <label class="col-md-3 control-label">Role</label>
					                                   <div class="col-md-9">                                                                                
				                                            <select class="form-control select" data-live-search="true">
				                                             	<option>Select a role</option>
				                                                <option>Role 1</option>
				                                                <option>Role 2</option>
				                                                <option>Role</option>
				                                                <option>Role 4</option>
				                                                <option>Role 5</option>
				                                            </select>
				                                            <span class="help-block">required Role</span>
					                                   </div>
				                                   </div>
				                            </div>
                                             <!-- form fields ends -->
                                            
                                        </div>
                                        
                                    </div>
                                </div>
                                <div class="panel-footer">
                                    <button class="btn btn-default">Clear Form</button>                                    
                                    <button class="btn btn-primary pull-right">Create</button>
                                </div>
                            </div>
                            </form:form>
                            
                        </div>
                    </div>                    
                    
                </div>
            
    <!-- END SCRIPTS -->     
                <!-- END PAGE CONTENT WRAPPER -->                                                
            <%@include file="includes/footer.jsp" %>  
            <script type='text/javascript' src='${js}/plugins/jquery-validation/jquery.validate.js'></script>   
            <script type="text/javascript" src="${js}/plugins/bootstrap/bootstrap-select.js"></script>
       	 	<script type="text/javascript" src="${js}/plugins/tagsinput/jquery.tagsinput.min.js"></script>
       	 	<script type="text/javascript" src="${js}/plugins/fileinput/fileinput.min.js"></script>
       	 	
       	 	   <script type="text/javascript">
            var jvalidate = $("#jvalidate").validate({
                ignore: [],
                rules: {                                            
                        firstName: {
                                required: true,
                                minlength: 2,
                                maxlength: 8
                        },
                        password: {
                                required: true,
                                minlength: 5,
                                maxlength: 10
                        },
                        confirmPassword: {
                                required: true,
                                minlength: 5,
                                maxlength: 10,
                                equalTo: "#password2"
                        },
                        email: {
                                required: true,
                                email: true
                        }
                    }                                        
                });                                    

            $(function(){
                $("#filephoto").fileinput({
                        showUpload: false,
                        showCaption: false,
                        browseClass: "btn btn-danger",
                        fileType: "any"
                });    
            });
        </script>