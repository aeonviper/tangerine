package tangerine.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import orion.core.Utility;
import orion.core.Constant;

public class SessionFilter implements Filter {

	public static final String sessionKey = Constant.sessionKey;

	public void init(FilterConfig config) throws ServletException {
	}

	public void destroy() {
	}

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {

		boolean valid = true;

		HttpServletRequest request = (HttpServletRequest) servletRequest;
		String path = Utility.getPath(request);
		if (!( // 
			path.startsWith("/resource") || //
			path.startsWith("/site") || //
			path.startsWith("/home") || //
			path.startsWith("/register") || //
			path.startsWith("/verify") || //
			path.startsWith("/job") || //
			path.startsWith("/forgot/password") || //
			path.startsWith("/reset/password") || //
			path.startsWith("/reset/password/do") || //
			path.startsWith("/login") //
			)) {
			HttpSession session = request.getSession();
			if (!(session != null && session.getAttribute(sessionKey) != null)) {
				valid = false;
			}
		}
		// valid = true;
		if (!valid) {
			HttpServletResponse response = (HttpServletResponse) servletResponse;
			response.sendRedirect(request.getContextPath() + "/home");
		} else {
			chain.doFilter(servletRequest, servletResponse);
		}
	}

}
