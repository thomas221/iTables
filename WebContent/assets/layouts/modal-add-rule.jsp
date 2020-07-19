<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="custom" uri="http://itables.ibyte.be/CustomJSPTags"%>

<fmt:setBundle basename="i18n.text" />

<!-- Modal -->
<div class="modal fade" id="modal-add-rule" tabindex="-1" role="dialog" aria-labelledby="modal-title" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h4 class="modal-title">
					<i class="icon-fixed-width icon-plus"></i>
					<fmt:message key="modal.add.rule.header" />
				</h4>
			</div>
			<div class="modal-body">
				<div class="alert alert-danger fade in modal-add-rule-alert" style="display: none">
					<button type="button" class="close" aria-hidden="true">&times;</button>
					<h4>
						<fmt:message key="alert.danger" />
					</h4>
					<p class="modal-add-rule-alert"></p>
				</div>

				<p>Gebruik dit formulier om een nieuwe firewall regel toe te voegen achteraan de regelset van de huidige chain. Voor tussenvoegen onderaan aanvinken s.v.p. </p>

				<form class="form-horizontal" role="form" id="form-add-rule" method="post" action="<c:url value="/my_configuration/rules?action=add"/>">
					<!-- Start protocol field -->
					<div class="form-group">
						<label for="modal-add-rule-protocol-select" class="col-lg-2 control-label" id="modal-add-rule-protocol-select-text">Protocol</label>
						<div class="col-lg-10">
							<select id="modal-add-rule-protocol-select" name="modal-add-rule-protocol-select" class="form-control">
								<option value="-1">*</option>
								<option value="6" selected="selected">TCP</option>
								<option value="17">UDP</option>
								<option value="1">ICMP</option>
								<option value="-3">protocol getal</option>
							</select>							
						</div>
					</div>
					<div class="form-group">
						<label for="modal-add-rule-protocol-getal" class="col-lg-2 control-label" id="modal-add-rule-protocol-getal-text">Protocol getal</label>
						<div class="col-lg-10">
							<input type="text" class="form-control protocol-getal" id="modal-add-rule-protocol-getal" name="modal-add-rule-protocol-getal" disabled="disabled" value="6" />
						</div>
					</div>
					<!-- End protocol field -->
					<!-- Start source IP field -->
					<div class="form-group">
						<label for="modal-add-rule-source-ip" class="col-lg-2 control-label">Bron IP</label>
						<div class="col-lg-10">
							<input type="text" class="form-control input-ip" id="modal-add-rule-source-ip" name="modal-add-rule-source-ip" placeholder="bron IP">
							<p class="help-block"></p>
						</div>
					</div>
					<!-- End source IP field -->
					<!-- Start source port field -->
					<div class="form-group">
						<label for="modal-add-rule-source-port" class="col-lg-2 control-label" id="modal-add-rule-source-port-text">Bronpoort</label>
						<div class="col-lg-10">
							<input type="text" class="form-control port-add-rule" id="modal-add-rule-source-port" name="modal-add-rule-source-port" placeholder="bronpoort">
						</div>
					</div>
					<!-- End source port field -->
					<!-- Start destination IP field -->
					<div class="form-group">
						<label for="modal-add-rule-destination-ip" class="col-lg-2 control-label">Doel IP</label>
						<div class="col-lg-10">
							<input type="text" class="form-control input-ip" id="modal-add-rule-destination-ip" name="modal-add-rule-destination-ip" placeholder="doel IP">
						</div>
					</div>
					<!-- End destination IP field -->
					<!-- Start destination port field -->
					<div class="form-group">
						<label for="modal-add-rule-destination-port" class="col-lg-2 control-label" id="modal-add-rule-destination-port-text">Doelpoort</label>
						<div class="col-lg-10">
							<input type="text" class="form-control port-add-rule" id="modal-add-rule-destination-port" name="modal-add-rule-destination-port"
								placeholder="doelpoort">
						</div>
					</div>
					<!-- End destination port field -->
					<!-- Start action field -->
					<div class="form-group">
						<label for="modal-add-rule-action" class="col-lg-2 control-label">Actie</label>
						<div class="col-lg-10">
							<select id="modal-add-rule-action" name="modal-add-rule-action" class="form-control">
								<option value="1">accept</option>
								<option value="0">drop</option>
							</select>
						</div>
					</div>
					<!-- End action field -->
					<!-- Start checkbox for showScope funtionality -->
					<div class="form-group">
						<label for="modal-add-rule-showscope" class="col-lg-10 control-label">Voeg in op een positie zodat hij geen conflict oplevert met voorgaande regels.</label>
						<div class="col-lg-2">
							<input type="checkbox" class="form-control" id="modal-add-rule-showscope" name="modal-add-rule-showscope" />	
						</div>				
					</div>
					<!-- End checkbox for showScope functionality -->
					<div class="modal-footer modal-footer-no-padding">
						<button type="button" class="btn btn-default" id="cancel-add-rule" data-dismiss="modal">
							<i class="icon-fixed-width icon-remove"></i>
							<fmt:message key="modal.edit.rule.cancel" />
						</button>
						<button type="submit" class="btn btn-success submit-add-rule-form" id="submit-button-add-rule-form">
							<i class="icon-fixed-width icon-plus"></i>
							<fmt:message key="modal.add.rule.save" />
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