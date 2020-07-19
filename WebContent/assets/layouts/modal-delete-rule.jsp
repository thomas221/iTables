<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="custom" uri="http://itables.ibyte.be/CustomJSPTags"%>

<fmt:setBundle basename="i18n.text" />

<!-- Modal -->
<div class="modal fade" id="modal-delete-rule" tabindex="-1" role="dialog" aria-labelledby="modal-title" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h4 class="modal-title">
					<i class="icon-fixed-width icon-trash"></i>
					<fmt:message key="modal.delete.rule.header" />
				</h4>
			</div>
			<div class="modal-body">
				<p>Weet u zeker dat u onderstaande regel definitief wenst te verwijderen?</p>
			
				<form class="form-horizontal" role="form" id="form-delete-rule" action="<c:url value="/my_configuration/rules?action=delete"/>" method="post">
					<!-- Hidden input for returning to current page after submit -->
					<input id="modal-delete-rule-context-page" type="hidden" name="modal-delete-rule-context-page" value="" />
					<!-- Start index field -->
					<div class="form-group">
						<label for="modal-delete-rule-index" class="col-sm-2 control-label">Index</label>
						<div class="col-sm-10">
							<p class="form-control-static" id="modal-delete-rule-index-p"></p>
							<input type="hidden" id="modal-delete-rule-index" name="modal-delete-rule-index">
						</div>
					</div>
					<!-- End index field -->
					<!-- Start protocol field -->
					<div class="form-group">
						<label for="modal-delete-rule-protocol" class="col-sm-2 control-label">Protocol</label>
						<div class="col-sm-10">
							<p class="form-control-static" id="modal-delete-rule-protocol-p"></p>
							<input type="hidden" id="modal-delete-rule-protocol" name="modal-delete-rule-protocol">
						</div>
					</div>
					<!-- End protocol field -->
					<!-- Start source IP field -->
					<div class="form-group">
						<label for="modal-delete-rule-source-ip" class="col-sm-2 control-label">Bron IP</label>
						<div class="col-sm-10">
							<p class="form-control-static" id="modal-delete-rule-source-ip-p"></p>
							<input type="hidden" id="modal-delete-rule-source-ip" name="modal-delete-rule-source-ip">
						</div>
					</div>
					<!-- End source IP field -->
					<!-- Start source port field -->
					<div class="form-group">
						<label for="modal-delete-rule-source-port" class="col-sm-2 control-label">Bronpoort</label>
						<div class="col-sm-10">
							<p class="form-control-static" id="modal-delete-rule-source-port-p"></p>
							<input type="hidden" id="modal-delete-rule-source-port" name="modal-delete-rule-source-port">
						</div>
					</div>
					<!-- End source port field -->
					<!-- Start destination IP field -->
					<div class="form-group">
						<label for="modal-delete-rule-destination-ip" class="col-sm-2 control-label">Doel IP</label>
						<div class="col-sm-10">
							<p class="form-control-static" id="modal-delete-rule-destination-ip-p"></p>
							<input type="hidden" id="modal-delete-rule-destination-ip" name="modal-delete-rule-destination-ip">
						</div>
					</div>
					<!-- End destination IP field -->
					<!-- Start destination port field -->
					<div class="form-group">
						<label for="modal-delete-rule-destination-port" class="col-sm-2 control-label">Doelpoort</label>
						<div class="col-sm-10">
							<p class="form-control-static" id="modal-delete-rule-destination-port-p"></p>
							<input type="hidden" id="modal-delete-rule-destination-port" name="modal-delete-rule-destination-port">
						</div>
					</div>
					<!-- End destination port field -->
					<!-- Start action field -->
					<div class="form-group">
						<label for="modal-delete-rule-action" class="col-sm-2 control-label">Actie</label>
						<div class="col-sm-10">
							<p class="form-control-static" id="modal-delete-rule-action-p"></p>
							<input type="hidden" id="modal-delete-rule-action" name="modal-delete-rule-action">
						</div>
					</div>
					<!-- End action field -->
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">
					<i class="icon-fixed-width icon-remove"></i>
					<fmt:message key="modal.edit.rule.cancel" />
				</button>
				<button type="button" class="btn btn-danger submit-delete-rule-form">
					<i class="icon-fixed-width icon-trash"></i>
					<fmt:message key="modal.delete.rule.save" />
				</button>
			</div>
		</div>
		<!-- /.modal-content -->
	</div>
	<!-- /.modal-dialog -->
</div>
<!-- /.modal -->