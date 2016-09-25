<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<style>
.autocomplete-suggestions { border: 1px solid #999; background: #FFF; overflow: auto; }
.autocomplete-suggestion { padding: 2px 5px; white-space: nowrap; overflow: hidden; }
.autocomplete-selected { background: #F0F0F0; }
.autocomplete-suggestions strong { font-weight: normal; color: #3399FF; }
.autocomplete-group { padding: 2px 5px; }
.autocomplete-group strong { display: block; border-bottom: 1px solid #000; }
</style>
<div class="row">
        <div class="col-md-12">
                    <div class="row">
                        <div class="col-md-6">
                            
                            <div class="form-group">
                                <label class="col-md-3 control-label">Project Name</label>
                                <div class="col-md-9">                                            
                                        <form:input type="text" class="autocomplete form-control" id="projectName" path="name"/>
                                    <span class="help-block">required project name</span>
                                </div>
                            </div>
                            
                            <div class="form-group">                                        
                                <label class="col-md-3 control-label">Project Category</label>
                                 <div class="col-md-9">                                                                                            
                                    <form:select class="form-control select" id="category" path="category">
                                        <option>Option 1</option>
                                        <option>Option 2</option>
                                        <option>Option 3</option>
                                        <option>Option 4</option>
                                        <option>Option 5</option>
                                    </form:select>
                                    <span class="help-block">required project type</span>
                                </div>
                            </div>
                            
                            <div class="form-group">
                                <label class="col-md-3 control-label">Summary</label>
                                <div class="col-md-9 col-xs-12">                                            
                                    <form:textarea class="form-control" rows="5" path="description" id="summary"></form:textarea>
                                    <span class="help-block">required summary</span>
                                </div>
                            </div>
                        <div class="form-group">
							<label class="col-xs-3 control-label">Project Type</label>
							<div class="col-xs-9">
								<div class="btn-group" data-toggle="buttons">
									<label class="btn btn-default" > <input type="radio"
										name="type" value="billable" id="billable"  /> Billable
									</label> 
									<label class="btn btn-default"> <input type="radio"
										name="type" value="nonbillable" id="nonbillable" /> Non Billable
									</label>
								</div>
							</div>
						</div>
                        
                        <div class="form-group">                                        
                                <label class="col-md-3 control-label">Priority</label>
                                <div class="col-md-6 col-xs-6">
                                    <div class="input-group">
                                        <form:select class="form-control" path="priority" id="priority">
                                        <option>Urgent</option>
                                        <option>High</option>
                                        <option>Medium</option>
                                        <option>Low</option>
                                    </form:select>
                                    </div>            
                                    <span class="help-block">required priority</span>
                                </div>
                                
                            </div>
                             <div class="form-group">                                        
                                <label class="col-md-3 control-label">Client</label>
                                <div class="col-md-6 col-xs-6">
                                    <div class="input-group">
                                      <form:select class="selectpicker form-control" path="clientId" id="clientId">
										  <optgroup label="Picnic">
										    <option>Mustard</option>
										    <option>Ketchup</option>
										    <option>Relish</option>
										  </optgroup>
										  <optgroup label="Camping">
										    <option>Tent</option>
										    <option>Flashlight</option>
										    <option>Toilet Paper</option>
										  </optgroup>
										</form:select>
                                    </div>            
                                    <span class="help-block">required priority</span>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-3 control-label">Tags</label>
                                <div class="col-md-9">
                                    <form:input type="text" class="tagsinput" placeholder="add a tag" path="tags" id="tags"/>
                                    <span class="help-block">tag project with keywords</span>
                                </div>
                            </div>
                            </div>
                        <div class="col-md-6">
                            
                             <div class="form-group">                                        
                                <label class="col-md-3 control-label">Reporter</label>
                                <div class="col-md-9">                                                                                            
                                   <form:input type="text" class="autocomplete form-control" value="Aneeshkumar" readonly="readonly" path="reporter" id="reporter"/>
                                    <span class="help-block">required reporter</span>
                                </div>
                            </div>
                            
                             <div class="form-group">                                        
                                <label class="col-md-3 control-label">Assignee</label>
                                <div class="col-md-9">                                                                                            
                                    <form:input type="text" class="autocomplete form-control" path="assignee" id="assignee"/>
                                    <span class="help-block">required assignee</span>
                                </div>
                            </div>
                            <div class="form-group">                                        
                                <label class="col-md-3 control-label">Est Start Time</label>
                                <div class="col-md-9">
                                    <div class="input-group">
                                        <span class="input-group-addon"><span class="fa fa-calendar"></span></span>
                                        <form:input type="date" class="form-control datepicker" value="2014-11-01" path="startTime" id="startTime"/>                                            
                                    </div>
                                    <span class="help-block">Click on input field to get datepicker</span>
                                </div>
                            </div>
                                                   
                            
                             <div class="form-group">                                        
                                <label class="col-md-3 control-label">Estimated completion time</label>
                               <div class="col-md-5">
                                            <div class="input-group bootstrap-timepicker">
                                                <form:input type="text" class="form-control timepicker24" path="duration" id="duration"/>
                                                <span class="input-group-addon"><span class="glyphicon glyphicon-time"></span></span>
                                            </div>
                                        </div>
                            </div>
                            
                            <div class="form-group">
							<label class="col-xs-3 control-label">Review</label>
							<div class="col-xs-9">
								<div class="btn-group" data-toggle="buttons">
									<label class="btn btn-default" id="Y"> <input type="radio"
										name="review" value="Y"  /> Yes
									</label> <label class="btn btn-default"> <input type="radio"
										name="review" value="N" id="N"/> No
									</label>
								</div>
							</div>
						</div>
						<form:input path="id" type="hidden" id="id"/>
                            <div class="form-group">                                        
                                <label class="col-md-3 control-label">Reviewer</label>
                                <div class="col-md-9">                                                                                            
                                    <form:input type="text" class="autocomplete form-control" path="reviewer" id="reviewer"/>
                                    <span class="help-block">required reviewer</span>
                                </div>
                            </div>
                             <div class="form-group">
                                <label class="col-md-3 control-label">Watchers</label>
                                <div class="col-md-9" id="watcher_tag_div">
                                    <form:input type="text" class="tagsinput autocomplete" placeholder="add an email" value="" path="watchers"  id="watchers"/>
                                    <span class="help-block">who all monitoring this project</span>
                                </div>
                            </div>
                        </div>
                        
                    </div>
                </div>
            </div>
