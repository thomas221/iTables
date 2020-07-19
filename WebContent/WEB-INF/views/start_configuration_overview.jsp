<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<fmt:setBundle basename="i18n.text" />

<fmt:message key="start.header" var="pagetitle_i18n" />

<jsp:include page="/assets/layouts/top.jsp">
	<jsp:param name="pagetitle" value="${pagetitle_i18n}" />
</jsp:include>

<jsp:include page="/assets/layouts/alerts.jsp" />

<div class="jumbotron" id="jumbotron-start">
	<h1>
		<fmt:message key="start.get_started" />
	</h1>
	<p class="lead">
		<fmt:message key="start.how_to_start" />
	</p>
</div>

<div class="section_header">
	<hr class="left" />
	<span class="lead">
		<fmt:message key="start.possibilities" />
	</span>
	<hr class="right" />
</div>

<div class="row features">
	<div class="col-md-4">
		<div class="thumbnail thumbnail-link" data-href="<c:url value="/start/upload"/>">
			<div class="caption">
				<h3>
					<fmt:message key="start.upload" />
				</h3>
				<p>
					<span class="icon-stack icon-4x">
						<i class="icon-circle icon-stack-base"></i>
						<i class="icon-cloud-upload icon-light"></i>
					</span>
				</p>
				<p>
					<fmt:message key="start.upload.description" />
				</p>
			</div>
		</div>
		<hr class="hidden-lg hidden-md">
	</div>
	<div class="col-md-4">
		<div class="thumbnail thumbnail-link" data-href="<c:url value="/start/new"/>">
			<div class="caption">
				<h3>
					<fmt:message key="start.new" />
				</h3>
				<p>
					<span class="icon-stack icon-4x">
						<i class="icon-circle icon-stack-base"></i>
						<i class="icon-file-alt icon-light"></i>
					</span>
				</p>
				<p>
					<fmt:message key="start.new.description" />
				</p>
			</div>
		</div>
		<hr class="hidden-lg hidden-md">
	</div>
	<div class="col-md-4">
		<div class="thumbnail thumbnail-link" data-href="<c:url value="/start/example"/>">
			<div class="caption">
				<h3>
					<fmt:message key="start.example" />
				</h3>
				<p>
					<span class="icon-stack icon-4x">
						<i class="icon-circle icon-stack-base"></i>
						<i class="icon-stackexchange icon-light"></i>
					</span>
				</p>
				<p>
					<fmt:message key="start.example.description" />
				</p>
			</div>
		</div>
	</div>
</div>

<jsp:include page="/assets/layouts/bottom.jsp" />