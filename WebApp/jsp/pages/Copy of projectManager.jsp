<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@include file="includes/header.jsp" %>
<link rel="stylesheet" type="text/css" href="${fn:escapeXml(css)}/gantt/dhtmlxganttp.css?v=6.1.5">
<link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="${fn:escapeXml(css)}/gantt/controls_stylesp.css?v=6.1.5">
<style>
.gantt_control {
    background: #fff;
    text-align: center;
}
		html, body {
			height: 100%;
			padding: 0px;
			margin: 0px;
			overflow: hidden;
		}
</style>
<!-- START CONTENT FRAME -->
<div class="content-frame margin5" style="min-height: 500px">
	<div class="content-frame-top">
		<div class="page-title">
			<h2>
				<span class="fa fa-list-alt"></span> Projects Manager
			</h2>
		</div>
	</div>
	<div class="row">
		<div class="col-md-12">

			<div class="panel panel-default">
				<div class="panel-body" style="padding: 0px;">
					<form class="gantt_control scaleControl" style="padding:0px">
						<input type="radio" id="scale0" class="gantt_radio" name="scale" value="0"/><label for="scale0"><i class="material-icons">radio_button_unchecked</i>Minute scale</label>
						<input type="radio" id="scale1" class="gantt_radio" name="scale" value="1"/><label for="scale1"><i class="material-icons">radio_button_unchecked</i>Hour scale</label>
						<input type="radio" id="scale2" class="gantt_radio" name="scale" value="2"/><label for="scale2"><i class="material-icons">radio_button_unchecked</i>Day scale</label>
						<input type="radio" id="scale3" class="gantt_radio" name="scale" value="3"/><label for="scale3"><i class="material-icons">radio_button_unchecked</i>Week scale</label>
						<input type="radio" id="scale4" class="gantt_radio" name="scale" value="4"/><label for="scale4"><i class="material-icons">radio_button_unchecked</i>Month scale</label>
						<input type="radio" id="scale5" class="gantt_radio" name="scale" value="5"/><label for="scale5"><i class="material-icons">radio_button_unchecked</i>Quarter scale</label>
						<input type="radio" id="scale6" class="gantt_radio" name="scale" value="6" checked/><label class="checked_label" for="scale6"><i class="material-icons icon_color">radio_button_checked</i>Year scale</label>
					</form>
				</div>
			</div>

		</div>
	</div>
	 <div class="row" id="projectGanttChart" style='width:100%; height:600px;'>
	 </div>
</div>
<!-- END CONTENT FRAME -->
<%@include file="includes/footer.jsp"%>
<script src="${js}/plugins/gantt/dhtmlxganttp.js?v=6.1.5"></script>
<script>

var demo_tasks = {
		"data":[
			{"id":11, "text":"Project #1", "start_date":"28-03-2018", "duration":"10", "progress": 0.6, "open": true},
			{"id":1, "text":"Project #2", "start_date":"01-04-2018", "duration":"8", "progress": 0.4, "open": true},

			{"id":2, "text":"Task #1", "start_date":"02-04-2018", "duration":"8", "parent":"1", "progress":0.5, "open": true},
			{"id":3, "text":"Task #2", "start_date":"11-04-2018", "duration":"8", "parent":"1", "progress": 0.6, "open": true},
			{"id":4, "text":"Task #3", "start_date":"13-04-2018", "duration":"6", "parent":"1", "progress": 0.5, "open": true},
			
			{"id":12, "text":"Task #1", "start_date":"03-04-2018", "duration":"5", "parent":"11", "progress": 1, "open": true},
			{"id":13, "text":"Task #2", "start_date":"02-04-2018", "duration":"7", "parent":"11", "progress": 0.5, "open": true},
			{"id":14, "text":"Task #3", "start_date":"02-04-2018", "duration":"6", "parent":"11", "progress": 0.8, "open": true},
			{"id":15, "text":"Task #4", "start_date":"02-04-2018", "duration":"5", "parent":"11", "progress": 0.2, "open": true},
			{"id":16, "text":"Task #5", "start_date":"02-04-2018", "duration":"7", "parent":"11", "progress": 0, "open": true},
		],
		"links":[
			{"id":"1","source":"1","target":"2","type":"1"},
			{"id":"2","source":"1","target":"3","type":"1"},
			{"id":"3","source":"1","target":"4","type":"1"},
			{"id":"10","source":"11","target":"12","type":"1"},
			{"id":"11","source":"11","target":"13","type":"1"},
			{"id":"12","source":"11","target":"14","type":"1"},
			{"id":"13","source":"11","target":"15","type":"1"},
			{"id":"14","source":"11","target":"16","type":"1"},
		]
	};
</script>
<script src="${js}/ganttInitializer.js"></script>
</body>