<%@include file="includes/header.jsp" %>
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
            Today
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
$_dropEnd = null;
var dragableItemData = {
    cursor: 'move',
    cancel: 'a',
    revert: 'invalid',
    snap: 'true',
    stop: function(event, ui) { 
       $(this).appendTo($_dropEnd);
       $_dropEnd = null;
    },
	start: function() {
      $(this).height(15).width(40);
       dropableItem=$(this);
   }};

$(function() {
  $(".draggable").draggable(dragableItemData);
  $('.droppable td.drop').droppable({
	  accept: function(ui, item) {
          if($(this).html().trim().length != 0)
             return false;

          if($(ui).index()==$(this).index())
            return true;

          var next=$(this).next().get(0);
          var prev=$(this).prev().get(0);
          var me=ui.get(0);

          if(me==next||me==prev)
          return true;

          return false;  
      },
      hoverClass: 'droppable-hover',
      greedy : true,
      drop: function(event, ui) {
         $_dropEnd = this;
        $(this).css('color', 'red');
        if (dropableItem != null) {
        	dropableItem.hide();
        }
        var item = $("<div class='draggable task'><span style='color:red'>P1</span></div>");
        item.draggable(dragableItemData);
        $(this).html("");
        $(this).append(item);
      }
  });
});


</script>


