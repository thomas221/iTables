<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<fmt:setBundle basename="i18n.text" />

<fmt:message key="documentation.header" var="pagetitle_i18n" />

<jsp:include page="/assets/layouts/top.jsp">
	<jsp:param name="pagetitle" value="${pagetitle_i18n}" />
</jsp:include>

<div class="row">
	<div class="col-xs-12">
		<div class="page-header">
			<h1>
				<i class="icon-fixed-width icon-info-sign"></i>
				<fmt:message key="documentation.header" />				
			</h1>
			<a href="<c:url value="/assets/documentation_images/gebruikershandleiding.pdf" />" target="_blank">Bekijk in PDF formaat</a>
			<ol class="breadcrumb pull-right hidden-xs">
				<li>
					<a href="<c:url value="/home"/>"> <fmt:message key="home.header" />
					</a>
				</li>
				<li class="active">
					<fmt:message key="documentation.header" />
				</li>
			</ol>
		</div>
	</div>
	
	<div class="row">
		<div class="col-xs-12">
			<p>
				iTables is een tool om firewall-configuraties te analyseren (althans een aantal aspecten daarvan) en deze aan te passen.				
			</p>
			<p>
				iTables is ontwikkeld als bachelor afstudeeropdracht van de Open Universiteit. De ontwikkelaars zij Thomas Van Poucke, Ron Melger en Jo&euml;l Craenhals.
			</p>
			<p>
				Op deze pagina staat een handleiding voor iTables.<br />
				Deze is verdeeld in secties. Doorheen de website staan links naar specifiek gedeelten van deze handleiding.<br />
				Bij het klikken op zo'n link krijgt de gebruiker enkel de relevante sectie te zien.
			</p>
			<p>
				Daarom is elke sectie zoveel mogelijk geschreven als een losstaand geheel. De secties zijn verdeeld over 5 blokken.<br />
				Men kan op deze pagina een sectie openklappen door er op te klikken.
			</p>
			<p>
				<b>Blok 1</b> bevat algemene informatie. Sectie 1.1 legt kort uit wat iTables is.<br />
				De iTables website is op verschillende browsers getest. Sectie 1.2 beschrijft welke browsers ondersteund worden.
			</p>
			<p>
				<b>Blok 2</b> bevat de basiskennis die nodig is om te kunnen werken met iTables. In sectie 2.1 staat de noodzakelijke kennis over firewall-configuraties. iTables werkt met iptables configuraties.
				iptables kent verschillende tables en chains, en ook om te kunnen werken met iTables is deze kennis nodig. In sectie 2.2 staat hier informatie over.<br />
				In sectie 2.3 wordt tenslotte beschreven wat een gebruiker moet doen om een configuratie te starten. Daarbij kan een gebruiker kiezen om met een lege configuratie te starten, zelf een iptables configuratie te uploaden, of &eacute;&eacute;n van de voorbeeldconfiguraties te kiezen.
			</p>
			<p>
				<b>Blok 3</b> gaat over de analyse.<br />
				Sectie 3.1 is daarbij belangrijk om de overige secties van deze blok te begrijpen. Deze sectie bevat een beschrijving van de nodige begrippen (segment, correlatiegroepen en policy conflicten) en hoe deze gepresenteerd worden in iTables.<br />
				Sectie 3.2 beschrijft daarna hoe deze policy conflicten in iTables opgelost kunnen worden naargelang bepaalde voorkeursacties.<br />
				Sectie 3.3 is een korte beschrijving hoe men ook op de pagina met de analyse de firewall regels kan wijzigen. Dit heeft niets te maken met de analyse zelf maar is in deze blok gezet omdat in deze blok de gebruiker rondgeleid wordt in de pagina met de analyse.<br />
				In sectie 3.4 staat hoe redundante (=overbodige) regels gevonden kunnen worden en in sectie 3.5 staat hoe men de inhoud van een segment kan opsommen.
			</p>
			<p>
				<b>Blok 4</b> gaat over de pagina met firewall regels. Sectie 4.1 beschrijft hoe regels toegevoegd moeten worden aan de configuratie - dit gebeurt zoals men zou verwachten door een knop om een regel toe te voegen.<br />
				Op de pagina met firewall regels staat ook een formulier waarin men de waarden van een virtueel netwerkpakket kan invoeren. De gebruiker krijgt dan terug bij welke regel het virtueel netwerkpakketje zou matchen. Dit staat uitgelegd in sectie 4.2.
			</p>
			<p>
				In <b>blok 5</b> staan de zaken die niet aan bod zijn gekomen in de overige blokken.<br />
				Sectie 5.1 legt uit hoe men het logboek moet bekijken.<br />
				Men kan op elk gewenst moment de configuratie terug inladen in een iptables firewall. Sectie 5.2 legt uit hoe.
				In sectie 5.3 staat kort hoe een rapport kan gedownload worden van de sessie. Sectie 5.4 beschrijft hoe een sessie be&euml;indigd kan worden.<br />
				Sectie 5.5 gaat niet over iTables zelf maar over de firewall iptables. Er wordt beschreven hoe men een configuratie aanmaakt in iptables, en welke functies van iptables ondersteund worden.
			</p>
		</div>
	</div>

	<div class="col-xs-12 clearfix">
		<jsp:include page="/assets/layouts/alerts.jsp" />

		<div class="panel-group" id="accordion">
			<h4>
				Blok 1: Algemene informatie
			</h4>
			<div class="panel panel-default">
				<div class="panel-heading">
					<h4 class="panel-title">
						<a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#doc-1">1.1 Wat is iTables?</a>
					</h4>
				</div>
				<div id="doc-1" class="panel-collapse collapse">
					<div class="panel-body">
						<p>
							iptables is een firewall in Linux. iptables is beschikbaar op Linux vanaf kernel versie 2.2 .<br />
							iTables is een tool om firewall configuraties te analyseren. Daarbij is het mogelijk zelf iptables configuratie te uploaden.<br />
							Achteraf kan de evt. gewijzigde configuratie terug geladen worden in iptables.<br />
						</p>
					</div>
				</div>
			</div>
			
			<div class="panel panel-default">
				<div class="panel-heading">
					<h4 class="panel-title">
						<a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#doc-22">1.2 Ondersteunde browsers</a>
					</h4>
				</div>
				<div id="doc-22" class="panel-collapse collapse">
					<div class="panel-body">
						<p>
							Ondersteunde browsers:<br />
						</p>
						<div class="row">
							<div class="col-sm-3">
								<table class="table table-bordered">
									<thead>
										<tr>
											<th>Browser</th>
											<th>Vanaf versie</th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<th>Internet Explorer</th>
											<td>9</td>
										</tr>
										<tr>
											<th>Firefox</th>
											<td>4</td>
										</tr>
										<tr>
											<th>Chrome</th>
											<td>14</td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>						
							<p>
								Voor een optimale gebruikerservaring raden wij de laatste versie van Internet Explorer, Firefox of Chrome aan.<br />
								<strong>Javascript</strong> moet ingeschakeld zijn.							</p>
					</div>
				</div>
			</div>
			<h4>
				Blok 2: Basiskennis
			</h4>			
			<div class="panel panel-default">
				<div class="panel-heading">
					<h4 class="panel-title">
						<a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#doc-14">2.1 Wat is een firewall configuratie?</a>
					</h4>
				</div>
				<div id="doc-14" class="panel-collapse collapse">
					<div class="panel-body">
						<p>
							Een firewall configuratie bepaalt of een pakket doorgelaten moet worden of niet.<br />
							Dit gebeurt op basis van een aantal firewall regels.<br />
							Een firewall configuratie ziet er als volgt uit:
						</p>
						<a href="/assets/documentation_images/large/wat-is-een-firewall-configuratie-1.png" data-lightbox="wat-is-een-firewall-configuratie-1">
							<img src="/assets/documentation_images/thumbs/wat-is-een-firewall-configuratie-1.png" class="img-rounded center-block" />
						</a>
						<p>
							Elk netwerk pakket dat arriveert op de firewall heeft volgende informatie:
						</p>
						<ul>
							<li>Het IP adres van de zender</li>
							<li>Het IP adres van de bestemming</li>
							<li>Het protocol</li>
							<li>De poort van de zender (indien protocol TCP of UDP)</li>
							<li>De poort van de bestemming (indien protocol TCP of UDP)</li>
						</ul>
						<p>Een firewall regel bevat volgende informatie:</p>
						<ul>
							<li>
								Een IP patroon voor de verzender. Deze is van de vorm a.b.c.d/e. Een IP adres bestaat eigenlijk uit 32=8*4 bits.
								Dus 8 bits per getal. Het getal e geeft aan hoeveel van de eerste bits overeen moeten komen met het netwerk pakket.
								Bijvoorbeeld als het IP patroon 54.0.0.0/8 is zal elk IP adres dat begint met het getal 54 overeenkomen.
							</li>
							<li>
								Een IP patroon voor de bestemming. Deze is van dezelfde vorm als het IP patroon voor de verzender.
							</li>
							<li>
								Een protocol. Elk getal tussen 0 en 255 correspondeert met een protocol. Op <a href="http://www.iana.org/assignments/protocol-numbers/protocol-numbers.xhtml" target="_blank">http://www.iana.org/assignments/protocol-numbers/protocol-numbers.xhtml</a> is een lijst te
								vinden met alle protocollen. Merk op dat de wildcard '<b>*</b>' betrekking heeft op alle 256 protocollen. In iTables kan men via een drop-down ook snel kiezen voor TCP (6), UDP (17) of ICMP (1).
							</li>
							<li>
								Een poort patroon voor de verzender. Als het protocol TCP of UDP is kunnen poorten opgegeven worden. Als dit een getal is moet het netwerk pakket deze poort als verzender hebben. 
								Als deze van de vorm a:b is moet a&lt;b zijn. Dan moet de poort van de verzender in het interval [a,b] liggen. <strong>*</strong> betekent elke poort.
							</li>
							<li>
								Een poort patroon voor de bestemming. Als het protocol TCP of UDP is kunnen poorten opgegeven worden. Analoog aan het poort patroon voor de verzender.
							</li>
						</ul>
						<p>
							Men zegt dat een netwerk pakket <i>matcht</i> aan een firewall regel als de waarden voor de 5 velden (protocol, bron IP, ...) overeenkomen
							met de patronen van die regel.<br />
							Dan wordt de actie van die regel uitgevoerd.
						</p>
						<p>
							Een firewall configuratie bestaat uit een aantal regels in een bepaalde volgorde. Als een netwerk pakket binnenkomt worden
							de regels in die volgorde doorlopen. Als er een <i>match</i> is met een regel wordt de actie van die regel uitgevoerd.
						</p>
						<p>
							Als blijkt dat geen enkele regel <i>matcht</i> wordt de default policy van de chain uitgevoerd. Deze kan ingesteld zijn
							op toelaten (ACCEPT) of weigeren (DROP). Op de firewall regel pagina kan men de default policy instellen.
						</p>
						<p>
							Een firewall heeft meestal meerdere netwerkinterfaces. Minimaal een netwerkinterface voor het eigen netwerk en een netwerkinterface voor het WAN.<br />
							Naast de 5 velden die hier genomed zijn zal een firewall regel vaak ook 2 velden hebben voor de netwerkinterfaces.<br />
							Deze geven aan van welke netwerkinterface een matchend netwerkpakket moet komen en naar welke netwerkinterface een matchend netwerkpakket moet gaan.<br />
							Zo kan men bv. voorkomen dat door IP spoofing een netwerkpakket doet alsof het van het eigen veilige netwerk komt.<br />
							iTables ondersteunt de 2 netwerkinterface velden niet.
					</div>
				</div>
			</div>
			
			
			<div class="panel panel-default">
				<div class="panel-heading">
					<h4 class="panel-title">
						<a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#doc-15">2.2 Wat zijn tables en chains?</a>
					</h4>
				</div>
				<div id="doc-15" class="panel-collapse collapse">
					<div class="panel-body">	
						<a href="/assets/documentation_images/large/wat-zijn-tables-en-chains-1.png" data-lightbox="wat-zijn-tables-en-chains-1">
								<img src="/assets/documentation_images/thumbs/wat-zijn-tables-en-chains-1.png" class="img-rounded pull-right" />
							</a>					
						<p>
							iTables werkt enkel met de iptables firewall. Tables en chains zijn begrippen die specifiek zijn voor iptables firewalls.<br />
							In iptables zijn er verschillende tables. Er zijn in iptables standaard 4 tables aanwezig. Daarvan worden er 3 door iTables ondersteund.
						</p>
						<ul>
							<li>
								FILTER table: Deze table dient om te bepalen of een pakket toegelaten moet worden of geweigerd. In deze table
								zullen firewall regels in de meeste gevallen komen.
							</li>							
							<li>
								MANGLE table: Dient voor het bewerken van netwerk pakketten. Bewerken van netwerk pakketten wordt niet ondersteund door iTables.
							</li>
							<li>
								RAW table: Wordt gebruikt in firewalls die bijhouden welke verbindingen opgebouwd zijn. Firewall regels met die functionaliteit worden niet ondersteund, maar andere regels wel.
							</li>
							<li>
								NAT table: Deze table dient voor Network Address Translation oftewel NAT. Omdat het weigeren van pakketten niet toegelaten is in deze table is een firewall functionaliteit niet mogelijk. Daarom wordt de NAT table niet mee ingelezen door iTables.
							</li>
						</ul>
						<p>
							iTables ondersteunt enkel zogenaamde <i>packet filtering firewalls</i> (zie <a href="http://nl.wikipedia.org/wiki/Firewall" target="_blank">Wikipedia</a>). Firewall regels van zo'n firewalls zullen in de meeste gevallen in de FILTER table zitten.<br />
							OPMERKING: Bij firewall regels met niet-ondersteunde opties worden slechts de ondersteunde opties ingelezen. Er verschijnt dan wel een waarschuwing.
						</p>
						<p>
							Bij elke table horen chains. iTables leest alle chains in. Sommige chains worden door de firewall automatisch op bepaalde momenten aangeroepen. Dit zijn volgende chains:
						</p>
						<ul>
							<li>INPUT chain: Om binnenkomende pakketten te filteren.</li>
							<li>FORWARD chain: Verwerkt alle pakketjes die door de firewall gerouteerd worden.</li>
							<li>OUTPUT chain: Verwerkt pakketjes die komen vanuit de computer waarop de firewall is ge&iuml;nstalleerd.</li>
							<li>PREROUTING chain: Wordt gebruikt om pakketjes te behandelen zodra ze binnenkomen - nog voor het routingproces er mee aan de slag kan.</li>
							<li>POSTROUTING chain: Behandelt pakketjes voordat ze het systeem verlaten, maar nadat het routingproces er mee aan de slag is geweest.</li>
						</ul>
						<p>
							Elke chain kan firewall regels bevatten. In iTables beschouwt men elke chain als een aparte firewall configuratie. Werken in de ene chain heeft
							geen invloed op een andere chain. De analyse gebeurt per chain.<br />
						</p>
						<p>
							Bron: <a href="http://nl.wikibooks.org/wiki/Linux_Systeembeheer/Firewalls" target="_blank">Firewalls - Wikipedia</a>
						</p>
					</div>
				</div>
			</div>

			<div class="panel panel-default">
				<div class="panel-heading">
					<h4 class="panel-title">
						<a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#doc-2">2.3 Starten met een configuratie</a>
					</h4>
				</div>
				<div id="doc-2" class="panel-collapse collapse">
					<div class="panel-body">
						<p>
							Bij het opstarten van de website ziet men volgend scherm:
						</p>	
						<a href="/assets/documentation_images/large/starten-met-een-configuratie-screenshot-1.png" data-lightbox="starten-met-een-configuratie-screenshot-1">
							<img src="/assets/documentation_images/thumbs/starten-met-een-configuratie-screenshot-1.png" class="img-rounded center-block" />
						</a>
						<p>
							Druk op 'Aan de slag' om te starten.<br />
							Dan krijgt men 3 opties:							
						</p>
						<ul>
							<li>Ofwel start men een lege configuratie. Dan wordt een lege configuratie gestart waaraan men zelf nog firewall regels moet toevoegen.</li>
							<li>Ofwel neemt men 1 van de voorbeeldconfiguraties.</li>
							<li>Ofwel uploadt men zelf een iptables configuratie bestand. In de sectie <a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#doc-16">Hoe maak ik een configuratie in iptables</a> 
								wordt uitgelegd hoe men een configuratie aanmaakt in iptables.</li>							
						</ul>
						<a href="/assets/documentation_images/large/starten-met-een-configuratie-screenshot-2.png" data-lightbox="starten-met-een-configuratie-screenshot-2.png">
							<img src="/assets/documentation_images/thumbs/starten-met-een-configuratie-screenshot-2.png" class="img-rounded center-block" />
						</a>
						<p>
							Na het starten van een configuratie zijn er 3 modules.
						</p>
						<ul>
							<li>Men kan de firewall regels bekijken.</li>
							<li>Men kan de anomalie&euml;n zoeken in de configuratie.</li>
							<li>Men kan het logbestand bekijken. Daarin staan alle acties die gedaan zijn op de configuratie.
						</ul>
						<p>
							Stel men heeft er voor gekozen om te starten met een lege configuratie. Het toevoegen van regels aan deze lege configuratie moet als volgt gebeuren.<br />
							Kies voor de optie om de firewall regels te bekijken.<br />
							Klik dan op de chain waaraan men de regels wil toevoegen. Meestal zal men de regels willen toevoegen in de FORWARD chain van de FILTER table. 
							Zie de sectie <a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#doc-15">Wat zijn tables en chains?</a> voor uitleg.<br />
							Men krijgt volgend scherm te zien:
						</p>
						<a href="/assets/documentation_images/large/starten-met-een-configuratie-screenshot-3.png" data-lightbox="starten-met-een-configuratie-screenshot-2.png">
							<img src="/assets/documentation_images/thumbs/starten-met-een-configuratie-screenshot-3.png" class="img-rounded center-block" />
						</a>
						<p>
							Met de knop 'Firewall regel toevoegen kan men dan zelf firewallregels toevoegen. 
							Zie de sectie <a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#doc-8">Toevoegen van een regel</a>.
						</p>
					</div>
				</div>
			</div>
			<h4>
				Blok 3: De analyse
			</h4>
			<div class="panel panel-default">
				<div class="panel-heading">
					<h4 class="panel-title">
						<a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#doc-3">3.1 Vinden van policy conflicten</a>
					</h4>
				</div>
				<div id="doc-3" class="panel-collapse collapse">
					<div class="panel-body">
						<p>							
							Start de voorbeeldconfiguratie <i>&rsquo;Configuration with shadowing&rsquo;</i> zoals beschreven in <a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#doc-2">Starten met een configuratie</a><br />
							Open de Firewallregels module. U ziet rechts een lijst met alle tables en chains van de iptables configuratie. Voor een uitleg over wat tables en chains zijn verwijzen we naar de sectie <a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#doc-15">Wat zijn tables en chains?</a>.<br />
							U ziet ook een getal naast de forward chain. Dit getal geeft aan hoeveel regels in die chain zitten. Klik op de forward chain om de regels te bekijken.
						</p>
						<a href="/assets/documentation_images/large/vinden-van-policy-conflicten-1.png" data-lightbox="vinden-van-policy-conflicten-1">
							<img src="/assets/documentation_images/thumbs/vinden-van-policy-conflicten-1.png" class="img-rounded center-block" />
						</a>
						<p>
							<br />Bekijk regel 3 en regel 4:
						</p>
						<a href="/assets/documentation_images/large/vinden-van-policy-conflicten-2.png" data-lightbox="vinden-van-policy-conflicten-2">
							<img src="/assets/documentation_images/thumbs/vinden-van-policy-conflicten-2.png" class="img-rounded center-block" />
						</a>
						<p>
							Hier is een policy conflict. Een policy conflict is een anomalie.<br />
							Een policy conflict komt voor als er netwerk pakketten zijn waarvoor meerdere regels een verschillende actie hebben.
						</p>						
						<p>
							Deze anomalie moeten we terug vinden op de anomalie&euml;n module. Open de anomalie&euml;n module. Open de anomalie&euml;n module door rechtsboven op &rsquo;Mijn configuratie&rsquo; te klikken en dan op &rsquo;Anomalie&euml;n&rsquo;.
						</p>
						<a href="/assets/documentation_images/large/vinden-van-policy-conflicten-3.png" data-lightbox="vinden-van-policy-conflicten-3">
							<img src="/assets/documentation_images/thumbs/vinden-van-policy-conflicten-3.png" class="img-rounded center-block" />
						</a>
						<p>
							Men kan op elk gewenst moment van module wisselen door dit menu op te roepen.<br />
							U krijgt volgend scherm te zien:
						</p>
						<a href="/assets/documentation_images/large/oplossen-van-policy-conflicten-1.png" data-lightbox="oplossen-van-policy-conflicten-1-2">
							<img src="/assets/documentation_images/thumbs/oplossen-van-policy-conflicten-1.png" class="img-rounded center-block" />
						</a>
						<p>
							De firewall regels worden in groepen opgedeeld.<br />
							Bij het zoeken naar anomalie&euml;n kan men die groepen afzonderlijk beschouwen.<br />
							In de groen gemarkeerde groepen zijn geen policy conflicten.<br />
							Groep 3 is rood dus daar is ergens een policy conflict.
						</p>
						<p>
							Elk netwerk pakket heeft 5 waarden:
						</p>
							<ul>
								<li>Een protocol</li>
								<li>Een bronpoort</li>
								<li>Een bronadres. Dit is het IP-adres van de afzender.</li>
								<li>Een doelpoort</li>
								<li>Een doeladres. Dit is het IP-adres van de bestemming.</li>
							</ul>
						<p>
							Een firewall configuratie bepaalt aan de hand van die 5 waarden of het pakket doorgelaten wordt of geweigerd.<br />
							Op deze anomalie&euml;n pagina wordt die configuratie opgedeeld in segmenten. Zo een segment is dus een gedeelte van de configuratie.<br />
							In de tabel is te zien welke regels effect hebben op pakketten die vallen binnen een segment.
						</p>
						<p>
							Naast segment 3 staat een uitroepteken. Dit wil zeggen dat hier een policy conflict is.<br />
							Regel 3 en regel 4 zitten beide in segment 3 maar geven een andere actie aan voor pakketten die vallen binnen dat segment.
						</p>
						<p>
							Zoals verwacht is er dus een policy conflict tussen regel 3 en regel 4.
						</p>
						<p>
							Nu men weet dat hier een policy conflict is kan men actie ondernemen om deze op te lossen.
						</p>
					</div>
				</div>
			</div>

			<div class="panel panel-default">
				<div class="panel-heading">
					<h4 class="panel-title">
						<a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#doc-4">3.2 Oplossen van policy conflicten</a>
					</h4>
				</div>
				<div id="doc-4" class="panel-collapse collapse">
					<div class="panel-body">
						<p>
							Start de voorbeeld configuratie <i>&rsquo;Configuration with shadowing&rsquo;</i>. Bekijk de anomalie&euml;n pagina.
						</p>
						<a href="/assets/documentation_images/large/oplossen-van-policy-conflicten-1.png" data-lightbox="oplossen-van-policy-conflicten-1">
							<img src="/assets/documentation_images/thumbs/oplossen-van-policy-conflicten-1.png" class="img-rounded center-block" />
						</a>
						<p>
							In segment 3 is er een policy conflict.<br />
							Men kan op de drop-down list onderaan de tabel van groep 3 kiezen wat de voorkeursactie is binnen elk segment waar een conflict is.<br />
							iTables zal dan proberen de regels herordenen zodat aan de gekozen voorkeuren wordt voldaan.<br />
							Het kan zijn dat het niet mogelijk is om de regels te herordenen zodat aan alle voorkeuren voldaan wordt. Men krijgt daar dan een melding van.
						</p>
						<p>
							Neem hier als voorkeursactie in segment 3 &rsquo;accept&rsquo;. Het resultaat is volgend:
						</p>
						<a href="/assets/documentation_images/large/oplossen-van-policy-conflicten-2.png" data-lightbox="oplossen-van-policy-conflicten-2">
							<img src="/assets/documentation_images/thumbs/oplossen-van-policy-conflicten-2.png" class="img-rounded center-block" />
						</a>
						<p>
							Men kan policy conflicten ook oplossen door de firewall regels aan te passen. Dit kan zowel op de firewallregels pagina als rechtstreeks op de anomalie&euml;n pagina.
						</p>
						
						<p>
							Regels wijzigen op de anomalie&euml;n pagina is eenvoudig. Klik op een regel en er verschijnt een pop-up. Daar kan men nieuwe waarden voor de regel invullen of de regel verwijderen.
						</p>
						<a href="/assets/documentation_images/large/oplossen-van-policy-conflicten-3.png" data-lightbox="oplossen-van-policy-conflicten-3">
							<img src="/assets/documentation_images/thumbs/oplossen-van-policy-conflicten-3.png" class="img-rounded center-block" />
						</a>
						<p>
							Na elke wijziging aan de configuratie worden de segmenten opnieuw bepaald.<br />
							Wel houdt de applicatie bij welke policy conflicten reeds opgelost zijn. Groepen met alleen opgeloste policy conflicten worden steeds als opgelost gemarkeerd.
						</p>
						<p>
							<i>Let op!</i> Bij het oplossen van een policy conflict kan het soms zijn alsof niet aan de ingegeven voorkeursacties voldaan werd. Dit lijkt dan slechts zo omdat de segment nummering en de positie van de kolommen van de segmenten anders zijn.
							Als men kijkt naar de inhoud van de segmenten dan ziet men dat het policy conflict wel is opgelost volgens de voorkeursacties.
						</p>						
					</div>
				</div>
			</div>

			<div class="panel panel-default">
				<div class="panel-heading">
					<h4 class="panel-title">
						<a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#doc-5">3.3 Wijzigen van firewall regels</a>
					</h4>
				</div>
				<div id="doc-5" class="panel-collapse collapse">
					<div class="panel-body">
						<p>
							We werken verder met de configuratie uit de vorige sectie. Dit is de voorbeeldconfiguratie <i>&rsquo;Configuration with shadowing&rsquo;</i>, waarin de voorkeursactie voor segment 3 gewijzigd is in 'accept'.
							<br />
							Ga naar de firewallregels pagina.
						</p>
						<a href="/assets/documentation_images/large/wijzigen-van-firewall-regels-1.png" data-lightbox="wijzigen-van-firewall-regels-1">
							<img src="/assets/documentation_images/thumbs/wijzigen-van-firewall-regels-1.png" class="img-rounded center-block" />
						</a>
						<p>
							Klik op een veld van een firewall regel om deze te wijzigen.<br />
							Klik op het vuilbakje om een regel te verwijderen.<br />
							Klikken op de knop &rsquo;Firewall toevoegen&rsquo; zorgt dat een regel toegevoegd wordt achteraan.<br />
							Versleep een regel om zijn positie te wijzigen.<br />
							Men kan het aantal regels dat in 1 keer getoond wordt wijzigen. Standaard staat deze op 10.<br />
						</p>
						<p>
							Gebruik voorgaande informatie om de actie van firewall regel 4 te wijzigen in 'accept'.<br />
						</p>
						<p>
							Met deze gewijzigde configuratie gaan we verder in <a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#doc-6">Zoeken naar redundante (overbodige) regels</a>.
						</p>
					</div>
				</div>
			</div>

			<div class="panel panel-default">
				<div class="panel-heading">
					<h4 class="panel-title">
						<a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#doc-6">3.4 Zoeken naar redundante (overbodige) regels</a>
					</h4>
				</div>
				<div id="doc-6" class="panel-collapse collapse">
					<div class="panel-body">
						<p>
							We werken in deze sectie verder met de firewall configuratie uit de vorige sectie <a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#doc-5">Wijzigen van firewall regels</a>.<br />
							Daarin is de voorkeursactie in segment 3 gewijzigd in 'accept'. Daarna is de actie van regel 4 gewijzigd in 'accept'.<br />
						</p>
						<p>
							Redundante regels zijn regels die overbodig zijn in de configuratie  . Redundante regels kan men zonder problemen verwijderen.<br />
							Redundantie is naast policy conflicten ook een soort anomalie.
						</p>
						<p>
							Kijk naar regel 3 en regel 4 op de firewallregels pagina.
						</p>
						<a href="/assets/documentation_images/large/zoeken-naar-redundante-regels-1.png" data-lightbox="zoeken-naar-redundante-regels-1">
							<img src="/assets/documentation_images/thumbs/zoeken-naar-redundante-regels-1.png" class="img-rounded center-block" />
						</a>
						<p>
							Regel 4 is algemener dan regel 3.<br />
							De actie van regel 3 is dezelfde als regel 4.<br />
							Daarom is regel 3 redundant.<br />
							Dit moeten we terugvinden op de anomalie&euml;n pagina.
						</p>
						<p>
							Ga naar de anomalie&euml;n pagina door rechtsboven op &rsquo;Mijn configuratie&rsquo; te klikken.<br />
							Kies voor &rsquo;Anomalie&euml;n&rsquo;.<br />
						</p>
						<p>
							In groep 3 is zichtbaar dat regel 3 redundant is.<br />
						</p>
						<a href="/assets/documentation_images/large/zoeken-naar-redundante-regels-2.png" data-lightbox="zoeken-naar-redundante-regels-2">
							<img src="/assets/documentation_images/thumbs/zoeken-naar-redundante-regels-2.png" class="img-rounded center-block" />
						</a>
						<p>
							Regel 4 heeft de actie &rsquo;accept&rsquo; in zowel segment 3 als segment 4. Regel 3 heeft de actie &rsquo;accept&rsquo; in enkel segment 3.<br />
							Als we dus regel 3 verwijderen blijft de configuratie dezelfde. De actie blijft &rsquo;accept&rsquo; voor netwerk pakketten die vallen binnen segment 3 of segment 4.
						</p>
						<p>
							iTables kan alle redundante regels zoeken. Klik daarvoor op de knop &rsquo;Chain onderzoeken op redundanties&rsquo;.<br />
							Het resultaat is zoals verwacht:
						</p>
						<a href="/assets/documentation_images/large/zoeken-naar-redundante-regels-3.png" data-lightbox="zoeken-naar-redundante-regels-2">
							<img src="/assets/documentation_images/thumbs/zoeken-naar-redundante-regels-3.png" class="img-rounded center-block" />
						</a>
						<p>
							Met de knop 'Verwijder deze regels' kunnen de redundante regels in 1 klik verwijderd worden.
						</p>
						<p>
							Nog een voorbeeld om aan te tonen dat de volgorde van de regels een rol speelt bij het bepalen van de redundante regels.<br />
							Beschouw een configuratie met de volgende segmentering:
						</p>
						<a href="/assets/documentation_images/large/zoeken-naar-redundante-regels-4.png" data-lightbox="zoeken-naar-redundante-regels-2">
							<img src="/assets/documentation_images/thumbs/zoeken-naar-redundante-regels-4.png" class="img-rounded center-block" />
						</a>
						<p>
							In de configuratie zijn geen redundante regels:<br />
						</p>
						<ul>
							<li>
								Verwijderen van regel 1 zou bv. zorgen dat de actie in segment 1 wijzigt naar DROP.
							</li>
							<li>
								Verwijderen van regel 3 zou de configuratie wijzigen want dan is er geen actie meer voor pakketten in segment 4. Voor pakketten in dit segment wordt dan de default policy uitgevoerd. Deze default policy wordt bij het bepalen van de anomalie&euml;n buiten beschouwing gehouden.
							</li>
							<li>
								Verwijderen van regel 4 zou ervoor zorgen dat er geen actie meer is voor pakketten in segment 5.
							</li>
						</ul>
						<p>
							Nu wisselen we de regels 1 en 4 van plaats. Resultaat:
						</p>
						<a href="/assets/documentation_images/large/zoeken-naar-redundante-regels-5.png" data-lightbox="zoeken-naar-redundante-regels-2">
							<img src="/assets/documentation_images/thumbs/zoeken-naar-redundante-regels-5.png" class="img-rounded center-block" />
						</a>
						<p>
							Nu is er wel een redundante regel.<br />
							Als men regel 4 verwijdert blijven immers de acties in elk segment dezelfde.
						</p>			
					</div>
				</div>
			</div>

			<div class="panel panel-default">
				<div class="panel-heading">
					<h4 class="panel-title">
						<a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#doc-7">3.5 Opsomming van een segment opvragen</a>
					</h4>
				</div>
				<div id="doc-7" class="panel-collapse collapse">
					<div class="panel-body">
						<p>
							Zoals uitgelegd in <a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#doc-3">Vinden van policy conflicten</a> deelt iTables de configuratie op in segmenten. Zo kunnen policy conflicten gevonden worden.<br />
							Ga naar de anomalie&euml;n pagina.<br />
							Het is mogelijk om de inhoud van een segment op te vragen.<br />
							Klik daarvoor op een segment.
						</p>
						<a href="/assets/documentation_images/large/opsomming-van-een-segment-opvragen-1.png" data-lightbox="opsomming-van-een-segment-opvragen-1">
							<img src="/assets/documentation_images/thumbs/opsomming-van-een-segment-opvragen-1.png" class="img-rounded center-block" />
						</a>
						<p>
							Herinner dat een netwerk pakket bestaat uit 5 velden.<br />
							De optie om de segmentinhoud uitgebreid te tonen zal een opsomming geven van alle combinaties van die 5 waarden die horen bij dat segment.
						</p>
						<p>
							Een netwerk pakket valt dus binnen een segment als het een van de opgesomde waarden heeft.
						</p>
						<p>
							Meestal is de opsomming zo groot dat het niet zinnig is deze volledig weer te geven. Zo ook hier:
						</p>
						<a href="/assets/documentation_images/large/opsomming-van-een-segment-opvragen-2.png" data-lightbox="opsomming-van-een-segment-opvragen-2">
							<img src="/assets/documentation_images/thumbs/opsomming-van-een-segment-opvragen-2.png" class="img-rounded center-block" />
						</a>
						<p>
							iTables heeft echter de mogelijkheid om een compacte versie van de opsomming weer te geven.<br />
							Klik op een segment en kies nu voor <i>&rsquo;Compacte segmentinhoud tonen&rsquo;</i>. Het resultaat is volgende:
						</p>
						<a href="/assets/documentation_images/large/opsomming-van-een-segment-opvragen-3.png" data-lightbox="opsomming-van-een-segment-opvragen-3">
							<img src="/assets/documentation_images/thumbs/opsomming-van-een-segment-opvragen-3.png" class="img-rounded center-block" />
						</a>
						<p>
							In de compacte weergave staan regels met &rsquo;begin&rsquo; en regels met &rsquo;einde&rsquo;.<br />
							De regels met &rsquo;begin&rsquo; worden altijd opgevolgd door een regel met &rsquo;einde&rsquo;.<br />
							Alle pakketten met waarden tussen de waarden in de &rsquo;begin&rsquo; regel en de waarden in de &rsquo;einde&rsquo; regel vallen binnen het segment.<br />
							Zo wordt de opsomming veel compacter.<br />
							<i>Opmerking:</i> Regels zonder &rsquo;begin&rsquo; of &rsquo;einde&rsquo; aanduiding hebben dezelfde betekenis als bij de uitgebreide opsomming.
						</p>
						<p>
							In dit voorbeeld vallen volgende netwerk pakketten binnen het segment:<br />
							Alle netwerk pakketten met protocol TCP, een bron IP adres tussen 10.2.54.0 en 10.2.54.255, bronpoort 1543, een doel IP adres tussen 192.168.5.0 en 192.168.5.255 en doelpoort 80 vallen binnen segment 3.
						</p>
						<p>
							<i>Opmerking:</i> Soms zijn in de protocol-kolom getallen te zien. Elk getal tussen 0 en 255 is een mogelijke protocol waarde. Op <a href="http://www.iana.org/assignments/protocol-numbers/protocol-numbers.xhtml" target="_blank">http://www.iana.org/assignments/protocol-numbers/protocol-numbers.xhtml</a> staat een lijst met alle protocollen.
						</p>
					</div>
				</div> 
			</div>
			<h4>
				Blok 4: De pagina met firewall regels
			</h4>

			<div class="panel panel-default">
				<div class="panel-heading">
					<h4 class="panel-title">
						<a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#doc-8">4.1 Toevoegen van een regel</a>
					</h4>
				</div>
				<div id="doc-8" class="panel-collapse collapse">
					<div class="panel-body">
						<p>
							Het toevoegen van een regel kan enkel op de firewallregels pagina.
						</p>
						<a href="/assets/documentation_images/large/toevoegen-van-een-regel-1.png" data-lightbox="toevoegen-van-een-regel-1">
							<img src="/assets/documentation_images/thumbs/toevoegen-van-een-regel-1.png" class="img-rounded center-block" />
						</a>
						<p>
							Klik op de firewallregel toevoegen knop.
						</p>
						<a href="/assets/documentation_images/large/toevoegen-van-een-regel-2.png" data-lightbox="toevoegen-van-een-regel-2">
							<img src="/assets/documentation_images/thumbs/toevoegen-van-een-regel-2.png" class="img-rounded center-block" />
						</a>
						<p>
							Voer de waarden van de nieuwe regel in.<br />
							Als laatste is er een checkbox. Als men deze checkbox niet aanvinkt wordt de regel achteraan de configuratie toegevoegd.<br />
							Als men deze checkbox aanvinkt gebeurt het volgende:
						</p>
						<p>
							De regel wordt in de configuratie toegevoegd zodanig dat er geen policy conflicten ontstaan met voorgaande regels.<br />
							Dat wil zeggen zodanig dat er geen overlappingen zijn met eerdere regels die een andere actie hebben. Zo zal de regel gegarandeerd volledig effect hebben.
						</p>
					</div>
				</div>
			</div>

			<div class="panel panel-default">
				<div class="panel-heading">
					<h4 class="panel-title">
						<a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#doc-9">4.2 Bepaal matchende regel</a>
					</h4>
				</div>
				<div id="doc-9" class="panel-collapse collapse">
					<div class="panel-body">
						<p>
							Een educatief speeltje. De gebruiker kan van de 5 velden een willekeurig aantal invullen, bijv. het protocol en het bron IP adres. Nu kan getest worden met welke regel van de chain dit 'pakketje' het eerst zal matchen.<br />
						</p>
						<p>
							Opmerking: Een IP adres moet volledig ingevuld worden of helemaal niet ingevuld worden.	
						</p>
						<a href="/assets/documentation_images/large/kijken-aan-welke-regel-een-netwerk-pakket-matcht-1.png" data-lightbox="kijken-aan-welke-regel-een-netwerk-pakket-matcht-1">
							<img src="/assets/documentation_images/thumbs/kijken-aan-welke-regel-een-netwerk-pakket-matcht-1.png" class="img-rounded center-block" />
						</a>
						<p>
							Voer daarvoor de gewenste waarden in en klik op de knop &rsquo;match&rsquo;.<br />
							
						</p>
					</div>
				</div>
			</div>
			
			<h4>
				Blok 5: Overige
			</h4>
			<div class="panel panel-default">
				<div class="panel-heading">
					<h4 class="panel-title">
						<a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#doc-10">5.1 Logboek bekijken</a>
					</h4>
				</div>
				<div id="doc-10" class="panel-collapse collapse">
					<div class="panel-body">
						<p>
							Alle acties van de gebruiker op de firewall configuratie worden bijgehouden.<br />
							Men kan deze bekijken op de Logbestand pagina. Op elk moment kan naar de Logbestand pagina gegaan worden door rechtsboven op &rsquo;Mijn configuratie&rsquo; te klikken en dan op &rsquo;Logbestand&rsquo;.
						</p>
						<a href="/assets/documentation_images/large/logboek-bekijken-1.png" data-lightbox="logboek-bekijken-1">
							<img src="/assets/documentation_images/thumbs/logboek-bekijken-1.png" class="img-rounded center-block" />
						</a>
						<p>
							Het Logbestand bevat alle acties met het tijdstip waarop de actie gedaan is.
						</p>
						<a href="/assets/documentation_images/large/logboek-bekijken-2.png" data-lightbox="logboek-bekijken-2">
							<img src="/assets/documentation_images/thumbs/logboek-bekijken-2.png" class="img-rounded center-block" />
						</a>
					</div>
				</div>
			</div>
			
			<div class="panel panel-default">
				<div class="panel-heading">
					<h4 class="panel-title">
						<a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#doc-11">5.2 Downloaden en laden van een (evt. gewijzigde) iptables configuratie</a>
					</h4>
				</div>
				<div id="doc-11" class="panel-collapse collapse">
					<div class="panel-body">
						<p>
							Klik op &rsquo;Mijn configuratie&rsquo; rechtsboven. kies voor &rsquo;Configuratie exporteren&rsquo;.
						</p>
						<a href="/assets/documentation_images/large/downloaden-van-een-evt-gewijzigde-iptables-configuratie-1.png" data-lightbox="downloaden-van-een-evt-gewijzigde-iptables-configuratie-1">
							<img src="/assets/documentation_images/thumbs/downloaden-van-een-evt-gewijzigde-iptables-configuratie-1.png" class="img-rounded center-block" />
						</a>
						<p>
							De configuratie kan terug ingelezen worden in iptables.<br />
							Gebruik daarvoor het <code>iptables-restore</code> commando in de Linux terminal.<br />
							Voor een bestand met de naam <i>iTables_export.iptables</i> ziet het commando er als volgt uit:<br />
							<code>sudo iptables-restore &lt; iTables_export.iptables</code>
						</p>
					</div>
				</div>
			</div>
			
			<div class="panel panel-default">
				<div class="panel-heading">
					<h4 class="panel-title">
						<a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#doc-12">5.3 Exporteren van de resultaten van de sessie</a>
					</h4>
				</div>
				<div id="doc-12" class="panel-collapse collapse">
					<div class="panel-body">
						<p>
							iTables biedt de mogelijk aan om de sessie te exporteren.<br />
							Daarbij worden de resultaten van de analyse en het Logbestand in een Excel-bestand gezet.<br />
							Het exporteren van de sessie kan op elk moment door rechtsboven op &rsquo;Mijn configuratie&rsquo; te klikken en voor &rsquo;Sessie exporteren&rsquo; te kiezen.
						</p>
						<a href="/assets/documentation_images/large/exporteren-van-de-resultaten-van-de-sessie-1.png" data-lightbox="exporteren-van-de-resultaten-van-de-sessie-1">
							<img src="/assets/documentation_images/thumbs/exporteren-van-de-resultaten-van-de-sessie-1.png" class="img-rounded center-block" />
						</a>
						<p>
							Voor het openen van het bestand is <i>Microsoft Excel 2003</i> (of later) nodig.
						</p>						
					</div>
				</div>
			</div>
			
			<div class="panel panel-default">
				<div class="panel-heading">
					<h4 class="panel-title">
						<a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#doc-13">5.4 Be&euml;indigen van een sessie (bv. als men met een andere firewall configuratie wil werken)</a>
					</h4>
				</div>
				<div id="doc-13" class="panel-collapse collapse">
					<div class="panel-body">
						<a href="/assets/documentation_images/large/beeindigen-van-een-sessie-1.png" data-lightbox="beeindigen-van-een-sessie-1">
							<img src="/assets/documentation_images/thumbs/beeindigen-van-een-sessie-1.png" class="img-rounded center-block" />
						</a>
						<p>
							OPGELET! Het be&euml;indigen van een sessie wist alle gegevens.<br />
							De firewall configuratie waar men mee aan het werken is verdwijnt. Het Logboek wordt gewist.<br />
							Als men terug met een firewall configuratie wil werken moet men een nieuwe firewall configuratie starten. (zie <a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#doc-2">Starten met een configuratie</a>).
						</p>
					</div>
				</div>
			</div>		
			
			<div class="panel panel-default">
				<div class="panel-heading">
					<h4 class="panel-title">
						<a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#doc-16">5.5 Hoe maak ik een configuratie in iptables?</a>
					</h4>
				</div>
				<div id="doc-16" class="panel-collapse collapse">
					<div class="panel-body">
						<p>
							Voor een complete uiteenzetting hoe iptables gebruikt kan
							worden verwijzen we naar de <i>man</i>-pagina&rsquo;s in linux. Gebruik daarvoor volgend commando:<br />
							<code>man iptables</code>.
						</p>
						<p>
							Wis eerst de bestaande iptables configuratie op uw computer. Gebruik daarvoor het commando<br />
							<code>sudo iptables --flush</code>
						</p>							
						<p>
							iTables beperkt zicht tot configuraties waarvan firewall regels slechts 5 velden hebben. Deze regels zijn als volgt te maken
							in iptables:<br />
							<code>sudo iptables [-t table] -A naam_chain [-s bron_IP_adres] [-d doel_IP_adres] [-p protocol] [--sport bron_poort] [--dport doel_poort] -j actie</code><br />
							Opties tussen vierkante haken zijn optioneel.<br />
							Hierin is:<br />
						</p>
						<ul>
							<li>
								<i>table</i>: Naam van de table waar de regel in moet komen. iTables ondersteunt enkel de waarden FILTER,NAT,MANGLE of RAW. Indien de -t optie niet wordt opgegeven zal iptables de FILTER table nemen.
							</li>
							<li>
								<i>naam_chain</i>: Naam van de chain van de table.
							</li>
							<li>
								<i>protocol</i>: Mogelijke waarden: tcp, udp, icmp of een getal tussen 0 en 255. Optie -p niet opgeven staat gelijk aan de wildcard ('*'). Let er op dat de opties <i>--sport</i> en <i>--dport</i> alleen mogen opgegeven worden als het protocol tcp of udp is.								
							</li>
							<li>
								<i>bron_IP_adres</i>: Een IP patroon voor de verzender. Deze is van de vorm a.b.c.d/e. Een IP adres bestaat eigenlijk uit 32=8&times;4 bits.
								Dus 8 bits per getal. Het getal e geeft aan hoeveel van de eerste bits overeen moeten komen met het netwerk pakket.
								Bijvoorbeeld als het IP patroon 54.0.0.0/8 is zal elk IP adres dat begint met het getal 54 overeenkomen.
							</li>
							<li>
								<i>doel_IP_adres</i>: Een IP patroon voor de bestemming. Deze is van dezelfde vorm als het IP patroon voor de verzender.
							</li>
							<li>
								<i>bron_poort</i>: Een poort patroon voor de verzender. Kan enkel opgegeven worden als protocol TCP of UDP is.
								Als dit een getal is moet het netwerk pakket deze poort als verzender hebben. Als deze van de vorm a:b
								is moet a&lt;b zijn. Dan moet de poort van de verzender in het interval [a,b] liggen.
							</li>
							<li>
								<i>doel_poort</i>: Een poort patroon voor de bestemming. Kan enkel opgegeven worden als protocol TCP of UDP is. Analoog aan het poort patroon voor de verzender.
							</li>
						</ul>
						<p>
							Men kan de configuratie bewaren onder de naam <i>naam_bestand</i> met het commando<br />
							<code>iptables-save > naam_bestand</code><br />
							Dat bestand kan ge&uuml;pload worden in iTables.
						</p>
						<p>
							<code>iptables-save</code> zet de firewall configuratie in een tekstbestand met UNIX indeling.<br />
							Ook in een ander besturingssysteem zoals Windows is het mogelijk een tekstbestand aan te maken met UNIX indeling. Bijvoorbeeld met de tool Notepad++ kan men kiezen voor een UNIX indeling.<br />
							Zo een tekstbestand volgt een bepaalde syntax:<br />
						</p>
	<pre>
# Regels die starten met # dienen voor commentaar. iptables en iTables negeren deze.
# Volgende regel geeft aan dat men vanaf hier in de FILTER tabel configureert.
*filter
# Volgende is een definitie van een chain. De ACCEPT in de volgende regel geeft de default policy in de INPUT chain aan.
:INPUT ACCEPT
		
:FORWARD DROP
# Regels na een definitie van een chain komen in die chain.
# Regel 1. Syntax voor een regel is idem aan de syntax die eerder uitgelegd is, maar het iptables commandowoord kan weggelaten worden.
-A FORWARD -s 135.20.30.88/32 -d 10.65.46.0/24 -p 49 -j ACCEPT
# Regel 2
-A FORWARD -d 173.252.110.27/32 -p tcp --dport 80 -j DROP
# Regel 3
-A FORWARD -s 10.6.27.96/32 -d 173.252.111.56/32 -p udp --sport 1404 --dport 107 -j ACCEPT
# Regel 4
-A FORWARD -s 135.20.30.0/24 -d 10.65.46.0/24 -p tcp --dport 88 -j ACCEPT
# Regel 5
-A FORWARD -d 10.65.46.0/24 -j DROP

:OUTPUT ACCEPT
# De COMMIT opdracht commit al het voorgaande naar de firewall.
COMMIT
# Tabel mangle
*mangle
:INPUT ACCEPT

:PREROUTING ACCEPT

:POSTROUTING ACCEPT

:FORWARD ACCEPT

:OUTPUT ACCEPT

COMMIT
# Tabel raw
*raw
:PREROUTING ACCEPT

:OUTPUT ACCEPT

COMMIT
	</pre>
						
					</div>
				</div>
			</div>			
		</div>

	</div>
</div>

<script src="/assets/javascripts/lightbox-2.6.min.js"></script>
<link href="/assets/stylesheets/lightbox.css" rel="stylesheet" />


<jsp:include page="/assets/layouts/bottom.jsp" />