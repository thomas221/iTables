package filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;

import static util.Constants.*;

/**
 * Servlet Filter implementation class BeheerdersFilter
 */
@WebFilter("/*")
public class DisableCachingFilter implements Filter {

	/**
	 * Default constructor.
	 */
	public DisableCachingFilter() {
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		if (response instanceof HttpServletResponse) {
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			// setHeaderFields(httpResponse);
			chain.doFilter(request, response);
			setHeaderFields(httpResponse);
		} else {
			chain.doFilter(request, response);
		}
	}

	private static void setHeaderFields(HttpServletResponse response) {
		// Set standard HTTP/1.1 no-cache headers.
		response.setHeader(HEADER_CACHE_CONTROL_NAME, HEADER_CACHE_CONTROL_VALUE);

		// Set standard HTTP/1.0 no-cache header.
		response.setHeader(HEADER_PRAGMA_NAME, HEADER_PRAGMA_VALUE);

		// Disable caching for proxies
		response.setDateHeader(HEADER_EXPIRES_NAME, HEADER_EXPIRES_VALUE);
	}
}
