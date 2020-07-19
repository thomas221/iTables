<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<fmt:setBundle basename="i18n.text" />

<fmt:message key="aan_de_slag_voorbeeldconfiguratie.header" var="pagetitle_i18n" />

<jsp:include page="/assets/layouts/top.jsp">
	<jsp:param name="pagetitle" value="${pagetitle_i18n}" />
</jsp:include>

<div class="row">
	<div class="col-xs-12">
		<div class="page-header">
			<h1>
				<i class="icon-fixed-width icon-stackexchange"></i> <fmt:message key="aan_de_slag_voorbeeldconfiguratie.header" />
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
					<fmt:message key="aan_de_slag_voorbeeldconfiguratie.header" />
				</li>
			</ol>
		</div>
	</div>

	<div class="col-xs-12">
		<jsp:include page="/assets/layouts/alerts.jsp" />

		<p>
			<fmt:message key="example.description" />
		</p>

		<form action="<c:url value="/start/example"/>" method="post" role="form">
			<div class="form-group">
				<select class="form-control" id="example_filename" name="example_filename">
					<option value=""></option>
					<c:forEach var="filename" items="${example_configurations}">
						<option value="${filename}">${filename}</option>
					</c:forEach>
				</select>
			</div>

			<button type="submit" class="btn btn-primary" data-loading-text="<fmt:message key="loading" />">
				<fmt:message key="example.submit" />
				<i class="icon-double-angle-right"></i>
			</button>

		</form>
	</div>
</div>

<jsp:include page="/assets/layouts/bottom.jsp" />