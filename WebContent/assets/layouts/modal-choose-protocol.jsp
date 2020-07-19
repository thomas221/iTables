<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="custom" uri="http://itables.ibyte.be/CustomJSPTags"%>

<fmt:setBundle basename="i18n.text" />

<!-- Modal -->
<div class="modal fade" id="modal-choose-protocol" tabindex="-1" role="dialog" aria-labelledby="modal-title" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h4 class="modal-title">
					<i class="icon-fixed-width icon-screenshot"></i>
					<fmt:message key="modal.choose_protocol.title" />
				</h4>
			</div>
			<div class="modal-body">				
				<p>Geef een nieuw protocol in voor regel <b><span id="rulenr"></span></b>.</p>
				

				<form class="form-horizontal" role="form" id="form-choose-protocol">
					<!-- Start hidden input field with rule number -->
					<input type="hidden" name="modal-choose-protocol-rulenr" id="modal-choose-protocol-rulenr" value="" />
					<!--  End hidden input field with rule number -->
					
					<!-- Start protocol field -->
					<p>Kies een van volgende protocollen:</p>
					<br />	
					<select class="form-control" id="modal-choose-protocol-option" name="modal-choose-protocol-option">
						<option value="-1">*</option>
						<option value="6">TCP</option>
						<option value="17">UDP</option>
						<option value="1">ICMP</option>
						<option value="-3">protocol getal</option>
					</select>
					<br />
					<p>
						U kunt daarbij ook kiezen voor het invoeren van een protocol getal. Er zijn 256 protocollen mogelijk voor een netwerkpakket. Elk getal tussen 0 en 255 komt overeen met een protocol. Hieronder kunt u een protocol getal invoeren.
					</p>
					<br />
					<input type="text" class="form-control" id="modal-choose-protocol-getal" name="modal-choose-protocol-getal" placeholder="Protocol getal" />
					<!-- End protocol field -->
					<br />
					<p>
						<a href="http://www.iana.org/assignments/protocol-numbers/protocol-numbers.xhtml" target="_blank">Hier</a> vindt u de offici&euml;le lijst van alle protocol getallen.
					</p>
					
					<p id="choose-protocol-error" class="hide text-danger">
					</p>
					
					<div class="modal-footer modal-footer-no-padding">
						<button type="button" class="btn btn-default" data-dismiss="modal">
							<i class="icon-fixed-width icon-remove"></i>
							Annuleren
						</button>
						<button type="button" class="btn btn-success submit-add-rule-form" id="submit-button-choose-protocol">
							<i class="icon-fixed-width icon-ok"></i>
							Wijzigen
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