# spring project

## signup
* input
    * email
    * password
    * password confirm
    * phone
    * name
    * latitude
    * longitude
    * address
* output
    * /login

## login
* input
    * email
    * password
* output
    * access token
        * id

## my/** (login)

### profile:GET
* input
    * access token
* output
    * email
    * password
    * password confirm
    * phone
    * name
    * latitude
    * longitude
    * address

### profile:POST
* input
    * email
    * phone
    * name
    * latitude
    * longitude
    * address
* output
    * redirect: my/profile

### password:POST
* input
    * password old
    * password new
    * password new confirm
* output
    * status: ok


### 출력: 리다이렉트
/

## search

### 입력: GET, Query
* 단어

### 출력: 강의 배열
* 제목
* 장소
* 시작 날짜
* 끝 날짜
* 평균 평점
* 수강생 모집 여부
* 강사 모집 여부

## attend

## **

## /notice/**

## /my/**