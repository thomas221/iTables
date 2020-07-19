<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="custom" uri="http://itables.ibyte.be/CustomJSPTags"%>

<fmt:setBundle basename="i18n.text" />

<div class="dropdown dropdown-rules-anomalies visible-xs visible-sm pull-right">
	<button class="btn btn-primary btn-sm dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown">
		<i class="icon-fixed-width icon-table"></i>
		<fmt:message key="my_configuration.tables_and_chains" />
		<span class="caret"></span>
	</button>
	<ul class="dropdown-menu" role="menu" aria-labelledby="dropdown-rules">
		<c:forEach var="varTable" items="${configuration.tables}" varStatus="varTableStatus">
			<li role="presentation" class="dropdown-header">
				<i class="icon-fixed-width icon-table"></i>
				${varTable.name}
				<fmt:message key="configuration.rules.table" />
			</li>

			<c:forEach var="varChain" items="${varTable.chains}">
				<li role="presentation" class="menuitem <c:if test="${(varTable.name eq table) && (varChain.name eq chain)}">active-chain</c:if>">
					<a role="menuitem" href="<c:url value="/my_configuration/anomalies?table=${varTable.name}&chain=${varChain.name}" />"> <i
							class="icon-fixed-width icon-angle-right"></i> ${varChain.name} <fmt:message key="configuration.rules.chain" /> &nbsp; <c:if
							test="${fn:length(varChain.rules) gt 0}">
<%-- 							<c:if test="${space.conflictCount gt 0}">
								<span class="badge badge-danger has-tooltip" data-toggle="tooltip"
									title="<fmt:message key="configuration.rules.the.chain" /> <fmt:message key="contains" /> ${space.conflictCount} <fmt:message key="conflicting_segments" />">${space.conflictCount}
									<i class="icon-warning-sign"></i>
								</span>
							</c:if> --%>
							<span class="badge has-tooltip" data-toggle="tooltip"
								title="<fmt:message key="configuration.rules.the.chain" /> <fmt:message key="contains" /> ${fn:length(varChain.rules)} <fmt:message key="rules" />">${fn:length(varChain.rules)}
								<i class="icon-gears"></i>
							</span>
						</c:if>
					</a>
				</li>
			</c:forEach>
			<c:if test="${!varTableStatus.last}">
				<li role="presentation" class="divider"></li>
			</c:if>
		</c:forEach>
	</ul>
</div>