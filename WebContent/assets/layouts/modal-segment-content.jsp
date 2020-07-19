<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="custom" uri="http://itables.ibyte.be/CustomJSPTags"%>

<fmt:setBundle basename="i18n.text" />

<!-- Modal -->
<div class="modal fade" id="modal-segment-content" tabindex="-1" role="dialog" aria-labelledby="modal-title" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="modal-segment-content-title">
					<i class="icon-fixed-width icon-search"></i>
					<fmt:message key="segment.content.title" />
					<span id="modal-segment-content-title-segment-number"></span>
					(<span id="modal-segment-content-title-compact"></span>)
				</h4>
			</div>
			<div class="modal-body" id="modal-segment-content-body">
			
				<span id="modal-segment-content-body-text-intro">
					
						Hier een opsomming van de inhoud van het segment.<br /><br />
					
				</span>
				
				<div id="modal-segment-content-body-div">
					
					<table cellpadding="0" cellspacing="0" border="0" class="table table-hover table-striped table-bordered"
						id="modal-segment-content-body-datatable">
						<thead>
							<tr>
								<th>Protocol</th>
								<th nowrap="nowrap">Bron IP</th>
								<th>Bronpoort</th>
								<th nowrap="nowrap">Doel IP</th>
								<th>Doelpoort</th>
								<th>Info</th>
							</tr>
						</thead>
					</table>
				</div>
				
				
				
				<span id="modal-segment-content-body-span">
					<fmt:message key="segment.content.too_big" />
				</span>
				
				
				<span id="modal-segment-content-body-text-compact">	
					In de compacte weergave staan regels met &rsquo;begin&rsquo; en regels met &rsquo;einde&rsquo;. <br />										
				</span>
				
				<p>
					Zie <a href="<c:url value="/documentation#doc-7" />" target="_blank" >Opsomming van een segment opvragen</a> voor uitleg.
				</p>
				
				

				
				
				<span id="modal-segment-content-body-error"></span>
			</div>
			<div class="modal-footer">
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