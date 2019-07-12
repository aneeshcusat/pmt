<%@include file="includes/header.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<link rel="stylesheet" type="text/css" id="theme" href="${fn:escapeXml(css)}/pages/dashboard1.css?v=${fsVersionNumber}"/>
<link rel="stylesheet" type="text/css" id="theme" href="${fn:escapeXml(css)}/simpledonut/simple-donut.css"/>
<div class="dashboardcontainer">

<%@include file="includes/dashboard1/dashboardhome.jsp" %>
<%@include file="includes/dashboardtu.jsp" %>
<%@include file="includes/dashboardbandwidth.jsp" %> 
<%-- <%@include file="includes/dashboardtucompare.jsp" %>  --%>
</div>
<script type="text/javascript">
	ajaxStartLabelDisabled=true;
</script>
<%@include file="includes/footer.jsp" %>
<script type="text/javascript">
	//var fullcaledartuSource = "/bops/dashboard/getEmpUtlAjaxFullcalendar/"+$(".dashboadgroup").val();
	//var fullcaledarBsSource = "/bops/dashboard/getEmpBWAjaxFullcalendar/"+$(".dashboadgroup").val();
</script>
<script type="text/javascript" src="${js}/plugins/fullcalendar/fullcalendar.min.js?v=${fsVersionNumber}"></script>
<script type="text/javascript" src="${js}/famstack.calender.js?v=${fsVersionNumber}"></script>
<script type="text/javascript" src="${js}/plugins/flot/jquery.flot.js?v=${fsVersionNumber}"></script>
<script  type="text/javascript" src="${js}/plugins/flot/jquery.flot.pie.js?v=${fsVersionNumber}"></script>
<script  type="text/javascript" src="${js}/plugins/flot/jquery.flot.tooltip.js?v=${fsVersionNumber}"></script>
 <script type="text/javascript" src="${js}/plugins/simpledonut/simple-donut-jquery.js?v=${fsVersionNumber}"></script>
 <script type="text/javascript" src="${js}/plugins/datepicker/jquery.datetimepicker.custom.min.js?v=${fsVersionNumber}"></script>
<script type="text/javascript" src="${js}/famstack.dashboard1.js?v=${fsVersionNumber}"></script>