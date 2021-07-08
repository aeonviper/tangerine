package tangerine.websocket;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.google.inject.Injector;

import orion.core.Core;
import tangerine.bean.Principal;
import tangerine.core.Utility;
import tangerine.service.ShowService;
import tangerine.enumeration.MembershipType;
import tangerine.enumeration.Page;

public class Channel {

	protected ShowService showService;

	private static final Set<Channel> connectionSet = new CopyOnWriteArraySet<>();
	private static final AtomicLong sequenceNumber = new AtomicLong(0L);

	protected Session session;
	protected Injector injector;

	protected Long userId;
	protected MembershipType membershipType;
	protected Page page;
	protected String channelId;
	protected Long questionId;

	protected Principal principal;

	public static String generateChannelId() {
		return sequenceNumber.incrementAndGet() + "-" + UUID.randomUUID();
	}

	@OnOpen
	public void start(Session session, EndpointConfig config) {
		this.session = session;
		injector = Core.getInjector();

		showService = injector.getInstance(ShowService.class);

		String path = session.getRequestURI().getPath();
		if (path.endsWith("/channel/summary")) {
			page = Page.Summary;
		} else if (path.endsWith("/channel/question/list")) {
			page = Page.QuestionList;
		} else if (path.endsWith("/channel/conversation/list")) {
			page = Page.ConversationList;
		} else if (path.endsWith("/channel/answer/list")) {
			page = Page.AnswerList;
		}

		String channelId = (String) session.getRequestParameterMap().get("channelId").get(0);
		this.channelId = channelId;

		// System.out.println(Utility.gson.toJson(session.getRequestParameterMap()));

		connectionSet.add(this);

		String sessionId = session.getRequestParameterMap().get("sessionId").get(0);
		WebSocketSessionTracker tracker = (WebSocketSessionTracker) config.getUserProperties().get(WebSocketSessionTracker.class.getName());
		HttpSession httpSession = tracker.getSessionById(sessionId);

		principal = (Principal) httpSession.getAttribute(Principal.class.getCanonicalName());

		if (principal != null) {
			userId = principal.getUserId();
			membershipType = principal.getMembershipType();

			Map map = new HashMap<>();
			if (page == Page.Summary) {
				map = showService.summary(principal.getMembershipType(), principal.getUserId());
			} else if (page == Page.QuestionList) {
				map = showService.questionList(principal.getMembershipType(), principal.getUserId());
			} else if (page == Page.ConversationList) {
				questionId = Long.valueOf((String) session.getRequestParameterMap().get("questionId").get(0));
				map = showService.conversationList(principal.getMembershipType(), principal.getUserId(), questionId);
			} else if (page == Page.AnswerList) {
				questionId = Long.valueOf((String) session.getRequestParameterMap().get("questionId").get(0));
				map = showService.answerList(principal.getMembershipType(), principal.getUserId());
			}
			send(Utility.gson.toJson(map));
		}
	}

	@OnClose
	public void end() {
		connectionSet.remove(this);
	}

	@OnMessage
	public void receive(String message) {
	}

	public synchronized void send(String message) {
		try {
			session.getBasicRemote().sendText(message);
		} catch (IOException e) {
			System.err.println(e);
			connectionSet.remove(this);
			try {
				session.close();
			} catch (IOException ioe) {
				System.err.println(ioe);
			}
		}
	}

	@OnError
	public void onError(Throwable t) throws Throwable {

	}

	public static List<Channel> listChannel(Long userId, Long otherUserId) {
		List<Channel> list = new ArrayList<>();
		for (Channel connection : connectionSet) {
			if (userId.equals(connection.getUserId()) || otherUserId.equals(connection.getUserId())) {
				list.add(connection);
			}
		}
		return list;
	}

	public static List<Channel> listAllChannel() {
		return new ArrayList<>(connectionSet);
	}

	public Long getUserId() {
		return userId;
	}

	public Page getPage() {
		return page;
	}

	public String getChannelId() {
		return channelId;
	}

	public MembershipType getMembershipType() {
		return membershipType;
	}

	public Long getQuestionId() {
		return questionId;
	}

	public Principal getPrincipal() {
		return principal;
	}

}
