<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<fmt:setBundle basename="i18n.text" />

<fmt:message key="under_construction.title" var="pagetitle_i18n" />

<jsp:include page="/assets/layouts/top.jsp">
	<jsp:param name="pagetitle" value="${pagetitle_i18n}" />
</jsp:include>

<div class="jumbotron">
	<h1>
		<i class="icon-building icon-2x"></i>
	</h1>
	<h1>
		<fmt:message key="under_construction.title" />
	</h1>
	<p class="lead">
		<fmt:message key="under_construction.description" />
	</p>
	<p class="lead">
		<a href="<c:url value="/home"/>" class="btn btn-primary btn-lg">
			<fmt:message key="error.go_to_home" />
			<i class="icon-double-angle-right"></i>
		</a>
	</p>
</div>

<jsp:include page="/assets/layouts/bottom.jsp" />