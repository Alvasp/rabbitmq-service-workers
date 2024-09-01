
# Spring AMQP demo
 
Sample app that demostrates an event driven design, using rabbitMQ + spring AMQP.

The chosen use case illustrate how to asynchronously process image related tasks (resizing or add watermark in this case). This is done using a direct exchange and 2 specific queues, one for each type of job. This design also uses a request / reply design pattern to let know the UI how the transformation job ended.


## Screenshots

![App Screenshot](/image-loader-ui/public/diagram.png)

![App Screenshot](/image-loader-ui/public/ui.png)

## Dependencies

- rabbit mq

docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3.13-management

- The UI, handler-api-rest and 2 workers in this monorepo. 