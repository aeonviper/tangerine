package tangerine.websocket;

import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.HandshakeResponse;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.server.ServerEndpointConfig;

public class WebSocketConfigurator {

	public static class Configurator extends ServerEndpointConfig.Configurator {

		@Override
		public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
			Object tracker = ((HttpSession) request.getHttpSession()).getServletContext().getAttribute(WebSocketSessionTracker.class.getName());
			// This is safe to do because it's the same instance of SessionTracker all the time
			sec.getUserProperties().put(WebSocketSessionTracker.class.getName(), tracker);
			super.modifyHandshake(sec, request, response);
		}

	}

}
