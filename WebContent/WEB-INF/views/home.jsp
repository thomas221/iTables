<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<fmt:setBundle basename="i18n.text" />

<fmt:message key="home.header" var="pagetitle_i18n" />

<jsp:include page="/assets/layouts/top.jsp">
	<jsp:param name="pagetitle" value="${pagetitle_i18n}" />
</jsp:include>

<div class="jumbotron">
	<h1>
		<fmt:message key="home.welcome" />
	</h1>
	<p class="lead">
		<fmt:message key="home.short_description" />
	</p>
	<p>
		<c:choose>
			<c:when test="${not empty configuration}">
				<a href="<c:url value="/my_configuration"/>" class="btn btn-primary btn-lg"><i class="icon-fixed-width icon-user"></i> <fmt:message key="configuration.header" />
				</a>

<%-- 				<a href="<c:url value="/my_configuration"/>" class="btn btn-danger btn-lg"> <fmt:message key="home.button_stop_session" /> <i
						class="icon-double-angle-right"></i>
				</a> --%>

				<a id="navbar_restart_link" class="btn btn-danger btn-lg" href="<c:url value="#restart_modal"/>" data-toggle="modal"> <i class="icon-fixed-width icon-off"></i> <fmt:message
						key="navbar.configuration.stop" />
				</a>
			</c:when>
			<c:otherwise>
				<a href="<c:url value="/start"/>" class="btn btn-primary btn-lg"> <fmt:message key="home.start" /> <i
						class="icon-double-angle-right"></i>
				</a>
			</c:otherwise>
		</c:choose>
	</p>
</div>

<div class="section_header">
	<hr class="left" />
	<span class="lead">
		<fmt:message key="home.features" />
	</span>
	<hr class="right" />
</div>

<div class="row features">
	<div class="col-md-4">
		<div class="panel panel-primary panel-kenmerk">
			<div class="panel-heading">
				<fmt:message key="home.features.educative.title" />
			</div>
			<div class="panel-body">
				<fmt:message key="home.features.educative.description" />
			</div>
		</div>

		<%-- 		<div class="thumbnail">
			<div class="caption">
				<h3>
					<fmt:message key="home.features.educative.title" />
				</h3>
				<!-- 				<p>
					<span class="icon-stack icon-4x"> <i class="icon-circle icon-stack-base"></i> <i class="icon-book icon-light"></i>
					</span>
				</p> -->
				<br>
				<p>
					<fmt:message key="home.features.educative.description" />
				</p>
			</div>
		</div> --%>
		<hr class="hidden-lg hidden-md">
	</div>
	<div class="col-md-4">
		<div class="panel panel-primary panel-kenmerk">
			<div class="panel-heading">
				<fmt:message key="home.features.easy.title" />
			</div>
			<div class="panel-body">
				<fmt:message key="home.features.easy.description" />
			</div>
		</div>

		<%-- 		<div class="thumbnail">
			<div class="caption">
				<h3>
					<fmt:message key="home.features.easy.title" />
				</h3>
				<!-- 				<p>
					<span class="icon-stack icon-4x"> <i class="icon-circle icon-stack-base"></i> <i class="icon-eye-open icon-light"></i>
					</span>
				</p> -->
				<br>
				<p>
					<fmt:message key="home.features.easy.description" />
				</p>
			</div>
		</div> --%>
		<hr class="hidden-lg hidden-md">
	</div>
	<div class="col-md-4">
		<div class="panel panel-primary panel-kenmerk">
			<div class="panel-heading">
				<fmt:message key="home.features.handy.title" />
			</div>
			<div class="panel-body">
				<fmt:message key="home.features.handy.description" />
			</div>
		</div>

		<%-- 		<div class="thumbnail">
			<div class="caption">
				<h3>
					<fmt:message key="home.features.handy.title" />
				</h3>
				<!-- 				<p>
					<span class="icon-stack icon-4x"> <i class="icon-circle icon-stack-base"></i> <i class="icon-bolt icon-light"></i>
					</span>
				</p> -->
				<br>
				<p>
					<fmt:message key="home.features.handy.description" />
				</p>
			</div>
		</div> --%>
	</div>
</div>

<jsp:include page="/assets/layouts/bottom.jsp" />