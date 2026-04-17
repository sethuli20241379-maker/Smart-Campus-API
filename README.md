# Smart-Campus-API
## RESTful API for the University’s "Smart Campus" initiative to manage Rooms and Sensors using JAX-RS. - CSA Coursework

### **Part 1 - Task 1 Question**

Default Lifecycle of a JAX-RS Resource class and in-memory data structures.

#### _Answer:_

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

You can do this by using java.util.concurrent.ConcurrentHashMap instead of a regular HashMap. This allows safe concurrent 
access. You can also use synchronised blocks or Collections.synchronizedList() when performing List operations. This 
ensures that only one thread can modify the collection at a time.

### **Part 1 - Task 2 Question**

Why Hypermedia (HATEOAS) considered a hallmark of advanced RESTful design and how it benefits client developers.

#### _Answer:_

Hypermedia, also known as HATEOAS (Hypermedia As The Engine of Application State), is a key principle of advanced RESTful API design. 
In a HATEOAS-compliant API, the server responses not only contain data, but also include links to related resources and possible next actions.

The server dynamically provides navigation information within each response, meaning that the client is not required to rely on hardcoded URLs or external 
documentation. For example, a response for a room might include links such as: /api/v1/rooms{id}, /api/v1/rooms/{id}/sensors, or /api/v1/sensors etc. 
This allows the client to discover available operations at runtime.

HATEOAS maybe considered a hallmark of advanced RESTful design because HATEOAS makes an API self-descriptive (each response explains what can be done next), 
decoupled (clients do not need to have prior knowledge of endpoint structures) and evolvable (the server can change URLS without breaking clients). 
These characteristics and allowances align with a core REST principle which is loose coupling between client and server.

Benefits of Hypermedia for client developers include reduced dependency on documentation (Clients can follow links provided in responses. 
They do not need to memorise or hardcode endpoints.), improved maintainability (Clients continues to work as long as they follow links and not fixed paths 
even if API structure changes.), simplified navigation (Developers can navigate through resources using links rather than constructing URLs manually, 
treating the API like a web application.), and better discoverability (New features and endpoints can be introduced without needing immediate client update 
because they will appear in responses.).

### **Part 2 - Task 1 Question**

Implications of returning only IDs versus returning full room objects when returning a list of rooms.

#### _Answer:_

When designing a RESTful API, returning either only room IDs or full room objects requires a choice between network efficiency and client convenience. 

Advantages of returning only IDs:
Reduced Network Bandwidth:
Because only identifiers are transmitted, the response size is much smaller.
Faster Response Time:
Less data being transmitted means quicker transmissions, which is especially beneficial for large datasets.
Better for Large Collections:
Scales well when there are many rooms

Disadvantages of returning only IDs:
Increased Client Requests:
The client must make additional API calls to fetch full details.
Higher Latency Overall:
Multiple round trips can slow down the application.
More Complex Client Logic:
The client must handle fetching and handling data.

Advantages of returning full room objects:
Fewer API Calls:
All required data is returned in a single request.
Simpler Client-Side Processing:
The client can directly use the data without additional requests no data handling.
Better User Experience:
Faster rendering in UI applications because data is immediately available.

Disadvantages of returning full room objects:
Increased Network Bandwidth Usage:
Larger payload sie, especially if objects contain many fields.
Over-fetching Data:
Clients may receive unnecessary information they don't need.

### **Part 2 - Task 2 Question**

Idempotency of the DELETE operation in my implementation and the justification for why.

#### _Answer:_

Yes, the DELETE operation is idempotent in this implementation. This is because making the same DELETE request multiple times results in the same final system 
state.

An operation is considered idempotent if repeating the same request multiple times produces the same outcome as when it is executed once. 
In the case of this implementation, the DELETE endpoint follows this principle. 

Analysis with cases:

Case 1: Room exists and has no sensors
First DELETE request:
- Room is removed from the rooms map
- Response:204 No Content
Subsequent DELETE requests:
- Room no longer exists
- Response: 404 Not Found

Case 2: Room exists but has active sensors:
First DELETE request:
- Operation is blocked
- RoomNotEmptyException is thrown
- Response: 409 Conflict
Subsequent DELETE requests:
- Same validation fails again
- Same 409 Conflict response returned 

Case 3: Room does not exist:
First DELETE request:
- Response: 404 Not Found
Subsequent DELETE requests:
- Same 404 ot Found

This confirms idempotency because the system does not change state after the first successful DELETE operation. 
Repeated DELETE requests do not recreate or modify data. It only returns different responses depending on state. 

### **Part 3 - Task 1 Question**

What happens if a client send data in a different format than @Consumes(MediaType.APPLICATION_JSON)?

#### _Answer:_

The @Consumes(MediaType.APPLICATION_JSON) annotation specifies that the endpoint only accepts requests with a JSON payload, meaning the server expects 
the request body to be in JSON format and the Content-Type header to be Content-Type: application/json.

If a client sends data in a different format (like text/plain or application/xml), JAX-RS performs content negotiation and tries to find an appropriate 
MessageBodyReader to convert the incoming data into a Java object.

