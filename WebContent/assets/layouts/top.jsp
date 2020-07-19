 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="custom" uri="http://itables.ibyte.be/CustomJSPTags"%>

<fmt:setBundle basename="i18n.text" />

<!DOCTYPE html>
<html lang="nl">
<head>

<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="<fmt:message key="application.description" />">
<meta name="author" content="<fmt:message key="application.author" />">
<META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE">

<title>
	<fmt:message key="application.title">
		<fmt:param value="${param.pagetitle}" />
	</fmt:message>
</title>

<link rel="shortcut icon" type="image/png" href="/assets/images/favicon.png?v=${custom:getVersionLayoutFiles()}">

<link rel="stylesheet" type="text/css" media="screen" href="/assets/stylesheets/bootstrap.min.css?v=${custom:getVersionLayoutFiles()}">
<link rel="stylesheet" type="text/css" media="screen" href="/assets/stylesheets/font-awesome.min.css?v=${custom:getVersionLayoutFiles()}">
<!--[if IE 7]>
  <link rel="stylesheet" type="text/css" media="screen" href="/assets/stylesheets/font-awesome-ie7.min.css">
<![endif]-->
<link rel="stylesheet" type="text/css" media="screen" href="/assets/stylesheets/bootstrap-editable.css?v=${custom:getVersionLayoutFiles()}">
<link rel="stylesheet" type="text/css" media="screen" href="/assets/stylesheets/dataTables.bootstrap.css?v=${custom:getVersionLayoutFiles()}">
<link rel="stylesheet" type="text/css" media="screen" href="/assets/stylesheets/main.min.css?v=${custom:getVersionLayoutFiles()}">

<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!--[if lt IE 9]>
      <script src="/assets/javascripts/html5shiv.js"></script>
      <script src="/assets/javascripts/respond.min.js"></script>
<![endif]-->

<script src="/assets/javascripts/jquery-1.9.0.min.js?v=${custom:getVersionLayoutFiles()}"></script>
<script src="/assets/javascripts/jquery-ui.min.js"></script>
<script src="/assets/javascripts/nod.js?v=${custom:getVersionLayoutFiles()}"></script>
<script src="/assets/javascripts/bootstrap.min.js?v=${custom:getVersionLayoutFiles()}"></script>
<script src="/assets/javascripts/bootstrap-editable.js?v=${custom:getVersionLayoutFiles()}"></script>
<script src="/assets/javascripts/jquery.dataTables.min.js?v=${custom:getVersionLayoutFiles()}"></script>
<script src="/assets/javascripts/dataTables.bootstrap.js?v=${custom:getVersionLayoutFiles()}"></script>
<script src="/assets/javascripts/jquery.dataTables.rowReordering.js?v=${custom:getVersionLayoutFiles()}"></script>
<script src="/assets/javascripts/main.min.js?v=${custom:getVersionLayoutFiles()}"></script>
</head>

