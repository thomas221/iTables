<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags"%>

<fmt:setBundle basename="i18n.text" />

<fmt:message key="my_configuration_logfile.header" var="pagetitle_i18n" />

<jsp:include page="/assets/layouts/top.jsp">
	<jsp:param name="pagetitle" value="${pagetitle_i18n}" />
</jsp:include>

<div class="row">
	<div class="col-xs-12">
		<div class="page-header">
			<h1>
				<i class="icon-fixed-width icon-list-alt"></i>
				<fmt:message key="my_configuration_logfile.header" />
			</h1>
			<ol class="breadcrumb pull-right visible-sm visible-md visible-lg">
				<li>
					<a href="<c:url value="/home"/>"> <fmt:message key="home.header" />
					</a>
				</li>
				<li>
					<a href="<c:url value="/my_configuration"/>"> <fmt:message key="navbar.configuration.title" />
					</a>
				</li>
				<li class="active">
					<fmt:message key="my_configuration_logfile.header" />
				</li>
			</ol>
		</div>
	</div>

	<div class="col-xs-12">
		<jsp:include page="/assets/layouts/alerts.jsp" />

		<table cellpadding="0" cellspacing="0" border="0" class="table table-striped table-hover table-bordered" id="table-logs">
			<thead>
				<tr>
					<th><fmt:message key="logfile.id" /></th>
					<th><fmt:message key="logfile.timestamp" /></th>
					<th><fmt:message key="logfile.type" /></th>
					<th><fmt:message key="logfile.message" /></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="log" items="${logfile.logs}">
					<tr>
						<td>${log.id + 1}</td>
						<td style="white-space: nowrap"><joda:format value="${log.timestamp}" pattern="dd/MM/yyyy '-' HH:mm:ss" /></td>
						<td style="white-space: nowrap"><fmt:message key="${log.type.i18n}" /></td>
						<td>${log.message}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<div class="button-group-rules">
<%-- 			<form style="display: inline;" method="post" action="<c:url value="/my_configuration/logfile?action=download_excel"/>">
				<button type="submit" class="btn btn-primary knop-logbestand-downloaden">
					<i class="icon-fixed-width icon-download"></i>
					<fmt:message key="knop.logbestand_downloaden_excel" />
				</button>
			</form> --%>
<%-- 			<form style="display: inline;" method="post" action="<c:url value="/my_configuration/logfile?action=download_pdf"/>">
				<button type="submit" class="btn btn-primary knop-logbestand-downloaden">
					<i class="icon-fixed-width icon-download"></i>
					<fmt:message key="knop.logbestand_downloaden_pdf" />
				</button>
			</form> --%>
		</div>

	</div>
</div>

<jsp:include page="/assets/layouts/bottom.jsp" />