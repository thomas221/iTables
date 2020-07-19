<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<fmt:setBundle basename="i18n.text" />
<fmt:setLocale value="nl_BE" />

<c:choose>
	<c:when test="${alert_message_type.index eq 1}">
		<div class="alert alert-success alert-dismissable fade in">
			<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
			<h4>
				<fmt:message key="alert.success" />
			</h4>
			<c:choose>
				<c:when test="${fn:length(alert_messages) eq 1}">
					<p class="paragraph_weight_400">
						<fmt:message key="${alert_messages[0]}">
							<c:forEach var="parameter" items="${alert_parameters}">
								<fmt:param value="${parameter}" />
							</c:forEach>
						</fmt:message>
					</p>
				</c:when>
				<c:when test="${fn:length(alert_messages) gt 1}">
					<ul>
						<c:forEach var="alert_message" items="${alert_messages}">
							<li>
								<fmt:message key="${alert_message}" />
							</li>
						</c:forEach>
					</ul>
				</c:when>
			</c:choose>
		</div>
	</c:when>
	<c:when test="${alert_message_type.index eq 2}">
		<div class="alert alert-info alert-dismissable">
			<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
			<h4>
				<fmt:message key="alert.info" />
			</h4>
			<c:choose>
				<c:when test="${fn:length(alert_messages) eq 1}">
					<p class="paragraph_weight_400">
						<fmt:message key="${alert_messages[0]}">
							<c:forEach var="parameter" items="${alert_parameters}">
								<fmt:param value="${parameter}" />
							</c:forEach>
						</fmt:message>
					</p>
				</c:when>
				<c:when test="${fn:length(alert_messages) gt 1}">
					<ul>
						<c:forEach var="alert_message" items="${alert_messages}">
							<li>
								<fmt:message key="${alert_message}" />
							</li>
						</c:forEach>
					</ul>
				</c:when>
			</c:choose>
		</div>
	</c:when>
	<c:when test="${alert_message_type.index eq 3}">
		<div class="alert alert-warning alert-dismissable">
			<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
			<h4>
				<fmt:message key="alert.warning" />
			</h4>
			<c:choose>
				<c:when test="${fn:length(alert_messages) eq 1}">
					<p class="paragraph_weight_400">
						<fmt:message key="${alert_messages[0]}">
							<c:forEach var="parameter" items="${alert_parameters}">
								<fmt:param value="${parameter}" />
							</c:forEach>
						</fmt:message>
					</p>
				</c:when>
				<c:when test="${fn:length(alert_messages) gt 1}">
					<ul>
						<c:forEach var="alert_message" items="${alert_messages}">
							<li>
								<fmt:message key="${alert_message}" />
							</li>
						</c:forEach>
					</ul>
				</c:when>
			</c:choose>
		</div>
	</c:when>
	<c:when test="${alert_message_type.index eq 4}">
		<div class="alert alert-danger alert-dismissable">
			<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
			<h4>
				<fmt:message key="alert.danger" />
			</h4>
			<c:choose>
				<c:when test="${fn:length(alert_messages) eq 1}">
					<p class="paragraph_weight_400">
						<c:choose>
							<c:when test="${empty isI18NKey || isI18NKey}">
								<fmt:message key="${alert_messages[0]}">
									<c:forEach var="parameter" items="${alert_parameters}">
										<fmt:param value="${parameter}" />
									</c:forEach>
								</fmt:message>								
							</c:when>
							<c:otherwise> 
								<%--Indien foutbericht geen key is in I18N bestand moet foutbericht letterlijk worden weergegeven. Nodig voor bepaalde foutberichten bij het parsen. --%>
								${alert_messages[0]}
								<%-- Zet terug op standaard waarde voor volgende request. (flash houdt request attributen bij tussen requests) --%>
								<% request.setAttribute("isI18NKey", true); %>
							</c:otherwise>
						</c:choose>
					</p>
				</c:when>
				<c:when test="${fn:length(alert_messages) gt 1}">
					<ul>
						<c:forEach var="alert_message" items="${alert_messages}">
							<li>
								<fmt:message key="${alert_message}"></fmt:message>
							</li>
						</c:forEach>
					</ul>
				</c:when>
			</c:choose>
		</div>
	</c:when>
</c:choose>