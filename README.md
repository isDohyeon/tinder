# ❤️ 틴더 (Tinder Clone)
**소개팅 서비스 Tinder의 핵심 기능들을 구현한 안드로이드 프로젝트**

<br>

## 📌 프로젝트 개요

- 이 프로젝트는 실제 소개팅 앱 **Tinder**를 클론코딩한 안드로이드 애플리케이션입니다.  
- 사용자는 다른 사용자들의 프로필을 좌우로 넘기며 **"좋아요" 또는 "싫어요"** 를 선택할 수 있으며,  
**서로 "좋아요"를 선택한 경우에만 매칭이 성사**되고, **푸시 알림으로 매칭 사실을 안내**합니다.  
- 매칭된 사용자끼리는 **실시간으로 메시지를 주고받을 수 있는 채팅 기능**을 이용할 수 있으며,  
각 사용자들은 **개인 프로필(사진, 나이, 지역 등)을 설정**하고 **탐색 화면에 노출**됩니다.

Firebase의 인증, 실시간 데이터베이스, 스토리지, 메시징 기능과 Node.js 기반의 메시징 서버를 함께 활용하여  
**클라우드 기반 실시간 소개팅 경험**을 제공합니다.

<br>

## 📱 화면 구성 미리보기

### 🔐 1. 로그인 / 회원가입 화면
* Firebase Authentication을 기반으로 이메일과 비밀번호를 통한 사용자 인증
<img src="https://github.com/user-attachments/assets/8dc79dcd-a97e-4922-a231-63dbdeb9b1bc" width=200>
<img src="https://github.com/user-attachments/assets/3b165804-9995-409f-9907-cfbce35d2e85" width=200>
<img src="https://github.com/user-attachments/assets/2f5feed9-3ddb-4954-ad88-4b12093160c6" width=200>


### 🏠 2. 홈(소개팅 탐색) 화면
* CardStackView 라이브러리를 사용하여 Tinder와 유사한 스와이프 UX의 탐색 화면
* 카드를 왼쪽으로 넘길 시 해당 사용자를 **"별로에요"** 로 설정
* 카드를 오른쪽으로 넘길 시 해당 사용자를 **"좋아요"** 로 설정
<img src="https://github.com/user-attachments/assets/6e8f041f-c48b-4f1b-b559-dc5bd2dcf589" width=200>
<img src="https://github.com/user-attachments/assets/32d0146b-1c36-4609-b90b-e9e94d7d9cfe" width=200>
<img src="https://github.com/user-attachments/assets/31141e9f-3115-433b-abc2-d31118e2df85" width=200>

### ❤️ 3. 유저 매칭 기능
* 서로 **"좋아요"** 로 설정한 유저끼리 매칭 알림 기능 제공
* 본인이 좋아요를 누른 사람과, 본인을 좋아요로 누른 사람의 정보를 확인 가능
<img src="https://github.com/user-attachments/assets/49fce430-8052-454d-8172-a4f199700cd0" width=200>
<img src="https://github.com/user-attachments/assets/311a35c3-c603-4fd4-b53f-92b5ded8ee61" width=200>

### 💬 4. 메시지 대화 기능
* 매칭된 유저에게 실시간으로 메시지를 보내고 받을 수 있으며, 받은 메시지 목록도 별도로 관리
* 오고 가는 메시지들은 최상위 중요도의 알림으로 전송
<img src="https://github.com/user-attachments/assets/fa377b76-9f87-40d3-90ef-85e84a639d12" width=200>
<img src="https://github.com/user-attachments/assets/8f223dda-8845-4e8d-ae00-7640a4cbf64b" width=200>
<img src="https://github.com/user-attachments/assets/3039f2cc-d80b-4870-adbc-73d72837899d" width=200>

#### 🔗 메시지 전송 흐름

1. 안드로이드 앱 → `OkHttp`로 Node.js 서버에 메시지 전송 요청 (FCM 토큰 포함)
2. Node.js 서버 → `Firebase Admin SDK`로 FCM 서버에 메시지 전달
3. FCM 서버 → 수신자 기기로 푸시 알림 전송

