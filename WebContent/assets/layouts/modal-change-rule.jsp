<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="custom" uri="http://itables.ibyte.be/CustomJSPTags"%>

<fmt:setBundle basename="i18n.text" />

<!-- Modal -->
<div class="modal fade" id="modal-change-rule" tabindex="-1" role="dialog" aria-labelledby="modal-title" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h4 class="modal-title">
					<i class="icon-fixed-width icon-pencil"></i>
					<fmt:message key="modal.change.rule.header" />
				</h4>
			</div>
			<div class="modal-body">
				<div class="alert alert-danger fade in modal-change-rule-alert" style="display: none">
					<button type="button" class="close" aria-hidden="true">&times;</button>
					<h4>
						<fmt:message key="alert.danger" />
					</h4>
					<p class="modal-change-rule-alert"></p>
				</div>

				<p>
					Gebruik onderstaand formulier om <b>firewallregel <span id="modal-change-rule-index-span"></span></b> te wijzigen.
				</p>

				<form class="form-horizontal" role="form" id="form-change-rule" method="post" action="<c:url value="/my_configuration/rules?action=edit_modal"/>">
					<!-- Give rule number with request -->
					<input id="modal-change-rule-index" type="hidden" name="modal-change-rule-index" value="" />
					<!-- Start protocol field -->
					<div class="form-group">
						<label for="modal-change-rule-protocol-choose" class="col-lg-2 control-label" id="modal-change-rule-protocol-choose-text">Protocol</label>
						<div class="col-lg-10">
							<select id="modal-change-rule-protocol-choose" name="modal-change-rule-protocol-choose" class="form-control">
								<option value="-1">*</option>
								<option value="6">TCP</option>
								<option value="17">UDP</option>
								<option value="1">ICMP</option>
								<option value="-3">protocol getal</option>
							</select>
						</div>
					</div>
					<div class="form-group">
						<label for="modal-change-rule-protocol-getal" class="col-lg-2 control-label" id="modal-change-rule-protocol-getal-text">Protocol getal</label>
						<div class="col-lg-10">
							<input type="text" class="form-control protocol-getal" id="modal-change-rule-protocol-getal" name="modal-change-rule-protocol-getal" />
						</div>
					</div>
					<!-- End protocol field -->
					<!-- Start source IP field -->
					<div class="form-group">
						<label for="modal-change-rule-source-ip" class="col-lg-2 control-label">Bron IP</label>
						<div class="col-lg-10">
							<input type="text" class="form-control input-ip" id="modal-change-rule-source-ip" name="modal-change-rule-source-ip" placeholder="bron IP">
							<p class="help-block"></p>
						</div>
					</div>
					<!-- End source IP field -->
					<!-- Start source port field -->
					<div class="form-group">
						<label for="modal-change-rule-source-port" class="col-lg-2 control-label" id="modal-change-rule-source-port-text">Bronpoort</label>
						<div class="col-lg-10">
							<input type="text" class="form-control port-change-rule" id="modal-change-rule-source-port" name="modal-change-rule-source-port"
								placeholder="bronpoort">
						</div>
					</div>
					<!-- End source port field -->
					<!-- Start destination IP field -->
					<div class="form-group">
						<label for="modal-change-rule-destination-ip" class="col-lg-2 control-label">Doel IP</label>
						<div class="col-lg-10">
							<input type="text" class="form-control input-ip" id="modal-change-rule-destination-ip" name="modal-change-rule-destination-ip"
								placeholder="doel IP">
						</div>
					</div>
					<!-- End destination IP field -->
					<!-- Start destination port field -->
					<div class="form-group">
						<label for="modal-change-rule-destination-port" class="col-lg-2 control-label" id="modal-change-rule-destination-port-text">Doelpoort</label>
						<div class="col-lg-10">
							<input type="text" class="form-control port-change-rule" id="modal-change-rule-destination-port" name="modal-change-rule-destination-port"
								placeholder="doelpoort">
						</div>
					</div>
					<!-- End destination port field -->
					<!-- Start action field -->
					<div class="form-group">
						<label for="modal-change-rule-action" class="col-lg-2 control-label">Actie</label>
						<div class="col-lg-10">
							<select id="modal-change-rule-action" name="modal-change-rule-action" class="form-control">
								<option value="1">accept</option>
								<option value="0">drop</option>
							</select>
						</div>
					</div>
					<!-- End action field -->
					<div class="modal-footer modal-footer-no-pchangeing">
<%-- 						<i id="modal-change-rule-remove" class="icon-trash hover-blue has-tooltip button-show-modal-delete-rule pull-left" data-toggle="tooltip"
							title="<fmt:message key="remove.rule" />"> Verwijder regel </i> --%>
						<button id="modal-change-rule-remove" type="button" class="btn btn-danger pull-left">
							<i class="icon-fixed-width icon-remove"></i>
							<fmt:message key="modal.edit.rule.delete_rule" />
						</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">
							<i class="icon-fixed-width icon-remove"></i>
							<fmt:message key="modal.edit.rule.cancel" />
						</button>
						<button type="submit" class="btn btn-success submit-change-rule-form" id="submit-button-change-rule-form">
							<i class="icon-fixed-width icon-pencil"></i>
							<fmt:message key="modal.change.rule.save" />
						</button>
					</div>
				</form>
			</div>
		</div>
		<!-- /.modal-content -->
	</div>
	<!-- /.modal-dialog -->
</div>
<!-- /.modal -->