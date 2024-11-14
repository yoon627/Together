let stompClient = null;

function connect() {
  // WebSocket 연결을 위한 SockJS 및 STOMP 설정
  const socket = new SockJS('/ws/chat'); // 서버 WebSocket 엔드포인트와 연결
  stompClient = Stomp.over(socket);

  // STOMP 연결
  stompClient.connect({}, function () {
    console.log('Connected to WebSocket');

    // 메시지 구독
    const coupleId = document.getElementById("coupleId").value;

    stompClient.subscribe('/topic/messages/' + coupleId,
        function (messageOutput) {
          showMessage(JSON.parse(messageOutput.body)); // 수신된 메시지 표시
        });
  });
}

// 메시지 전송
async function sendMessage() {
  const coupleId = document.getElementById("coupleId").value;
  const messageContent = document.getElementById("message").value;
  const fileInput = document.getElementById("fileInput").files[0];
  let fileUrl = null;

  // 파일이 선택된 경우 업로드
  if (fileInput) {
    const formData = new FormData();
    formData.append("file", fileInput);
    const token = "token";
    // 파일 업로드 요청
    try {
      const response = await fetch("/photo", {
        method: "POST",
        headers: {
          Authorization: "Bearer " + token
        },
        body: formData,
      });
      fileUrl = await response.text(); // 업로드된 파일의 URL을 가져옴
    } catch (error) {
      console.error("File upload failed:", error);
      return;
    }
  }
  if (stompClient && messageContent) {
    stompClient.send("/app/send", {},
        JSON.stringify(
            {
              coupleId: coupleId,
              senderId: "user1",
              content: messageContent,
              imgUrl: fileUrl
            }));
    document.getElementById("message").value = ''; // 전송 후 입력창 초기화
  }
}

// 수신된 메시지를 화면에 표시
function showMessage(message) {
  const messageElement = document.createElement('li');
  messageElement.innerText = message.content; // 수신된 메시지 내용 표시
  if (message.imgUrl) {
    const imgElement = document.createElement('img');
    imgElement.src = message.imgUrl;
    imgElement.alt = "Image"; // 이미지 설명 (옵션)
    imgElement.style.maxWidth = "200px"; // 이미지 크기 조절
    imgElement.style.display = "block";
    imgElement.style.marginTop = "5px";

    messageElement.appendChild(imgElement);
  }
  document.getElementById("messages").appendChild(messageElement);
}

// 페이지 로딩 시 WebSocket 연결
window.onload = connect;
