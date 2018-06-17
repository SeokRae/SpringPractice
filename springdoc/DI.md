### @Inject, @Resource, Autowired

### DI(Dependancy Injection)

#### 1. 스프링없이 의존성 주입

- 의존성은 new이다

##### 1.1. 생성자를 통한 의존성 주입(스프링 x)

```java
public interface Tire {
	String getBrand();
}
```

```java
public class KoreaTire implements Tire {
	public String getBrand() {
		return "코리아 타이어";
	}
}
```

```java
public class AmericaTire implements Tire {
	public String getBrand() {
		return "미쿡 타이어";
	}
}
```

```java
public class Car {
	Tire tire;

	public Car(Tire tire) {
		this.tire = tire;
	}

	public String getTireBrand() {
		return "장착된 타이어: " + tire.getBrand();
	}
}
```

```java
public class Driver {
	public static void main(String[] args) {
		Tire tire = new KoreaTire();
		//Tire tire = new AmericaTire();
		Car car = new Car(tire);

		System.out.println(car.getTireBrand());
	}
}
```



##### 1.2. 속성을 통한 의존성 주입(스프링 x)

```java
public interface Tire {
	String getBrand();
}
```

```java
public class KoreaTire implements Tire {
	public String getBrand() {
		return "코리아 타이어";
	}
}
```

```java
public class Car {
	Tire tire;

	public Tire getTire() {
		return tire;
	}

	public void setTire(Tire tire) {
		this.tire = tire;
	}

	public String getTireBrand() {
		return "장착된 타이어: " + tire.getBrand();
	}
}
```

```java
public class Driver {
	public static void main(String[] args) {
		Tire tire = new KoreaTire();
		Car car = new Car();
		car.setTire(tire);

		System.out.println(car.getTireBrand());
	}
}
```



#### 2. 스프링을 통한 의존성

##### 2.1. XML 파일을 이용한 의존성 주입

```java
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class Driver {
	public static void main(String[] args) {
		ApplicationContext context = new FileSystemXmlApplicationContext("/src/main/java/expert002/expert.xml");
		Tire tire = (Tire)context.getBean("tire");
		Car car = (Car)context.getBean("car");
		car.setTire(tire);

		System.out.println(car.getTireBrand());
	}
}
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.springframework.org/schema/beans 
					http://www.springframework.org/schema/beans/spring-beans.xsd">

	<bean id="tire" class="expert002.KoreaTire"></bean>
	<bean id="americaTire" class="expert002.AmericaTire"></bean>
	<bean id="car" class="expert002.Car"></bean>
</beans>
```



##### 2.2. xml에서 속성 주입

```java
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class Driver {
	public static void main(String[] args) {
		ApplicationContext context = new FileSystemXmlApplicationContext("/src/main/java/expert003/expert.xml");
		Car car = (Car)context.getBean("car");

		System.out.println(car.getTireBrand());
	}
}
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.springframework.org/schema/beans 
					http://www.springframework.org/schema/beans/spring-beans.xsd">

	<bean id="koreaTire" class="expert003.KoreaTire"></bean>

	<bean id="americaTire" class="expert003.AmericaTire"></bean>

	<bean id="car" class="expert003.Car">
		<property name="tire" ref="koreaTire"></property>
	</bean>

</beans>
```

- TestCode

```java
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("expert.xml")
public class CarTest {
	@Autowired
	Car car; 

	@Test
	public void 자동차_코리아타이어_장착_타이어브랜드_테스트() {
		assertEquals("장착된 타이어: 코리아 타이어", car.getTireBrand());
	}
}
```



###### 2. @Autowired

- @Autowired 어노테이션을 사용하면 get/set접근 메서드를 만들지 않아도 
  Springframework이 설정 파일을 통해서 알아서 get/set 접근 메서드 대신 일을 해준다.
- .xml파일에 context namesapce 설정으로  <<context:annotation-config/>>를 통해 어노테이션 기능을 사용할 수 있도록 해준다.