<body data-session="<%= request.getSession().getId() %>">
	<!-- Enkel zichtbaar op screenreaders -->
	<a class="sr-only" href="#content">
		<fmt:message key="skip.to.content" />
	</a>

	<!-- Start navigation bar -->
	<nav class="navbar navbar-default navbar-fixed-top" role="navigation">
		<div class="container">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
					<span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span>
				</button>
				<a id="navbar_header_link" class="navbar-brand" href="<c:url value="/home"/>">
					<i class="icon-shield"></i>
					<fmt:message key="application.name" />
					<c:if test="${custom:isBeta()}">
						<span class="label label-danger"><fmt:message key="applicatie.versie" /></span>
					</c:if>

				</a>
			</div>
			<div class="collapse navbar-collapse">
				<ul class="nav navbar-nav">
					<li>
						<a id="navbar_home_link" href="<c:url value="/home"/>">
							<i class="icon-fixed-width icon-home "></i>
							<fmt:message key="navbar.home" />
						</a>
					</li>
					<li>
						<a id="navbar_documentation_link" href="<c:url value="/documentation"/>">
							<i class="icon-fixed-width icon-info-sign"></i>
							<fmt:message key="navbar.documentation" />
						</a>
					</li>
					<li>
						<a id="navbar_contact_link" href="<c:url value="/contact"/>">
							<i class="icon-fixed-width icon-comments"></i>
							<fmt:message key="navbar.contact" />
						</a>
					</li>
				</ul>
				<ul class="nav navbar-nav navbar-right">
					<c:choose>
						<c:when test="${not empty configuration}">
							<li class="dropdown">
								<a href="#" class="dropdown-toggle" data-toggle="dropdown">
									<i class="icon-fixed-width icon-user"></i>
									<fmt:message key="navbar.configuration.title" />
									<b class="caret"></b>
								</a>
								<ul class="dropdown-menu">
									<li>
										<a href="<c:url value="/my_configuration/anomalies"/>">
											<i class="icon-fixed-width icon-bug"></i>
											<fmt:message key="navbar.configuration.anomalies" />
										</a>
									</li>
									<li>
										<a href="<c:url value="/my_configuration/rules"/>">
											<i class="icon-fixed-width icon-cogs"></i>
											<fmt:message key="navbar.configuration.rules" />
										</a>
									</li>
									<li>
										<a href="<c:url value="/my_configuration/logfile"/>">
											<i class="icon-fixed-width icon-list-alt"></i>
											<fmt:message key="navbar.configuration.logfile" />
										</a>
									</li>
									<li class="divider"></li>
									<li>
										<a href="<c:url value="/my_configuration/export?file=iptables"/>" class="knop-export-configuration">
											<i class="icon-fixed-width icon-download"></i>
											<fmt:message key="navbar.configuration.export.iptables" />
										</a>
									</li>
									<li>
										<a href="<c:url value="/my_configuration/export?file=sessie"/>">
											<i class="icon-fixed-width icon-share"></i>
											<fmt:message key="navbar.configuration.export.log" />
										</a>
									</li>
									<li>
										<a id="navbar_restart_link" href="<c:url value="#restart_modal"/>" data-toggle="modal">
											<i class="icon-fixed-width icon-off"></i>
											<fmt:message key="navbar.configuration.stop" />
										</a>
									</li>
								</ul>
							</li>
						</c:when>
						<c:otherwise>
							<li>
								<a id="navbar_start_link" href="<c:url value="/start"/>">
									<i class="icon-fixed-width icon-circle-arrow-right "></i>
									<fmt:message key="navbar.start" />
								</a>
							</li>
						</c:otherwise>
					</c:choose>
				</ul>
			</div>
		</div>
	</nav>
	<!-- End navigation bar -->

	<div class="modal fade" id="restart_modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h4 class="modal-title">
						<i class="icon-fixed-width icon-off"></i>
						<fmt:message key="modal.stop_configuration.header" />
					</h4>
				</div>
				<div class="modal-body">
					<p>
						<fmt:message key="modal.stop_configuration.description_1" />
					</p>
					<p>
						<strong><fmt:message key="modal.stop_configuration.description_2" /></strong>
					</p>
				</div>
				<div class="modal-footer">
					<form class="form-inline" action="<c:url value="/my_configuration/export"/>" method="get" role="form">
						<button type="submit" class="btn btn-success">
							<i class="icon-fixed-width icon-share"></i>
							<fmt:message key="modal.stop_configuration.button_export" />
						</button>
					</form>
					<form class="form-inline" action="<c:url value="/my_configuration/stop"/>" method="post" role="form">
						<button type="submit" class="btn btn-danger">
							<i class="icon-fixed-width icon-off"></i>
							<fmt:message key="modal.stop_configuration.button_stop" />
						</button>
					</form>
				</div>
			</div>
		</div>
	</div>

	<!-- Start container -->
	<div class="container">

		<!-- Start page content -->
		<div class="content">