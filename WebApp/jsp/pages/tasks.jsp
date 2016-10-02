<%@include file="includes/header.jsp" %>
 <ul class="breadcrumb">
     <li><a href="${applicationHome}/index">Home</a></li>  
     <li class="active">Tasks</li>
 </ul>
<!-- START CONTENT FRAME -->
<div class="content-frame">     
    <!-- START CONTENT FRAME TOP -->
    <div class="content-frame-top">                        
        <div class="page-title">                    
            <h2><span class="fa fa-tasks"></span> Tasks</h2>
        </div>                                                
        <div class="pull-right">
            <button class="btn btn-default content-frame-left-toggle"><span class="fa fa-bars"></span></button>
        </div>                                
        <div class="pull-right" style="width: 100px; margin-right: 5px;">
            <select class="form-control select">
                <option>All</option>                                
                <option>Work</option>
                <option>Home</option>
                <option>Friends</option>
                <option>Closed</option>
            </select>
        </div>
        
    </div>                    
          
    <!-- END CONTENT FRAME TOP -->
    
    <!-- START CONTENT FRAME BODY -->
        <div class="row ">
            <div class="col-md-4">
                
                <h3>To-do List</h3>
                
                <div class="tasks" id="tasks">

                    <div class="task-item task-primary">                                    
                        <div class="task-text">Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris rutrum velit vel erat fermentum, a dignissim dolor malesuada.</div>
                        <div class="task-footer">
                            <div class="pull-left"><span class="fa fa-clock-o"></span> 1h 30min</div>                                    
                        </div>                                    
                    </div>

                    <div class="task-item task-success">                                    
                        <div class="task-text">Suspendisse a tempor eros. Curabitur fringilla maximus lorem, eget congue lacus ultrices eu. Nunc et molestie elit. Curabitur consectetur mollis ipsum, id hendrerit nunc molestie id.</div>
                        <div class="task-footer">
                            <div class="pull-left"><span class="fa fa-clock-o"></span> 1h 45min</div>
                            <div class="pull-right"><a href="#"><span class="fa fa-chain"></span></a> <a href="#"><span class="fa fa-comments"></span></a></div>
                        </div>                                    
                    </div>

                    <div class="task-item task-warning">                                    
                        <div class="task-text">Donec lacus lacus, iaculis nec pharetra id, congue ut tortor. Donec tincidunt luctus metus eget rhoncus.</div>
                        <div class="task-footer">
                            <div class="pull-left"><span class="fa fa-clock-o"></span> 1day ago</div>
                        </div>                                    
                    </div>

                    <div class="task-item task-danger">                                    
                        <div class="task-text">Pellentesque faucibus molestie lectus non efficitur. Vestibulum mattis dignissim diam, eget dapibus urna rutrum vitae.</div>
                        <div class="task-footer">
                            <div class="pull-left"><span class="fa fa-clock-o"></span> 2days ago</div>
                            <div class="pull-right"><a href="#"><span class="fa fa-chain"></span></a> <a href="#"><span class="fa fa-comments"></span></a></div>
                        </div>                                    
                    </div>

                    <div class="task-item task-info">                                    
                        <div class="task-text">Quisque quis ipsum quis magna bibendum laoreet.</div>
                        <div class="task-footer">
                            <div class="pull-left"><span class="fa fa-clock-o"></span> 3days ago</div>
                            <div class="pull-right"><a href="#"><span class="fa fa-chain"></span></a> <a href="#"><span class="fa fa-comments"></span></a></div>
                        </div>                                    
                    </div>
                    
                </div>                            

            </div>
            <div class="col-md-4">
                <h3>In Progress</h3>
                <div class="tasks" id="tasks_progreess">

                    <div class="task-item task-primary">
                        <div class="task-text">In mauris nunc, blandit a turpis in, vehicula viverra metus. Quisque dictum purus lorem, in rhoncus justo dapibus eget. Aenean pretium non mauris et porttitor.</div>
                        <div class="task-footer">
                            <div class="pull-left"><span class="fa fa-clock-o"></span> 2h 55min</div>
                            <div class="pull-right"><span class="fa fa-pause"></span> 4:51</div>
                        </div>                                    
                    </div>                            
                    
                    <div class="task-drop push-down-10">
                        <span class="fa fa-cloud"></span>
                        Drag your task here to start it tracking time
                    </div>
                    
                </div>
            </div>
            <div class="col-md-4">
                <h3>Completed</h3>
                <div class="tasks" id="tasks_completed">
                    <div class="task-item task-danger task-complete">                                    
                        <div class="task-text">Donec maximus sodales feugiat.</div>
                        <div class="task-footer">
                            <div class="pull-left"><span class="fa fa-clock-o"></span> 15min</div>                                    
                        </div>                                    
                    </div>
                    <div class="task-item task-info task-complete">                                    
                        <div class="task-text">Aliquam eget est a dui tincidunt commodo in nec ante.</div>
                        <div class="task-footer">
                            <div class="pull-left"><span class="fa fa-clock-o"></span> 35min</div>                                    
                        </div>                                    
                    </div>
                    <div class="task-drop">
                        <span class="fa fa-cloud"></span>
                        Drag your task here to finish it
                    </div>                                    
                </div>
            </div>
        </div>                        
                                
    <!-- END CONTENT FRAME BODY -->
    
</div>
<!-- END CONTENT FRAME -->

<!-- MODALS -->        
<div class="modal fade" id="taskEdit" tabindex="-1" role="dialog" aria-labelledby="taskEditModalHead" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                <h4 class="modal-title" id="taskEditModalHead">Edit Task</h4>
            </div>
            <div class="modal-body">
                <div class="form-group">
                    <label>Task description</label>
                    <textarea class="form-control" id="task-text" rows="4"></textarea>
                </div>
                <div class="form-group">
                    <label>Task group</label>
                    <select class="form-control select">
                        <option>Work</option>
                        <option>Home</option>
                        <option>Friends</option>
                    </select>
                </div>
            </div>
            <div class="modal-footer">                        
                <button type="button" class="btn btn-primary">Save changes</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>        
<!-- END MODALS -->
<%@include file="includes/footer.jsp" %>  
<script type="text/javascript" src="${js}/plugins/bootstrap/bootstrap-select.js"></script>
 <script type="text/javascript" src="${js}/demo_tasks.js"></script>

