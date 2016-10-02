<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<div class="row">
	<div class="col-md-12">
	<div class="row">
		<div class="col-md-6">
			<!-- START JQUERY VALIDATION PLUGIN -->
			<div class="panel-body">
				<div class="form-group">
					<label class="col-md-3 control-label">First Name</label>
					<div class="col-md-9">
						<form:input path="firstName" type="text" value=""
							name="firstName" id="firstName" class="form-control" placeholder="eg:Myname"
							tabindex="1" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-xs-3 control-label">Gender</label>
					<div class="col-xs-9">
						<div class="btn-group" data-toggle="buttons">
							<label class="btn btn-default" id="male"> <input type="radio"
								name="gender" value="male" /> Male
							</label> <label class="btn btn-default"> <input type="radio"
								name="gender" value="female" id="female"/> Female
							</label> <label class="btn btn-default"> <input type="radio"
								name="gender" value="other" id="other"/> Other
							</label>
						</div>
					</div>
				</div>
				
				
				<div class="form-group">
					<label class="col-md-3 control-label">Mobile Number:</label>
					<div class="col-md-9">
						<form:input path="mobileNumber" id="mobileNumber" type="text" value=""
							name="mobile" class="form-control"
							placeholder="eg:+919686301304" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-3 control-label">Date of birth:</label>
					<div class="col-md-9">
						<div class="input-group">
							<span class="input-group-addon"><span
								class="fa fa-calendar"></span></span> <form:input type="date"
								class="form-control" data-date-format="dd-mm-yyyy"
								placeholder="eg:05-07-2014" path="dateOfBirth" id="dateOfBirth" style="position: relative; z-index: 100000 !important;"/>
							
      						<span class="add-on"><i data-time-icon="icon-time" data-date-icon="icon-calendar"></i></span>
						</div>
					</div>
				</div>
				<div class="form-group">
                    <label class="col-md-3 control-label">Designation</label>
                    <div class="col-md-9">
                        <form:select path="designation" id="designation" class="form-control select" data-live-search="true">
                            <option>Select a designation</option>
                            <option>Designation 1</option>
                            <option>Designation 2</option>
                            <option>Designation 3</option>
                            <option>Designation 4</option>
                            <option>Designation 5</option>
                        </form:select>
                    </div>
                </div>
				<div class="form-group">
					<div class="col-md-6">
						<label>Choose a Photo</label><form:input type="hidden" multiple=""
							id="filePhoto" name="filephoto" path="filePhoto"/>
							
							<div class="image-editor">
						      <input type="file" name="file" id="file" class="cropit-image-input inputfile" onchange="$('.cropit-preview').show();$('.cropit-image-zoom-input').show();$('.image-size-label').show();">
						      <label  class="btn btn-default" for="file"><i class="fa fa-upload fa-2x" style="color: red"></i></label>
						      <div class="cropit-preview" style="display: none"></div>
						      <div class="image-size-label" style="display: none">
						        Resize image
						      </div>
						      <input type="range" class="cropit-image-zoom-input"  style="display: none">
						    </div>
    
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
						<form:input type="text" value="" name="lastName"
							class="form-control" placeholder="eg:Lastname" id="lastName" tabindex="1" path="lastName"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-3 control-label">E-mail:</label>
					<div class="col-md-9">
						<form:input path="email" type="text" value="" name="email" id="email"
							class="form-control" placeholder="eg:name@email.com" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-3 control-label">Group</label>
					<div class="col-md-9">
						<form:select path="group" id="group" class="form-control select" data-live-search="true" name="group">
							<option>Select a group</option>
							<option>Group 1</option>
							<option>Group 2</option>
							<option>Group 3</option>
							<option>Group 4</option>
							<option>Group 5</option>
						</form:select>
					</div>
				</div>

				<div class="form-group">
					<label class="col-md-3 control-label">Qualification</label>
					<div class="col-md-9">
						<form:select path="qualification" id="qualification" class="form-control select" data-live-search="true">
							<option>Select a qualification</option>
							<option>Qualification 1</option>
							<option>Qualification 2</option>
							<option>Qualification 3</option>
							<option>Qualification 4</option>
							<option>Qualification 5</option>
						</form:select>
					</div>
				</div>

				<form:input path="id" type="hidden" id="id"/>

				<div class="form-group">
					<label class="col-md-3 control-label">Role</label>
					<div class="col-md-9">
						<form:select path="role" id="role" class="form-control select" data-live-search="true">
							<option>Select a role</option>
							<option>MANAGER</option>
							<option>ANALYST</option>
							<option>ADMIN</option>
							<option>SUPPORT</option>
							<option>BUSINESS</option>
							<option>CLUBUSER</option>
						</form:select>
    
					</div>
				</div>
			</div>
			<!-- form fields ends -->

		</div>

	</div>
</div>
</div>