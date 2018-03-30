<script type="text/javascript">
        // Set timeout variables.
        var timoutWarning = 1200000; // Display warning in 20Mins.
        var timoutNow = 1260000; // Timeout in 21 mins.
        var logoutUrl = '${applicationHome}/logout';
        var warningTimer;
        var timeoutTimer;
        // Start timers.
        function StartTimers() {
            warningTimer = setTimeout("IdleWarning()", timoutWarning);
            timeoutTimer = setTimeout("IdleTimeout()", timoutNow);
        }
        // Reset timers.
        function ResetTimers() {
            clearTimeout(warningTimer);
            clearTimeout(timeoutTimer);
            StartTimers();
            //$("#timeout").dialog('close');
        }
        function IdleWarning() {
            /* $("#timeout").dialog({
                modal: true
            }); */
        }
        function IdleTimeout() {
            window.location = logoutUrl;
        }
        
        StartTimers();
        $(document).on("mouseover",function(){
        	ResetTimers();
        });
    </script>
    <div id="sessionTimeOutDiv" style="display:none">
        <h1>
            Session About To Timeout</h1>
        <p>
            You will be automatically logged out in 1 minute.<br />
        To remain logged in move your mouse over this window.
    </div>
