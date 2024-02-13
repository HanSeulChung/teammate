# TeamMate : 팀원들과의 원할한 협업을 위한 서비스
TeamMate는 협업 해야하는 사람들과 팀을 만들어 문서 공유와 캘린더로 일정 공유를 할 수 있어 원활한 협업을 가능하게 합니다.

협업 프로젝트를 혼자서 처음부터 다시 구현하기 위해 fork 후 작업 진행 예정입니다.

[original 링크](https://github.com/100backfro/teammate)

        
## 기술 스택
### Backend
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=spring-security&logoColor=white)
![Spring Data JPA](https://img.shields.io/badge/Spring_Data_JPA-6DB33F?style=for-the-badge&logo=spring&logoColor=white)

![OAuth2](https://img.shields.io/badge/OAuth-2CA5E0?style=for-the-badge&logo=oauth2&logoColor=white&labelColor=black)
![Redis](https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white)
![AWS](https://img.shields.io/badge/AWS-232F3E?style=for-the-badge&logo=amazon-aws&logoColor=white)
![Amazon S3](https://img.shields.io/badge/Amazon_S3-569A31?style=for-the-badge&logo=amazon-s3&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Websocket](https://img.shields.io/badge/Websocket-808080?style=for-the-badge&logo=websocket&logoColor=white)
![Stomp](https://img.shields.io/badge/Stomp-32CD32?style=for-the-badge&logo=stomp&logoColor=white)


### DataBase
![MongoDB](https://img.shields.io/badge/MongoDB-47A248?style=for-the-badge&logo=mongodb&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)

### Deploy
![Vercel](https://img.shields.io/badge/Vercel-000000?style=for-the-badge&logo=vercel&logoColor=white)
![Jenkins](https://img.shields.io/badge/Jenkins-D24939?style=for-the-badge&logo=jenkins&logoColor=white)
![Naver Cloud](https://img.shields.io/badge/Naver_Cloud-03C75A?style=for-the-badge&logo=naver&logoColor=white)


### Tool
![Git](https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=git&logoColor=white)
![IntelliJ](https://img.shields.io/badge/IntelliJ_IDEA-000000?style=for-the-badge&logo=intellij-idea&logoColor=white)


## ERD
![사본 -TeamMateERD](https://github.com/100backfro/teammate/assets/94779505/f6fecb2c-2832-49b9-a9df-a77b5c5aa05b)

* MongoDB: Documents, Comment
* MySQL: Member, Team, TeamParticipants, ScheduleCategory, SimpleSchedule, RepeatSchedule, TeamParticipantsSchedule

## Architecture
Architecture 재구성 필요/예정

![image](https://github.com/100backfro/teammate/assets/110381560/35856126-d4da-4c93-bdc2-c42cd16f22a1)


## API
[http 요청](backend/http)

## 화면
| 첫 화면  |
|:-:|
|  ![image](https://github.com/100backfro/teammate/assets/94779505/6d743e48-ba97-4710-b6c2-e833672e045f) |

| 회원가입<br>(시작하기 버튼) | 로그인 | 소셜로그인(네이버) |
|:------:|:------:|:-----------:|
|    ![image](https://github.com/100backfro/teammate/assets/94779505/8ba29ff4-5fd6-4bff-8135-9749c145df0c)| ![image](https://github.com/100backfro/teammate/assets/94779505/a4300348-3503-4270-9703-3bb7f00c1b06)|  ![image](https://github.com/100backfro/teammate/assets/94779505/c0240014-1b01-4bea-8524-95f93fe304ff)

| 이메일 전송   |  이메일 인증 링크 클릭  |
|:-:|:-:|
 ![image](https://github.com/100backfro/teammate/assets/94779505/fb47dd23-c1a8-419d-ae97-391151770370) | ![image](https://github.com/100backfro/teammate/assets/94779505/41e2f058-3dd5-42ec-a5f3-7bb5f340c349)
  

| 로그인 후 첫 화면(팀이 없을 때)  |  로그인 후 첫 화면(팀이 있을 때)  |
|:-:|:-:|
 ![image](https://github.com/100backfro/teammate/assets/94779505/b1f46968-afe6-49a7-9a5e-9ecb23128de9)  | ![image](https://github.com/100backfro/teammate/assets/94779505/cc376a13-1e79-497c-a43d-fb60fc33a5ef)
 

| 팀 생성  | 팀 이미지 없을 시 생성 불가   |  팀 생성 완료 후 팝업  |
|:-:|:-:|:-:|
| ![image](https://github.com/100backfro/teammate/assets/94779505/06f47380-f102-498d-bc3e-72e466a9208f)  | ![image](https://github.com/100backfro/teammate/assets/94779505/482951df-0fc6-49f5-854a-5eedeb1b6364)| ![image](https://github.com/100backfro/teammate/assets/94779505/4f46b88d-d7f9-4e80-877a-80063098e166)  


| 팀 첫 화면  | 문서 |  캘린더 |
|:-:|:-:|:-:|
 ![image](https://github.com/100backfro/teammate/assets/94779505/76d717d7-5106-4910-9213-4caa66974387)  | ![image](https://github.com/100backfro/teammate/assets/94779505/95e5621b-a45f-40f5-8625-efc8d503874c)| ![image](https://github.com/100backfro/teammate/assets/94779505/76ae530f-875d-4769-a83f-b682e8d58e66)
  


| 문서 작성  | 문서 수정  | 댓글 |
|:-:|:-:|:-:|
 ![image](https://github.com/100backfro/teammate/assets/94779505/38c7255a-3920-44e2-9e8f-98c27c79c5f5)  | ![image](https://github.com/100backfro/teammate/assets/94779505/e47a47ec-ff1d-495f-b193-27159346cfa5)| ![image](https://github.com/100backfro/teammate/assets/94779505/2759c70f-4e2a-46f4-8373-2d2c529cec00)


| 캘린더 카테고리 추가  | 캘린더 카테고리 수정  | 일정 등록 |
|:-:|:-:|:-:|
![image](https://github.com/100backfro/teammate/assets/94779505/79b23aec-7790-404f-9f28-135701be4b04) |![image](https://github.com/100backfro/teammate/assets/94779505/74e9baef-1176-40ca-a61c-d9c49db24062) | ![image](https://github.com/100backfro/teammate/assets/94779505/e18821d8-aea8-449c-bec1-c3c4adaf62e6)



| 마이페이지  | 비밀번호 수정 실패<br>(비밀번호가 틀렸을 때)  | 비밀번호 수정 완료  |
|:-:|:-:|:-:|
![image](https://github.com/100backfro/teammate/assets/94779505/592c71b0-1c57-4e15-9f93-c3ccf7da424a)   | ![image](https://github.com/100backfro/teammate/assets/94779505/55f5c20c-2424-4495-b873-31cfa18d531a)|  ![image](https://github.com/100backfro/teammate/assets/94779505/8105beab-64cb-48ce-b175-25c62e3ab3a3)


| 내 팀 프로필  | 내 팀 프로필 선택 | 내 팀 프로필 수정  |
|:-:|:-:|:-:|
![image](https://github.com/100backfro/teammate/assets/94779505/99950346-de25-45d3-9785-2dce1980285c)|![image](https://github.com/100backfro/teammate/assets/94779505/8fe2a462-3659-4404-9808-0be58c9738e0)|![image](https://github.com/100backfro/teammate/assets/94779505/4e53ee1c-463c-4a47-b0d6-e7601c439f43)  

| 팀 프로필에서 <br>팀장의 권한으로 초대 코드 url 복사|다른 사용자가 해당 초대코드로 팀 참가 가능|   
|:-:|:-:|
|![image](https://github.com/100backfro/teammate/assets/94779505/fe393300-52c6-4559-89e5-36b159ddd1fe)|![image](https://github.com/100backfro/teammate/assets/94779505/5fd6e71f-b9e9-49e2-a93b-58cde68994a8)

| 팀장일때 팀 프로필 | 팀원일 때 팀 프로필|   
|:-:|:-:|
![image](https://github.com/100backfro/teammate/assets/94779505/6c200ccb-18e7-4f3d-8b01-ef604577e803) | ![image](https://github.com/100backfro/teammate/assets/94779505/ca735f0e-753d-4c7a-908f-b46f0b40131d)


## 시연 영상 

[![Video Label](https://github.com/100backfro/teammate/assets/94779505/2773d5bd-d3d6-41a1-92b2-bbc2b4f473a5)
](https://youtu.be/PN6SRZ18Meg)

## Notion 
[100% 팀 노션](https://www.notion.so/f6266a684b02452d99bcfec7a44192ed?pvs=4)