If the client, for example, sends an unsupported media type and no suitable reader if found, JAX-RS automatically returns HTTP 415 Unsupported Media Type. 
This means that the server refuses to process the request and that the resources method was never executed.
If the client also, for example, sets malformed or invalid JSON (for example, set Content-Type: application/json but sends invalid JSON), the framework 
attempts to parse it and fails, resulting in a HTTP 400 Bad Request. Here, the format is correct, but the content is not parseable. 

### **Part 3 - Task 2 Question**

Filtering using @QueryParam vs Path-based filtering in REST APIs

#### _Answer:_
Using @QueryParam for filtering aligns better with RESTful design principles. Because of this, it is generally superior to embedding the filter in the URL path.
Query parameters are specifically intended for filtering, searching and refining collections. Path parameters, on the other hand, are meant to identify 
specific resources. 

The differences between the two include:
Flexibility and Scalability:
Query parameters allow multiple filters easily: /api/v1/sensors?type=CO2&status=ACTIVE while with path-based filtering this may become messy: 
/api/v1/sensors/type/CO2/status/ACTIVE. It quickly becomes harder to maintain and less readable. 
Optional Nature:
Query parameters are naturally optional: /sensors (all sensors) and /sensors?type=CO2 (filtered sensors). With path parameters, you would need separate 
endpoints or complex routing logic.
Client-Side Simplicity:
Clients can easily construct and modify query strings dynamically, making search functionality more convenient. 
REST Best Practices:
REST conventions recommend path parameters for resource identification and query parameters for filtering and searching. 

### **Part 4 - Task 1 Question**
Architectural benefits of the Sub-Resource Locator pattern and how its helps manage complexity.

#### _Answer:_
The Sub-Resource Locator pattern is a design approach in JAX-RS where a parent resource delegates the handling of nested paths to a separate resource class. 
A method returns another resource instance responsible for handling further request processing, instead of defining all endpoints in a single class.

The benefits of this approach include:
Promotes separation of concerns:
Each resource class is responsible for a specific part of the API. This keeps each class focused, making the code easier to understand and maintain. 
Improves code organization and readability:
Without the sub-resource locator approach, all nested endpoints will have to be defined in one large controller class. This would quickly become cluttered and 
difficult to navigate and so by delegating to a separate class, the structure of the API is reflected clearly in the codebase.
Enhances scalability and maintainability:
As the API grows, new functionality related to, for example sensor readings, can be added directly to the relevant class, in this case the 
SensorReadingResource, without modifying an unrelated class, for example the main SensorResource. This reduces the likelihood of introducing bugs into 
unrelated parts of the system and makes future extensions easier.
Improves reusability and testability:
Because sub-resources are independent classes, they can be tested in isolation. This allows for more focused unit testing and better code quality.
Aligns with RESTful principles:
RESTful principles treat resources as hierarchical entities. The URL structure is naturally mapped to a corresponding object structure in the code, 
making the API more intuitive for both developers and clients.

In contrast, having all nested logic in a single large controller class leads to tight coupling, reduced clarity and increased maintenance difficulty. 

### **Part 5 - Task 2 Question**
Why HTTP 422 is more semantically accurate than 404 for missing references inside a valid JSON payload.

#### _Answer:_
A 404 Not Found response indicates that the requested resources does not exist on the server. In this case, however, the endpoint is valid and accessible. 
HTTP 422 Unprocessable Entity is considered more semantically accurate because the error does not relate to the requested endpoint itself but rather to the 
content of the request payload. Returning a 404 in this case, therefore, would be misleading.

Here, the issue arises from the client's submitted data. The request body is syntactically correct JSON and can be parsed successfully, but it contains a 
logical error. For example, the roomId provided does not correspond to any existing room in the system. This means the server understands the request but 
cannot process it due to invalid semantic content. 

422 Unprocessable Entity is designed for when:
- The request format is correct, valid JSON
- The request is understood by the server
- But the request cannot be processed due to business rule violations or invalid references

Therefore, HTTP 422 is more appropriate because it correctly reflects that the request is valid in structure but invalid in meaning. In short, a 404 indicates 
the URI (address) is wrong and a 422 indicates the payload (the content) is wrong.

### **Part 5 - Task 4 Question**
Risks of exposing internal Java stack traces to external API consumers.

#### _Answers:_
Exposing internal Java stack traces to API consumers poses considerable cybersecurity risks. This is because it reveals sensitive implementation details about 
the system. These details can be used by attackers to better understand the internal structure of the system and identify and exploit potential 
vulnerabilities.

The risks include the following:
1) Stack traces usually expose class names, package structures and method calls. This reveals how the application is organised and which components handle 
which specific functionality, making it easier to target attacks.
2) Stack traces may reveal file paths and server environment details like directory structure and deployment locations. This information helps attackers 
understand the operating system, server configuration or hosting environment, helping them craft more precise attacks.
3) Stack traces can reveal internal logic and potential weaknesses. Through examining the sequence of method calls, attackers can identify where errors occur 
and infer flaws like poor input validation, null handling issues or unprotected operations. They can then lead targeted attacks like injection attacks 
or denial-of-service attacks. 
4) Stack traces may also expose sensitive data like variable values, request parameters and partial credentials if included in exception messages. This could 
lead to data leakage compromising system security. 

Exposing stack traces also violates the principle of information hiding. It increases the system's attack surface, giving attack information that would have 
otherwise require effort to discover. 

### **Part 5 - Task 5 Question**

#### _Answers:_