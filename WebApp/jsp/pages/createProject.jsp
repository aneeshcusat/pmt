<%@include file="includes/header.jsp" %>    
<style>
.autocomplete-suggestions { border: 1px solid #999; background: #FFF; overflow: auto; }
.autocomplete-suggestion { padding: 2px 5px; white-space: nowrap; overflow: hidden; }
.autocomplete-selected { background: #F0F0F0; }
.autocomplete-suggestions strong { font-weight: normal; color: #3399FF; }
.autocomplete-group { padding: 2px 5px; }
.autocomplete-group strong { display: block; border-bottom: 1px solid #000; }
</style>
<!-- PAGE CONTENT WRAPPER -->
<div class="page-content-wrap">

    <div class="row">
        <div class="col-md-12">
            
            <form class="form-horizontal">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title"><strong>Create</strong> a new project</h3>
                    <ul class="panel-controls">
                        <li><a href="#" class="panel-remove"><span class="fa fa-times"></span></a></li>
                    </ul>
                </div>
                <div class="panel-body">                                                                        
                    
                    <div class="row">
                        
                        <div class="col-md-6">
                            
                            <div class="form-group">
                                <label class="col-md-3 control-label">Project Name</label>
                                <div class="col-md-9">                                            
                                    <div class="input-group">
                                        <span class="input-group-addon"><span class="fa fa-pencil"></span></span>
                                        <input type="text" class="form-control"/>
                                    </div>                                            
                                    <span class="help-block">required project name</span>
                                </div>
                            </div>
                            
                            <div class="form-group">                                        
                                <label class="col-md-3 control-label">Project Type</label>
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
                                <label class="col-md-3 control-label">Priority</label>
                                <div class="col-md-9 col-xs-12">
                                    <div class="input-group">
                                        <select class="form-control select">
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
                                <label class="col-md-3 control-label">Tags</label>
                                <div class="col-md-9">
                                    <input type="text" class="tagsinput" value="First,Second,Third"/>
                                    <span class="help-block">tag project with keywords</span>
                                </div>
                            </div>
                            </div>
                        <div class="col-md-6">
                            
                              <div class="form-group">                                        
                                <label class="col-md-3 control-label">Assignee</label>
                                <div class="col-md-9">                                                                                            
                                    <input type="text" class="autocomplete form-control"/>
                                    <span class="help-block">required assignee</span>
                                </div>
                            </div>
                            
                             <div class="form-group">                                        
                                <label class="col-md-3 control-label">Reporter</label>
                                <div class="col-md-9">                                                                                            
                                   <input type="text" class="autocomplete form-control"/>
                                    <span class="help-block">required reporter</span>
                                </div>
                            </div>
                            <div class="form-group">                                        
                                <label class="col-md-3 control-label">Estimated completion date</label>
                                <div class="col-md-9">
                                    <div class="input-group">
                                        <span class="input-group-addon"><span class="fa fa-calendar"></span></span>
                                        <input type="text" class="form-control datepicker" value="2014-11-01">                                            
                                    </div>
                                    <span class="help-block">Click on input field to get datepicker</span>
                                </div>
                            </div>
                            
                             <div class="form-group">                                        
                                <label class="col-md-3 control-label">Estimated completion hours</label>
                               <div class="col-md-5">
                                            <div class="input-group bootstrap-timepicker">
                                                <input type="text" class="form-control">
                                                <span class="input-group-addon"><span class="glyphicon glyphicon-time"></span></span>
                                            </div>
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
                                <label class="col-md-3 control-label">Watchers</label>
                                <div class="col-md-9">
                                    <input type="text" class="tagsinput autocomplete" value="aneesh@gmail.com,udhayan@gmail.com,aneesh@gmail.com,udhayan@gmail.com"  id="autocomplete"/>
                                    <span class="help-block">who all monitoring this project</span>
                                </div>
                            </div>
                        </div>
                        
                    </div>
                     </form>
  					<div class="row">
	                    <div class="col-md-12">
	                    	  <div class="form-group">
                                <label class="col-md-1 control-label">File upload</label>
                                <div class="col-md-12">
                                    <form action="#" class="dropzone dropzone-mini"></form>
								</div>
								</div>
                    	</div>
                    </div>
                </div>
                <div class="panel-footer">
                    <button class="btn btn-default">Clear Form</button>                                    
                    <button class="btn btn-primary pull-right">Submit</button>
                </div>
            </div>
        </div>
    </div>                    
    
</div>
<!-- END PAGE CONTENT WRAPPER --> 

<%@include file="includes/footer.jsp" %>  
<script type='text/javascript' src='${js}/plugins/jquery-validation/jquery.validate.js'></script>   
<script type="text/javascript" src="${js}/plugins/bootstrap/bootstrap-select.js"></script>
<script type="text/javascript" src="${js}/plugins/tagsinput/jquery.tagsinput.min.js"></script>
<script type="text/javascript" src="${js}/plugins/fileinput/fileinput.min.js"></script>
<script type="text/javascript" src="${js}/plugins/dropzone/dropzone.min.js"></script>
<script type="text/javascript" src="${js}/plugins/autocomplete/jquery.autocomplete.js"></script>

<script>
var countries = [
                 { value: 'Andorra', data: 'AD' },
                 // ...
                 { value: 'Zimbabwe', data: 'ZZ' }
             ];

$('.autocomplete').autocomplete({
    //serviceUrl: '/autocomplete/countries',
     lookup: countries,
    onSelect: function (suggestion) {
        alert('You selected: ' + suggestion.value + ', ' + suggestion.data);
    }
});

</script>