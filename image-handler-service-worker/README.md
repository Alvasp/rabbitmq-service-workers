
# Image handler worker

This is just a `Dummy` service worker that simulate some expensive image related operations. It uses rabbitmq + request/reply pattern to receive and inform operation status.



## Profiles
2 spring boot profiles are available. It tells the app what behaviour to adopt be default:

- resize: image resizing operation by default
- watermark: image watermark operation by default

## Repository
The only implementation to fetch files is through file system. In order to specify the path of the repository an env variable must be defined

APP_LOCALREPOSITORY=/path/to/some/folder


##  Rabbitmq
Producer must specify the following message properties, in order to get responses:

- reply_to: name of the queue to responde.
- correlation_id: identifier of the operation.

For input messages, the following Exchanges & queues configuration are used (haredcoded):

- exchange name: image-processing-exchange
- queue for resizing: mage-processing-queue-tasks-resizer
- queue for watermark: image-processing-queue-tasks-watermark

## Examples

--starting app to handle image resizing

java -jar image-worker-0.1.1.jar --spring.profiles.active=resizer --APP_LOCALREPOSITORY=/Downloads/repo

--or start the app to handle water mark adding

java -jar image-worker-0.1.1.jar --spring.profiles.active=watermark --APP_LOCALREPOSITORY=/Downloads/repo