<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<fmt:setBundle basename="i18n.text" />

<fmt:message key="my_configuration.header" var="pagetitle_i18n" />

<jsp:include page="/assets/layouts/top.jsp">
	<jsp:param name="pagetitle" value="${pagetitle_i18n}" />
</jsp:include>

<jsp:include page="/assets/layouts/alerts.jsp" />

<div class="jumbotron" id="jumbotron-start">
	<h1>
		<fmt:message key="my_configuration.header" />
	</h1>
	<p class="lead">
		<fmt:message key="my_configuration.subheader" />
	</p>
</div>

<div class="section_header">
	<hr class="left" />
	<span class="lead">
		<fmt:message key="my_configuration.modules" />
	</span>
	<hr class="right" />
</div>

<div class="row features">
	<div class="col-sm-4">
		<div class="thumbnail thumbnail-link" data-href="<c:url value="/my_configuration/anomalies"/>">
			<div class="caption">
				<h3>
					<fmt:message key="my_configuration.anomalies.title" />
				</h3>
				<p>
					<span class="icon-stack icon-4x">
						<i class="icon-circle icon-stack-base"></i>
						<i class="icon-bug icon-light"></i>
					</span>
				</p>
				<p>
					<fmt:message key="my_configuration.anomalies.description" />
				</p>
			</div>
		</div>
		<hr class="hidden-lg hidden-md hidden-sm">
	</div>
	<div class="col-sm-4">
		<div class="thumbnail thumbnail-link" data-href="<c:url value="/my_configuration/rules"/>">
			<div class="caption">
				<h3>
					<fmt:message key="my_configuration.rules.title" />
				</h3>
				<p>
					<span class="icon-stack icon-4x">
						<i class="icon-circle icon-stack-base"></i>
						<i class="icon-cogs icon-light"></i>
					</span>
				</p>
				<p>
					<fmt:message key="my_configuration.rules.description" />
				</p>
			</div>
		</div>
		<hr class="hidden-lg hidden-md hidden-sm">
	</div>
	<div class="col-sm-4">
		<div class="thumbnail thumbnail-link" data-href="<c:url value="/my_configuration/logfile"/>">
			<div class="caption">
				<h3>
					<fmt:message key="my_configuration.logfile.title" />
				</h3>
				<p>
					<span class="icon-stack icon-4x">
						<i class="icon-circle icon-stack-base"></i>
						<i class="icon-list-alt icon-light"></i>
					</span>
				</p>
				<p>
					<fmt:message key="my_configuration.logfile.description" />
				</p>
			</div>
		</div>
	</div>
</div>

<div class="section_header">
	<hr class="left" />
	<span class="lead">
		<fmt:message key="my_configuration.subfeatures" />
	</span>
	<hr class="right" />
</div>

<div class="row features">
	<div class="col-sm-4">
		<div class="thumbnail thumbnail-link-modal knop-export-configuration" data-href="<c:url value="/my_configuration/export?file=iptables" />">
			<div class="caption">
				<h3>Configuratie exporteren</h3>
				<p>
					<span class="icon-stack icon-4x">
						<i class="icon-circle icon-stack-base"></i>
						<i class="icon-download icon-light"></i>
					</span>
				</p>
				<p>
					<fmt:message key="my_configuration.download_configuration" />
				</p>
			</div>
		</div>
	</div>
	<div class="col-sm-4">
		<div class="thumbnail thumbnail-link" data-href="<c:url value="/my_configuration/export"/>">
			<div class="caption">
				<h3>Sessie exporteren</h3>
				<p>
					<span class="icon-stack icon-4x">
						<i class="icon-circle icon-stack-base"></i>
						<i class="icon-share icon-light"></i>
					</span>
				</p>
				<p>
					<fmt:message key="my_configuration.export_session" />
				</p>
			</div>
		</div>
		<hr class="hidden-lg hidden-md hidden-sm">
	</div>
	<div class="col-sm-4">
		<div class="thumbnail thumbnail-link-modal knop-modal-stop-session">
			<div class="caption">
				<h3>Sessie beëindigen</h3>
				<p>
					<span class="icon-stack icon-4x">
						<i class="icon-circle icon-stack-base"></i>
						<i class="icon-off icon-light"></i>
					</span>
				</p>
				<p>
					<fmt:message key="my_configuration.stop_session" />
				</p>
			</div>
		</div>
		<hr class="hidden-lg hidden-md hidden-sm">
	</div>
</div>

<jsp:include page="/assets/layouts/bottom.jsp" />