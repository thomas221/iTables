<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="custom" uri="http://itables.ibyte.be/CustomJSPTags"%>

<fmt:setBundle basename="i18n.text" />

<jsp:useBean id="now" class="java.util.Date" />
<fmt:formatDate var="current_year" value="${now}" pattern="yyyy" />

<jsp:include page="/assets/layouts/modal-export-configuration.jsp" />
</div>

<!-- End page content -->

<hr>

<!-- Start footer -->
<div id="footer" class="row footer">
	<div class="col-xs-4">
		<p id="footer_copyright_message" class="">
			<small> 
				<fmt:message key="application.copyright">
					<fmt:param value="${current_year}" />
				</fmt:message>
				<fmt:message key="applicatie.versie" />
			</small>
		</p>
	</div>
	<div class="col-xs-4 text-center">
		<c:if test="${custom:isDebug()}">
			<p>
				<span class="label label-danger cursor-pointer toggle-popover" data-toggle="popover" data-placement="top" data-title="Sessie attributen"
					data-content="
				<c:forEach var="sessionEntry" items="${sessionScope}">
					<c:out value="${sessionEntry.key}" /> = <c:out value="${sessionEntry.value}" />;
				</c:forEach>"><fmt:message
						key="debug" /></span>
			</p>
		</c:if>
	</div>
	<div class="col-xs-4">
		<p class="text-right">
			<small><a id="footer_link_top" href="#">
					<fmt:message key="footer.top" />
					<i class="icon-caret-up"></i>
				</a></small>
		</p>
	</div>
</div>
<!-- End footer -->

</div>
<!-- End container -->

<noscript>
	<div id="javascript_disabled_alert">
		<fmt:message key="javascript.disabled.alert" />
	</div>
</noscript>

</body>
</html>