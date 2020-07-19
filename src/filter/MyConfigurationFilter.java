package filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static util.Constants.*;

/**
 * Servlet Filter implementation class loginFilter
 */
@WebFilter({ "/my_configuration", "/my_configuration/*" })
public class MyConfigurationFilter extends ExtendableFilter implements Filter {

	/**
	 * Default constructor.
	 */
	public MyConfigurationFilter() {
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
		if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			HttpServletResponse httpResponse = (HttpServletResponse) response;

			if (isConfiguratieGeldig(httpRequest)) {
				chain.doFilter(request, response);
			} else {
				redirect(httpRequest, httpResponse, SERVLET_START);
			}
		} else {
			chain.doFilter(request, response);
		}
	}
}
