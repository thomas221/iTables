<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="custom" uri="http://itables.ibyte.be/CustomJSPTags"%>

<fmt:setBundle basename="i18n.text" />

<!-- Start sidebar -->
<div class="sidebar well sidebar-chains" role="navigation">
	<ul class="nav">
		<c:forEach var="varTable" items="${configuration.tables}">
			<li class="chains-table-header">
				<h4>
					<i class="icon-fixed-width icon-table"></i>
					${varTable.name}
					<fmt:message key="configuration.rules.table" />
				</h4>
			</li>

			<c:forEach var="varChain" items="${varTable.chains}">
				<li class="chains-chain-item <c:if test="${(varTable.name eq table) && (varChain.name eq chain)}">active-chain</c:if>">
					<a href="<c:url value="/my_configuration/rules?table=${varTable.name}&chain=${varChain.name}" />"> <i
							class="icon-fixed-width icon-chevron-right"></i> ${varChain.name} <fmt:message key="configuration.rules.chain" /> &nbsp; <c:if
							test="${fn:length(varChain.rules) gt 0}">
							<span class="badge pull-right has-tooltip" data-toggle="tooltip"
								title="<fmt:message key="configuration.rules.the.chain" /> <fmt:message key="contains" /> ${fn:length(varChain.rules)} <fmt:message key="rules" />">${fn:length(varChain.rules)} <i class="icon-gears"></i></span>
						</c:if>
					</a>
				</li>
			</c:forEach>
		</c:forEach>
	</ul>
</div>
<!-- End sidebar -->