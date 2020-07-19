package test.integration;

import static net.sourceforge.jwebunit.junit.JWebUnit.*;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Klasse die de integratietests bevat.
 * 
 * @author Joel Craenhals
 * 
 */
public class IntegrationTest {

    /**
     * Methode die voor elke test in deze klasse wordt uitgevoerd en die zorgt
     * voor het instellen van de basis URL als ook voor het beschikbaar stellen
     * van de resource bundel met de vertalingen.
     */
    @Before
    public void prepare() {
	setBaseUrl("http://localhost:8080");
	getTestContext().setResourceBundleName("i18n.text");
	getTestContext().addRequestHeader("Accept-Language", "nl");
	// FIXME: Problem with bootstrap or JWebUnit (bootstrap.js block the
	// execution while webtester has downloaded it). Set to false when
	// problem solve!
	setScriptingEnabled(true);
    }

    /**
     * Methode die na elke test in deze klasse wordt uitgevoerd en die zorgt
     * voor het sluiten van de browsersessie.
     */
    @After
    public void close() {
	closeBrowser();
    }

    /**
     * Methode om de algemene layout en user interface van de applicatie te
     * testen.
     */
    @Test
    public void testLayout() {
	beginAt("/");

	assertResponseCode(200);
	assertCookiePresent("JSESSIONID");

	assertNotNull(getPageSource());

	Object[] args = new Object[1];
	args[0] = getMessage("home.header");
	assertTitleEqualsKey("application.title", args);

	assertElementPresent("navigation_bar");
	assertHeaderEquals("Content-Type", "text/html;charset=UTF-8");

	assertLinkPresent("navbar_header_link");
	assertLinkPresent("navbar_start_link");
	assertLinkPresent("navbar_documentation_link");
	assertLinkPresent("navbar_contact_link");

	assertLinkPresentWithText(getMessage("application.name"));

	assertElementPresent("footer");
	assertLinkPresent("footer_link_top");
	assertLinkPresentWithText(getMessage("footer.top"));
	assertElementPresent("footer_copyright_message");
	assertTextInElement("footer_copyright_message", getMessage("application.copyright"));
    }

    /**
     * Methode die de home-pagina test en nagaat of die alle nodige elementen
     * bevat.
     */
    @Test
    public void testHomePage() {
	beginAt("/home");

	assertResponseCode(200);
	assertCookiePresent("JSESSIONID");

	assertNotNull(getPageSource());

	Object[] args = new Object[1];
	args[0] = getMessage("home.header");
	assertTitleEqualsKey("application.title", args);

	assertLinkPresentWithText(getMessage("home.start"));

	assertElementPresentByXPath("//div[@class='jumbotron']");
	assertElementPresentByXPath("//h2[@class='section_header']");
	assertElementPresentByXPath("//div[@class='row features']");
    }

    /**
     * Methode die de start-pagina test en nagaat of die alle nodige elementen
     * bevat.
     */
    @Test
    public void testStartPage() {
	beginAt("/start");

	assertResponseCode(200);
	assertCookiePresent("JSESSIONID");

	assertNotNull(getPageSource());

	Object[] args = new Object[1];
	args[0] = getMessage("start.header");
	assertTitleEqualsKey("application.title", args);
    }

    /**
     * Methode die de documentatie-pagina test en nagaat of die alle nodige
     * elementen bevat. TODO: dient nog verder te worden uitgewerkt van zodra
     * die pagina klaar is.
     */
    @Test
    public void testDocumentationPage() {
	beginAt("/documentation");

	assertResponseCode(200);
	assertCookiePresent("JSESSIONID");

	assertNotNull(getPageSource());

	Object[] args = new Object[1];
	args[0] = getMessage("documentation.header");
	assertTitleEqualsKey("application.title", args);
    }

    /**
     * Methode die de contact-pagina test en nagaat of die alle nodige elementen
     * bevat. TODO: dient nog verder te worden uitgewerkt van zodra die pagina
     * klaar is.
     */
    @Test
    public void testContactPage() {
	beginAt("/contact");

	assertResponseCode(200);
	assertCookiePresent("JSESSIONID");

	assertNotNull(getPageSource());

	Object[] args = new Object[1];
	args[0] = getMessage("contact.header");
	assertTitleEqualsKey("application.title", args);
    }

    /**
     * Methode die de melding test voor het gebruik van cookies op de website.
     */
    @Test
    public void testCookieUsageAlert() {
	beginAt("/");

	assertResponseCode(200);
	assertCookiePresent("JSESSIONID");

	assertElementPresentByXPath("//div[@class='cc-cookies ']");
	assertElementPresentByXPath("//html//body//div[2]");
	assertElementPresentByXPath("//html//body//div[2]//a[1]");
    }

    /**
     * Methode die de melding test als Javascript is uitgeschakeld in de browser
     * van de gebruiker.
     */
    @Test
    public void testJavascriptDisabledAlert() {
	setScriptingEnabled(false);

	beginAt("/");

	assertElementPresent("javascript_disabled_alert");

	assertTextInElement("javascript_disabled_alert", getMessage("javascript.disabled.alert"));

	setScriptingEnabled(true);

	beginAt("/");

	assertElementNotPresent("javascript_disabled_alert");
    }
}
