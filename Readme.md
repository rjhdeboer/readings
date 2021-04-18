# Description
This project simulates an environment where IoT devices are sending continuous sensory data
to a data collection platform. The sensory data is sent out every second. 

It has been built in such a way that it is:

- Scalable and extendable to work with more IoT devices
- Fast
- Self-contained

The following image depicts the architecture of the platform:

![Architecture](architecture.png)

The architecture is based on the Command Query Separation model (CQRS). The IoT gateway
acts as a command service, being a producer of Kafka. The Statistics service acts as a
query service, being a consumer of Kafka. The advantages to this model are that both services
can be scaled separately and can employ their own domain model.

# Requirements
- Maven (http://maven.apache.org)
- Docker (http://www.docker.com)

Note: It is recommended to increase the default available 2GB memory of Docker (ex. to 6GB).

# Usage

Make sure Docker is running. Then run the following commands on the commandline from
the root of the project.

To build the project artifacts: \
`mvn clean install`

To startup the platform in Docker:\
`docker-compose up -d --scale statistics=2`

Notes:
- The number of statistics instances can be scaled arbitrarily
- nginx takes a few seconds (10-20) to start up. The statistics endpoint might not be available 
  until then.

To retrieve statistics about the various readings taken:\
`GET http://localhost:4000/statistics?type=[TYPE]&category=[CATEGORY]&from=[FROM]&until=[UNTIL]`

|PARAM|DESCRIPTION|ALLOWED VALUES|
|---|---|---|
|type|Type of sensor|TEMPERATURE, FUEL, HEART_RATE|
|category|Category of sensors|ENVIRONMENTAL, MEDICAL, TRANSPORTATION|
|from|Timestamp boundary (in milliseconds) from which the reading was taken|Ex. 1618647849594|
|until|Timestamp boundary (in milliseconds) from which the reading was taken|Ex. 1618647849594|

The following request headers are required:

|HEADER|VALUE|EXAMPLE|
|---|---|---|
|Authorization|Bearer <token>|Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbmFsaXN0IiwiYXV0aCI6W3siYXV0aG9yaXR5IjoiUk9MRV9BTkFMSVNUIn1dLCJpYXQiOjE2MTg2NDQyODN9.4hlDKWmXssVOs1Zj2Y_rSC5ls62PEQMj7YTBXSKDgxQ| 
|Accept|application/json||
|Content-Type|application/json|

A sample authentication token is printed in the server logs of the 'statistics' container(s). Look for "*** AUTHENTICATION TOKEN"

# Limitations

Although the platform provides a good base and demonstrates the CQRS model well, it is not production-ready 
and has several limitations. Most of these limitations are not difficult to overcome or require little effort.

**IoT devices**
- The devices themselves are a gross oversimplification of their real life representations. They only focus on emitting 
  a reading every second to the data collection platform and provide little functionality otherwise
- The readings themselves are random values between somewhat realistic boundaries for the type of sensor

**IoT gateway**
- The command sent by the IoT gateway are themselves not sent to Kafka. This means that there is
  a small opportunity for the command to be lost if the instance crashes before producing the event.
- Although this service can be scaled as well, some extra configuration is needed in nginx to create a reverse proxy so
  that the IoT devices are able to communicate with the load balancer.

**Statistics**
- Security is handled by means of JWT tokens. No authentication server is provided however, which means tokens can not be 
  generated through an API. A sample token is provided on startup and printed on the command line.
- Event data consumed from Kafka is stored in an in-memory repository. Although it will take a while for the instance 
  to go out of memory, it eventually will. Furthermore, the more event data is collected, the worse the queries will
  perform. Due to the prototype nature of this implementation, this will not be a viable production-ready solution.
  A better approach would be to store the event data in a NoSQL column store such as _Cassandra_ or a document store
  such as _Elasticsearch_. To keep this implementation as lightweight as possible, while still demonstrating the power
  of the CQRS model, these options were left out. It should however not be that difficult to add implementations for
  these data stores due to most of the code talking to interfaces.

**Kafka**
- Only a single Kafka instance is set up by default. This means no replication or partition balancing is possible. 
  Adding more instances requires little effort however.