server.js
```Javascript
const express = require('express');
const admin = require('firebase-admin');

// Firebase Admin SDK 초기화 (서비스 계정 키 파일 경로)
const serviceAccount = require('./serviceAccountKey.json');
admin.initializeApp({
    credential: admin.credential.cert(serviceAccount)
});

const app = express();
app.use(express.json());

// 메시지를 보낸 엔드포인트 설정
app.post('/send-fcm', (req, res) => {
    console.log('업체서 데이터를 받았습니다.');
    const { targetToken, title, body } = req.body;  // 업체서 보내는 데이터

    // 메시지 구성
    const message = {
        token: targetToken,  // 수신할 앱의 FCM 토큰
        notification: {
            title: title,  // 알림 제목
            body: body,    // 알림 내용
        },
        data: {
            key1: 'value1',  // 필요시 추가할 데이터
            key2: 'value2',
        },
    };

    // FCM 메시지 보내기
    admin.messaging().send(message)
        .then((response) => {
            res.status(200).send(`메시지 전송 성공: ${response}`);
            console.log('메시지 전송 성공');
        })
        .catch((error) => {
            res.status(500).send(`메시지 전송 실패: ${error}`);
            console.log('메시지 전송 실패');
        });
});

// 서버 시작
app.listen(80, () => {
    console.log('서버가 80번 포트에서 실행 중입니다.');
});
```

## 🛠️ 사용 기술 스택

| 구분         | 기술 및 설명 |
|--------------|----------------|
| ![android](https://img.shields.io/badge/Android%20Studio-3DDC84?style=for-the-badge&logo=androidstudio&logoColor=white) | **Android Studio**를 기반으로 전체 프로젝트를 개발 |
| ![kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white) | 안드로이드 앱 개발 언어로 **Kotlin** 사용 |
| ![firebase](https://img.shields.io/badge/Firebase-FFCA28?style=for-the-badge&logo=firebase&logoColor=black) | 전체 백엔드 기능을 제공하는 **BaaS 플랫폼** |
| ![auth](https://img.shields.io/badge/Firebase%20Auth-FFB300?style=flat-square&logo=firebase&logoColor=white) | 이메일 기반 사용자 인증 |
| ![realtime](https://img.shields.io/badge/Firebase%20Realtime%20DB-039BE5?style=flat-square&logo=firebase&logoColor=white) | 실시간 채팅 및 사용자 데이터 저장 |
| ![storage](https://img.shields.io/badge/Firebase%20Storage-FFA000?style=flat-square&logo=firebase&logoColor=white) | 사용자 프로필 이미지 업로드 |
| ![fcm](https://img.shields.io/badge/Firebase%20Messaging-FF5252?style=flat-square&logo=firebase&logoColor=white) | 푸시 알림 메시지 수신 |
| ![nodejs](https://img.shields.io/badge/Node.js-339933?style=for-the-badge&logo=node.js&logoColor=white) | 푸시 메시지 전송용 백엔드 서버 구현 |
| ![express](https://img.shields.io/badge/Express.js-000000?style=for-the-badge&logo=express&logoColor=white) | Node.js 기반 HTTP 라우팅 처리 |
| ![okhttp](https://img.shields.io/badge/OkHttp-007396?style=flat-square&logo=java&logoColor=white) | Android에서 서버로 HTTP 요청 처리 |
| ![glide](https://img.shields.io/badge/Glide-FF6F00?style=flat-square&logo=android&logoColor=white) | 이미지 로딩 라이브러리 |
| ![cardstack](https://img.shields.io/badge/CardStackView-E91E63?style=flat-square&logo=android&logoColor=white) | Tinder 스타일의 스와이프 카드 뷰 구현 |

## 📁 프로젝트 구조

```
TinderClone/
├── Tinder/                    
│   ├── auth/                  # 로그인, 회원가입 관련 액티비티 및 모델
│   │   ├── IntroActivity
│   │   ├── JoinActivity
│   │   ├── SplashActivity
│   │   └── UserModel
│   │
│   ├── messages/              # 메시지 수신/전송 관련 UI 및 어댑터
│   │   ├── LikesActivity
│   │   ├── LikesAdapter
│   │   ├── MessagesActivity
│   │   ├── MessagesAdapter
│   │   └── MessageModel
│   │
│   ├── mypage/                # 마이페이지 (프로필 정보)
│   │   └── MyPageActivity
│   │
│   ├── util/                  # 유틸리티 클래스 (Firebase, 알림, 데이터 정의)
│   │   ├── FirebaseRef            # 파이어베이스 경로 상수
│   │   ├── MyData                 # 로그인 유저 정보 저장
│   │   ├── MyFirebaseMessagingSender # FCM 전송 요청용 클래스
│   │   ├── MyFirebaseMessagingService # FCM 수신 핸들링
│   │   └── NotificationUtil       # 로컬 알림 처리
│   │
│   ├── CardStackAdapter       # 카드 스와이프 뷰 어댑터
│   └── MainActivity           # 앱 메인 액티비티 (탭바 및 탐색 중심)
│
└── TinderServerJS/            # Node.js 백엔드 서버
    ├── server.js              # 메시지 수신 및 FCM 전송 처리
    └── serviceAccountKey.json # Firebase Admin 인증 키
```
