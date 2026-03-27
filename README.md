# Smart-Campus-API
RESTful API for the University’s "Smart Campus" initiative to manage Rooms and Sensors using JAX-RS. - CSA Coursework

Part 1 - Task 1 Question
Default Lifecycle of a JAX-RS Resource class

Answer:
By default, in JAX-RS, resource classes are request-scoped. This means a new instance is created for each incoming request. This is to maintain the stateless 
nature of RESTful services, where the server does not store any client related data between requests. Once the request is processed and the response is sent back 
to the client, the specific instance created for the request is discarded.

Because a new object is created for every request, standard instance variables defined with a class will not retain its data after the completion of a request. 
For example, if a list of sensors is stored in a normal variable, that list will be empty when a new request arrives. 

Therefore, the architectural decision must be to use static data structures or a singleton pattern in order to prevent this data loss and to maintain the state of 
the Smart Campus. If Maps and Lists are declared as static, they belong to the class instead of to an individual instance. This means the data will remain in the 
server memory as long as the application is running. 

Because the server handles many requests simultaneously using different threads, thread safety becomes a major concern. For example, if two different users try to 
create a room at the same, both of them modifying the static Map at the same time, it leads to data corruption or a race condition. To prevent this, you can 
synchronise access to the in-memory structures. 

You can do this by using java.util.concurrent.ConcurrentHashMap instead of a regular HashMap. This allows safe concurrent access. You can also use synchronised 
blocks or Collections.synchronizedList() when performing List operations. This ensures that only one thread can modify the collection at a time.