# Smart-Campus-API
RESTful API for the University’s "Smart Campus" initiative to manage Rooms and Sensors using JAX-RS. - CSA Coursework

Part 1 - Task 1 Question
Default Lifecycle of a JAX-RS Resource class
Answer:
By default, in JAX-RS resource classes are request-scoped. This means a new instance is created for each incoming request. This is to maintain the stateless 
nature of RESTful services, where the server does not store any client related data between requests. Once the request is processed and sent back to the client, 
the specific instance created for the request is discarded.