package tangerine.websocket;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

@WebFilter("/channel/*")
public class WebSocketFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;

		// final Map<String, String[]> fakedParamMap = Collections.singletonMap("sessionId", new String[] { httpRequest.getSession().getId() });

		Map<String, String[]> fakedParamMap = new HashMap<>();
		fakedParamMap.putAll(request.getParameterMap());
		fakedParamMap.put("sessionId", new String[] { httpRequest.getSession().getId() });

		HttpServletRequestWrapper wrappedRequest = new HttpServletRequestWrapper(httpRequest) {
			@Override
			public Map<String, String[]> getParameterMap() {
				return fakedParamMap;
			}
		};
		chain.doFilter(wrappedRequest, response);
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
	}

	@Override
	public void destroy() {
	}

}
