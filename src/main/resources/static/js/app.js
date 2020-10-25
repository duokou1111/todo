var stompClient = null;
var subcribeTopic = "/test";
var BASE_WEBSOCKET_URL ="http://127.0.0.1:8080/todo";
function setConnected(connected) {
    console.log("websocket服务器连接成功");
}
connect();
function connect() {
    var socket = new SockJS( BASE_WEBSOCKET_URL);
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe(subcribeTopic+'/', function (message) {
           /* alert(message.body);*/
            $(".mask2").fadeIn();
            $(".message-text").html(message.body);
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function send(data) {
    stompClient.send(sendTopic, {},data);
}


/*$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });
});*/


