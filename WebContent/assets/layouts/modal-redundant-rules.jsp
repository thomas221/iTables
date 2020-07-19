<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="custom" uri="http://itables.ibyte.be/CustomJSPTags"%>

<fmt:setBundle basename="i18n.text" />

<!-- Modal -->
<div class="modal fade" id="modal-redundant-rules" tabindex="-1" role="dialog" aria-labelledby="modal-title" aria-hidden="true">
	<div class="modal-dialog" style="width:700px;">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="modal-redundant-rules-title">
					<i class="icon-fixed-width icon-random"></i>
					Redundante firewallregels
				</h4>
			</div>
			<div class="modal-body" id="modal-redundant-rules-body">
				<div id="modal-redundant-rules-body-div">
					<table cellpadding="0" cellspacing="0" border="0" class="table table-hover table-striped table-bordered table-no-wrap"
						id="modal-redundant-rules-body-datatable">
						<thead>
							<tr>
								<th>#</th>
								<th>Protocol</th>
								<th>Bron IP</th>
								<th>Bronpoort</th>
								<th>Doel IP</th>
								<th>Doelpoort</th>
								<th>Actie</th>
							</tr>
						</thead>
					</table>
				</div>

				<span id="modal-redundant-rules-body-span">
					De chain bevat geen redundante firewallregels.
				</span>
				
				<span id="modal-redundant-rules-body-error"></span>
			</div>
			<div class="modal-footer">
				<form method="post" action="/my_configuration/show_redundancy" class="form-inline">
					<button type="submit" id="remove-redundant-rules" class="btn btn-danger" >
						<i class="icon-fixed-width icon-trash"></i>
						Verwijder deze regels
					</button>
				</form>
				<button type="button" class="btn btn-default" data-dismiss="modal">
					<i class="icon-fixed-width icon-remove"></i>
					<fmt:message key="modal.segment_content.close" />
				</button>
			</div>
		</div>
		<!-- /.modal-content -->
	</div>
	<!-- /.modal-dialog -->
</div>
<!-- /.modal -->