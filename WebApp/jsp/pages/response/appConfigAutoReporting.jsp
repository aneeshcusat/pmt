<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>		
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="userMap" value="${applicationScope.applicationConfiguraion.userList}"/>

<c:if test="${not empty autoReportingList}">

<table style="margin-bottom: 0px;" class="table">
<thead>
      <tr>
      	<th width="10%">Name</th>
      	<th width="10%">Type</th>
      	<th width="15%">Subject</th>
      	<th width="10%">Report date</th>
      	<th width="15%">Time</th>
      	<th width="20%">To List</th>
      	<th width="20%">CC/Exclude list</th>
      	<th width="2%">Enabled</th>
      	<th width="2%">Notify Defaulters</th>
      	<th width="2%">Actions</th>
      </tr>
  </thead>
  <tbody>
  <c:forEach var="autoReportingItem" items="${autoReportingList}">
  	<tr class="autoreporting autoReportConf${autoReportingItem.id}">
  		<td>${autoReportingItem.name}</td>
  		<td>
  			<c:if test="${autoReportingItem.type == 'USER_SITE_ACTIVITY'}">
  				User Activity
  			</c:if>
  			<c:if test="${autoReportingItem.type == 'USER_UTILIZATION'}">
  				User Utilization
  			</c:if>
  			<c:if test="${autoReportingItem.type == 'WEEKWISE_USER_UTILIZATION_MONTHLY'}">
  				Monthly Utilization
  			</c:if>
  			<c:if test="${autoReportingItem.type == 'WEEKLY_PO_ESTIMATION'}">
  				PO Estimation
  			</c:if>
        </td>
  		<td>${autoReportingItem.subject}</td>
  		<td>
  			${autoReportingItem.reportingDate}<br/>
  		</td>
  		<td>
	  		<b>${autoReportingItem.scheduleTime}</b>  <b></b><br/>
  			Last Run : <b><fmt:formatDate value="${autoReportingItem.lastRun}" pattern="yyyy-MM-dd HH:mm" /> </b><br/>
	  		Next Run : <b><fmt:formatDate value="${autoReportingItem.nextRun}" pattern="yyyy-MM-dd HH:mm" /></b><br/>
	  		End Date : <b><fmt:formatDate value="${autoReportingItem.endDate}" pattern="yyyy-MM-dd"/></b>
  		</td>
  		<td>
  		<select data-live-search="true" class="emailto${autoReportingItem.id}">
		 <c:if test="${not empty userMap}">
			<c:forEach var="user" items="${userMap}">
				  <option value="${user.email}">${user.firstName}</option>
 				</c:forEach>
 			</c:if>
		</select><button name="save" onclick="addEmailAutoReportingConfig(${autoReportingItem.id},'to')">Add</button><br/>
			<ul style="margin-top: 10px" class="emailtolist${autoReportingItem.id}">
				<c:if test="${not empty autoReportingItem.emailToList}">
					<c:forEach var="emailId" items="${autoReportingItem.emailToList}">		
						<li>${emailId}<a href="javascript:removeEmailAutoReportingConfig('${emailId}',${autoReportingItem.id},'to')"><i class="fa fa-times" style="color: red" aria-hidden="true"></i></a></li>
					</c:forEach>
				</c:if>
			</ul>
		</td>
  		<td>
  		<div>
  		<select data-live-search="true"  class="emailcc${autoReportingItem.id}">
		 <c:if test="${not empty userMap}">
			<c:forEach var="user" items="${userMap}">
				  <option value="${user.email}">${user.firstName}</option>
 				</c:forEach>
 			</c:if>
		</select><button name="save" onclick="addEmailAutoReportingConfig(${autoReportingItem.id},'cc')">Add</button><br/>
  		<ul  style="margin-top: 10px"  class="emailcclist${autoReportingItem.id}">
				<c:if test="${not empty autoReportingItem.emailCCList}">
					<c:forEach var="emailId" items="${autoReportingItem.emailCCList}">		
						<li>${emailId}<a href="javascript:removeEmailAutoReportingConfig('${emailId}',${autoReportingItem.id},'cc')"><i class="fa fa-times" style="color: red" aria-hidden="true"></i></a></li>
					</c:forEach>
				</c:if>
			</ul>
			</div>
			
			<div style="margin-top: 10px;">
			<b>Exclude Email List</b>
			<select data-live-search="true"  class="emailexdef${autoReportingItem.id}">
		 <c:if test="${not empty userMap}">
			<c:forEach var="user" items="${userMap}">
				  <option value="${user.email}">${user.firstName}</option>
 				</c:forEach>
 			</c:if>
		</select><button name="save" onclick="addEmailAutoReportingConfig(${autoReportingItem.id},'exdef')">Add</button><br/>
  		<ul  style="margin-top: 10px"  class="emailexdeflist${autoReportingItem.id}">
				<c:if test="${not empty autoReportingItem.excludeMailList}">
					<c:forEach var="emailId" items="${autoReportingItem.excludeMailList}">		
						<li>${emailId}<a href="javascript:removeEmailAutoReportingConfig('${emailId}',${autoReportingItem.id},'exdef')"><i class="fa fa-times" style="color: red" aria-hidden="true"></i></a></li>
					</c:forEach>
				</c:if>
			</ul>
			
			</div>	
			
  		</td>
  		
  		<td> 
  		 <c:if test="${autoReportingItem.enabled}">
  		 	<input class="autoReportEnabled" type="checkbox" checked="checked"  data-configid="${autoReportingItem.id}"/>
  		 </c:if>
  		  <c:if test="${!autoReportingItem.enabled}">
  		 	<input class="autoReportEnabled" type="checkbox" data-configid="${autoReportingItem.id}"/>
  		 </c:if>
  		 </td> 
  		 <td> 
  		 <c:if test="${autoReportingItem.notifyDefaulters}">
  		 	<input class="autoReportNotifyDefaulters" type="checkbox" checked="checked"  data-configid="${autoReportingItem.id}"/>
  		 </c:if>
  		  <c:if test="${!autoReportingItem.notifyDefaulters}">
  		 	<input class="autoReportNotifyDefaulters" type="checkbox" data-configid="${autoReportingItem.id}"/>
  		 </c:if>
  		 </td> 
  		 <td> 
  		  <input type="hidden" class="reportingName${autoReportingItem.id}" value="${autoReportingItem.name}"/>
  		  <input type="hidden" class="reportingSubject${autoReportingItem.id}" value="${autoReportingItem.subject}"/>
  		  <input type="hidden" class="reportingType${autoReportingItem.id}" value="${autoReportingItem.type}"/>
  		  <input type="hidden" class="reportingStartDay${autoReportingItem.id}" value="${autoReportingItem.startDays}"/>
  		  <input type="hidden" class="reportingPreDay${autoReportingItem.id}" value="${autoReportingItem.lastHowManyDays}"/>
  		  <input type="hidden" class="reportingScheduleDay${autoReportingItem.id}" value="${autoReportingItem.cronDay}"/>
  		  <input type="hidden" class="reportingScheduleTime${autoReportingItem.id}" value="${autoReportingItem.cronTime}"/>
  		  <input type="hidden" class="reportingEndDate${autoReportingItem.id}" value='<fmt:formatDate value="${autoReportingItem.endDate}" pattern="yyyy-MM-dd"/>'/>
  		    
   		 <a href="javascript:deleteAutoReportingConf(${autoReportingItem.id});" title="Delete"><i class="fa fa-times  fa-2x" style="color: red" aria-hidden="true"></i></a>
     	 <a href="javascript:editautoreporting(${autoReportingItem.id});" title="Edit"><i class="fa fa-pencil-square-o  fa-2x" style="color: blue"  aria-hidden="true"></i></a>
     	 <a href="javascript:triggerautoreporting(${autoReportingItem.id});" title="Trigger report email"><i class="fa fa-send-o  fa-2x" style="color: red;padding-top: 10px;"  aria-hidden="true"></i></a>
  		</td>
  	</tr>
  	  </c:forEach>
  </tbody>
  </table>
</c:if>