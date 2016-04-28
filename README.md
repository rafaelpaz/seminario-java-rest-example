SEMINARIO-REST-SERVICE-EXAMPLE
================================

#### Apresentação:

Esse é um exemplo simples de aplicação que disponibiliza serviços REST, como também como consumir esses serviços no JAVA

#### Informações Técnicas:

Dentre as tecnologias adotadas, as principais são:

- JPA 2.1
- Hibernate 5
- Weld 2
- Wildfly 9
- JERSEY 2.22.1
- Jackson 2.6.4
- Genson 1.4

#### Client REST

Exemplos de como criar um Client REST estão na classe de testes unitários (SeminarioRestTest)

#### Cross-domain request 

Esse exemplo contém uma filter (HeadersResponseFilter) que habilita o Cross-domain request