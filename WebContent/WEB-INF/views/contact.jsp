<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<fmt:setBundle basename="i18n.text" />

<fmt:message key="contact.header" var="pagetitle_i18n" />

<jsp:include page="/assets/layouts/top.jsp">
	<jsp:param name="pagetitle" value="${pagetitle_i18n}" />
</jsp:include>

<div class="row">
	<div class="col-xs-12">
		<div class="page-header">
			<h1>
				<i class="icon-fixed-width icon-comments"></i>
				<fmt:message key="contact.header" />
			</h1>
			<ol class="breadcrumb pull-right hidden-xs">
				<li>
					<a href="<c:url value="/home"/>"> <fmt:message key="home.header" />
					</a>
				</li>
				<li class="active">
					<fmt:message key="contact.header" />
				</li>
			</ol>
		</div>
	</div>

	<div class="col-sm-3 col-xs-12">
		<p>
			<strong><fmt:message key="contact.admin.name" /></strong>
		</p>

		<p>
			<img src="/assets/images/harald-vranken.jpg" alt="Harald Vranken" width="150px" class="img-rounded">
		</p>

		<address>
			<fmt:message key="contact.admin.ou" />
			<br>
			<fmt:message key="contact.admin.address_1" />
			<br>
			<fmt:message key="contact.admin.address_2" />
			<br>
			<fmt:message key="contact.admin.address_3" />
		</address>
		<ul class="contactinfo">
			<li>
				<i class="icon-fixed-width icon-phone"></i>
				<fmt:message key="contact.admin.phone" />
			</li>
			<li>
				<i class="icon-fixed-width icon-envelope"></i>
				<a href="mailto:<fmt:message key="contact.admin.email" />"> <fmt:message key="contact.admin.email" />
				</a>
			</li>
			<li>
				<i class="icon-fixed-width icon-skype"></i>
				<a href="skype:<fmt:message key="contact.admin.skype" />?chat"> <fmt:message key="contact.admin.skype" />
				</a>
			</li>
			<li>
				<i class="icon-fixed-width icon-globe"></i>
				<a target="_blank" href="http://<fmt:message key="contact.admin.web" />"> <fmt:message key="contact.admin.web" />
				</a>
			</li>
		</ul>
	</div>

	<div class="col-sm-9 hidden-xs">
		<iframe width="100%" height="400px" frameborder="0" scrolling="no" marginheight="0" marginwidth="0"
			src="https://maps.google.nl/maps?f=q&amp;source=s_q&amp;hl=nl&amp;geocode=&amp;q=Valkenburgerweg+177,+Heerlen&amp;aq=0&amp;oq=Valkenburgerweg+177+&amp;sll=52.469397,5.509644&amp;sspn=3.001803,8.448486&amp;ie=UTF8&amp;hq=&amp;hnear=Valkenburgerweg+177,+6419+AT+Heerlen,+Limburg&amp;t=m&amp;z=14&amp;ll=50.878507,5.957793&amp;output=embed"></iframe>
	</div>
</div>
<div class="row">
	<div class="col-xs-12">
		<hr />
		<p>
			iTables is ontwikkeld in het kader van een bachelor eindopdracht. <br />
			De ontwikkelaars zijn Thomas Van Poucke, Jo&euml;l Craenhals en Ron Melger.
		</p>
	</div>
</div>


<jsp:include page="/assets/layouts/bottom.jsp" />