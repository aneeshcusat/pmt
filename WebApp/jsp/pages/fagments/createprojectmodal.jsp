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
                                        <input type="text" class="autocomplete form-control"/>
                                    <span class="help-block">required project name</span>
                                </div>
                            </div>
                            
                            <div class="form-group">                                        
                                <label class="col-md-3 control-label">Project Category</label>
                                 <div class="col-md-9">                                                                                            
                                    <select class="form-control select">
                                        <option>Option 1</option>
                                        <option>Option 2</option>
                                        <option>Option 3</option>
                                        <option>Option 4</option>
                                        <option>Option 5</option>
                                    </select>
                                    <span class="help-block">required project type</span>
                                </div>
                            </div>
                            
                            <div class="form-group">
                                <label class="col-md-3 control-label">Summary</label>
                                <div class="col-md-9 col-xs-12">                                            
                                    <textarea class="form-control" rows="5"></textarea>
                                    <span class="help-block">required summary</span>
                                </div>
                            </div>
                        <div class="form-group">
							<label class="col-xs-3 control-label">Project Type</label>
							<div class="col-xs-9">
								<div class="btn-group" data-toggle="buttons">
									<label class="btn btn-default" id="male"> <input type="radio"
										name="gender" value="male" /> Billable
									</label> <label class="btn btn-default"> <input type="radio"
										name="gender" value="female" id="female"/> Non Billable
									</label>
								</div>
							</div>
						</div>
                        
                        <div class="form-group">                                        
                                <label class="col-md-3 control-label">Priority</label>
                                <div class="col-md-6 col-xs-6">
                                    <div class="input-group">
                                        <select class="form-control">
                                        <option>Urgent</option>
                                        <option>High</option>
                                        <option>Medium</option>
                                        <option>Low</option>
                                    </select>
                                    </div>            
                                    <span class="help-block">required priority</span>
                                </div>
                                
                            </div>
                             <div class="form-group">                                        
                                <label class="col-md-3 control-label">Client</label>
                                <div class="col-md-6 col-xs-6">
                                    <div class="input-group">
                                      <select class="selectpicker form-control">
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
										</select>
                                    </div>            
                                    <span class="help-block">required priority</span>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-3 control-label">Tags</label>
                                <div class="col-md-9">
                                    <input type="text" class="tagsinput" value="First,Second,Third" placeholder="add a tag"/>
                                    <span class="help-block">tag project with keywords</span>
                                </div>
                            </div>
                            </div>
                        <div class="col-md-6">
                            
                             <div class="form-group">                                        
                                <label class="col-md-3 control-label">Reporter</label>
                                <div class="col-md-9">                                                                                            
                                   <input type="text" class="autocomplete form-control" value="Aneeshkumar" readonly="readonly"/>
                                    <span class="help-block">required reporter</span>
                                </div>
                            </div>
                            
                             <div class="form-group">                                        
                                <label class="col-md-3 control-label">Assignee</label>
                                <div class="col-md-9">                                                                                            
                                    <input type="text" class="autocomplete form-control"/>
                                    <span class="help-block">required assignee</span>
                                </div>
                            </div>
                            <div class="form-group">                                        
                                <label class="col-md-3 control-label">Est Start Time</label>
                                <div class="col-md-9">
                                    <div class="input-group">
                                        <span class="input-group-addon"><span class="fa fa-calendar"></span></span>
                                        <input type="text" class="form-control datepicker" value="2014-11-01">                                            
                                    </div>
                                    <span class="help-block">Click on input field to get datepicker</span>
                                </div>
                            </div>
                                                   
                            
                             <div class="form-group">                                        
                                <label class="col-md-3 control-label">Estimated completion time</label>
                               <div class="col-md-5">
                                            <div class="input-group bootstrap-timepicker">
                                                <input type="text" class="form-control timepicker24">
                                                <span class="input-group-addon"><span class="glyphicon glyphicon-time"></span></span>
                                            </div>
                                        </div>
                            </div>
                            
                            <div class="form-group">
							<label class="col-xs-3 control-label">Review</label>
							<div class="col-xs-9">
								<div class="btn-group" data-toggle="buttons">
									<label class="btn btn-default" id="male"> <input type="radio"
										name="gender" value="male" /> Yes
									</label> <label class="btn btn-default"> <input type="radio"
										name="gender" value="female" id="female"/> No
									</label>
								</div>
							</div>
						</div>
                            <div class="form-group">                                        
                                <label class="col-md-3 control-label">Reviewer</label>
                                <div class="col-md-9">                                                                                            
                                    <input type="text" class="autocomplete form-control"/>
                                    <span class="help-block">required reviewer</span>
                                </div>
                            </div>
                             <div class="form-group">
                                <label class="col-md-3 control-label">Watchers</label>
                                <div class="col-md-9">
                                    <input type="text" class="tagsinput autocomplete" placeholder="add an email" value="aneesh@gmail.com,udhayan@gmail.com,aneesh@gmail.com,udhayan@gmail.com"  id="autocomplete"/>
                                    <span class="help-block">who all monitoring this project</span>
                                </div>
                            </div>
                        </div>
                        
                    </div>
  					<div class="row">
	                    <div class="col-md-12">
	                    	  <div class="form-group">
                                <label class="col-md-2 control-label">File upload</label>
                                <div class="col-md-12">
                                   <div class="dropzone dropzone-mini dz-clickable" id="my-dropzone">
							        </div>
								</div>
								</div>
                    	</div>
                    </div>
                </div>
            </div>
