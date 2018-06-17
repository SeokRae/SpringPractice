#### 제어 역전(IoC: Inversion of Control)

- 제어의 역전이란 ?

```reStructuredText
흐름의 구조를 바꾸는 것
```

```reStructuredText
어떠한 일을 하도록 만들어진 프레임워크에 제어의 권한을 넘김으로써 클라이언트 코드가 신경써야 할 것을 줄이는 전략
```

```reStructuredText
프레임워크에 정의되어 있는 인터페이스(interface), 추상타입(abstract)을 나의 코드에서 구현, 상속한 후 프레임워크에 넘겨주는 것이다.
```

```reStructuredText
오브젝트는 자신이 사용할 오브젝트를 스스로 생성하거나 선택하지 않는다.
```



- 스프링의 IoC

```reStructuredText
스프링 컨테이너가 생성, 관계설정, 사용 등을 제어해주는 제어의 역전이 적용된 오브젝트
```



- IoC를 구현하는 방법

  - DL(Dependency Lookup) - 의존성 검색

  ```reStructuredText
  저장소에 저장되어 있는 빈(bean)에 접근하기 위하여 개발자들이 컨테이너에서 제공하는 API를 이용하여 빈(Bean)을 Lookup 하는 것
  (DL을 사용 시에는 컨테이너에 대한 종속성이 증가)
  ```

  - DI(Dependency Injection) - 의존성 주입

  ```reStructuredText
  각 계층 사이, 각 객체(클래스) 사이에 필요로 하는 의존 관계를 컨테이너가 자동으로 연결해주는 것
   각 클래스 사이의 의존 관계를 빈 설정(Bean Definition) 정보를 바탕으로 컨테이너가 자동으로 연결해주는 것
  ```

  ```reStructuredText
  - Setter Injection
  - Constructore Injection
  ```

  ​

- Bean

- Bean Factory

```reStructuredText
빈의 생성과 관계설정 같은 제어를 담당하는 IoC 오브젝트
빈을 생성하고 관계를 설정하는 IoC의 기본 기능에 초점을 둔다.
```

- Application Context

```reStructuredText
IoC방식을 따라 만들어진 일종의 Bean Factory
어플리케이션 전반에 걸쳐 모든 구성요소의 제어 작업을 담당하는 IoC엔진이라는 의미가 더 큼
```



- Configuration metadata
- Container
- Spring Framework



[스프링 깃](https://github.com/SeokRae/SpringPractice)

[스프링](http://yuneejeong.blogspot.kr/2012/10/bean-bean-factory-appliction-context.html)