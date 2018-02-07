This project contains some examples explaining how an onion architecture 
can be implemented using spring boot for all infrastructure-concerns.

# fruehlingszwiebel-demo
Simpliest project containing only one aggregate with depending entities in one single onion.
The domain is just about Cars. It contains two webservice implementations, SpringMVC (already existing) and OData with Apache Olingo (Coming soon).

# Coming soon...fruehlingszwiebel-complex-demo
This project is based on a simple domain containing three aggregates in one single onion.
The domain is about Cars, Customers and Deliveries. Outer shells and assembly are implemented using Spring Boot. 

# Coming soon...cargo-demo
Contains three aggregates within a single onion. 
The domain is inspired by the cargo-example from Eric Evans Domain-Driven-Design book. 
Outer shells and assembly are implemented using Spring Boot.

# Coming soon...multi-onion-cargo-demo
This project is a little more complex and contains three aggregates, each embedded 
within its own onion. The domain is inspired by the cargo-example from Eric Evans Domain-Driven-Design book. 
Outer shells and assembly are implemented using Spring Boot.

# Coming soon...multi-module-cargo-demo
This project shows how the seperation by maven-modules cloud look like. It implements the domain from the cargo-demo
using three loosely coupled onions.