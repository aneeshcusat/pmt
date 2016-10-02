<%@include file="includes/header.jsp" %>
 <ul class="breadcrumb">
     <li><a href="${applicationHome}/index">Home</a></li>  
     <li class="active">Task Allocator</li>
 </ul>
<style>
.disabled .fc-day-content {
    background-color: #123959;
    color: #FFFFFF;
    cursor: default;
}
#taskAllocationTable .drop {
	width: 40px;
	background-color: #fcf8e3;
	height: 15px;
	padding: 2px;
}

#taskAllocationTable .nodrop {
	width: 100px;
	height: 15px;
	padding: 2px;
} 

#taskAllocationTable th {
	font-weight: normal;
	font-size: 8pt;
	padding: 3px;
	text-align: center;
}
#taskAllocationTable tr {
	height: 5px;
}
.autocomplete-suggestions { border: 1px solid #999; background: #FFF; overflow: auto; }
.autocomplete-suggestion { padding: 2px 5px; white-space: nowrap; overflow: hidden; }
.autocomplete-selected { background: #F0F0F0; }
.autocomplete-suggestions strong { font-weight: normal; color: #3399FF; }
.autocomplete-group { padding: 2px 5px; }
.autocomplete-group strong { display: block; border-bottom: 1px solid #000; }
.draggable{
z-index: 1000;
}
.draggable{
font-size: 5pt;
position: static;
}
.draggable.task{
border: 1px solid blue;
background-color: lightblue;
font-size: 1.4em;
}
</style>
<!-- START CONTENT FRAME -->
<div class="content-frame">            
    <!-- START CONTENT FRAME TOP -->
    <div class="content-frame-top">                        
        <div class="page-title">                    
            <h2><span class="fa fa-calendar"></span> Task allocator</h2>
        </div>  
        <div class="pull-right">
            <button class="btn btn-default content-frame-left-toggle"><span class="fa fa-bars"></span></button>
        </div>                                                                                
    </div>
    <!-- END CONTENT FRAME TOP -->
    
    <!-- START CONTENT FRAME LEFT -->
    <div class="content-frame-left"  style="background-color: white">
        <h4>Unassigned Projects</h4>
        <div class="list-group border-bottom" id="external-events">                                    
           <span class="list-group-item draggable ">Project 1</span>
           <span class="list-group-item draggable ">Project 2</span>
           <span class="list-group-item draggable ">Project 3</span>
           <span class="list-group-item draggable">Project 4</span>
           <span class="list-group-item draggable">Project 5</span>
           
        </div>    
        
    </div>
    <!-- END CONTENT FRAME LEFT -->
    
    <!-- START CONTENT FRAME BODY -->
    <div class="content-frame-body padding-bottom-0">
        
        <div class="row">
            <div class="col-md-12">
            <div class="fc-toolbar"><div class="fc-left"><div class="fc-button-group"><button type="button" class="fc-prev-button fc-button fc-state-default fc-corner-left"><span class="fc-icon fc-icon-left-single-arrow"></span></button><button type="button" class="fc-next-button fc-button fc-state-default fc-corner-right"><span class="fc-icon fc-icon-right-single-arrow"></span></button></div><button type="button" class="fc-today-button fc-button fc-state-default fc-corner-left fc-corner-right fc-state-disabled" disabled="disabled">today</button></div><div class="fc-right"><div class="fc-button-group"><button type="button" class="fc-agendaDay-button fc-button fc-state-default fc-corner-left fc-state-active">day</button><button type="button" class="fc-agendaWeek-button fc-button fc-state-default">week</button><button type="button" class="fc-month-button fc-button fc-state-default fc-corner-right">month</button></div></div><div class="fc-center"><h2>September 24, 2016</h2></div><div class="fc-clear"></div></div>
             <table class="table table-bordered" id="taskAllocationTable">
             <thead>
                 <tr style="width: 500px;overflow: scroll;font-weight: none;font-size: 10pt">
                     <th class="nodrop">Employee</th>
                     <th>09:00 - 10:00</th>
                     <th>10:01 - 11:00</th>
                     <th>11:01 - 12:00</th>
                     <th>12:01 - 01:00</th>
                     <th style="background-color: gray; width: 50px">13</th>
                     <th>14:01 - 15:00</th>
                     <th>15:01 - 16:00</th>
                     <th>16:01 - 17:00</th>
                     <th>17:01 - 18:00</th>
                     <th>18:01 - 19:00</th>
                     <th>19:01 - 20:00</th>
                     <th>20:01 - 21:00</th>
                     <th style="width: 50px;font-weight:bold">Total Hrs</th>
                     <th style="width: 50px;font-weight:bold">Avlbl Hrs</th>
                 </tr>
             </thead>
             <tbody>
             <tr class="droppable">
           		  <td class="nodrop">Aneesh </td>
                  <td class="drop"></td>
                  <td class="drop"></td>
                  <td class="drop"></td>
                  <td class="drop"></td>
                  <td style="background-color: gray; width: 50px"></td>
                  <td class="drop"></td>
                  <td class="drop"></td>
                  <td class="drop"></td>
                  <td class="drop"></td>
                  <td class="drop"></td>
                  <td class="drop"></td>
                  <td class="drop"></td>
                   <td style="background-color: white;"></td>
                    <td style="background-color: white;"></td>
              </tr>
               <tr class="droppable">
           		  <td class="nodrop">Aneesh </td>
                  <td class="drop"></td>
                  <td class="drop"></td>
                  <td class="drop"></td>
                  <td class="drop"></td>
                  <td style="background-color: gray; width: 50px"></td>
                  <td class="drop"></td>
                  <td class="drop"></td>
                  <td class="drop"></td>
                  <td class="drop"></td>
                  <td class="drop"></td>
                  <td class="drop"></td>
                  <td class="drop"></td>
                  <td style="background-color: white;"></td>
                    <td style="background-color: white;"></td>
              </tr>
               <tr class="droppable">
           		  <td class="nodrop">Aneesh </td>
                  <td class="drop"></td>
                  <td class="drop"></td>
                  <td class="drop"></td>
                  <td class="drop"></td>
                  <td style="background-color: gray; width: 50px"></td>
                  <td class="drop"></td>
                  <td class="drop"></td>
                  <td class="drop"></td>
                  <td class="drop"></td>
                  <td class="drop"></td>
                  <td class="drop"></td>
                  <td class="drop"></td>
                  <td style="background-color: white;"></td>
                    <td style="background-color: white;"></td>
              </tr>
              </tbody>
              </table>
            </div>
        </div>
        
    </div>                    
    <!-- END CONTENT FRAME BODY -->
    
</div>               
<!-- END CONTENT FRAME -->                                
 <%@include file="includes/footer.jsp" %>            

<script>
$('#dataTables-example').DataTable({
    responsive: true
});
var dropableItem=null;
var dragDivHeight;
var dragDivWidth;
var dragableItemData = {
    cursor: 'move',
    cancel: 'a',
    revert: 'invalid',
    snap: 'false',
    stop: function(event, ui) { 
    	$(this).height(dragDivHeight);
    	$(this).width(dragDivWidth);
    },
	start: function() {
	  dragDivHeight =  $(this).height();
	  dragDivWidth =$(this).width();
      $(this).height(5).width(10);
      console.log($(this).parent());
      $(this).parent().prop("colspan","");
      $(this).parent().after("<td class='drop ui-droppable'></td>");
      dropableItem=$(this);
   }};

$(function() {
  $(".draggable").draggable(dragableItemData);
  $('.droppable td.drop').droppable({
	  accept: function(ui, item) {
         if($(this).find("div").length != 0 || $(this).find("div").css("display") == 'none'){
	  		return false;
  		 }
          return true;
      }, 
      hoverClass: 'droppable-hover',
      greedy : true,
      drop: function(event, ui) {
        $(this).css('color', 'red');
        if (dropableItem != null) {
        	$(dropableItem).hide();
        }
        var item = $("<div class='draggable task'><span style='color:red'>P1</span></div>");
        item.draggable(dragableItemData);
        $(this).html("");
        $(this).prop("colspan",2);
        $(this).next().remove();
        $(this).append(item);
      }
  });
});


</script>


