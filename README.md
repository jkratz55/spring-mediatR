# Spring MediatR

An implementation of [MediatR](https://github.com/jbogard/MediatR) on the JVM for the Spring Framework. This project is 
heavily inspired by the [MediatR](https://github.com/jbogard/MediatR) project for .NET by Jimmy Bogard. Many of the 
concepts from [MediatR](https://github.com/jbogard/MediatR) have been adapted in this project for the JVM and Spring
Framework.

Spring MediatR is a simple library intended to help developers write cleaner more focused code. This library can also
be used to implement some of the ideas and concepts from CQRS.

## Basics

Spring MediatR has three kinds of messages it dispatches:

* Request - Dispatches to a single handler, has return value
* Command - Dispatches to a single handler, does not provide a mechanism to return value
* Event - Dispatches to one or many handlers, does not provide a mechanism to return value

MediatR supports both synchronous and asynchronous dispatching. Asynchronous dispatching methods return a CompletableFuture.
If you don't know whether or not you need to dispatch synchronously or asynchronously, you probably are fine just using
the synchronous API. The asynchronous API is best suited for longer running operations and tasks where you don't want to
block the thread handling the request. 

#### A Word About Spring's @Transactional

If you are not aware yet you should know that Spring's @Transactional does not work across threads. This is very 
important to pay attention to if you intend to use the asynchronous dispatch and emit methods of Mediator.

### Request and RequestHandler

Requests and RequestHandlers are used to provide a mechanism to make requests of the application and then have them
fulfilled by a RequestHandler. Requests have a return value, and thus could be used for queries in terms of CQRS. 
Request and RequestHandlers are flexible enough they could be used for either commands or queries. If you have a return
value it is recommended to use Commands over Requests. 

### Command and CommandHandler

Commands and CommandHandlers are very similar to Request and RequestHandlers. The difference being that Commands do not
return a value. This makes the API a little more convenient for cases where you don't want a return value. With the Java/Kotlin
type system it is not possible to have the following while in C# this would be valid.

```kotlin
interface Request

interface Request<TReponse>

```

And having to declare the return type of Request to Void is rather awkward.

```kotlin
class ReturnNothingRequest: Request<Void>

class ReturnNothingRequestHandler: RequestHandler<ReturnNothingRequest, Void> {

    fun handle(request: ReturnNothingRequest): Void {
        return null
    }
}

```

Commands and CommandHandlers also resonate closer to CQRS and Event Sourcing concepts.

### Event and EventHandler


## Getting Started

At present this library is still experimental as it has not undergone extensive testing. I would not recommend using
this library for a mission critical system at this time. I am using it for other personal projects and will continue to
update it as I come across issues.

As said above this library is still experimental so I have not published it to Maven Central or JCenter yet. In order to
use it you will need to add a repository to your build.gradle or pom.xml

### Gradle

```groovy
repositories {
	maven {
		url  "https://dl.bintray.com/jkratz55/maven-public"
	}
}

dependencies {
    compile 'io.jkratz.springmediatr:spring-mediatr:1.0-RC2'
}
```

### Maven

```xml
<settings xmlns='http://maven.apache.org/SETTINGS/1.0.0' xsi:schemaLocation='http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'>
  <profiles>
    <profile>
      <repositories>
        <repository>
          <snapshots>
            <enabled>
              false
            </enabled>
          </snapshots>
          <id>
            bintray-jkratz55-maven-public
          </id>
          <name>
            bintray
          </name>
          <url>
            https://dl.bintray.com/jkratz55/maven-public
          </url>
        </repository>
      </repositories>
      <pluginRepositories>
        <pluginRepository>
          <snapshots>
            <enabled>
              false
            </enabled>
          </snapshots>
          <id>
            bintray-jkratz55-maven-public
          </id>
          <name>
            bintray-plugins
          </name>
          <url>
            https://dl.bintray.com/jkratz55/maven-public
          </url>
        </pluginRepository>
      </pluginRepositories>
      <id>
        bintray
      </id>
    </profile>
  </profiles>
  <activeProfiles>
    <activeProfile>
      bintray
    </activeProfile>
  </activeProfiles>
</settings>
```

```xml
<dependency>
	<groupId>io.jkratz.springmediatr</groupId>
	<artifactId>spring-mediatr</artifactId>
	<version>1.0-RC2</version>
	<type>pom</type>
</dependency>
```

## Troubleshooting

Something will go here

## Contributing


