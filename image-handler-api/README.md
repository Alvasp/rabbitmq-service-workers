
# Image Api Loader

This is just a `Dummy` rest api that handle request from a ui, and resolve request by adding async message to a rabbitmq queue that'll be handled by some service worker. It currently use a in-memory repository to keep track of jobs.


## Api Contract

http://localhost:8080/swagger-ui/index.html

the following operations are defined:

- addJob: add a new image processing job to the queue
- getJob: get the status of the current jobs from the queue
- download: download the result file related to the job

## Repository
Some operations required user to upload a file to resize or add watermark, in order to store the file the following env variable is required

APP_LOCALREPOSITORY=/path/to/some/folder


##  Rabbitmq
It uses the following exchange configuration to send and receive messages:

- request exchange: image-processing-exchange + (`resize` or `watermark` routing key)  
- responses queue: image-processing-queue-tasks-response (default exchange)
## Examples

--starting app to handle image resizing

java -jar rest-api-0.1.1.jar --APP_LOCALREPOSITORY=/Downloads/repo

