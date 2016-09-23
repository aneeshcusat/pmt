<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
            </div>            
            <!-- END PAGE CONTENT -->
               
        </div>
        <!-- END PAGE CONTAINER -->
        
        <!-- MESSAGE BOX-->
        <div class="message-box animated fadeIn" data-sound="alert" id="mb-signout">
            <div class="mb-container">
                <div class="mb-middle">
                    <div class="mb-title"><span class="fa fa-sign-out"></span> Log <strong>Out</strong> ?</div>
                    <div class="mb-content">
                        <p>Are you sure you want to log out?</p>                    
                        <p>Press No if you want to continue work. Press Yes to logout current user.</p>
                    </div>
                    <div class="mb-footer">
                        <div class="pull-right">
                            <a href="logout" class="btn btn-success btn-lg">Yes</a>
                            <button class="btn btn-default btn-lg mb-control-close">No</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!-- END MESSAGE BOX-->
        
         <!-- MESSAGE BOX-->
        <div class="message-box message-box-danger animated fadeIn" data-sound="alert" id="confirmationbox">
            <div class="mb-container">
                <div class="mb-middle">
                    <div class="mb-title"><span class="fa fa-times"></span> <span class="msgConfirmText"></span> <strong  class="msgConfirmText1"></strong> ?</div>
                    <div class="mb-content">
                        <p>Do you really want to <span class="msgConfirmText"></span>?</p>                    
                        <p>Press No if you want to continue work. Press Yes to <span class="msgConfirmText"></span> <strong  class="msgConfirmText1"></strong>.</p>
                    </div>
                    <div class="mb-footer">
                        <div class="pull-right">
                            <a href="#" class="btn btn-success btn-lg" id="confirmYesId">Yes</a>
                            <button class="btn btn-default btn-lg mb-control-close">No</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!-- END MESSAGE BOX-->

        <!-- START PRELOADS -->
        <audio id="audio-alert" src="${audio}/alert.mp3" preload="auto"></audio>
        <audio id="audio-fail" src="${audio}/fail.mp3" preload="auto"></audio>
        <!-- END PRELOADS -->                  
        
    <!-- START SCRIPTS -->
        <!-- START PLUGINS -->
        <script type="text/javascript" src="${js}/plugins/jquery/jquery.min.js"></script>
        <script type="text/javascript" src="${js}/plugins/jquery/jquery-ui.min.js"></script>
        <script type="text/javascript" src="${js}/plugins/bootstrap/bootstrap.min.js"></script>  
              
        <!-- END PLUGINS -->

        <!-- START THIS PAGE PLUGINS-->        
        <script type='text/javascript' src="${js}/plugins/icheck/icheck.min.js"></script>        
        <script type="text/javascript" src="${js}/plugins/mcustomscrollbar/jquery.mCustomScrollbar.min.js"></script>
        <script type="text/javascript" src="${js}/plugins/scrolltotop/scrolltopcontrol.js"></script>
        <script type="text/javascript" src="${js}/jquery.cropit.js"></script>  
        
        <script type="text/javascript" src="${js}/plugins/morris/raphael-min.js"></script>
        <script type="text/javascript" src="${js}/plugins/morris/morris.min.js"></script>       
        <script type="text/javascript" src="${js}/plugins/rickshaw/d3.v3.js"></script>
        <script type="text/javascript" src="${js}/plugins/rickshaw/rickshaw.min.js"></script>
        <script type='text/javascript' src="${js}/plugins/jvectormap/jquery-jvectormap-1.2.2.min.js"></script>
        <script type='text/javascript' src="${js}/plugins/jvectormap/jquery-jvectormap-world-mill-en.js"></script>                
        <script type='text/javascript' src="${js}/plugins/bootstrap/bootstrap-datepicker.js"></script>                
        <script type="text/javascript" src="${js}/plugins/owl/owl.carousel.min.js"></script>                 
        
        <script type="text/javascript" src="${js}/plugins/moment.min.js"></script>
        <script type="text/javascript" src="${js}/plugins/daterangepicker/daterangepicker.js"></script>
        <script type="text/javascript" src="${js}/plugins/bootstrap/bootstrap-timepicker.min.js"></script>
        <script type="text/javascript" src="${js}/plugins/datatables/jquery.dataTables.min.js"></script>  
        <script type="text/javascript" src="${js}/plugins/jquery/jquery.form.min.js"></script>
        <script type="text/javascript" src="${js}/plugins/blockui/jquery.blockUI.js"></script>
        <!-- END THIS PAGE PLUGINS-->        

<script>
var site_settings = '<div class="ts-button">'
        +'<span class="fa fa-cogs fa-spin"></span>'
    +'</div>'
    +'<div class="ts-body">'
	    +'<div class="ts-title">Themes</div>'
        +'<div class="ts-themes">'
            +'<a href="#" class="active" data-theme="css/theme-default.css"><img src="${image}/themes/default.jpg"/></a>'            
            +'<a href="#" data-theme="../css/theme-brown.css"><img src="${image}/themes/brown.jpg"/></a>'
            +'<a href="#" data-theme="../css/theme-blue.css"><img src="${image}/themes/blue.jpg"/></a>'                        
            +'<a href="#" data-theme="../css/theme-white.css"><img src="${image}/themes/light.jpg"/></a>'            
            +'<a href="#" data-theme="../css/theme-black.css"><img src="${image}/themes/black.jpg"/></a>'
        +'</div>'
		+'<div class="ts-title">Layout</div>'
        +'<div class="ts-row">'
            +'<label class="check"><input type="radio" class="iradio" name="st_layout_boxed" value="0" checked/> Full Width</label>'
        +'</div>'
        +'<div class="ts-row">'
            +'<label class="check"><input type="radio" class="iradio" name="st_layout_boxed" value="1"/> Boxed</label>'
        +'</div>'
        +'<div class="ts-title">Options</div>'
        +'<div class="ts-row">'
            +'<label class="check"><input type="checkbox" class="icheckbox" name="st_head_fixed" value="1"/> Auto refresh</label>'
        +'</div>'
    +'</div>';

    </script>

        <!-- START TEMPLATE -->
        <script type="text/javascript" src="${js}/settings.js"></script>
        
        <script type="text/javascript" src="${js}/plugins.js"></script>        
        <script type="text/javascript" src="${js}/actions.js"></script>
        <!--
        <script type="text/javascript" src="${js}/demo_dashboard.js"></script>
         END TEMPLATE -->
    <!-- END SCRIPTS -->         
    </body>
</html>
