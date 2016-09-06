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
							name="firstName" class="form-control" placeholder="eg:Myname"
							tabindex="1" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-xs-3 control-label">Gender</label>
					<div class="col-xs-9">
						<div class="btn-group" data-toggle="buttons">
							<label class="btn btn-default"> <input type="radio"
								name="gender" value="male" /> Male
							</label> <label class="btn btn-default"> <input type="radio"
								name="gender" value="female" /> Female
							</label> <label class="btn btn-default"> <input type="radio"
								name="gender" value="other" /> Other
							</label>
						</div>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-3 control-label">Password:</label>
					<div class="col-md-9">
						<form:input path="password" type="password"
							class="form-control" name="password" id="password2" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-3 control-label">Confirm Password:</label>
					<div class="col-md-9">
						<form:input path="confirmPassword" type="password"
							class="form-control" name="confirmPassword" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-3 control-label">Mobile Number:</label>
					<div class="col-md-9">
						<form:input path="mobileNumber" type="text" value=""
							name="mobile" class="form-control"
							placeholder="eg:+919686301304" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-3 control-label">Date of birth:</label>
					<div class="col-md-9">
						<div class="input-group">
							<span class="input-group-addon"><span
								class="fa fa-calendar"></span></span> <input type="text"
								class="form-control datepicker" data-date-format="dd-mm-yyyy"
								placeholder="eg:05-07-2014">
						</div>
					</div>
				</div>
				<div class="form-group">
					<div class="col-md-12">
						<label>Photo</label><br /> <input type="file" multiple
							id="filephoto" accept="image/*" name="filephoto"/>
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
						<input type="text" value="" name="lastName"
							class="form-control" placeholder="eg:Lastname" tabindex="1" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-3 control-label">E-mail:</label>
					<div class="col-md-9">
						<form:input path="email" type="text" value="" name="email"
							class="form-control" placeholder="eg:name@email.com" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-3 control-label">Group</label>
					<div class="col-md-9">
						<select class="form-control select" data-live-search="true" name="group">
							<option>Select a group</option>
							<option>Group 1</option>
							<option>Group 2</option>
							<option>Group 3</option>
							<option>Group 4</option>
							<option>Group 5</option>
						</select>
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
					</div>
				</div>
			</div>
			<!-- form fields ends -->

		</div>

	</div>

</div>
