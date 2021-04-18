# Description
This project simulates an environment where IoT devices are sending continuous data
to a data collection platform. It has been built in such a way that it is:

- Scalable and extendable to work with more IoT devices
- Fast
- Self-contained

# Requirements
- Maven (http://maven.apache.org)
- Docker (http://www.docker.com)

Note: It is recommended to increase the available 2GB memory of Docker (ex. to 6GB).

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
|TYPE|Type of sensor|TEMPERATURE, FUEL, HEART_RATE|
|CATEGORY|Category of sensors|ENVIRONMENTAL, MEDICAL, TRANSPORTATION|
|FROM|Timestamp boundary (in milliseconds) from which the reading was taken|Ex. 1618647849594|
|UNTIL|Timestamp boundary (in milliseconds) from which the reading was taken|Ex. 1618647849594|

The following request headers are required:

|HEADER|VALUE|EXAMPLE|
|---|---|---|
|Authorization|Bearer <token>|Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbmFsaXN0IiwiYXV0aCI6W3siYXV0aG9yaXR5IjoiUk9MRV9BTkFMSVNUIn1dLCJpYXQiOjE2MTg2NDQyODN9.4hlDKWmXssVOs1Zj2Y_rSC5ls62PEQMj7YTBXSKDgxQ| 
|Accept|application/json||
|Content-Type|application/json|

A sample authentication token is printed in the server logs of the 'statistics' container(s). Look for "*** AUTHENTICATION TOKEN"

# Limitations

- Security