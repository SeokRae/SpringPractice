# Oracle

## Archtecture

###1. Oracle

```
오라클서버란?
오라클 데이터베이스와, 오라클 인스턴스의 합이다.
```

#### 1.1. 

##### 1.1.1. Shutdown -> Startup 

```sql
SQL> shutdown immedate
데이터베이스가 닫혔습니다.
데이터베이스가 마운트 해제되었습니다.
ORACLE 인스턴스가 종료되었습니다.
```

- nomount

```sql
SQL> startup nomount
 -- 서버 프로세스가 Parameter File 에서 구조파악 후 인스턴스 생성

SQL> select status from v$instance;

```



![nomount](C:\Users\SeokRae Kim\Desktop\nomount.png)

- mount

```sql
 
SQL> alter database mount;
 -- Control File 확인하여 DB 이상유무 검사
 -- 정상경우 Open, 이상경우 Instance recovery 수행 후 복구 진행

SQL> select status from v$instance;

```

![mount](C:\Users\SeokRae Kim\Desktop\mount.png)

- open

```sql

SQL> alter database open;
 -- Control File 내 지정된 Datafile, Redo Log File 위치확인하여 유무 검사
SQL> select status from v$instance;
```

![open](C:\Users\SeokRae Kim\Desktop\open.png)



- 결과

![element](C:\Users\SeokRae Kim\Desktop\element.png)



#### 1.2. Oracle Shutdown 절차

##### 1.2.1. Startup -> Shutdown

```sql
SQL> shutdown normal
 -- shutdown 명령 전에 접속되어 있던 사용자가 있을 경우 강제로 종료시키지 않고 해당 사용자들이 모두 스스로 접속 할때까지 기다렸다가 종료.

SQL> shutdown transitional
 -- 사용자의 접속 종료를 기다리지 않고 강제로 접속 중지 시킨후 instance를 종료. 강제로 접속을 중지시키는 시점은 Transaction이 끝나는 시점.

SQL> shutdown immediate -- 주로 많이 사용됨
 -- 사용자의 행동에 상관없이 즉시 접속을 강제 종료. 종료 시점에 commit된 데이터는 datafile에 기록하고, commit 되지 않은 데이터는 Rollback.

SQL> shutdown abort
 -- immediate 처럼 즉시 강제 종료 하나, commit 된 데이터를 저장하거나, commit 되지 않은 데이터를 Rollback 하지 않는다. 

 -- 비정상 종료이며 다른 말로 Instance Crush 라고 부른다. startup 시 SMON이 Instance Recovery를 수행하여 복구해야 한다.

```



##### 1.1.2.1. Oracle 서버



##### 1.1.3. Oracle 인스턴스

##### 1.1.4. Oracle 데이터베이스

##### 1.1.5. 물리적 구조

##### 1.1.6. 메모리 구조

##### 1.1.6.1. 시스템 글로벌 영역

##### 1.1.7. 연결 설정 및 세션 생성





##### 1.1.8. 연결 설정 및 세션 생성



##### 1.1.9. SQL문 처리

##### 1.1.9.1. SELECT문 수행 순서



![select1](C:\Users\SeokRae Kim\Desktop\select1.png)



![select2](C:\Users\SeokRae Kim\Desktop\select2.png)



![select3](C:\Users\SeokRae Kim\Desktop\select3.png)

