# About

The ideia is to construct a microservice capable of:

- Process order with RabbitMQ


- Create an API REST that allows us to consult:
  - The value of an order;
  - The quantity of orders by client;
  - A list of orders made by a client.


- The API has to be Restfull;
- We can use (Java, Javascript, .Net, etc);
- Any structure of storage (MongoDB, PostgreSQL, MySQL, etc).

[Notion Bruno Grana](https://brunograna.notion.site/Desafio-Backend-BTG-Pactual-Build-Run-3f48048e3e594fbea580c006eac6ff08)

[Github BTG Challenge](https://github.com/buildrun-tech/buildrun-desafio-backend-btg-pactual/blob/main/problem.md)

We are going to use **MongoDB**!

# Technical requirements

This ⬇️ is an example of a message that's going to be consumed by the RabbitMQ queue.

![img.png](img.png)

With this message, we have to structure how the data modeling will be.

❗❗Remember
-

- We have to inform the total value of an order;
- The quantity by client;
- A list of orders by client.

# What is RabbitMQ?

It's a system that's based in queue that implements the AMQP protocol (Advanced Message Queuing Protocol). It's used to
allow asynchronous communication between two different components from a system (or between different systems).

Basically it's just like a "mail", where messages are sent to queues and can be consumed by one or more people.

## Concepts

### 1 - Messages

Are data that you wish to transmit. It can be a text, JSON, binary ou any format of your choice.

### 2 - Producer

It's the application that sent the messages to a queue in RabbitMQ.

### 3 - Consumer

The application consumes the messages of a queue.

### 4 - Exchange

Responsible for receiving messages from producers, directing them to the proper queues (it use some routing rules).

### 5 - Queue

Where the messages are stored until are consumed. A queue can be consumed by one or many consumers.

### 6 - Binding

Connection between a **exchange** and one **queue**, it defines how the messages are routed.

## How does it work?

### 1

The producer sends a message to an exchange.

### 2

The exchange directs the message to one or more queues (it depends on the config: outing key, type of exchange).

### 3

The consumer reads the message (that are in the queue).

## Types of exchange

### Direct exchange

Messages are rooted to the queues using an exact **routing key**.

### Fanout exchange

Messages are sent to all the queues that are linked to the exchange, they don't use the **routing key**.

### Topic exchange

Messages are rooted using patterns or jokers (curinga) in the **routing key**.

### Headers Exchange

Routing is done based on message headers.

## When to use it

### Service decoupling (desacoplamento)

Components can be independent and change messages without the need to use direct communication.

### Asynchronous processing

Messages can be queued and processed in the background, increasing system performance.

### Communication between different systems

Allow that systems that are written in different languages or on different platforms to communicate. 

### Load distribution

Multiple consumers can process the messages from the same queue, dividing the workload.

# Configuring RabbitMQ and MongoDB with Docker

We'll create a package named ``local`` on the router. Inside, we'll create an ``docker-compose.yml``.

## DockerCompose

Here, we'll define all the services that we need inside our application.

The image of the ``rabbitmq`` service will be: "rabbitmq:3.13-management". This version has a painel that we will manage
everything that happens inside our RabbitMQ.

We'll have two ports:

- 15672:15672: where the painel will be;
- 5672:5672: where application will connect.

```dockerfile
services:
  mongodb:
    image: mongo
    ports:
      - 27017:27017
    environment:
      - MONGO_INITDB_ROOT_USERNAME=admin
      - MONGO_INITDB_ROOT_PASSWORD=123

  rabbitmq:
    image: rabbitmq:3.13-management
    ports:
      - 15672:15672
      - 5672:5672
```

> cd local
> docker compose up

To test the MongoDB, we'll open the MongoDB Compass.

Set up a new connection: ``mongodb://localhost:27017``.

In "Advanced Connection Options" go to "Authentication" > username/password will be the same stuff from the file above.

You can access the RabbitMQ dashboard in: ``localhost:15672``. Username: guest / password: guest

# Configuring Spring Boot and MongoDB

We'll go to the resource package > application.properties.

```properties

# autenticação na base de administrador
spring.data.mongodb.authentication-database=admin

# fará a criação dos indices de forma automática
spring.data.mongodb.auto-index-creation=true

spring.data.mongodb.host=localhost
spring.data.mongodb.port=27017

# qualquer nome, o spring vai criar ao subir a aplicação
spring.data.mongodb.database=btgpactualdb

# mesma coisa do docker-compose
spring.data.mongodb.username=admin
spring.data.mongodb.password=
```

# Configuring RabbitMQ queue

Create a ``config`` package and a class ``RabbitMqConfig``.

```java
@Configuration
public class RabbitMqConfig {
    
    // This is the queue that we want to consume
    public static final String ORDER_CREATED_QUEUE = "btg-pactual-order-created";
    
    //insert the method to convert the JSON
  
    //insert the method to create the queue
}
```

![img_1.png](img_1.png)

# Features

- [ ] [Map the entities (Order, OrderItem)](#entities)
- [ ] [Create the RabbitMQ queue listener](#creating-rabbitmq-queue-listener)
- [ ] [Create the save implementation (saving the order on MongoDB)](#create-the-save-implementation-saving-order-on-mongodb)
- [ ] [Test the flow (rabbitmq -> spring -> mongodb)](#testing-the-flow-rabbitmq---spring---mongodb)

![img_2.png](img_2.png)

This ⬆️ is a "queue contract". The json object has 2 entities:

1. Order - codigoPedido, codigoPendente, itens (uma lista)
2. OrderItems - A list of itens (that goes inside the Order)

# Functionalities

- [ ] [Endpoints]()
  - [ ] [Total orders by client]()
  - [ ] [Total value of an order]()
  - [ ] [Order quantity by client]()

## Important stuff for the entities

❗Eventually, we are going to calculate the total value of an order, the quantity of orders by client and the list of
the orders by client.

❗Remember: MongoDB uses @Document for entities and @MongoId for Ids!

And also, since we are going to do a lot of queries searching for a specific client, we can use 
``@Indexed(name="customer_id_index")``. Everytime we make a search on the database, it'll be faster.

MongoDB stores BigDecimal as a String, to save with numeric value we can use ``@Field(targetType = FieldType.DECIMAL128``.

## Entities

### Order

```java
```

### OrderItem

```java
```

## Creating RabbitMQ queue listener

Create a package ``listener`` and a class ``OrderCreatedListener``, this class will be a @Component.

This class will be responsible for consuming our queue.

It'll be a public method (void) and we can pass the @RabbitListener! Inside of it, we pass the queue name.

After we consume this queue from RabbitMQ, we have a specific contract to use, it's a interface called "Message" (from
Spring Messaging). 

Inside this contract, we'll use a Generics. In this case, the payload of the message that we are going to consume. 

Thus, we need to create a [DTO](#dto).

This DTO will have everything that is returned in that json object (codigoPedido, codigoClient, Itens).

We also have to create a DTO for the list of itens :).

### DTO

### Inside the method

Since we are using this Message interface, we can have access to the payload and the headers (who came from the queue):

![img_3.png](img_3.png)

The message by default will be a JSON object, we need to define a JSON converter.

We'll create a bean inside the RabbitMqConfig, responsible to make these payloads conversions.

```java
```

To check the created queue go to: ``localhost:15672/queues``.

To know if our application is consuming the queue properly, we have to publish a message.

Queues and streams > scroll down > publish message > 

**Headers**: we are not going to use this time

**Payload**: we'll use the json object from the challenge itself:

```json
   {
       "codigoPedido": 1001,
       "codigoCliente":1,
       "itens": [
           {
               "produto": "lápis",
               "quantidade": 100,
               "preco": 1.10
           },
           {
               "produto": "caderno",
               "quantidade": 10,
               "preco": 1.00
           }
       ]
   }
```

As soon as we published it, we can check the log on the application (if we inserted the Logger).




## Create the save implementation (Saving Order on MongoDB)

Now we'll get the message from the queue and save it on the database.

### OrderRepository

Extends from MongoRepository.

```java
```

### OrderService

We'll receive the OrderCreatedEvent and convert to an entity.

We can use streams to set the items and after, create a method for it.

```java
```

After finishing the service, we have to join te OrderService with the listener.

## Testing the flow (rabbitmq -> spring -> mongodb)

Import the OrderService, inject on the constructor.

Inside the listen method, use the ``.save()`` inserting the payload (the message itself).

After that, we can run the application and publish the message on RabbitMQ.

Checking the MongoDB Compass, the register should be in the database.

