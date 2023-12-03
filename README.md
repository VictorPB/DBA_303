# Prácticas DBA 23/24 - Grupo 303

![Badge en Desarrollo](https://img.shields.io/badge/STATUS-EN%20DESAROLLO-green)
![Badge Java](https://img.shields.io/badge/Java_+_JADE-00599C)

Repositorio del grupo de trabajo 303 de la asignatura DBA, ETSIIT, UGR

![LOGO UGR](https://secretariageneral.ugr.es/sites/webugr/secretariageneral/public/inline-files/UGR-MARCA-02-monocromo.png)

***


## Índice

* [Integrantes](#Integrantes.)
* [Recursos Externos](#recursos-externos)
* [PRÁCTICA 2](#PRÁCTICA-2.-Movimiento-de-una-agente-en-un-mundo-bidimensional)
* [PRÁCTICA 3]

***


## Integrantes.

* [![Static Badge](https://img.shields.io/badge/Jorge_Bailón_González-grey?logo=github)](https://github.com/giorgiogiovanni)
* [![Static Badge](https://img.shields.io/badge/María_Florencio_Díaz-grey?logo=github)](https://github.com/mariafd412)
* [![Static Badge](https://img.shields.io/badge/Carlos_Pérez_Cruz-grey?logo=github)](https://github.com/capcrz12)
* [![Static Badge](https://img.shields.io/badge/Víctor_Pérez_Barranco-grey?logo=github)](https://github.com/VictorPB)

***


## Recursos Externos

[Carpeta compartida Drive](https://drive.google.com/drive/folders/1so4ijG-vE99j2p71qAZ6ro4TsgbWVNi4?usp=drive_link)


## PRÁCTICA 2. Movimiento de una agente en un mundo bidimensional

### Documentos

+ [**Enunciado**](/docs/DBA_P2_Enunciado.pdf)


### **ESTADO FINAL ENTREGA:**

[**_Release P2_**](https://github.com/VictorPB/DBA_303/releases/tag/P2) -
_Tag_ [**P2**](https://github.com/VictorPB/DBA_303/releases/tag/P2-main) _en main_


### Evaluación de la Práctica

* Correcta resolución de los mundos (3ptos)
  * Mundo sin obstáculos y mundo con obstáculos básicos (1pto)
  * Mundo con obstáculos cóncavos (1pto)
  * Mundo con obstáculos convexos (1pto)
* Correcta resolución de un mundo sorpresa (1.5ptos)
* Código. Se valorará la correcta resolución de los ejercicios, el diseño e implementación de los distintos algoritmos y
del sistema completo (3ptos)
* Defensa de la práctica. Al igual que en las prácticas anteriores, en clase de prácticas se evaluará el funcionamiento
y se pedirá que se expliquen ciertas partes del código (1pto)
* Presentación. En clase de teoría se realizará una presentación de 5 minutos explicando las decisiones de diseño e
implementación que se han tomado, así como los errores cometidos y como se han resuelto los mismos. La presentación se
evaluará de forma colectiva (1.5ptos)


#### Documentos a entregar

- [x] Código fuente.
- [x] Memoria. Arquitectura de clases.
- [x] Memoria. Decisiones de diseño e implementación.
- [x] Presentación y/o vídeo.


#### Ejercicios a resolver

Como se comentó al principio, el agente tendrá que ser capaz de dirigirse hacia su objetivo de la forma más eficiente
posible, evitando obstáculos, y por supuesto, evitando salirse de los márgenes de su mundo.

Se deben resolver, como mínimo, los siguientes mapas:
* Sin obstáculos, yendo directo al objetivo.
* Con obstáculos básicos. Una línea horizontal de obstáculos, así como una línea vertical.
* Con obstáculos cóncavos. El agente tendrá que ser capaz de enfrentarse a obstáculos en forma de U invertida.
* Con obstáculos convexos. El agente tendrá que ser capaz de enfrentarse a obstáculos en forma de U.


## PRÁCTICA 3. Comunicación entre Agentes

### Documentos

+ [Enunciado](/docs/DBA_P3_Enunciado.pdf)
+ [Tutorial de Comunicación](/docs/DBA_P3_TutorialComunicacion.pdf)

### Estado final de la entrega


### [Resumen Enunciado](/docs/DBA_P3_Enunciado.pdf)

Tras una gran ventisca, _Santa Claus_ he perdido algunos de sus renos. Para conseguir encontrarlos,
recurre a pedir ayuda a un Agente que se encargará de encontrarlos siguiendo las indicaciones de
_Rudolph_. El objetivo principal es ayudar a _Rudolph_ a rescatar a los renos perdidos.

En el problema habrá tres agentes distintos: Santa Claus, _Rudolph_ y nuestro agente buscador, que se
comunicarán para conseguir, colaborativamente, solucionar la situación:

+ El agente se presentará ante _Santa Claus_ indicando que se presenta a la misión. Éste valorará si
es adecuado para ésta, determinando al azar que lo es en el 80% de los casos.
  + Si es rechazado, el agente terminará la ejecución.
  + En caso de ser aceptado, le dará un código secreto para comunicarse con _Rudolph_.
+ Se establecerá un canal de comunicación secreta con _Rudolph_ usando el código secreto, a lo que
_Rudolph_ contestará aceptando o rechazando el código en función de su validez.
+ Una vez aceptado, Rudolph empezará a comunicarle coordenadas de cada uno de los renos.
+ El agente recogerá al reno, y lo llevará de vuelta con _Rudolph_. Una vez entregado se informará
a _Santa Clauss_ del avance, y se volverán a pedir coordenadas.
+ Al finalizar, le pediremos las coordenadas a _Santa Claus_, y nos dirigiremos hasta esa posición.
+ Al llegar, enviaremos un mensaje para que sepa que ya estamos allí y, si todo ha ido bien, él nos
contestará un "HoHoHo!"


### Evaluación

1. Correcto funcionamiento de los agentes (3 puntos)
2. Diseño del protocolo de comunicación. Se deberá explicar el diagrama de secuencia, las
performativas empleadas, y el motivo de su elección. (2 puntos)
3. Código y memoria. Se valorará la correcta resolución de los ejercicios, el diseño e
implementación de los distintos algoritmos y del sistema completo (3 puntos).
4. Defensa de la práctica. Al igual que en las prácticas anteriores, en clase de prácticas se
evaluará el funcionamiento y se pedirá que se expliquen ciertas partes del código. (1 punto)
5. Presentación. En clase de teoría se realizará una presentación de 5 minutos explicando las
decisiones de diseño e implementación que se han tomado, así como los errores cometidos y como se
han resuelto los mismos. La presentación se evaluará de forma colectiva (1 puntos)


#### Documentos a entregar

* [ ] Código fuente
* [ ] Memoria explicando la estructura de clases, diagrama de secuencia del protocolo, así como las
decisiones de diseño e implementación tomadas
* [ ] Presentación o video que se usará el día de la presentación.