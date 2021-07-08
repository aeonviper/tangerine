package tangerine.websocket;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import orion.core.Core;
import tangerine.bean.Principal;
import tangerine.core.Utility;
import tangerine.service.ShowService;

@ServerEndpoint(value = "/channel/answer/list", configurator = WebSocketConfigurator.Configurator.class)
public class AnswerListChannel extends Channel {

	@OnOpen
	public void start(Session session, EndpointConfig config) {
		super.start(session, config);
	}

	@OnClose
	public void end() {
		super.end();
	}

	@OnMessage
	public void receive(String message) {
		super.receive(message);
	}

	@OnError
	public void onError(Throwable t) throws Throwable {
		super.onError(t);
	}

}
