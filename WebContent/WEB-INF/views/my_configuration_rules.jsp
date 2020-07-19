<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="custom" uri="http://itables.ibyte.be/CustomJSPTags"%>

<fmt:setBundle basename="i18n.text" />

<c:choose>
	<c:when test="${table!=null && chain!=null}">
		<fmt:message key="my_configuration_rules.header_active_chain" var="pagetitle_i18n">
			<fmt:param value="${fn:toLowerCase(table)}" />
			<fmt:param value="${fn:toLowerCase(chain)}" />
		</fmt:message>
	</c:when>
	<c:otherwise>
		<fmt:message key="my_configuration_rules.header_no_active_chain" var="pagetitle_i18n" />
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
						<i class="icon-fixed-width icon-cogs"></i>
						<fmt:message key="my_configuration_rules.header" />
						<c:if test="${table!=null && chain!=null}">
							<br />
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
							<fmt:message key="my_configuration_rules.header" />
						</li>
					</ol>

					<c:if test="${fn:length(configuration.tables) gt 0}">
						<jsp:include page="/assets/layouts/dropdown-rules.jsp" />
					</c:if>

				</div>
			</div>

			<div class="col-sm-12">
				<jsp:include page="/assets/layouts/alerts.jsp" />

				<c:choose>
					<c:when test="${fn:length(configuration.tables) gt 0 && table==null && chain==null}">
						<p>
							<fmt:message key="configuration.rules.select.chain" />
						</p>
					</c:when>
					<c:when test="${fn:length(configuration.tables) gt 0 && table!=null && chain!=null}">

						<div class="alert alert-success fade in flash-success-div" style="display: none">
							<button type="button" class="close" aria-hidden="true">&times;</button>
							<h4>
								<fmt:message key="alert.success" />
							</h4>
							<p class="flash-success-p"></p>
						</div>

						<div class="alert alert-danger fade in flash-danger-div" style="display: none">
							<button type="button" class="close" aria-hidden="true">&times;</button>
							<h4>
								<fmt:message key="alert.danger" />
							</h4>
							<p class="flash-danger-p"></p>
						</div>
						
						<div id="table-rules-div">

							<table cellpadding="0" cellspacing="0" border="0" class="table table-hover table-striped table-bordered" id="table-rules" data-table="${table}"
								data-chain="${chain}">
								<thead>
									<tr>
										<th style="width: 17px;"><fmt:message key="configuration.rules.index" /></th>
										<th style="width: 86px;"><fmt:message key="configuration.rules.protocol" /></th>
										<th style="width: 146px;"><fmt:message key="configuration.rules.source_ip" /></th>
										<th style="width: 103px;"><fmt:message key="configuration.rules.source_port" /></th>
										<th style="width: 167px;"><fmt:message key="configuration.rules.destination_ip" /></th>
										<th style="width: 100px;"><fmt:message key="configuration.rules.destination_port" /></th>
										<th style="width: 72px;"><fmt:message key="configuration.rules.action" /></th>
										<th style="width: 21px;"></th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="rule" items="${rules}">
										<tr id="${rule.ruleNr}">
											<td>${rule.ruleNr}</td>
											<td class="protocol-cell"><a href="#" class="editable-fields editable-field-rule-protocol" data-url="/my_configuration/rules?action=edit"
												data-source="/my_configuration/rules?action=get_protocols" data-pk="${rule.ruleNr}" data-type="text" data-name="protocol"
												data-title="Protocol bewerken" data-prepend="true" data-value="${rule.protocol}">${Protocol.toString(rule.protocol)}</a></td>
											<td class="source-ip-cell"><a href="#" class="editable-fields" data-url="/my_configuration/rules?action=edit" data-pk="${rule.ruleNr}" data-type="text"
												data-name="source-ip" data-title="Bron IP bewerken" data-value="${rule.sourceIPWithAnyAsStar}">${rule.sourceIPWithAnyAsStar}</a></td>
											<td class="source-port-cell"><a href="#" class="editable-fields" data-url="/my_configuration/rules?action=edit" data-pk="${rule.ruleNr}" data-type="text"
												data-name="source-port" data-title="Bronpoort bewerken" data-value="${rule.sourcePort}">${rule.sourcePort}</a></td>
											<td class="destination-ip-cell"><a href="#" class="editable-fields" data-url="/my_configuration/rules?action=edit" data-pk="${rule.ruleNr}" data-type="text"
												data-name="destination-ip" data-title="Doel IP bewerken" data-value="${rule.destinationIPWithAnyAsStar}">${rule.destinationIPWithAnyAsStar}</a></td>
											<td class="destination-port-cell"><a href="#" class="editable-fields" data-url="/my_configuration/rules?action=edit" data-pk="${rule.ruleNr}" data-type="text"
												data-name="destination-port" data-title="Doelpoort bewerken" data-value="${rule.destinationPort}">${rule.destinationPort}</a></td>
											<td class="action-cell"><a href="#" class="editable-field-rule-action" data-url="/my_configuration/rules?action=edit"
												data-source="/my_configuration/rules?action=get_actions" data-pk="${rule.ruleNr}" data-type="select" data-name="action"
												data-title="Actie bewerken" data-prepend="true" data-value="${rule.actionInt}"><span
														class="label label-editable-field ${custom:actionToCssLabelClass(rule.action)}">${rule.actionString}</span></a></td>
											<td style="text-align: center"><i class="icon-trash hover-blue has-tooltip button-show-modal-delete-rule"
													data-rule-index="${rule.ruleNr}" data-protocol="<fmt:message key="${rule.protocol}" />" data-source-ip="${rule.sourceIP}"
													data-source-port="${rule.sourcePort}" data-destination-ip="${rule.destinationIP}" data-destination-port="${rule.destinationPort}"
													data-action="<fmt:message key="${custom:actionToString(rule.action)}" />" data-toggle="tooltip" title="<fmt:message key="remove.rule" />"></i></td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
						<form class="form-inline" role="form" id="default-policy">
							<!-- Stel default policy in -->
							<label for="select-default-policy" class="control-label" id="select-default-policy-text">Default Policy:&nbsp;&nbsp;&nbsp;</label>
								<select id="select-default-policy" name="select-default-policy">
									<c:choose>
										<c:when test="${configuration.getTable(table).getChain(chain).getPolicy().getName() == 'ACCEPT'}">
											<option value="0">DROP</option>
											<option value="1" selected="selected">ACCEPT</option>
										</c:when>
										<c:otherwise>
											<option value="0" selected="selected">DROP</option>
											<option value="1">ACCEPT</option>
										</c:otherwise>
									</c:choose>
								</select>
						</form>
						<p id="default-policy-result" class="hide">
						</p>		
						<br />
						<br />
						<p class="button-group-rules">
							<button type="button" class="btn btn-primary knop-regel-toevoegen">
								<i class="icon-fixed-width icon-plus"></i>
								<fmt:message key="knop.firewallregel.toevoegen" />
							</button>
						</p>
					</c:when>
					<c:otherwise>
						<p>
							<fmt:message key="configuration.no.tables.or.chains" />
						</p>
					</c:otherwise>
				</c:choose>
			</div>
			<c:if test="${table!=null && chain!=null}">
				<!-- Paragraaf met functionaliteit om op te zoeken wat de actie is voor een bepaald netwerk pakket. -->
				<div class="col-sm-12">
					<h3>
						<i class="icon-fixed-width icon-search"></i>
						<fmt:message key="my_configuration.match" />
					</h3>
					<p>
						<fmt:message key="my_configuration.match.description" /> Zie <a href="<c:url value="/documentation#doc-9" />" target="_blank" >Bepaal matchende regel</a> voor uitleg.
					</p>
					<form class="form-inline" action="/my_configuration/rules" method="post">
						<table cellpadding="0" cellspacing="0" border="0" class="table table-hover table-striped table-bordered" id="packet-first-match-table">
							<tr>
								<th>Protocol</th>
								<th>Bron IP</th>
								<th>Bronpoort</th>
								<th>Doel IP</th>
								<th>Doelpoort</th>
							</tr>
							<tr>
								<td>
									<select id="match_protocol" class="input-small" name="match_protocol">
										<option value="-2" selected="selected">---</option>
										<option value="6">TCP</option>
										<option value="17">UDP</option>
										<option value="1">ICMP</option>
										<option value="-3">getal</option>
									</select>
								
									<input type="text" class="input-small match-IP" name="match_protocol_getal" id="match_protocol_getal" disabled="disabled" />
								</td>
								<td>
									<input type="text" class="input-small match-IP" name="match_bron_IP1" id="match_bron_IP1" />
									<b>.</b>
									<input type="text" class="input-small match-IP" name="match_bron_IP2" id="match_bron_IP2"/>
									<b>.</b>
									<input type="text" class="input-small match-IP" name="match_bron_IP3" id="match_bron_IP3" />
									<b>.</b>
									<input type="text" class="input-small match-IP" name="match_bron_IP4" id="match_bron_IP4" />
								</td>
								<td><input type="text" class="input-small match-port" name="match_bronpoort" id="match_bronpoort" /></td>
								<td>
									<input type="text" class="input-small match-IP" name="match_doel_IP1" id="match_doel_IP1" />
									<b>.</b>
									<input type="text" class="input-small match-IP" name="match_doel_IP2" id="match_doel_IP2"/>
									<b>.</b>
									<input type="text" class="input-small match-IP" name="match_doel_IP3" id="match_doel_IP3" />
									<b>.</b>
									<input type="text" class="input-small match-IP" name="match_doel_IP4" id="match_doel_IP4" />
								</td>
								<td><input type="text" class="input-small match-port" name="match_doelpoort" id="match_doelpoort" /></td>
							</tr>
						</table>
						<button class="btn btn-primary knop-match-pakket" type="button"><i class="icon-fixed-width icon-search"></i>match</button>
						<button class="btn btn-default clear-form" type="button">wis invoer</button>
					</form>
					<!-- melding resultaat match knop -->
					<div style="margin-top:10px;">
						<!-- in geval van succes -->
						<div class="col-sm-12 alert alert-success alert-dismissable hide" id="match_result_div_success">
							<p class="paragraph_weight_400" id="match_result_success"></p>
						</div>
						<!-- in geval van foutmelding -->
						<div class="col-sm-12 alert alert-danger alert-dismissable hide" id="match_result_div_warning">
							<p class="paragraph_weight_400" id="match_result_warning"></p>
						</div>
					</div>
				</div>
					
					
			</c:if>
		</div>
	</div>

	<div class="col-md-3 visible-md visible-lg">
		<c:if test="${fn:length(configuration.tables) gt 0}">
			<jsp:include page="/assets/layouts/sidebar-rules.jsp" />
		</c:if>
	</div>
</div>

<jsp:include page="/assets/layouts/modal-choose-protocol.jsp" />

<jsp:include page="/assets/layouts/modal-add-rule.jsp" />

<jsp:include page="/assets/layouts/modal-delete-rule.jsp" />

<jsp:include page="/assets/layouts/bottom.jsp" />