<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<fmt:setBundle basename="i18n.text" />

<fmt:message key="upload_configuration.header" var="pagetitle_i18n" />

<jsp:include page="/assets/layouts/top.jsp">
	<jsp:param name="pagetitle" value="${pagetitle_i18n}" />
</jsp:include>

<div class="row">
	<div class="col-xs-12">
		<div class="page-header">
			<h1>
				<i class="icon-fixed-width icon-cloud-upload"></i> <fmt:message key="upload_configuration.header" />
			</h1>
			<ol class="breadcrumb pull-right visible-md visible-lg">
				<li>
					<a href="<c:url value="/home"/>">
						<fmt:message key="home.header" />
					</a>
				</li>
				<li>
					<a href="<c:url value="/start"/>">
						<fmt:message key="start.header" />
					</a>
				</li>
				<li class="active">
					<fmt:message key="upload_configuration.header" />
				</li>
			</ol>
		</div>
	</div>

	<div class="col-xs-12">
		<jsp:include page="/assets/layouts/alerts.jsp" />

		<p>
			<fmt:message key="upload.description" />
		</p>
		<p>
			Zie <a href="<c:url value="/documentation#doc-16" />" target="_blank" >Hoe maak ik een configuratie in iptables?</a> voor meer informatie.
		</p>

		<form action="<c:url value="/start/upload"/>" method="post" role="form" enctype="multipart/form-data">
			<div class="form-group">
				<div>
					<input type="file" id="upload_filename" name="upload_filename">
				</div>
			</div>

			<button type="submit" class="btn btn-primary" data-loading-text="<fmt:message key="loading" />">
				<fmt:message key="upload.submit" />
				<i class="icon-double-angle-right"></i>
			</button>
		</form>
	</div>
</div>

<jsp:include page="/assets/layouts/bottom.jsp" />