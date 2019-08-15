<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="currentUser" value="${applicationScope.applicationConfiguraion.currentUser}"/>
<c:set var="userMap" value="${applicationScope.applicationConfiguraion.userList}"/>
			      <tr class="projectDetailsUpdateRow hide">
			      	<td>
			      		 <select class="form-control userIdSelector">
							<c:choose>
  							<c:when  test="${param.hasFullPermission eq 'true'}">
								<option value="">- select -</option>
								<c:if test="${not empty userMap}">
	    							<c:forEach var="user" items="${userMap}">
		    						 	<c:if test="${currentUser.id eq user.id}">
										 	<option selected="selected" value="${user.id}">${user.firstName}</option>
										 </c:if>
										  <c:if test="${currentUser.id ne user.id}">
										  	<option value="${user.id}">${user.firstName}</option>
										  </c:if>
		    						</c:forEach>
		    					</c:if>
		    				</c:when>
		    				<c:otherwise>
		    						<option value="${currentUser.id}">${currentUser.firstName}</option>
							</c:otherwise>
							</c:choose>
						</select>
			      	<td>
			      		<select class="form-control projectTypeSelector">
                              <option value="BILLABLE">Billable</option>
                              <option value="NON_BILLABLE">Non Billable</option>
                        </select>
                    </td>
			      	<td>
			      	 	<div style="float: left;width: 90%;">
			      	 		<input type="text" class="form-control projectNameSelector" placeholder="Type min 4 letters project name/Code/POId.."/>
			      	 		<input type="hidden" class="projectNameSelectorIdHidden"/>
			      	 	</div>
			      	 	<div class="popoverContainer">
			      	 	
						</div>
			      	</td>
			      	<td>
			      		<select class="form-control billableTaskSelector bootstrap-select">
                        </select>
                        
                        <select class="form-control nonBillableTaskSelector hide">
							 <%@include file="unbilledtaskcategories.jsp"%>   
						</select>
			      	</td>
			      	<td>
			      		 <textarea class="form-control taskcomments" cols="5" rows="1" maxlength="250"  placeholder="Max 250 chars"></textarea>
			      	</td>
			      	
			      	<td>
			      		 <input type="number"  min="0" class="form-control weekday day1"/>
			      	</td>
			      	<td>
			      		<input type="number"  min="0" class="form-control weekday day2"/>
			      	</td>
			      	<td>
			      		<input type="number"  min="0" class="form-control weekday day3"/>
			      	</td>
			      	<td>
			      		<input type="number"  min="0" class="form-control weekday day4"/>
			      	</td>
			      	<td>
			      		<input type="number"  min="0" class="form-control weekday day5"/>
			      	</td>
			      	<td>
			      		<input type="number"  min="0" class="form-control weekday day6"/>
			      	</td>
			      	<td>
			      		<input type="number"  min="0" class="form-control weekday day7"/>
			      	</td>
			      	<td style="background: #f1f5f9;padding-top: 15px;font-size: 10px;font-weight: bold;">
			      		<span class="weekdayTotal" style="padding-left: 5px">00:00</span>
			      		<a href="#" class="projectDetailsUpdateRowDelLink"><span class="fa fa-times" style="color: red;float:right"></span></a>
			      	</td>
			      </tr>