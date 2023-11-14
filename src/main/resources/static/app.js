const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8082/mobicaster-websocket/androidId1234'
});

// --------------------------------------------------------------------------------------------
const stompClient2 = new StompJs.Client({
    brokerURL: 'ws://localhost:8082/mobicaster-websocket/androidId2345'
});
stompClient2.onConnect = (frame) => {
    stompClient2.subscribe('/topic/greetings', (greeting) => {
        showGreeting(JSON.parse(greeting.body).content);
    });
    stompClient2.subscribe('/user/topic/live', (greeting) => {
        // showGreeting(JSON.parse(greeting.body).content);
        console.log('Live: ' + greeting);
    });

    stompClient2.subscribe('/user/topic/foldback', (greeting) => {
        // showGreeting(JSON.parse(greeting.body).content);
        console.log('foldback: ' + greeting);
    });
};
stompClient2.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

stompClient2.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};
// --------------------------------------------------------------------------------------------
stompClient.onConnect = (frame) => {
    setConnected(true);
    console.log('Connected: ' + frame);
    stompClient.subscribe('/topic/greetings', (greeting) => {
        showGreeting(JSON.parse(greeting.body).content);
    });

    stompClient.subscribe('/user/topic/live', (greeting) => {
        // showGreeting(JSON.parse(greeting.body).content);
        console.log('Live: ' + greeting);
    });

    stompClient.subscribe('/user/topic/foldback', (greeting) => {
        // showGreeting(JSON.parse(greeting.body).content);
        console.log('foldback: ' + greeting);
    });
};

stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    } else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    stompClient.activate();
    stompClient2.activate();
}

function disconnect() {
    stompClient.deactivate();
    stompClient2.deactivate();
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    stompClient.publish({
        destination: "/app/greetings",
        body: JSON.stringify({'androidId': "abad123svbasd12", 'liveAction': "start"})
    });

    stompClient.publish({
        destination: "/app/live",
        body: JSON.stringify({'androidId': "abad123svbasd12", 'liveAction': "start"})
    });

    stompClient2.publish({
        destination: "/app/greetings",
        body: JSON.stringify({'androidId': "abad123svbasd12", 'liveAction': "start"})
    });

    stompClient2.publish({
        destination: "/app/live",
        body: JSON.stringify({'androidId': "abad123svbasd12", 'liveAction': "start"})
    });
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', (e) => e.preventDefault());
    $("#connect").click(() => connect());
    $("#disconnect").click(() => disconnect());
    $("#send").click(() => sendName());
});