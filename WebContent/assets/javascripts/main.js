function closeEditorWarning(){
    return 'It looks like you have been editing something -- if you leave before submitting your changes will be lost.';
}

//window.onbeforeunload = closeEditorWarning;

$(document)
		.ready(
				function() {
					// Global variables
					var request;

					$(".has-popover").popover();
					
					location.hash && $(location.hash + '.collapse').collapse('show');
					
					$(".modal").draggable({ cursor: "crosshair" });

					$(".thumbnail-link").click(function() {
						var href = $(this).data("href");
						window.location = href;
					});
					
					$(".clear-form").on("click",function(){
						$('form').find('input[type=text]').val('');
						$("#match_protocol").val(-2);
						$("#match_result_div_warning").addClass("hide");
						$("#match_result_div_success").addClass("hide");
					});
					
					$("#select-default-policy").on("change",function(){
						var params = "action=edit_policy&select-default-policy="+$("#select-default-policy").val();
						$.ajax({
							type: "POST",
							url: "/my_configuration/rules;jsessionid="+$("body").data("session"),
							data: params,
							dataType: "json",
							error: function(xhr, status, error) {
								$("#default-policy-result").text("Onverwachte fout gebeurd bij wijzigen van default policy. Server reageert niet.");
								$("#default-policy-result").removeClass("hide");
							},
							success: function(data){
								$("#default-policy-result").addClass("hide");
							}
						});
						
					});
					
					$("#submit-button-choose-protocol").on("click",function(){
						//check of invoer correct is
						var protocolGetal = $("#modal-choose-protocol-getal").val();
						var correct = ($.isNumeric(protocolGetal) && protocolGetal >= 0 && protocolGetal <= 255 && !(protocolGetal==0 && protocolGetal!="0") && !(protocolGetal > 0 && protocolGetal.charAt(0) == '0')) || $("#modal-choose-protocol-option").val() == -1;
						if($("#modal-choose-protocol-option").val() == -3 && !correct){ //indien gekozen is om een protocol getal in te voeren moet veld met protocol getal correct zijn
							$("#choose-protocol-error").text("Protocol getal onjuist. Het protocol getal moet een getal zijn tussen 0 en 255.");
							$("#choose-protocol-error").removeClass("hide");
						}else{
							//invoer is correct
							var ruleNr = $("#modal-choose-protocol-rulenr").val();
							var tr = $("#"+ruleNr);
							var oldProtocol = tr.children(".protocol-cell a").attr("data-value");
							/*var oTable = $("#table-rules").dataTable();
							oTable.fnUpdate(protocolGetal,ruleNr-1,1);
							$("#choose-protocol-error").addClass("hide");
							$("#modal-choose-protocol").modal("hide");*/
							
							
							var params = "action=modal_choose_protocol&modal-choose-protocol-rulenr="+ruleNr+"&modal-choose-protocol-option="+$("#modal-choose-protocol-option").val()+"&modal-choose-protocol-getal="+$("#modal-choose-protocol-getal").val();
							$.ajax({
								type: "POST",
								url: "/my_configuration/rules;jsessionid="+$("body").data("session"),
								data: params,
								dataType: "json",
								error: function(xhr, status, error) {
									$("#choose-protocol-error").text("Fout bij verzenden van AJAX request.");
									$("#choose-protocol-error").removeClass("hide");
								},
								success: function(data){
									$("#choose-protocol-error").addClass("hide");
									$("#modal-choose-protocol").modal("hide");
									//update tabel
									
									$.each(data, function(key, value) {
										
										//pas cel met protocol aan
										var cellContent = "";
										if(value == -1){
											cellContent = "<a class=\"editable-fields editable-field-rule-protocol editable editable-click\" data-value=\"-1\" data-prepend=\"true\" data-title=\"Protocol bewerken\" data-name=\"protocol\" data-type=\"text\" data-pk=\""+ruleNr+"\" data-source=\"/my_configuration/rules?action=get_protocols\" data-url=\"/my_configuration/rules?action=edit\" href=\"#\">*</a>";							
										}else if(value == 1){
											cellContent = "<a class=\"editable-fields editable-field-rule-protocol editable editable-click\" data-value=\"1\" data-prepend=\"true\" data-title=\"Protocol bewerken\" data-name=\"protocol\" data-type=\"text\" data-pk=\""+ruleNr+"\" data-source=\"/my_configuration/rules?action=get_protocols\" data-url=\"/my_configuration/rules?action=edit\" href=\"#\">ICMP</a>";							
										}else if(value == 6){
											cellContent = "<a class=\"editable-fields editable-field-rule-protocol editable editable-click\" data-value=\"6\" data-prepend=\"true\" data-title=\"Protocol bewerken\" data-name=\"protocol\" data-type=\"text\" data-pk=\""+ruleNr+"\" data-source=\"/my_configuration/rules?action=get_protocols\" data-url=\"/my_configuration/rules?action=edit\" href=\"#\">TCP</a>";							
										}else if(value == 17){
											cellContent = "<a class=\"editable-fields editable-field-rule-protocol editable editable-click\" data-value=\"17\" data-prepend=\"true\" data-title=\"Protocol bewerken\" data-name=\"protocol\" data-type=\"text\" data-pk=\""+ruleNr+"\" data-source=\"/my_configuration/rules?action=get_protocols\" data-url=\"/my_configuration/rules?action=edit\" href=\"#\">UDP</a>";							
										}else{
											cellContent = "<a class=\"editable-fields editable-field-rule-protocol editable editable-click\" data-value=\""+value+"\" data-prepend=\"true\" data-title=\"Protocol bewerken\" data-name=\"protocol\" data-type=\"text\" data-pk=\""+ruleNr+"\" data-source=\"/my_configuration/rules?action=get_protocols\" data-url=\"/my_configuration/rules?action=edit\" href=\"#\">"+value+"</a>";							
										}
										
										
										tr.children(".protocol-cell").html(cellContent);
										
										
										//oTable.fnUpdate(cellContent,ruleNr-1,1,false);
																				
										if (value != "6" && value != "17")
							            {
											tr.children(".source-port-cell").html("");
											tr.children(".destination-port-cell").html("");
							            	//oTable.fnUpdate("",ruleNr-1,3,false);
							            	//oTable.fnUpdate("",ruleNr-1,5,false);
										//pas cellen met bronpoort en doelpoort aan als protocol TCP of UDP wordt
							            } else if((value == 6 || value == 17) && (oldProtocol != 6 && oldProtocol != 17)){
												var bronPoortContent = "<a class=\"editable-fields editable editable-click\" data-value=\"*\" data-title=\"Bronpoort bewerken\" data-name=\"source-port\" data-type=\"text\" data-pk=\""+ruleNr+"\" data-url=\"/my_configuration/rules?action=edit\" href=\"#\">*</a>";
												
												//oTable.fnUpdate(bronPoortContent,ruleNr-1,3,false);
												var doelPoortContent = "<a class=\"editable-fields editable editable-click\" data-value=\"*\" data-title=\"Doelpoort bewerken\" data-name=\"destination-port\" data-type=\"text\" data-pk=\""+ruleNr+"\" data-url=\"/my_configuration/rules?action=edit\" href=\"#\">*</a>";
												tr.children(".source-port-cell").html(bronPoortContent);
												tr.children(".destination-port-cell").html(doelPoortContent);
												//oTable.fnUpdate(doelPoortContent,ruleNr-1,5,false);
										}
										
									});
									
								}
							});	
						}
						
						
					});
					
					$("#modal-add-rule-protocol-select").on("change",function(){
						var chosen = $("#modal-add-rule-protocol-select").val();
						
						if(chosen == "6" || chosen == "17"){
							$("#modal-add-rule-source-port").css("visibility", "visible");
							$("#modal-add-rule-destination-port").css("visibility", "visible");
							$("#modal-add-rule-source-port-text").css("visibility", "visible");
							$("#modal-add-rule-destination-port-text").css("visibility", "visible");

							//$("#modal-add-rule-source-port").addClass("port-add-rule");
							//$("#modal-add-rule-destination-port").addClass("port-add-rule");
							$("#modal-add-rule-source-port").next().show();
							$("#modal-add-rule-destination-port").next().show();
						}else{
							$("#modal-add-rule-source-port").css("visibility", "hidden");
							$("#modal-add-rule-destination-port").css("visibility", "hidden");
							$("#modal-add-rule-source-port-text").css("visibility", "hidden");
							$("#modal-add-rule-destination-port-text").css("visibility", "hidden");
							
							$("#modal-add-rule-source-port").next().hide();
							//$("#modal-add-rule-source-port").removeClass("port-add-rule");
							//$("#modal-add-rule-destination-port").removeClass("port-add-rule");
							$("#modal-add-rule-destination-port").next().hide();
						}
						
						if(chosen == 6){
							$("#modal-add-rule-protocol-getal").next().hide();
							$("#modal-add-rule-protocol-getal").val("6");
							$("#modal-add-rule-protocol-getal").attr('disabled','disabled');
						}else if(chosen == 17){
							$("#modal-add-rule-protocol-getal").next().hide();
							$("#modal-add-rule-protocol-getal").val("17");
							$("#modal-add-rule-protocol-getal").attr('disabled','disabled');
						}else if(chosen == 1){
							$("#modal-add-rule-protocol-getal").next().hide();
							$("#modal-add-rule-protocol-getal").val("1");
							$("#modal-add-rule-protocol-getal").attr('disabled','disabled');
						}else if(chosen == -1){
							$("#modal-add-rule-protocol-getal").next().hide();
							$("#modal-add-rule-protocol-getal").removeAttr('placeholder');
							$("#modal-add-rule-protocol-getal").val("");
							$("#modal-add-rule-protocol-getal").attr('disabled','disabled');
						}else{
							$("#modal-add-rule-protocol-getal").next().show();
							$("#modal-add-rule-protocol-getal").removeAttr('disabled');
							$("#modal-add-rule-protocol-getal").attr('placeholder','protocol getal');
							$("#modal-add-rule-protocol-getal").val("");
						}
					});
					
					
					
					
					
					$("#table-rules").on("click","td.protocol-cell a",function() {
						//var option = $(this).find("option");
						//var protocol = option.filter(":selected").val();
						//if(protocol == "-3"){
							$(this).parent().children("div").hide();
							$("#choose-protocol-error").addClass("hide");
							var tr = $(this).parent().parent();
							var ruleNr = tr.find(".sorting_2").text();
							$("#modal-choose-protocol-rulenr").val(ruleNr);
							$("span#rulenr").text(ruleNr);
							var protocol = $(this).text();
							
							if(protocol == "TCP"){
								$("#modal-choose-protocol-getal").val("6");
								$("#modal-choose-protocol-getal").attr('disabled','disabled');
								$("#modal-choose-protocol-option").val("6");
							}else if(protocol == "UDP"){
								$("#modal-choose-protocol-getal").val("17");
								$("#modal-choose-protocol-getal").attr('disabled','disabled');
								$("#modal-choose-protocol-option").val("17");
							}else if(protocol == "ICMP"){
								$("#modal-choose-protocol-getal").val("1");
								$("#modal-choose-protocol-getal").attr('disabled','disabled');
								$("#modal-choose-protocol-option").val("1");
							}else if(protocol == "*"){
								$("#modal-choose-protocol-getal").val("");
								$("#modal-choose-protocol-getal").attr('disabled','disabled');
								$("#modal-choose-protocol-option").val("-1");
							}else{
								$("#modal-choose-protocol-getal").val(protocol);
								$("#modal-choose-protocol-getal").removeAttr('disabled');
								$("#modal-choose-protocol-option").val("-3");
							}
							
							$("#modal-choose-protocol").modal('show');
						//}
					});
					
					/*
					$("#table-rules").on("click","td.protocol-cell button.editable-submit",function() {	
						var tr = $(this).parent().parent().parent().parent().parent().parent().parent().parent().parent();
						var protocol = tr.find("td.protocol-cell option").filter(":selected").val();						
						if(protocol != "6" && protocol != "17"){							
							tr.find("td.source-port-cell a").hide();
							tr.find("td.destination-port-cell a").hide();
						}else{
							tr.find("td.source-port-cell a").show();
							tr.find("td.destination-port-cell a").show();
						}
					});*/
					
					$("#modal-choose-protocol-option").on("change",function(){
						$("#choose-protocol-error").addClass("hide");
						var selected = $(this).find("option").filter(":selected").val();
						if(selected == 6){							
							$("#modal-choose-protocol-getal").val("6");
							$("#modal-choose-protocol-getal").attr('disabled','disabled');
						}else if(selected == 17){
							$("#modal-choose-protocol-getal").val("17");
							$("#modal-choose-protocol-getal").attr('disabled','disabled');
						}else if(selected == 1){
							$("#modal-choose-protocol-getal").val("1");
							$("#modal-choose-protocol-getal").attr('disabled','disabled');
						}else if(selected == -1){
							$("#modal-choose-protocol-getal").removeAttr("placeholder");
							$("#modal-choose-protocol-getal").val("");
							$("#modal-choose-protocol-getal").attr('disabled','disabled');						
						}else{
							$("#modal-choose-protocol-getal").attr("placeholder","Protocol getal");
							$("#modal-choose-protocol-getal").removeAttr('disabled');
							$("#modal-choose-protocol-getal").val("");
						}

					});	
					$("#table-rules").on("click","td.source-ip-cell button.editable-submit",function() {	
						var field = $(this).parent().parent().parent().parent().parent().parent().parent().parent().parent().find("td.source-ip-cell input");
						var sourceIPString = field.val();
						if(sourceIPString != "*" && !sourceIPString.contains("/")){							
							field.val(sourceIPString+"/32");
						}
					});
					
					$("#table-rules").on("click","td.destination-ip-cell button.editable-submit",function() {	
						var field = $(this).parent().parent().parent().parent().parent().parent().parent().parent().parent().find("td.destination-ip-cell input");
						var destinationIPString = field.val();
						if(destinationIPString !="*" &&!destinationIPString.contains("/")){							
							field.val(destinationIPString+"/32");
						}
					});
					
					$('#table-rules').on("keypress","input", function(e){
						if (e.keyCode == '13'){
							e.preventDefault();
						}
					});
					
					
					
					$("#modal-change-rule-protocol-choose").on("change",function() {
						var protocol = $(this).val();
						if(protocol != 6 && protocol != 17){
							$("#modal-change-rule-source-port").css("visibility", "hidden");;
							$("#modal-change-rule-destination-port").css("visibility", "hidden");
							$("#modal-change-rule-source-port-text").css("visibility", "hidden");;
							$("#modal-change-rule-destination-port-text").css("visibility", "hidden");
							$("#modal-change-rule-source-port").next().hide();
							$("#modal-change-rule-destination-port").next().hide();
						}else{
							$("#modal-change-rule-source-port").css("visibility", "visible");;
							$("#modal-change-rule-destination-port").css("visibility", "visible");
							$("#modal-change-rule-source-port-text").css("visibility", "visible");;
							$("#modal-change-rule-destination-port-text").css("visibility", "visible");
							$("#modal-change-rule-source-port").next().show();
							$("#modal-change-rule-destination-port").next().show();
						}
						if(protocol != -3){
							$("#modal-change-rule-protocol-getal").next().hide();
						}else{
							$("#modal-change-rule-protocol-getal").next().show();
						}
						if(protocol == 1){
							$("#modal-change-rule-protocol-getal").removeAttr("placeholder");
							$("#modal-change-rule-protocol-getal").val("1");
							$("#modal-change-rule-protocol-getal").attr("disabled","disabled");
						}else if(protocol == 6){
							$("#modal-change-rule-protocol-getal").removeAttr("placeholder");
							$("#modal-change-rule-protocol-getal").val("6");
							$("#modal-change-rule-protocol-getal").attr("disabled","disabled");
						}else if(protocol == 17){
							$("#modal-change-rule-protocol-getal").removeAttr("placeholder");
							$("#modal-change-rule-protocol-getal").val("17");
							$("#modal-change-rule-protocol-getal").attr("disabled","disabled");
						}else if(protocol == -1){
							$("#modal-change-rule-protocol-getal").removeAttr("placeholder");
							$("#modal-change-rule-protocol-getal").val("");
							$("#modal-change-rule-protocol-getal").attr("disabled","disabled");
						}else{
							$("#modal-change-rule-protocol-getal").attr("placeholder","protocol getal");
							$("#modal-change-rule-protocol-getal").val("");
							$("#modal-change-rule-protocol-getal").removeAttr("disabled");
						}
					});
					
					

					$(".knop-modal-stop-session").click(function() {
						// Modal openen
						$('#restart_modal').modal('show');
					});

					$(".knop-regel-toevoegen").click(function() {
						// Formulier resetten
						$("#form-add-rule")[0].reset();

						// Modal openen
						$('#modal-add-rule').modal('show');
					});
					
					$("#modal-export-configuration-form").submit(function(e) {
						$("#modal-export-configuration").modal("hide");
						
					});
					
					
					
					 $(".knop-export-configuration").click(function(e){
						e.preventDefault();
						$("#modal-export-configuration").modal('show');
						/*var params = "portnoprotocol=yes";
						$.ajax({
							type: "GET",
							url: "/my_configuration/export;jsessionid="+$("body").data("session"),
							data: params,
							dataType: "json",
							error: function(xhr, status, error) {
								$("#modal-export-configuration-text").html("Fout bij verzenden van AJAX request.");
								//modal openen met AJAX foutmelding
								//verberg knop om door te gaan
								$("#modal-export-configuration-accept").hide();
								$("#modal-export-configuration").modal('show');
							},
							success: function(data){
								$.each(data, function(key, value) {														
									if(value == "yes"){
										//modal openen met waarschuwing
										//toon knop om door te gaan
										$("#modal-export-configuration-text").html("<p>U kunt de firewall configuratie downloaden als iptables bestand. Deze kunt u inlezen in iptables. </p><p><span class=\"text-danger\">Waarschuwing !</span><br />Deze configuratie bevat regels met een bronpoort of doelpoort maar geen protocol. In iptables is dit niet mogelijk.<br />Als u wenst door te gaan zullen er voor elk van deze regels 2 iptables regels gegenereerd worden:<br />1 regel met protocol tcp en 1 regels met protocol udp.</p><p>Wenst u door te gaan?</p>");
										$("#modal-export-configuration-accept").show();
										$("#modal-export-configuration").modal('show');
									}else{
										//Waarschuwing niet nodig
										$("#modal-export-configuration-text").html("U kunt de firewall configuratie downloaden als iptables bestand. Deze kunt u inlezen in iptables. Wilt u de configuratie downloaden?");
										$("#modal-export-configuration-accept").show();
										$("#modal-export-configuration").modal('show');
									}
									
								});
							}
						});		*/				
						
					});
					

					 $("#match_protocol").on("change",function(){
						var chosen =  $(this).val();
						if(chosen == -3){
							$("#match_protocol_getal").removeAttr("disabled");
							$("#match_protocol_getal").val("");
						}else if (chosen == -2){
							$("#match_protocol_getal").val("");
							$("#match_protocol_getal").attr("disabled","disabled");
						}else{
							$("#match_protocol_getal").val(""+chosen);
							$("#match_protocol_getal").attr("disabled","disabled");
						}
					 });
					
					$(".knop-match-pakket").click(function() {
						//waarden van netwerk pakket ophalen uit formulier
						
						var match_bron_IP1 = $("#match_bron_IP1").val();
						var match_bron_IP2 = $("#match_bron_IP2").val();
						var match_bron_IP3 = $("#match_bron_IP3").val();
						var match_bron_IP4 = $("#match_bron_IP4").val();
						var match_doel_IP1 = $("#match_doel_IP1").val();
						var match_doel_IP2 = $("#match_doel_IP2").val();
						var match_doel_IP3 = $("#match_doel_IP3").val();
						var match_doel_IP4 = $("#match_doel_IP4").val();
						var match_bronpoort = $("#match_bronpoort").val();
						var match_doelpoort = $("#match_doelpoort").val();
						
						var match_protocol = $("#match_protocol").val();
						
						var protocolCorrect = true;
						if(match_protocol == -3){
							match_protocol = $("#match_protocol_getal").val();
							//controleer of protocol waarde correct is
							var correct = $.isNumeric(match_protocol) && 0<=match_protocol && match_protocol<=255 && !(match_protocol == 0 && match_protocol != "0") && !(match_protocol > 0 && match_protocol.charAt(0)=='0');
							if(!correct){
								protocolCorrect = false;
							}
						}
						if(!protocolCorrect){
							$("#match_result_warning").html("Protocol getal heeft ongeldige waarde. Dit getal moet tussen 0 en 255 liggen.");							
							$("#match_result_div_warning").removeClass("hide");
							$("#match_result_div_success").addClass("hide");
						//controleer of bron IP waarde correct is	
						}else if(!(match_bron_IP1 == "" || ($.isNumeric(match_bron_IP1) && !(match_bron_IP1==0 && match_bron_IP1!="0") && !(match_bron_IP1 > 0 && match_bron_IP1.charAt(0) == '0') && match_bron_IP1 >= 0 && match_bron_IP1 <= 255 && match_bron_IP1.charAt(0) != "-"))){
							$("#match_result_warning").html("Eerste getal van bron IP heeft ongeldige waarde. Dit getal moet tussen 0 en 255 liggen.");							
							$("#match_result_div_warning").removeClass("hide");
							$("#match_result_div_success").addClass("hide");
						}
						else if(!(match_bron_IP2 == "" || ($.isNumeric(match_bron_IP2) && !(match_bron_IP2==0 && match_bron_IP2!="0") && !(match_bron_IP2 > 0 && match_bron_IP2.charAt(0) == '0') && match_bron_IP2 >= 0 && match_bron_IP2 <= 255 && match_bron_IP2.charAt(0) != "-"))){
							$("#match_result_warning").html("Tweede getal van bron IP heeft ongeldige waarde. Dit getal moet tussen 0 en 255 liggen.");							
							$("#match_result_div_warning").removeClass("hide");
							$("#match_result_div_success").addClass("hide");
						}
						else if(!(match_bron_IP3 == "" || ($.isNumeric(match_bron_IP3)  && !(match_bron_IP3==0 && match_bron_IP3!="0") && !(match_bron_IP3 > 0 && match_bron_IP3.charAt(0) == '0')&& match_bron_IP3 >= 0 && match_bron_IP3 <= 255 && match_bron_IP3.charAt(0) != "-"))){
							$("#match_result_warning").html("Derde getal van bron IP heeft ongeldige waarde. Dit getal moet tussen 0 en 255 liggen.");							
							$("#match_result_div_warning").removeClass("hide");
							$("#match_result_div_success").addClass("hide");
						}
						else if(!(match_bron_IP4 == "" || ($.isNumeric(match_bron_IP4) && !(match_bron_IP4==0 && match_bron_IP4!="0") && !(match_bron_IP4 > 0 && match_bron_IP4.charAt(0) == '0') && match_bron_IP4 >= 0 && match_bron_IP4 <= 255 && match_bron_IP4.charAt(0) != "-"))){
							$("#match_result_warning").html("Vierde getal van bron IP heeft ongeldige waarde. Dit getal moet tussen 0 en 255 liggen.");							
							$("#match_result_div_warning").removeClass("hide");
							$("#match_result_div_success").addClass("hide");
						}
						
						//controleer of doel IP waarde correct is
						else if(!(match_doel_IP1 == "" || ($.isNumeric(match_doel_IP1) && !(match_doel_IP1==0 && match_doel_IP1!="0") && !(match_doel_IP1 > 0 && match_doel_IP1.charAt(0) == '0') && match_doel_IP1 >= 0 && match_doel_IP1 <= 255 && match_doel_IP1.charAt(0) != "-"))){
							$("#match_result_warning").html("Eerste getal van doel IP heeft ongeldige waarde. Dit getal moet tussen 0 en 255 liggen.");							
							$("#match_result_div_warning").removeClass("hide");
							$("#match_result_div_success").addClass("hide");
						}
						else if(!(match_doel_IP2 == "" || ($.isNumeric(match_doel_IP2) && !(match_doel_IP2==0 && match_doel_IP2!="0") && !(match_doel_IP2 > 0 && match_doel_IP2.charAt(0) == '0') && match_doel_IP2 >= 0 && match_doel_IP2 <= 255 && match_doel_IP2.charAt(0) != "-"))){
							$("#match_result_warning").html("Tweede getal van doel IP heeft ongeldige waarde. Dit getal moet tussen 0 en 255 liggen.");							
							$("#match_result_div_warning").removeClass("hide");
							$("#match_result_div_success").addClass("hide");
						}
						else if(!(match_doel_IP3 == "" || ($.isNumeric(match_doel_IP3) && !(match_doel_IP3==0 && match_doel_IP3!="0") && !(match_doel_IP3 > 0 && match_doel_IP3.charAt(0) == '0') && match_doel_IP3 >= 0 && match_doel_IP3 <= 255 && match_doel_IP3.charAt(0) != "-"))){
							$("#match_result_warning").html("Derde getal van doel IP heeft ongeldige waarde. Dit getal moet tussen 0 en 255 liggen.");							
							$("#match_result_div_warning").removeClass("hide");
							$("#match_result_div_success").addClass("hide");
						}
						else if(!(match_doel_IP4 == "" || ($.isNumeric(match_doel_IP4) && !(match_doel_IP4==0 && match_doel_IP4!="0") && !(match_doel_IP4 > 0 && match_doel_IP4.charAt(0) == '0') && match_doel_IP4 >= 0 && match_doel_IP4 <= 255 && match_doel_IP4.charAt(0) != "-"))){
							$("#match_result_warning").html("Vierde getal van doel IP heeft ongeldige waarde. Dit getal moet tussen 0 en 255 liggen.");							
							$("#match_result_div_warning").removeClass("hide");
							$("#match_result_div_success").addClass("hide");
						}
						else if((match_bron_IP1 == "" && (match_bron_IP2 != "" || match_bron_IP3 != "" || match_bron_IP4 != "")) || (match_bron_IP2 == "" && (match_bron_IP1 != "" || match_bron_IP3 != "" || match_bron_IP4 != "")) || (match_bron_IP3 == "" && (match_bron_IP2 != "" || match_bron_IP1 != "" || match_bron_IP4 != "")) || (match_bron_IP4 == "" && (match_bron_IP2 != "" || match_bron_IP3 != "" || match_bron_IP1 != ""))){
							$("#match_result_warning").html("Vul a.u.b het bron IP adres volledig in.");							
							$("#match_result_div_warning").removeClass("hide");
							$("#match_result_div_success").addClass("hide");
						}
						else if((match_doel_IP1 == "" && (match_doel_IP2 != "" || match_doel_IP3 != "" || match_doel_IP4 != "")) || (match_doel_IP2 == "" && (match_doel_IP1 != "" || match_doel_IP3 != "" || match_doel_IP4 != "")) || (match_doel_IP3 == "" && (match_doel_IP2 != "" || match_doel_IP1 != "" || match_doel_IP4 != "")) || (match_doel_IP4 == "" && (match_doel_IP2 != "" || match_doel_IP3 != "" || match_doel_IP1 != ""))){
							$("#match_result_warning").html("Vul a.u.b het doel IP adres volledig in.");							
							$("#match_result_div_warning").removeClass("hide");
							$("#match_result_div_success").addClass("hide");
						}
						//controleer of bronpoort correct is ingevuld
						else if(!(match_bronpoort == "" || ($.isNumeric(match_bronpoort) && !(match_bronpoort==0 && match_bronpoort!="0") && !(match_bronpoort > 0 && match_bronpoort.charAt(0) == '0') && match_bronpoort >= 0 && match_bronpoort <= 65535))){
							$("#match_result_warning").html("Bronpoort heeft ongeldige waarde. Een poort is een getal tussen 0 en 65535.");										
							$("#match_result_div_warning").removeClass("hide");
							$("#match_result_div_success").addClass("hide");
						}
						//controleer of doelpoort correct is ingevuld
						else if(!(match_doelpoort == "" || ($.isNumeric(match_doelpoort) && !(match_doelpoort==0 && match_doelpoort!="0") && !(match_doelpoort > 0 && match_doelpoort.charAt(0) == '0') && match_doelpoort >= 0 && match_doelpoort <= 65535))){
							$("#match_result_warning").html("Doelpoort heeft ongeldige waarde. Een poort is een getal tussen 0 en 65535.");											
							$("#match_result_div_warning").removeClass("hide");
							$("#match_result_div_success").addClass("hide");
						}else{
							//invoer is correct
							//indien veld van bron IP leeggelaten is, dan krijgt deze waarde -2
							if(match_bron_IP1 == ""){
								match_bron_IP1 = "-2";
							}
							if(match_bron_IP2 == ""){
								match_bron_IP2 = "-2";
							}
							if(match_bron_IP3 == ""){
								match_bron_IP3 = "-2";
							}
							if(match_bron_IP4 == ""){
								match_bron_IP4 = "-2";
							}
							
							
							//indien veld van doel IP leeggelaten is, dan krijgt deze waarde -2
							if(match_doel_IP1 == ""){
								match_doel_IP1 = "-2";
							}
							if(match_doel_IP2 == ""){
								match_doel_IP2 = "-2";
							}
							if(match_doel_IP3 == ""){
								match_doel_IP3 = "-2";
							}
							if(match_doel_IP4 == ""){
								match_doel_IP4 = "-2";
							}
							
							//indien veld van bronpoort leeggelaten is, dan krijgt deze waarde -2
							if(match_bronpoort == ""){
								match_bronpoort = "-2";
							}
							
							//indien veld van doelpoort leeggelaten is, dan krijgt deze waarde -2
							if(match_doelpoort == ""){
								match_doelpoort = "-2";
							}
							
							//indien invoer correct - doe AJAX request
							var params = "action=match&match_protocol="+match_protocol+"&match_bronpoort="+match_bronpoort+"&match_doelpoort="+match_doelpoort+"&match_bron_IP1="+match_bron_IP1+"&match_bron_IP2="+match_bron_IP2+"&match_bron_IP3="+match_bron_IP3+"&match_bron_IP4="+match_bron_IP4+"&match_doel_IP1="+match_doel_IP1+"&match_doel_IP2="+match_doel_IP2+"&match_doel_IP3="+match_doel_IP3+"&match_doel_IP4="+match_doel_IP4;
							$.ajax({
								type: "POST",
								url: "/my_configuration/rules;jsessionid="+$("body").data("session"),
								data: params,
								dataType: "json",
								error: function(xhr, status, error) {
									$("#match_result_warning").html("Fout bij verzenden van AJAX request.");
									$("#match_result_div_warning").removeClass("hide");
									$("#match_result_div_success").addClass("hide");													
								},
								success: function(data){
									$.each(data, function(key, value) {														
										if(value == -1){
											$("#match_result_warning").html("Dit 'pakketje' heeft met geen enkele regel een match. <br />De actie van de default policy wordt daarom uitgevoerd: <b>"+$("#select-default-policy option:selected").html()+"</b>");															
											$("#match_result_div_warning").removeClass("hide");
											$("#match_result_div_success").addClass("hide");
										}else if(value == -2){
											$("#match_result_warning").html("Een van de ingevoerde velden heeft een ongeldige waarde. Een poort is een getal van 0 tot 65535. Een IP adres bestaat uit 4 getallen van 0 tot 255.");															
											$("#match_result_div_warning").removeClass("hide");
											$("#match_result_div_success").addClass("hide");
										}else{
											$("#match_result_success").html("Er is een match met regel " + value + ".");															
											$("#match_result_div_warning").addClass("hide");
											$("#match_result_div_success").removeClass("hide");
										}
									});
								}
							});
						}
					});
					
					function makeDeleteButtonWork() {
						var indexToRemove =  $("#modal-change-rule-remove").data("rule-index");
						
						var protocolToRemove = $("#modal-change-rule-remove").data("protocol");
						var sourceIPToRemove =  $("#modal-change-rule-remove").data("source-ip");
						var destinationIPToRemove =  $("#modal-change-rule-remove").data("destination-ip");
						var sourcePortToRemove =  $("#modal-change-rule-remove").data("source-port");
						var destinationPortToRemove =  $("#modal-change-rule-remove").data("destination-port");
						var actionToRemove =  $("#modal-change-rule-remove").data("action");
						//geef velden in modal voor verwijderen regel juiste waarden
						$("#modal-delete-rule-context-page").val("anomalies_page");
												
						$("#modal-delete-rule-index-p").html(indexToRemove);
						$("#modal-delete-rule-index").val(indexToRemove);

						$("#modal-delete-rule-protocol-p").html(protocolToRemove);
						$("#modal-delete-rule-protocol").val(protocolToRemove);

						$("#modal-delete-rule-source-ip-p").html(sourceIPToRemove);
						$("#modal-delete-rule-source-ip").val(sourceIPToRemove);
						
						$("#modal-delete-rule-destination-ip-p").html(destinationIPToRemove);
						$("#modal-delete-rule-destination-ip").val(destinationIPToRemove);
						
						
						
						if(protocolToRemove != 6 && protocolToRemove != 17 && protocolToRemove != "TCP" && protocolToRemove != "UDP"){
							
							$("#modal-delete-rule-source-port").parent().parent().hide();
							
							$("#modal-delete-rule-destination-port").parent().parent().hide();
							
						}else{
							
							$("#modal-delete-rule-source-port").parent().parent().show();
							
							$("#modal-delete-rule-destination-port").parent().parent().show();
							
							$("#modal-delete-rule-source-port-p").html(sourcePortToRemove);
							$("#modal-delete-rule-source-port").val(sourcePortToRemove);
	
							$("#modal-delete-rule-destination-port-p").html(destinationPortToRemove);
							$("#modal-delete-rule-destination-port").val(destinationPortToRemove);
						}

						$("#modal-delete-rule-action-p").html(actionToRemove);
						$("#modal-delete-rule-action").val(actionToRemove);
						
						$("#modal-delete-rule").modal("show");
					};
					
					function makeFieldsEditable(){
						// Editable fields instellen
						// $.fn.editable.defaults.mode = 'inline';
						$('.editable-fields').editable({
						    params: function(params) {
						        //originally params contain pk, name and value
						        params.rownr = $(this).parent().parent().attr("id");
						        return params;
						    }
						});

						$('.editable-field-rule-action').editable({
							params: function(params) {
						        //originally params contain pk, name and value
						        params.rownr = $(this).parent().parent().attr("id");
						        return params;
						    },
							display : function(value, response) {
								if (value == 0) {
									$(this).html("<span class='label label-danger'>drop</span>");
								} else {
									$(this).html("<span class='label label-success'>accept</span>");
								}
							}
						});
					}

					makeFieldsEditable();

					$(".knop-regel-wijzigen").click(function() {
						// Regel attributen ophalen
						var index = $(this).data("rule-index");
						var protocol = $(this).data("protocol");
						var sourceIP = $(this).data("source-ip");
						var sourcePort = $(this).data("source-port");
						var destinationIP = $(this).data("destination-ip");
						var destinationPort = $(this).data("destination-port");
						var action = $(this).data("action");

						// Regel nummer in begeleidende tekst zetten
						$("#modal-change-rule-index-span").html(index);

						// Regel attributen invullen in het formulier voor het
						// wijzigen
						$("#modal-change-rule-index").val(index);
						

						$("#modal-change-rule-source-ip").val(sourceIP);
						$("#modal-change-rule-source-ip").trigger('change');

						$("#modal-change-rule-destination-ip").val(destinationIP);
						$("#modal-change-rule-destination-ip").trigger('change');
						
						
						
						$("#modal-change-rule-action").val(action);

						// Knop voor verwijderen van regel juiste attributen geven
						$("#modal-change-rule-remove").data("rule-index", index);
						var protocolString = "";
						if (protocol == "-1") {
							protocolString = "*";
						} else if (protocol == "6") {
							protocolString = "TCP";
						} else if (protocol == "1"){
							protocolString = "ICMP";
						} else if (protocol =="17"){
							protocolString = "UDP";
						} else {
							protocolString = ""+protocol;
						}
						$("#modal-change-rule-remove").data("protocol", protocolString);
						$("#modal-change-rule-remove").data("source-ip", sourceIP);
						$("#modal-change-rule-remove").data("source-port", sourcePort);
						$("#modal-change-rule-remove").data("destination-ip", destinationIP);
						$("#modal-change-rule-remove").data("destination-port", destinationPort);
						$("#modal-change-rule-remove").data("context_page", "anomalies_page");
						var actionString = "";
						if (action == 0) {
							actionString = "drop";
						} else if (action == 1) {
							actionString = "accept";
						}
						$("#modal-change-rule-remove").data("action", actionString);
						
						//selecteer juiste waarde in drop-down list voor protocol
						if(protocol == 6 || protocol == 1 || protocol == 17 || protocol == -1){
							$("#modal-change-rule-protocol-choose").val(""+protocol);
						}else{
							$("#modal-change-rule-protocol-choose").val("-3");
						}
						//bepaal of poorten getoond moeten worden
						if(protocol != 6 && protocol != 17){
							$("#modal-change-rule-source-port").val("");
							
							$("#modal-change-rule-destination-port").val("");
							
							
							$("#modal-change-rule-source-port").css("visibility", "hidden");;
							$("#modal-change-rule-destination-port").css("visibility", "hidden");
							$("#modal-change-rule-source-port-text").css("visibility", "hidden");;
							$("#modal-change-rule-destination-port-text").css("visibility", "hidden");
														
							$("#modal-change-rule-source-port").next().hide();
							$("#modal-change-rule-destination-port").next().hide();
						}else{
							$("#modal-change-rule-source-port").val(sourcePort);
							$("#modal-change-rule-source-port").trigger("change");
							$("#modal-change-rule-destination-port").val(destinationPort);
							$("#modal-change-rule-destination-port").trigger("change");
							
							$("#modal-change-rule-source-port").css("visibility", "visible");;
							$("#modal-change-rule-destination-port").css("visibility", "visible");
							$("#modal-change-rule-source-port-text").css("visibility", "visible");;
							$("#modal-change-rule-destination-port-text").css("visibility", "visible");
														
							$("#modal-change-rule-source-port").next().show();
							$("#modal-change-rule-destination-port").next().show();
						}
						//geef invoer veld voor protocol getal juiste waarde
						if(protocol == 1){
							$("#modal-change-rule-protocol-getal").removeAttr("placeholder");
							$("#modal-change-rule-protocol-getal").val("1");
							$("#modal-change-rule-protocol-getal").trigger("change");
							$("#modal-change-rule-protocol-getal").attr("disabled","disabled");
						}else if(protocol == 6){
							$("#modal-change-rule-protocol-getal").removeAttr("placeholder");
							$("#modal-change-rule-protocol-getal").val("6");
							$("#modal-change-rule-protocol-getal").trigger("change");
							$("#modal-change-rule-protocol-getal").attr("disabled","disabled");
						}else if(protocol == 17){
							$("#modal-change-rule-protocol-getal").removeAttr("placeholder");
							$("#modal-change-rule-protocol-getal").val("17");
							$("#modal-change-rule-protocol-getal").trigger("change");
							$("#modal-change-rule-protocol-getal").attr("disabled","disabled");
						}else if(protocol == -1){
							$("#modal-change-rule-protocol-getal").removeAttr("placeholder");
							$("#modal-change-rule-protocol-getal").val("");
							$("#modal-change-rule-protocol-getal").attr("disabled","disabled");
						}else{
							$("#modal-change-rule-protocol-getal").attr("placeholder","protocol getal");
							$("#modal-change-rule-protocol-getal").val(""+protocol);
							$("#modal-change-rule-protocol-getal").trigger("change");
							$("#modal-change-rule-protocol-getal").removeAttr("disabled");
						}
						
						
						
						
						
						$("#modal-change-rule-remove").click(makeDeleteButtonWork);
						
						
						
						// Modal openen
						$('#modal-change-rule').modal('show');
						
						
					});
					
					

					// Functie die de firewallregels ophaalt via JSON en aan de tabel
					// toevoegt
					$('#table-rules').dataTable({
						"bProcessing" : true,
						"bFilter" : false,
						"bAutoWidth": false,
						"oLanguage" : {
							"sProcessing" : "Bezig met verwerking...",
							"sLengthMenu" : "Toon _MENU_ firewallregels per pagina",
							"sZeroRecords" : "De chain bevat geen firewallregels",
							"sInfo" : "_START_ tot _END_ van _TOTAL_ firewallregels",
							"sInfoEmpty" : "0 tot 0 van _TOTAL_ firewallregels",
							"sSearch" : "Zoeken:",
							"sEmptyTable" : "Geen firewallregels aanwezig in de chain",
							"sInfoFiltered" : "Gefilterd uit _MAX_ firewallregels",
							"sInfoThousands" : ".",
							"sLoadingRecords" : "Even geduld aub...",
							"sInfoPostFix" : "",
							"oPaginate" : {
								"sFirst" : "Eerste",
								"sLast" : "Laatste",
								"sNext" : "",
								"sPrevious" : ""
							}
						},
						 "fnRowCallback": function( nRow, aData, iDisplayIndex ) {
							 	var protocol = $("td:eq(1)", nRow).children("a").attr("data-value");
					            if (protocol != "6" && protocol != "17")
					            {
					                $('td:eq(3)', nRow).children("a").hide();
					                $('td:eq(5)', nRow).children("a").hide();
					            }else{
					            	$('td:eq(3)', nRow).children("a").show();
					                $('td:eq(5)', nRow).children("a").show();
					            }
					            if (protocol == 6){
					            	$('td:eq(1)', nRow).children("a").text("TCP");
					            }else if (protocol == 17){
					            	$('td:eq(1)', nRow).children("a").text("UDP");
					            }else if (protocol == 1){
					            	$('td:eq(1)', nRow).children("a").text("ICMP");
					            }else if (protocol == -1){
					            	$('td:eq(1)', nRow).children("a").text("*");
					            }
					            var id = aData[0];
					            $(nRow).attr("id",id);
					            $(nRow).children(".protocol-cell").children("a").attr("data-pk",id);
					            $(nRow).children(".source-ip-cell").children("a").attr("data-pk",id);
					            $(nRow).children(".source-port-cell").children("a").attr("data-pk",id);
					            $(nRow).children(".destination-ip-cell").children("a").attr("data-pk",id);
					            $(nRow).children(".destination-port-cell").children("a").attr("data-pk",id);
					            $(nRow).children(".action-cell").children("a").attr("data-pk",id);
					            $(nRow).children(".button-show-modal-delete-rule").attr("data-rule-index",id);
					            
					            makeFieldsEditable();
					            					            
					            
					            return nRow;
					        },
						"aoColumnDefs" : [ {
							'bSortable' : false,
							'aTargets' : [ 7 ]
						} ]
					}).rowReordering({
						sURL : "/my_configuration/rules;jsessionid="+$("body").data("session")+"?action=edit_order",
						sRequestType : "POST"
						/*callback: function(){
							$("#table-rules tr").each(function(id){
								$(this).attr("id",id);
					            $(this).children(".protocol-cell").children("a").attr("data-pk",id);
					            $(this).children(".source-ip-cell").children("a").attr("data-pk",id);
					            $(this).children(".source-port-cell").children("a").attr("data-pk",id);
					            $(this).children(".destination-ip-cell").children("a").attr("data-pk",id);
					            $(this).children(".destination-port-cell").children("a").attr("data-pk",id);
					            $(this).children(".action-cell").children("a").attr("data-pk",id);
					            $(this).children(".button-show-modal-delete-rule").attr("data-rule-index",id);
							});
							makeFieldsEditable();
						}*/
					});

					// Functie die de logs ophaalt via JSON en aan de tabel
					// toevoegt
					$('#table-logs').dataTable({
						"bProcessing" : true,
						"bFilter" : false,
						"bSort" : false,
						"oLanguage" : {
							"sProcessing" : "Bezig met verwerking...",
							"sLengthMenu" : "Toon _MENU_ logs per pagina",
							"sZeroRecords" : "Het logbestand bevat geen items",
							"sInfo" : "_START_ tot _END_ van _TOTAL_ logs",
							"sInfoEmpty" : "0 tot 0 van _TOTAL_ logs",
							"sSearch" : "Zoeken:",
							"sEmptyTable" : "Geen logs aanwezig in het logbestand",
							"sInfoFiltered" : "Gefilterd uit _MAX_ logs",
							"sInfoThousands" : ".",
							"sLoadingRecords" : "Even geduld aub...",
							"sInfoPostFix" : "",
							"oPaginate" : {
								"sFirst" : "Eerste",
								"sLast" : "Laatste",
								"sNext" : "",
								"sPrevious" : ""
							}
						},
						"aoColumnDefs" : [ {
							'bSortable' : false,
							'aTargets' : [ 3 ]
						} ]
					});
					
					
					
					function make_delete_rule_icon_work (){
						// Regel attributen ophalen
						var index = $(this).parent().prev().prev().prev().prev().prev().prev().prev().text();
						var protocol = $(this).parent().prev().prev().prev().prev().prev().prev().children("a").text();
						var sourceIP = $(this).parent().prev().prev().prev().prev().prev().children("a").text();
						var sourcePort = $(this).parent().prev().prev().prev().prev().children("a").text();
						var destinationIP = $(this).parent().prev().prev().prev().children("a").text();
						var destinationPort = $(this).parent().prev().prev().children("a").text();
						var action = $(this).parent().prev().children("a").text();

						// Regel attributen invullen in het formulier voor het
						// verwijderen
						
						$("#modal-delete-rule-context-page").val("rules_page");
						

						$("#modal-delete-rule-index-p").html(index);
						$("#modal-delete-rule-index").val(index);

						$("#modal-delete-rule-protocol-p").html(protocol);
						$("#modal-delete-rule-protocol").val(protocol);

						$("#modal-delete-rule-source-ip-p").html(sourceIP);
						$("#modal-delete-rule-source-ip").val(sourceIP);

						

						$("#modal-delete-rule-destination-ip-p").html(destinationIP);
						$("#modal-delete-rule-destination-ip").val(destinationIP);
						
						if(protocol != 6 && protocol != 17 && protocol != "TCP" && protocol != "UDP"){
							
							$("#modal-delete-rule-source-port").parent().parent().hide();
	
							$("#modal-delete-rule-destination-port").parent().parent().hide();
							
						}else{
							
							$("#modal-delete-rule-source-port").parent().parent().show();
							
							$("#modal-delete-rule-destination-port").parent().parent().show();
							
							$("#modal-delete-rule-source-port-p").html(sourcePort);
							$("#modal-delete-rule-source-port").val(sourcePort);
	
							$("#modal-delete-rule-destination-port-p").html(destinationPort);
							$("#modal-delete-rule-destination-port").val(destinationPort);
						}

						$("#modal-delete-rule-action-p").html(action);
						$("#modal-delete-rule-action").val(action);

						// Modal openen
						$('#modal-delete-rule').modal('show');
					}
					
					$('#table-rules_length select').change(function(){
						makeFieldsEditable();
						
						$(".button-show-modal-delete-rule").click(
							make_delete_rule_icon_work);
							
					});	
					
					$('#table-rules-div').click(function(){
						makeFieldsEditable();
						
						$(".button-show-modal-delete-rule").click(
								make_delete_rule_icon_work);
						
					});
									
					$(".button-show-modal-delete-rule").click(
							make_delete_rule_icon_work);
					
					$("#cancel-add-rule").on("click",function(){
						$('.alert-success').hide();
					});

					/*$(".submit-add-rule-form-ajax").click(function() {
						// abort any pending request
						if (request) {
							request.abort();
						}

						var $form = $("#form-add-rule");
						var $inputs = $form.find("input, select");
						var serializedData = $form.serialize();

						// let's disable the inputs for the duration of the ajax
						// request
						$inputs.prop("disabled", true);

						// fire off the request
						request = $.ajax({
							url : "/my_configuration/rules;jsessionid="+$("body").data("session")+"?action=add",
							type : "post",
							data : serializedData
						});

						// callback handler that will be called on success
						request.success(function(response, textStatus, jqXHR) {
							$(".flash-success-p").html(response);
							var $div = $('.flash-success-div');
							$div.show();
						});

						// callback handler that will be called on failure
						request.fail(function(jqXHR, textStatus, errorThrown) {
							$(".flash-danger-p").html(jqXHR.responseText);
							var $div = $('.flash-danger-div');
							$div.show();
						});

						// callback handler that will be called regardless
						// if the request failed or succeeded
						request.always(function() {
							$inputs.prop("disabled", false);
						});

						$('#modal-add-rule').modal('hide');
					});*/

					$(".submit-delete-rule-form").click(function() {
						$("#form-delete-rule").submit();
					});

					/*$(".submit-delete-rule-form-ajax").click(function() {
						// abort any pending request
						if (request) {
							request.abort();
						}

						var $form = $("#form-delete-rule");
						var serializedData = $form.serialize();

						request = $.ajax({
							url : "/my_configuration/rules;jsessionid="+$("body").data("session")+"?action=delete",
							type : "post",
							data : serializedData
						});

						request.success(function(response, textStatus, jqXHR) {
							// Status bericht tonen
							$(".flash-success-p").html(response);
							var $div = $('.flash-success-div');
							$div.show();

							// Datatables opnieuw laden
						});

						// callback handler that will be called on failure
						request.fail(function(jqXHR, textStatus, errorThrown) {
							$(".flash-danger-p").html(jqXHR.responseText);
							var $div = $('.flash-danger-div');
							$div.show();
						});

						$('#modal-delete-rule').modal('hide');
					});*/

					$('.has-tooltip').tooltip();

					$('.toggle-popover').popover();

					$("div.flash-danger-div > button.close").click(function(e) {
						$('div.flash-danger-div').hide();
					});

					$("div.flash-success-div > button.close").click(function(e) {
						$('div.flash-success-div').hide();
					});

					$("div.modal-add-rule-alert > button.close").click(function(e) {
						$('div.modal-add-rule-alert').hide();
					});
					
					

					var options = {
						delay : 100,
						silentSubmit : false,
						disableSubmitBtn : false
					};

					var metrics = [
					        [
					         		'#modal-add-rule-protocol-getal',
					         		(function(x) {
					         			if($("#modal-add-rule-protocol-select").val() != -1){
					         				return $.isNumeric(x) && x<=255 && x>=0 && !(x==0 && x!="0") && !(x>0 && x.charAt(0)=='0');
					         			}else{
					         				return true;
					         			}
					         		}),
					         			'Het protocol getal moet een getal zijn van 0 tot 255.'
					         ],
					       /* [
					         	   '#modal-add-rule-protocol-select',
					         	   (function() {
					         		   $('.port-add-rule').trigger('change');
					         		   return true;
					         	   }), ''
					         ],*/
					        [
					               '.port-add-rule',
					               (function(x) {

					            	   var protocol = $("#modal-add-rule-protocol-getal").val();

					            	   var no_ports = protocol != "6" && protocol != "17"; //geen poorten indien protocol niet TCP of UDP
					            	   if(!no_ports){
						            	   if(x=='*'){
												return true;
											}else{
												var parts = x.split(':');
												if(parts.length == 1){
													var lowerBound = parts[0];
													return ($.isNumeric(lowerBound) && lowerBound >= 0 && lowerBound <= 65535 && !(lowerBound==0 && lowerBound!="0") && !(lowerBound > 0 && lowerBound.charAt(0) == '0') && lowerBound.charAt(0) != '-');
													
												}else if(parts.length == 2){
													var lowerBound = parts[0];
													var lowerBoundInt = parseInt(lowerBound);
													var upperBound = parts[1];
													var upperBoundInt = parseInt(upperBound);
													var lowerBoundCorrect = ($.isNumeric(lowerBound) && lowerBoundInt >= 0 && lowerBoundInt <= 65535 && !(lowerBoundInt==0 && lowerBound!="0") && !(lowerBoundInt > 0 && lowerBound.charAt(0) == '0') && lowerBound.charAt(0) != '-');
													var upperBoundCorrect = ($.isNumeric(upperBound) && upperBoundInt >= 0 && upperBoundInt <= 65535 && !(upperBoundInt==0 && upperBoundInt!="0") && !(upperBoundInt > 0 && upperBound.charAt(0) == '0') && upperBound.charAt(0) != '-');													
													return (lowerBoundCorrect && upperBoundCorrect && lowerBoundInt <= upperBoundInt);
												}else{
													return false;
												}
											}
										}else{
											return true;
										}
					               }), 'Ongeldig poort-formaat aangetroffen. Een geldige poort is bijvoorbeeld poort \'80\' of \'22:30\', of \'*\' voor alle poorten. Geldig formaat: interval van poorten als \'x:y\' met 0 &lt;= x &lt;= 65535 en 0 &lt;= y &lt;= 65535 en x &lt;= y of een enkele poort als \'x\' met 0 &lt;= x &lt;= 65535 of alle poorten als \'*\'.' 
					         ],
					         [
				         		'#modal-change-rule-protocol-getal',
				         		(function(x) {
				         			if($("#modal-change-rule-protocol-choose").val() != -1){
				         				return $.isNumeric(x) && x<=255 && x>=0 && !(x==0 && x!="0") && !(x>0 && x.charAt(0)=='0');
					         		}else{
					         			return true;
					         		}
				         		}),
				         			'Het protocol getal moet een getal zijn van 0 tot 255.'
				         	],
					        /* [
				         	   '#modal-change-rule-protocol-choose',
				         	   (function() {
				         		   $('.port-change-rule').trigger('change');
				         		   return true;
				         	   }), ''
				             ],*/
				             [
				               '.port-change-rule',
				               (function(x) {
				            	   var protocol = $("#modal-change-rule-protocol-getal").val();
				            	   var no_ports = protocol != "6" && protocol != "17"; //geen poorten indien protocol niet TCP of UDP
				            	   if(!no_ports){
					            	   if(x=='*'){
											return true;
										}else{
											var parts = x.split(':');
											if(parts.length == 1){
												var lowerBound = parts[0];
												return ($.isNumeric(lowerBound) && lowerBound >= 0 && lowerBound <= 65535 && !(lowerBound > 0 && lowerBound.charAt(0) == '0') && lowerBound.charAt(0) != '-');
												
											}else if(parts.length == 2){
												var lowerBound = parts[0];
												var upperBound = parts[1];
												var lowerBoundCorrect = ($.isNumeric(lowerBound) && lowerBound >= 0 && lowerBound <= 65535  && !(lowerBound==0 && lowerBound!="0") && !(lowerBound > 0 && lowerBound.charAt(0) == '0') && lowerBound.charAt(0) != '-');
												var upperBoundCorrect = ($.isNumeric(upperBound) && upperBound >= 0 && upperBound <= 65535  && !(upperBound==0 && upperBound!="0") && !(upperBound > 0 && upperBound.charAt(0) == '0') && upperBound.charAt(0) != '-');
												return (lowerBoundCorrect && upperBoundCorrect && parseInt(lowerBound) <= parseInt(upperBound));
											}else{
												return false;
											}
										}
									}else{
										return true;
									}
				               }), 'Ongeldig poort-formaat aangetroffen. Een geldige poort is bijvoorbeeld poort \'80\' of \'22:30\', of \'*\' voor alle poorten. Geldig formaat: interval van poorten als \'x:y\' met 0 &lt;= x &lt;= 65535 en 0 &lt;= y &lt;= 65535 en x &lt;= y of een enkele poort als \'x\' met 0 &lt;= x &lt;= 65535 of alle poorten als \'*\'.' 
				         ],
							[
									'.input-port',
									(function(x) {
										//if(modal_add_rule_protocol_is_any == false){
											
											if(x=='*'){
												return true;
											}else{
												var parts = x.split(':');
												if(parts.length == 1){
													var lowerBound = parts[0];
													return ($.isNumeric(lowerBound) && lowerBound >= 0 && lowerBound <= 65535 && !(lowerBound > 0 && lowerBound.charAt(0) == '0') && lowerBound.charAt(0) != '-');
													
												}else if(parts.length == 2){
													var lowerBound = parts[0];
													var upperBound = parts[1];
													var lowerBoundCorrect = ($.isNumeric(lowerBound) && lowerBound >= 0 && lowerBound <= 65535  && !(lowerBound==0 && lowerBound!="0") && !(lowerBound > 0 && lowerBound.charAt(0) == '0') && lowerBound.charAt(0) != '-');
													var upperBoundCorrect = ($.isNumeric(upperBound) && upperBound >= 0 && upperBound <= 65535 && !(upperBound==0 && upperBound!="0") && !(upperBound > 0 && upperBound.charAt(0) == '0') && upperBound.charAt(0) != '-');
													return (lowerBoundCorrect && upperBoundCorrect && parseInt(lowerBound) <= parseInt(upperBound));
												}else{
													return false;
												}
											}
										//}
									}), 'Ongeldig poort-formaat aangetroffen. Een geldige poort is bijvoorbeeld poort \'80\' of \'22:30\', of \'*\' voor alle poorten. Geldig formaat: interval van poorten als \'x:y\' met 0 &lt;= x &lt;= 65535 en 0 &lt;= y &lt;= 65535 en x &lt; y of een enkele poort als \'x\' met 0 &lt;= x &lt;= 65535 of alle poorten als \'*\'.' 
							],
							[ 
							  		'.input-ip',
							  		(function(x) {
							  			if(x=='*'){ 
							  				return true;							  			
							  			}else{
								  			var parts = x.split('/');
								  			if(parts.length == 2){ //er is een masker opgegeven
									  			var address = parts[0];
									  			var mask = parts[1];
									  			var addressParts = address.split('.');
									  			
									  			var IP1Correct = $.isNumeric(addressParts[0]) && 0 <= addressParts[0] && addressParts[0] <= 255 && addressParts[0].charAt(0) != "-" && !(addressParts[0] > 0 && addressParts[0].charAt(0) == '0') && !(addressParts[0]==0 && addressParts[0]!="0");
									  			var IP2Correct = $.isNumeric(addressParts[1]) && 0 <= addressParts[1] && addressParts[1] <= 255 && addressParts[1].charAt(0) != "-" && !(addressParts[1] > 0 && addressParts[1].charAt(0) == '0') && !(addressParts[1]==0 && addressParts[1]!="0");
									  			var IP3Correct = $.isNumeric(addressParts[2]) && 0 <= addressParts[2] && addressParts[2] <= 255 && addressParts[2].charAt(0) != "-" && !(addressParts[2] > 0 && addressParts[2].charAt(0) == '0') && !(addressParts[2]==0 && addressParts[2]!="0");
									  			var IP4Correct = $.isNumeric(addressParts[3]) && 0 <= addressParts[3] && addressParts[3] <= 255 && addressParts[3].charAt(0) != "-" && !(addressParts[3] > 0 && addressParts[3].charAt(0) == '0') && !(addressParts[3]==0 && addressParts[3]!="0");
										
									  			var addressCorrect = (addressParts.length == 4 && IP1Correct && IP2Correct && IP3Correct && IP4Correct);
									  			
									  			var maskCorrect = ($.isNumeric(mask) && 0 <= mask && mask <= 32 && !(mask > 0 && mask.charAt(0) == '0') && mask.charAt(0) != '-' && !(mask==0 && mask!="0"));
									  			var correct = addressCorrect && maskCorrect;
									  			return correct;	
								  			}else if(parts.length == 1){ //er is geen masker opgegeven - equivalent met masker 32
								  				var address = parts[0];
									  			var addressParts = address.split('.');
									  			
									  			var IP1Correct = $.isNumeric(addressParts[0]) && 0 <= addressParts[0] && addressParts[0] <= 255 && addressParts[0].charAt(0) != "-" && !(addressParts[0] > 0 && addressParts[0].charAt(0) == '0') && !(addressParts[0]==0 && addressParts[0]!="0");
									  			var IP2Correct = $.isNumeric(addressParts[1]) && 0 <= addressParts[1] && addressParts[1] <= 255 && addressParts[1].charAt(0) != "-" && !(addressParts[1] > 0 && addressParts[1].charAt(0) == '0') && !(addressParts[1]==0 && addressParts[1]!="0");
									  			var IP3Correct = $.isNumeric(addressParts[2]) && 0 <= addressParts[2] && addressParts[2] <= 255 && addressParts[2].charAt(0) != "-" && !(addressParts[2] > 0 && addressParts[2].charAt(0) == '0') && !(addressParts[2]==0 && addressParts[2]!="0");
									  			var IP4Correct = $.isNumeric(addressParts[3]) && 0 <= addressParts[3] && addressParts[3] <= 255 && addressParts[3].charAt(0) != "-" && !(addressParts[3] > 0 && addressParts[3].charAt(0) == '0') && !(addressParts[3]==0 && addressParts[3]!="0");
									  			
									  			var addressCorrect = (addressParts.length == 4 && IP1Correct && IP2Correct && IP3Correct && IP4Correct);									  			
									  			return addressCorrect;	
								  			}else{
								  				return false;
								  			}
							  			}
							  		}), 'Ongeldig IP-formaat aangetroffen. Een geldige invoer is bijvoorbeeld \'192.168.0.0/16\', of \'*\' voor alle IP adressen. Geldig formaat: \'x.x.x.x/y\' met 0 &lt;= x &lt;= 255 en 0 &lt;= y &lt;= 32 of \'*\' indien alle IP-adressen toegelaten zijn. Een IP-adres van de vorm x.x.x.x is ook toegelaten.' 
							] 
					];

					var submitFormaddRule = function(event, data) {

					};

					var submitFormchangeRule = function(event, data) {

					};

					if ($("#form-add-rule").length > 0) {
						$("#form-add-rule").nod(metrics, options);
						$('#form-add-rule').on('silentSubmit', submitFormaddRule);
					}

					if ($("#form-change-rule").length > 0) {
						$("#form-change-rule").nod(metrics, options);
						$('#form-change-rule').on('silentSubmit', submitFormchangeRule);
					}
					
					$("#modal-add-rule-protocol-getal").on("change",function(){
						var protocolGetal = $("#modal-add-rule-protocol-getal").val();
						if(protocolGetal == "6" || protocolGetal == "17"){
							$("#modal-add-rule-source-port").css("visibility", "visible");
							$("#modal-add-rule-destination-port").css("visibility", "visible");
							$("#modal-add-rule-source-port-text").css("visibility", "visible");
							$("#modal-add-rule-destination-port-text").css("visibility", "visible");
							
							$("#modal-add-rule-source-port").next().show();
							$("#modal-add-rule-destination-port").next().show();
						}else{
							$("#modal-add-rule-source-port").css("visibility", "hidden");
							$("#modal-add-rule-destination-port").css("visibility", "hidden");
							$("#modal-add-rule-source-port-text").css("visibility", "hidden");
							$("#modal-add-rule-destination-port-text").css("visibility", "hidden");
							
							$("#modal-add-rule-source-port").next().hide();
							$("#modal-add-rule-destination-port").next().hide();
						}
					});
					
					$("#modal-change-rule-protocol-getal").on("change",function(){
						var protocolGetal = $("#modal-change-rule-protocol-getal").val();
						if(protocolGetal == "6" || protocolGetal == "17"){
							$("#modal-change-rule-source-port").css("visibility", "visible");;
							$("#modal-change-rule-destination-port").css("visibility", "visible");
							$("#modal-change-rule-source-port-text").css("visibility", "visible");;
							$("#modal-change-rule-destination-port-text").css("visibility", "visible");
							
							
							$("#modal-change-rule-source-port").next().show();
							$("#modal-change-rule-destination-port").next().show();
							$("#form-change-rule").nod(metrics, options);
						}else{
							$("#modal-change-rule-source-port").css("visibility", "hidden");;
							$("#modal-change-rule-destination-port").css("visibility", "hidden");
							$("#modal-change-rule-source-port-text").css("visibility", "hidden");;
							$("#modal-change-rule-destination-port-text").css("visibility", "hidden");
														
							$("#modal-change-rule-source-port").next().hide();
							$("#modal-change-rule-destination-port").next().hide();
						}
					});

					$(".segment-content-link").click(function() {
						if (request) {
							request.abort();
						}

						var segmentNumber = $(this).data("segment-number");
						var compact = $(this).data("compact");

						request = $.ajax({
							url : "/my_configuration/segment_content;jsessionid="+$("body").data("session")+"?segment_number=" + segmentNumber + "&compact=" + compact,
							dataType : 'json',
							type : "get"
						});

						request.success(function(response, textStatus, jqXHR) {
							$("#modal-segment-content-title-segment-number").html(segmentNumber);

							if (response.compact) {
								$("#modal-segment-content-title-compact").html("compacte weergave");
								$("#modal-segment-content-body-text-compact").show();							
							} else {
								$("#modal-segment-content-body-text-compact").hide();
								$("#modal-segment-content-title-compact").html("uitgebreide weergave");
							}

							if (response.toonbaar) {
								$('#modal-segment-content-body-datatable').dataTable({
									"bSort" : false,
									"bFilter" : false,
									"bLengthChange" : false,
									"bRetrieve" : false,
									"bDestroy" : true,
									"oLanguage" : {
										"sProcessing" : "Bezig met verwerking...",
										"sLengthMenu" : "Toon _MENU_ regels per pagina",
										"sZeroRecords" : "Het segment bevat geen regels",
										"sInfo" : "_START_ tot _END_ van _TOTAL_ regels",
										"sInfoEmpty" : "0 tot 0 van _TOTAL_ regels",
										"sSearch" : "Zoeken:",
										"sEmptyTable" : "Geen inhoud aanwezig in het segment",
										"sInfoFiltered" : "Gefilterd uit _MAX_ regels",
										"sInfoThousands" : ".",
										"sLoadingRecords" : "Even geduld aub...",
										"sInfoPostFix" : "",
										"oPaginate" : {
											"sFirst" : "Eerste",
											"sLast" : "Laatste",
											"sNext" : "",
											"sPrevious" : ""
										}
									},
									"aaData" : response.aaData
								});

								$("#modal-segment-content-body-error").hide();
								$("#modal-segment-content-body-span").hide();
								$("#modal-segment-content-body-text-intro").show();
								$("#modal-segment-content-body-div").show();
							} else {
								$("#modal-segment-content-body-text-intro").hide();
								$("#modal-segment-content-body-error").hide();
								$("#modal-segment-content-body-div").hide();
								$("#modal-segment-content-body-span").show();
							}
						});

						request.fail(function(jqXHR, textStatus, errorThrown) {
							$("#modal-segment-content-title-segment-number").html(segmentNumber);
							$("#modal-segment-content-title-compact").html("foutmelding");

							$("#modal-segment-content-body-div").hide();
							$("#modal-segment-content-body-span").hide();
							$('#modal-segment-content-body-error').html(jqXHR.responseText);
							$("#modal-segment-content-body-error").show();
						});

						request.always(function() {
							$('#modal-segment-content').modal('show');
						});
					});

					$(".button-show-redundant-rules").click(function() {
						if (request) {
							request.abort();
						}
						

						request = $.ajax({
							url : "/my_configuration/show_redundancy;jsessionid="+$("body").data("session"),
							dataType : 'json',
							type : "get"
						});

						// callback handler that will be called on success
						request.success(function(response, textStatus, jqXHR) {
							if (response.iTotalRecords > 0) {
								$('#modal-redundant-rules-body-datatable').dataTable({
									"bSort" : false,
									"bFilter" : false,
									"bLengthChange" : false,
									"bRetrieve" : false,
									"bDestroy" : true,
									"oLanguage" : {
										"sProcessing" : "Bezig met verwerking...",
										"sLengthMenu" : "Toon _MENU_ firewallregels per pagina",
										"sZeroRecords" : "Geen redundante firewallregels",
										"sInfo" : "_START_ tot _END_ van _TOTAL_ firewallregels",
										"sInfoEmpty" : "0 tot 0 van _TOTAL_ firewallregels",
										"sSearch" : "Zoeken:",
										"sEmptyTable" : "Huidige chain bevat geen redundanties",
										"sInfoFiltered" : "Gefilterd uit _MAX_ firewallregels",
										"sInfoThousands" : ".",
										"sLoadingRecords" : "Even geduld aub...",
										"sInfoPostFix" : "",
										"oPaginate" : {
											"sFirst" : "Eerste",
											"sLast" : "Laatste",
											"sNext" : "",
											"sPrevious" : ""
										}
									},
									"aaData" : response.aaData
								});

								$("#remove-redundant-rules").show();
								$("#modal-redundant-rules-body-error").hide();
								$("#modal-redundant-rules-body-span").hide();
								$("#modal-redundant-rules-body-div").show();
							} else {
								$("#remove-redundant-rules").hide();
								$("#modal-redundant-rules-body-error").hide();
								$("#modal-redundant-rules-body-div").hide();
								$("#modal-redundant-rules-body-span").show();
							}
						});

						// callback handler that will be called on failure
						request.fail(function(jqXHR, textStatus, errorThrown) {
							$("#modal-redundant-rules-body-div").hide();
							$("#modal-redundant-rules-body-span").hide();
							$('#modal-redundant-rules-body-error').html(jqXHR.responseText);
							$("#modal-redundant-rules-body-error").show();
						});

						// callback handler that will be called regardless
						// if the request failed or succeeded
						request.always(function() {
							$('#modal-redundant-rules').modal('show');
						});
					});
					
					
				});