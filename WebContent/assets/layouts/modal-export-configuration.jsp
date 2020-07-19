<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="custom" uri="http://itables.ibyte.be/CustomJSPTags"%>

<fmt:setBundle basename="i18n.text" />

<!-- Modal -->
<div class="modal fade" id="modal-export-configuration" tabindex="-1" role="dialog" aria-labelledby="modal-title" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="modal-export-configuration-title">
					<i class="icon-fixed-width icon-download"></i>
					<fmt:message key="navbar.configuration.export.iptables" />
				</h4>
			</div>
			<div class="modal-body" id="modal-export-configuration-body">
				<div id="modal-export-configuration-text">U kunt de firewall configuratie downloaden als iptables bestand. Deze kunt u inlezen in iptables. Wilt u de configuratie downloaden?</div> 
				<br />
				<br />
				<p>
					Zie <a href="<c:url value="/documentation#doc-11" />" target="_blank" >Downloaden en laden van een (evt. gewijzigde) iptables configuratie</a> voor meer uitleg.
				</p>
			</div>
			<div class="modal-footer">
				<form method="get" action="<c:url value="/my_configuration/export" />" class="form-inline" id="modal-export-configuration-form">
					<input type="hidden" name="file" value="iptables" />
					<button type="submit" id="modal-export-configuration-accept" class="btn btn-danger">
						<i class="icon-fixed-width icon-download"></i>
						<fmt:message key="label.yes" />
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