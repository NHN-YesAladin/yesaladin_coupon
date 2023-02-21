# yesaladin_coupon
YesAladin Coupon은 YesAladin 서비스의 쿠폰 관련 기능을 제공하는 메시지 기반 마이크로서비스입니다.

## Getting Started

```bash
./mvnw spring-boot:run
```

## Features

### [@김홍대](https://github.com/mongmeo-dev)

- MyBatis를 사용하여 쿠폰 Bulk Insert 구현
- NHN Cloud Log & Crash를 연동하여 모니터링 환경 구축
- Spring Cloud Config를 연동하여 설정 정보 외부화

### [@서민지](https://github.com/narangd0)

- Spring Scheduler를 이용하여 선착순 발행 쿠폰의 발행 스케쥴링
- NHN Cloud의 Object Storage를 이용하여 파일 업로드 기능 구현
- Kafka를 이용하여 메시지 큐 기반의 비동기 시스템 구축
- 마이크로서비스의 트랜잭션 보장을 위한 시스템 설계
- Kafka를 이용하여 대량 트래픽에도 안정적인 서비스가 가능한 시스템 설계
  - nGrinder를 사용한 부하테스트에서 쿠폰 발행 API기준 10분간 vUsers 1000 테스트 통과 (TPS 378.3, Error Rate 1.1)
  - ![LoadTest](http://drive.google.com/uc?export=view&id=1eeqiLfIIXkIt-vP4BaLkKFt1dMwuX5Y4)

## ERD

![ERD](http://drive.google.com/uc?export=view&id=1CRsYklkKJeh9vKwkOX6h1xN9LX-Gp2JJ)

## Project Architecture

![Project Architecture]()

## Technical Issue

### (있다면 작성해주시고 없으면 Technical Issue는 지우셔도 됩니다.)

## Tech Stack

### Languages

![Java](https://img.shields.io/badge/Java-007396?style=flat-square&logo=Java)

### Frameworks

![SpringBoot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=flat&logo=SpringBoot&logoColor=white)
![SpringCloud](https://img.shields.io/badge/Spring%20Cloud-6DB33F?style=flat&logo=Spring&logoColor=white)
![SpringDataJPA](https://img.shields.io/badge/Spring%20Data%20JPA-6DB33F?style=flat&logo=Spring&logoColor=white)
![QueryDSL](http://img.shields.io/badge/QueryDSL-4479A1?style=flat-square&logo=Hibernate&logoColor=white)

### Build Tool

![ApacheMaven](https://img.shields.io/badge/Maven-C71A36?style=flat&logo=ApacheMaven&logoColor=white)

### Database

![MySQL](http://img.shields.io/badge/MySQL-4479A1?style=flat-square&logo=MySQL&logoColor=white)
![Redis](https://img.shields.io/badge/Redis-DC382D?style=flat-square&logo=Redis&logoColor=white)

### DevOps

![Kafka](https://img.shields.io/badge/Kafka-231F20?style=flat&logo=ApacheKafka&logoColor=white)
![NHN Cloud](https://img.shields.io/badge/-NHN%20Cloud-blue?style=flat&logo=iCloud&logoColor=white)
![Jenkins](http://img.shields.io/badge/Jenkins-D24939?style=flat-square&logo=Jenkins&logoColor=white)
![SonarQube](https://img.shields.io/badge/SonarQube-4E98CD?style=flat&logo=SonarQube&logoColor=white)
![Grafana](https://img.shields.io/badge/Grafana-F46800?style=flat&logo=Grafana&logoColor=white)

### Unit Test

![Junit5](https://img.shields.io/badge/Junit5-25A162?style=flat&logo=Junit5&logoColor=white)

### 형상 관리 전략

![Git](https://img.shields.io/badge/Git-F05032?style=flat&logo=Git&logoColor=white)
![GitHub](https://img.shields.io/badge/GitHub-181717?style=flat&logo=GitHub&logoColor=white)

- Git Flow 전략을 사용하여 Branch를 관리하며 Main/Develop Branch로 Pull Request 시 코드 리뷰 진행 후 merge 합니다.
  ![image](https://user-images.githubusercontent.com/60968342/219870689-9b9d709c-aa55-47db-a356-d1186b434b4a.png)
- Main: 배포시 사용
- Develop: 개발 단계가 끝난 부분에 대해 Merge 내용 포함
- Feature: 기능 개발 단계
- Hot-Fix: Merge 후 발생한 버그 및 수정 사항 반영 시 사용

## Contributors

<a href="https://github.com/NHN-YesAladin/yesaladin_coupon/graphs/contributors">
  <img src="https://contrib.rocks/image?repo=NHN-YesAladin/yesaladin_coupon" />
</a>