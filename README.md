# Spring MediatR

An implementation of [MediatR](https://github.com/jbogard/MediatR) on the JVM for the Spring Framework. This project is heavily inspired by
the [MediatR](https://github.com/jbogard/MediatR) project for .NET by Jimmy Bogard. Many of the concepts
from [MediatR](https://github.com/jbogard/MediatR) have been incorporated in this project.

Spring MediatR is a simply library with the goal of helping de-couple components, avoid 
fat services/controllers, and reduce the need to inject excessive dependencies. Spring MediatR 
is meant to be light and simple.

## Basics

Spring MediatR has two kinds of messages it dispatches:

* Commands - Dispatches to a single handler, can return value
* Event - Dispatches to one or many handlers, does not provide a mechanism to return value

### Request and RequestHandler

### Command

A command describes what operation should be performed, the data to perform said operation,
and the return type. You can make use of the JavaX Validation annotations in commands as the
MediatR will validate them before execution.

```java
public class CreateUserCommand implements Command<Integer> {
    
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String password;
    
    public CreateUserCommand(String firstName,
                             String lastName,
                             String email,
                             String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }
    
    // getters omitted 
}
```

### CommandHandler

A command handler as its name suggest, handles a specific command. The logic to execute/handle 
the command goes into the CommandHandler. **Command to CommandHandler is a one to one. Each command
must have only a single CommandHandler. If no CommandHandler is found for a given Command a 
NoCommandHandlerAvailableException will be thrown. Conversely if there are multiple handlers for the
same command the application will fail to start due to MultipleCommandHandlerException.**

CommandHandlers are retrieved from the Spring ApplicationContext and thus must be Spring managed beans.
The easiest way to do this is simply annotate your CommandHandler classes with @Component, or @Service.

```java
@Component
public class CreateUserCommandHandler implements CommandHandler<CreateUserCommand, Integer> {
    
    @Override
    public Integer handle(CreateUserCommand command) {
        
        //
        // logic to create the user and return the ID goes here
        //
        return 42;
    }
} 
```

### Event

### EventHandler

### MediatR



## Getting Started

Something will go here

## Troubleshooting

Something will go here

## Contributing


