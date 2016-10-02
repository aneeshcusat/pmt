<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<div class="row">
	<div class="col-md-12">
		<div class="row">
			<div class="col-md-6">

				<div class="form-group">
					<label class="col-md-3 control-label">Task Name</label>
					<div class="col-md-9">
						<form:input type="text" class="autocomplete form-control"
							id="projectName" path="name" />
						<span class="help-block">required task name</span>
					</div>
				</div>


				<div class="form-group">
					<label class="col-xs-3 control-label">Task Type</label>
					<div class="col-xs-9">
						<div class="btn-group" data-toggle="buttons">
							<label class="btn btn-default"> <input type="radio"
								name="type" value="billable" id="billable" /> Billable
							</label> <label class="btn btn-default"> <input type="radio"
								name="type" value="nonbillable" id="nonbillable" /> Non
								Billable
							</label>
						</div>
					</div>
				</div>
			</div>
			<div class="col-md-6">

				<div class="form-group">
					<label class="col-md-3 control-label">Assignee</label>
					<div class="col-md-9">
						<form:select class="form-control select" path="assignee"
							id="assignee">
							<option value="-1">Select a assignee</option>
						</form:select>
						<span class="help-block">required assignee</span>
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-3 control-label">Est Start Time</label>
					<div class="col-md-9">
						<div class="input-group">
							<span class="input-group-addon"><span
								class="fa fa-calendar"></span></span>
							<form:input type="date" class="form-control datepicker"
								value="2014-11-01" path="startTime" id="startTime" />
						</div>
						<span class="help-block">Click on input field to get
							datepicker</span>
					</div>
				</div>


				<div class="form-group">
					<label class="col-md-3 control-label">Estimated completion
						time</label>
					<div class="col-md-5">
						<div class="input-group bootstrap-timepicker">
							<form:input type="text" class="form-control timepicker24"
								path="duration" id="duration" />
							<span class="input-group-addon"><span
								class="glyphicon glyphicon-time"></span></span>
						</div>
					</div>
				</div>
			</div>

		</div>
	</div>
</div>
