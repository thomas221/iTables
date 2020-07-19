<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="custom" uri="http://itables.ibyte.be/CustomJSPTags"%>

<fmt:setBundle basename="i18n.text" />

<fmt:message key="my_configuration_anomalies.header" var="pagetitle_i18n" />

<c:choose>
	<c:when test="${table!=null && chain!=null}">
		<fmt:message key="my_configuration_anomalies.header_active_chain" var="pagetitle_i18n">
			<fmt:param value="${fn:toLowerCase(table)}" />
			<fmt:param value="${fn:toLowerCase(chain)}" />
		</fmt:message>
	</c:when>
	<c:otherwise>
		<fmt:message key="my_configuration_anomalies.header_no_active_chain" var="pagetitle_i18n" />
	</c:otherwise>
</c:choose>

<jsp:include page="/assets/layouts/top.jsp">
	<jsp:param name="pagetitle" value="${pagetitle_i18n}" />
</jsp:include>

<div class="row">
	<div class="col-sm-12 col-md-9">
		<div class="row">
			<div class="col-sm-12">
				<div class="page-header">
					<h1>
						<i class="icon-fixed-width icon-bug"></i>
						<fmt:message key="my_configuration_anomalies.header" />
						<c:if test="${table!=null && chain!=null}">
							<br>
							<small class="text-lowercase">${table} <fmt:message key="configuration.rules.table" /> <i class="icon-long-arrow-right"></i> ${chain}
								<fmt:message key="configuration.rules.chain" /></small>
						</c:if>
					</h1>
					<ol class="breadcrumb pull-right visible-lg">
						<li>
							<a href="<c:url value="/home"/>"> <fmt:message key="home.header" />
							</a>
						</li>
						<li>
							<a href="<c:url value="/my_configuration"/>"> <fmt:message key="navbar.configuration.title" />
							</a>
						</li>
						<li class="active">
							<fmt:message key="my_configuration_anomalies.header" />
						</li>
					</ol>

					<c:if test="${fn:length(configuration.tables) gt 0}">
						<jsp:include page="/assets/layouts/dropdown-anomalies.jsp" />
					</c:if>
				</div>
			</div>

			<div class="col-sm-12">
				<jsp:include page="/assets/layouts/alerts.jsp" />

				<c:choose>
					<c:when test="${fn:length(configuration.tables) gt 0 && table==null && chain==null}">
						<p>
							<fmt:message key="configuration.anomalies.select.chain" />
						</p>
					</c:when>
					<c:when test="${fn:length(configuration.tables) gt 0 && table!=null && chain!=null}">
						<c:choose>
							<c:when test="${fn:length(setOfGroups.correlationGroups) eq 0}">
								<p>
									<fmt:message key="my_configuration.anomalies.empty_chain.message" />
								</p>
							</c:when>
							<c:otherwise>
								<h3>
									<i class="icon-fixed-width icon-random"></i>
									<fmt:message key="my_configuration.redundanties" />
								</h3>

								<p>
									<fmt:message key="my_configuration.redundanties.description" /> Zie <a href="<c:url value="/documentation#doc-6" />" target="_blank" >Zoeken naar redundante (overbodige) regels</a> voor uitleg.
								</p>

								<p>
									<button type="button" class="btn btn-primary button-show-redundant-rules">
										<i class="icon-fixed-width icon-magic"></i>
										<fmt:message key="knop.redundante.regels.aanduiden" />
									</button>
								</p>

								<h3>
									<i class="icon-fixed-width icon-sitemap"></i>
									<fmt:message key="navbar.configuration.correlation_groups" />
								</h3>
								<p>
									<fmt:message key="configuration.segments.description">
										<fmt:param value="${fn:length(setOfGroups.correlationGroups)}" />
										<fmt:param value="${fn:length(segments)}" />
									</fmt:message> Zie <a href="<c:url value="/documentation#doc-3" />" target="_blank" >Vinden van policy conflicten</a> en <a href="<c:url value="/documentation#doc-4" />" target="_blank" >Oplossen van policy conflicten</a> voor uitleg.
								</p>

								<c:forEach var="correlationGroup" items="${setOfGroups.correlationGroups}">

									<div class="panel ${custom:getCssClassPanelConflictingCorrelationGroup(correlationGroup.conflicting,correlationGroup.isSolved)}">
										<div class="panel-heading">
											<h3 class="panel-title">
												<i class="icon-fixed-width icon-sitemap"></i>
												<fmt:message key="configuration.group.name" />
												${correlationGroup.groupNumber}
												<c:if test="${correlationGroup.isSolved}">
													&nbsp;&nbsp;&nbsp;-&nbsp;&nbsp;&nbsp;Conflicten opgelost
												</c:if>
											</h3>
										</div>
										<form action="<c:url value="/my_configuration/solve_conflicts"/>" method="post">
											<table class="table table-bordered table-segment-correlation-group" style="margin-bottom: 0px;"
												id="segment_table${correlationGroup.groupNumber}">
												<thead>
													<tr>
														<th style="text-align: left; padding: 0px;"></th>
														<c:forEach var="segment" items="${correlationGroup.segments}">
															<th style="padding: 0px;">
																<div class="dropdown">
																	<c:if test="${segment.conflicting}">
																		<span class="has-tooltip" data-toggle="tooltip" title="Segment bevat conflicterende firewallregels">
																			<i class="icon-fixed-width icon-warning-sign icon-large icon-red"></i>
																		</span>
																	</c:if>
																	<button class="btn dropdown-toggle btn-dropdown-toggle-segment-content" type="button"
																		id="dropdown-menu-segment-${segment.segmentNumber}" data-toggle="dropdown">
																		<fmt:message key="configuration.segments.segment.lowercase" />
																		${segment.segmentNumber}
																		<i class="icon-fixed-width icon-caret-down"></i>
																	</button>
																	<ul class="dropdown-menu" role="menu" aria-labelledby="dropdown-segment-content-options">
																		<li role="presentation">
																			<a role="menuitem" tabindex="-1" href="#" class="segment-content-link" data-segment-number="${segment.segmentNumber}"
																				data-compact="1"><i class="icon-fixed-width icon-zoom-out"></i> <fmt:message key="configuration.segments.show_compact_content" /></a>
																		</li>
																		<li role="presentation">
																			<a role="menuitem" tabindex="-1" href="#" class="segment-content-link" data-segment-number="${segment.segmentNumber}"
																				data-compact="0"><i class="icon-fixed-width icon-zoom-in"></i> <fmt:message key="configuration.segments.show_full_content" /></a>
																		</li>
																	</ul>
																</div>
															</th>
														</c:forEach>
													</tr>
												</thead>
												<tbody>
													<c:if test="${correlationGroup.conflicting}">
														<tr>
															<th style="padding: 0px; width: 150px;"><input type="hidden" name="correlation_group_number"
																	value="${correlationGroup.groupNumber}" />
																<button type="submit" style="white-space:normal;width: 100%;" class="btn btn-info btn-sm">
																	<i class="icon-fixed-width icon-edit"></i>
																	Voorkeursactie wijzigen
																</button></th>
															<c:forEach var="segment" items="${correlationGroup.segments}">
																<td style="padding: 0px;"><c:if test="${segment.conflicting}">
																		<select style="text-align: center;" name="action_${segment.segmentNumber}" class="form-control" required>
																			<option value="allow">accept</option>
																			<option value="deny">drop</option>
																		</select>
																	</c:if></td>
															</c:forEach>
														</tr>
													</c:if>
													<c:forEach var="ruleOfGroup" items="${correlationGroup.rules}" varStatus="status">
														<tr id="segment_table_rule_${ruleOfGroup.ruleNr}" class="knop-regel-wijzigen" data-rule-index="${ruleOfGroup.ruleNr}" data-protocol="${ruleOfGroup.protocol}" data-source-ip="${ruleOfGroup.sourceIP}" data-source-port="${ruleOfGroup.sourcePort}" data-destination-ip="${ruleOfGroup.destinationIP}" data-destination-port="${ruleOfGroup.destinationPort}" data-action="${ruleOfGroup.actionInt}">

															<th style="width: 150px; cursor: pointer;" id="focus_group_${correlationGroup.groupNumber+1}" class="has-popover" data-placement="top"
																data-title="Definitie van firewallregel ${ruleOfGroup.ruleNr}" data-trigger="hover" data-container="body" data-html="true"
																data-content="<strong>Index:</strong> ${ruleOfGroup.ruleNr}<br/><strong>Protocol:</strong> ${(ruleOfGroup.protocol==6) ? 'TCP' : (ruleOfGroup.protocol==17 ? 'UDP' : (ruleOfGroup.protocol==1 ? 'ICMP' : (ruleOfGroup.protocol==-1 ? '*' : ruleOfGroup.protocol)))}<br/><strong>Bron IP:</strong> ${ruleOfGroup.sourceIP}<br/><c:if test="${ruleOfGroup.protocol == 6 || ruleOfGroup.protocol == 17}"><strong>Bronpoort:</strong> ${ruleOfGroup.sourcePort}<br/></c:if><strong>Doel IP:</strong> ${ruleOfGroup.destinationIP}<br/><c:if test="${ruleOfGroup.protocol == 6 || ruleOfGroup.protocol == 17}"><strong>Doelpoort:</strong> ${ruleOfGroup.destinationPort}<br/></c:if><strong>Actie:</strong> ${ruleOfGroup.actionString}<br/>"><fmt:message
																	key="my_configuration.anomalies.rule" /> <span style="white-space: nowrap;">${ruleOfGroup.ruleNr}</span></th>


															<c:forEach var="segmentInColumn" items="${correlationGroup.segments}">
																<c:choose>
																	<c:when test="${segmentInColumn.rules.contains(ruleOfGroup)}">
																		<td style="text-align: center" class="${custom:actionToCssTrClass(ruleOfGroup.action)}">${ruleOfGroup.actionString}</td>
																	</c:when>
																	<c:otherwise>
																		<td></td>
																	</c:otherwise>
																</c:choose>
															</c:forEach>
														</tr>
													</c:forEach>
												</tbody>
											</table>
										</form>
									</div>
								</c:forEach>
							</c:otherwise>
						</c:choose>
					</c:when>
					<c:otherwise>
						<p>
							<fmt:message key="configuration.no.tables.or.chains" />
						</p>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
	</div>

	<div class="col-md-3 visible-md visible-lg">
		<c:if test="${fn:length(configuration.tables) gt 0}">
			<jsp:include page="/assets/layouts/sidebar-anomalies.jsp" />
		</c:if>
	</div>
</div>

<jsp:include page="/assets/layouts/modal-change-rule.jsp" />

<jsp:include page="/assets/layouts/modal-delete-rule.jsp" />

<jsp:include page="/assets/layouts/modal-segment-content.jsp" />

<jsp:include page="/assets/layouts/modal-redundant-rules.jsp" />

<jsp:include page="/assets/layouts/bottom.jsp" />