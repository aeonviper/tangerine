var Channel = {};
Channel.socket = null;
Channel.connect = function(channel, onMessageFunction) {
	if (window.location.protocol == 'http:') {
		host = 'ws://' + window.location.host + channel;
	} else {
		host = 'wss://' + window.location.host + channel;
	}

	if ('WebSocket' in window) {
		Channel.socket = new WebSocket(host);
 	} else if ('MozWebSocket' in window) {
		Channel.socket = new MozWebSocket(host);
	} else {
		console.log('Error: WebSocket is not supported by this browser.');
		return;
	}

 	Channel.socket.onopen = function () {
		console.log('Info: WebSocket connection opened.');		
	};

	Channel.socket.onclose = function () {                
		console.log('Info: WebSocket closed.');
	};

	Channel.socket.onmessage = onMessageFunction;
	
	// Channel.socket.send(message);
};