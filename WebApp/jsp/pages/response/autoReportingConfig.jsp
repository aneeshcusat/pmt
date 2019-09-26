<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>		
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<table style="margin-bottom: 0px;" class="table table-bordered">
<thead>
      <tr>
      	<th width="20%">Name</th>
      	<th width="15%">Type</th>
      	<th width="20%">Subject</th>
      	<th width="12%">Start Day</th>
      	<th width="13%">Previous Days</th>
      	<th width="10%">Schedule time</th>
      	<th width="10%">End Date</th>
      </tr>
  </thead>
  <tbody>
  	<tr>
  		<td><input type="text" class="form-control autoReportName"/></td>
  		<td>
  			<select class="form-control autoReportType">
                <option value="USER_SITE_ACTIVITY">User activity</option>
                <option value="USER_UTILIZATION">User Utilization</option>
          	</select>
        </td>
  		<td> <textarea class="form-control autoReportSubject" cols="5" rows="2" maxlength="250"  placeholder="Max 250 chars"></textarea></td>
  		<td>
  		<select class="form-control autoReportStartDay">
  			<option value="0">Current Day</option>
  			<option value="-1">Previous Day</option>
  			<option value="-2">2 Days before</option>
  			<option value="-3">3 Days before</option>
  			<option value="-4">4 Days before</option>
  			<option value="-5">5 Days before</option>
  			<option value="-6">6 Days before</option>
  			<option value="-7">7 Days before</option>
  		</select>
  			
  		</td>
  		<td>
  			<select class="form-control autoReportHowmany">
  			<option value="0">Current Day</option>
  			<option value="-1">Previous Day</option>
  			<option value="-2">2 Days before</option>
  			<option value="-3">3 Days before</option>
  			<option value="-4">4 Days before</option>
  			<option value="-5">5 Days before</option>
  			<option value="-6">6 Days before</option>
  			<option value="-7">7 Days before</option>
  		</select>
  		</td>
  		<td>
  		Every <select class="form-control autoReportDaySelection">
  				<option value="MON,TUE,WED,THU,FRI,SAT,SUN">Day</option>
  				<option value="MON,TUE,WED,THU,FRI">Week Days</option>
  				<option value="SAT,SUN">Weekends</option>
  				<option value="MON">Monday</option>
  				<option value="TUE">Tuesday</option>
  				<option value="WED">Wednesday</option>
  				<option value="THU">Thursday</option>
  				<option value="FRI">Friday</option>
  				<option value="SAT">Saturday</option>
  				<option value="SUN">Sunday</option>
  				
  			</select>
  			At 
  			<input type="text" class="form-control autoReportTimeSelection" placeholder=""/>
  		
  		</td>
  		<td>
  			<input type="text" class="autoReportingEndDate"/>
  		</td>
  	</tr>
  </tbody>
  <tfoot>
  	<tr>
  		<td colspan="7" style="text-align: center;">
  					<input type="hidden" class="autoReportingConfigId"/>
						<button type="button" id="" onclick="createAutoReportingConfig();"
							class="btn btn-info ">
							<span class="saveAutoReporting">Save</span>
						</button>
						
						<button type="button" id="" onclick="cleareditautoreporting();"
							class="btn btn-warning hide clearAutoReporting">
							<span>Clear</span>
						</button>
  		</td>
  	</tr>
  </tfoot>
  </table>
