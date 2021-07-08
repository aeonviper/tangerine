package tangerine.websocket;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener
public class WebSocketSessionTracker implements ServletContextListener, HttpSessionListener {

	private final ConcurrentMap<String, HttpSession> sessionMap = new ConcurrentHashMap<>();

	@Override
	public void contextInitialized(ServletContextEvent event) {
		event.getServletContext().setAttribute(getClass().getName(), this);
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
	}

	@Override
	public void sessionCreated(HttpSessionEvent event) {
		sessionMap.put(event.getSession().getId(), event.getSession());
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		sessionMap.remove(event.getSession().getId());
	}

	public HttpSession getSessionById(String id) {
		return sessionMap.get(id);
	}

}
