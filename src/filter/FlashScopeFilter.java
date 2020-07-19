package filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import static util.Constants.*;

/**
 * Ensures that any request parameters whose names start with 'flash.' are
 * available for the next request too.
 */
@WebFilter("/*")
public class FlashScopeFilter implements Filter {

	/**
	 * Method executed while initializing the filter.
	 * 
	 * @param filterConfig
	 *            A filter configuration object used by a servlet container to
	 *            pass information to a filter during initialization.
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	/**
	 * Method executed before destroying the filter.
	 */
	public void destroy() {
	}

	/**
	 * Methods of the filter to be executed.
	 * 
	 * @param request
	 *            The request.
	 * @param response
	 *            The response.
	 * @param chain
	 *            The filter chain associated with this filter.
	 */
	@SuppressWarnings("unchecked")
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		// Attributen die aanwezig zijn in de sessie opnieuw toevoegen aan de
		// request.
		if (request instanceof HttpServletRequest) {
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			HttpSession session = httpRequest.getSession();
			if (session != null) {
				Map<String, Object> flashParams = (Map<String, Object>) session.getAttribute(P_FLASH_SCOPE_MAP);
				if (flashParams != null) {
					for (Map.Entry<String, Object> flashEntry : flashParams.entrySet()) {
						request.setAttribute(flashEntry.getKey(), flashEntry.getValue());
					}
					session.removeAttribute(P_FLASH_SCOPE_MAP);
				}
			}
		}

		// De chain verder uitvoeren.
		chain.doFilter(request, response);

		// Attributen die beginnen met "flash."en die aanwezig zijn in de
		// request, worden toegevoegd aan de sessie waarbij eerst het "flash."
		// gedeelte wordt weggelaten.
		if (request instanceof HttpServletRequest) {
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			Map<String, Object> flashParams = new HashMap<String, Object>();
			Enumeration<String> e = httpRequest.getAttributeNames();
			while (e.hasMoreElements()) {
				String paramName = (String) e.nextElement();
				if (paramName.startsWith(FLASH)) {
					Object value = request.getAttribute(paramName);
					paramName = paramName.substring(FLASH.length(), paramName.length());
					flashParams.put(paramName, value);
				}
			}
			if (flashParams.size() > 0) {
				HttpSession session = httpRequest.getSession();
				session.setAttribute(P_FLASH_SCOPE_MAP, flashParams);
			}
		}
	}
}