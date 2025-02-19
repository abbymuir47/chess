# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```

## phase2 diagram link:

https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0wfQA0U7jqydAc45MzUyjDwEgIK1MAvpjCJTAFrOxclOX9g1AjYxNTs33zqotQyw9rfRtbO58HbE43FgpyOonKUCiMUyUAAFJForFKJEAI4+NRgACUh2KohOhVk8iUKnU5XsKDAAFUOrCbndsYTFMo1Kp8UYdKUAGJITgwamURkwHRhOnAUaYRnElknUG4lTlNA+BAIHEiFRsyXM0kgSFyFD8uE3RkM7RS9Rs4ylBQcDh8jqM1VUPGnTUk1SlHUoPUKHxgVKw4C+1LGiWmrWs06W622n1+h1g9W5U6Ai5lCJQpFQSKqJVYFPAmWFI6XGDXDp3SblVZPQN++oQADW6ErU32jsohfgyHM5QATE4nN0y0MxWMYFXHlNa6l6020C3Vgd0BxTKuvL5-AFoOxyTAADIQaJJAJpDJZbt5IvFEvVOpNVoGdQJNBD0WjWYvN4cA7FkGFfMlj0UxvvcfRfJ+SwrJMALnAWspqig5QIIePKwgeR6ouisTYgmhgumGbpkhSBq0uWo4mkS4YWhy3K8gagrCjAIGhpRbqdrhpT0dojrOgSBEsh6uqZLG-ogbM06zugmIwCxTJsZGHLRjA06CpARjaHoBiyWaEbwU68oSY26A8YmyawZc6E8tmuaYABIJ6YBAxkaMC6Tn0hlzq5fQ-te9mFNkPYwP2g69E5I4ueOrZfB5zaRYuZicKua7eH4gReCg6D7oevjMCe6SZJgAWXkU1A3tIACie7lfU5XNC0j6qM+3QxWg7Z-mcQKXNO0BIAAXvEiTlAAPC1+S2eZ7Ucch9g5Wh2W+phGI4XKeF8axAkcCg3DCUGAZBpJaAUXJ0oKaUkTDBANDKftRlJAAZt4wzqfImliK6J16eCMCKsqJl4WZnVlOhOXWQgeYTexvmlD0PmlX5XY5GApTBUOS6JUl66pQEkK2nu0IwAA4qOrJ5WehUXswDlphUBPVXV9ijs1N1zm1bJ2aU3VQH1A1oMNo3jYDbJTdCaHQot2F-Rq-GksgsRE6MqiwkdOnUaUBMUoTo6CtpVFJleCFq1r3G4Z27N47EoPg4LeslSU0NTAzCvjJU-SOygACS0jOwAjL2ADMAAsszdH0p6ZAaFYTF8OgIKADYR6OUHB-ObsAHKjlHewwI0sPHDbRVIyjvQO8TzsVK7o6ez7-tBzAIdh-qzmgdHscgPHTdJ3XKejunoyZ9naMrhjKWbtgPhQNg3DwEJhjy4YpMFQXQtQ5UtQNPTjPBMz6BDmno65x2AOpvbfR7y5UcwDB1v6-piGenqsJwDP4tYpL+HraS5JgCJe11rdyu60KJaSIFhUBXU5tzAgiQdbyU+vKES8YVpSw-u6e+mQ56wjniGd65pTpKSwdoGAaktAvX0G9aWukb7ggIfIN+R9gSlCfl6TIlsBapkhnDE+bsq7lF9oHS+rN84U1KBUYKzQgKn0rl7XhNcBHLmHhuQIlgtrIWSDAAAUhAHkmtRiBBjnHcmiNl6cNXpSe8LQ3ZMz-nOIck9gDKKgHACAyEoCzG4dIA+7V2YSLsQ4pxLiVgAHUWDuxqi0AAQnuBQcAADSXx3HV34VfdhcDEIACstFoFhJoqyKA0RLTfmtY6n8KQ-xagA+SQCOQgK5igcBQYer9Sga1HBlDbbggQcbJB79inuj8CQlAGDfGUH8dANxlcOAVJOlUq0NodEoFUhAZ6ugyEwI+lQ+UNDgB0P-BNUoOS0CsLshwu2MNBGnALsjAcqMEpD2SoogIXh7Fdi9LAYA2BJ6EB5ikfK54jFU3KBUCqVUap1WMII3ZgNobJLghsu+3A8BK0KTIChHoEVwimbgmZiBXnKWVMsmAD0IBPRISsrSrSTlfR+iqE2Nt2Y4rwEciGNtfzQvOf5YRRcznyKAA

## updated phase2 diagram link, with TA corrections:

https://sequencediagram.org/index.html?presentationMode=readOnly&shrinkToFit=true#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0wfQA0U7jqydAc45MzUyjDwEgIK1MAvpjCJTAFrOxclOX9g1AjYxNTs33zqotQyw9rfRtbO58HbE43FgpyOonKUCiMUyUAAFJForFKJEAI4+NRgACUh2KohOhVk8iUKnU5XsKDAAFUOrCbndsYTFMo1Kp8UYdKUAGJITgwamURkwHRhOnAUaYRnElknUG4lTlNA+BAIHEiFRsyXM0kgSFyFD8uE3RkM7RS9Rs4ylBQcDh8jqM1VUPGnTUk1SlHUoPUKHxgVKw4C+1LGiWmrWs06W622n1+h1g9W5U6Ai5lCJQpFQSKqJVYFPAmWFI6XGDXDp3SblVZPQN++oQADW6ErU32jsohfgyHM5QATE4nN0y0MxWMYFXHlNa6l6020C3Vgd0BxTKuvL5-AFoOxyTAADIQaJJAJpDJZbt5IvFEvVOpNVoGdQJNBD0WjWYvN4cA7FkGFfMlj0UxvvcfRfJ+SwrJMALnAWspqig5QIIePKwgeR6ouisTYgmhgumGbpkhSBq0uWo4mkS4YWhy3K8gagrCjAIGhpRbqdrhpT0dojrOgSBEsh6uqZLG-ogbM06zugmIwCxTJsZGHLRjA06CpARjaHoBiyWaEbwU6Kg8YmyawZc6E8tmuaYABIJ6YBAxkaMC6Tn0EmNs246tn0P7XjZhTZD2MD9oOvT2SOjkedWU5BpJ84RW2y6rmu3h+IEXgoOg+6Hr4zAnukmSYP5l5FNQN7SAAonuZX1GVzQtI+qjPt0rlzu2f5nEClzTtASAAF7xIk5QADzNeg+RWSZbUcch9jZWhWW+phGI4XKeF8axAnkmAIkBtFbloBRcnSgppSRBYqA0MpQbdX1BCJNpVFJleCFWkG8YrRq-GkhwKDcMJQY7XWe0HTp1EnRsEAXSNSQAGbeMM6nyJpYiukdemiIZeHGR1ZTodlFkIHmE3sT5pQ9N5JW+V2ORgKUQVDkunCJZ4yWbpCtp7tCMAAOKjqyuVngVF7MLZaYVFzVW1fYo5NbtLW-my1mlF1UC9f1aBDVDY3WWyU3Qmh0KLdhGMfetpKbdtUPAw9hSWqdKsoJDV0qzdz73fJaPyiJb0ISbh2ksgsQ86Mqiwlb8k2xyXMUtzo6Cm7qNPfpiFByg3tJ52isc7E+OE9jxMU6TUxS8H4yVP0xcoAAktIpcAIy9gAzAALLM3R9KemQGhWExfDoCCgA2XejlBrfzhXAByo493sMCNOTxyPVTPZ070Re86XFTl6O1d143LcwG3Hf6g5oG9-3ICDyfI8H2Po6T6M0+zwzK6JeuKUBNgPhQNg3DwEJhgpxSHlc81MdYk0qLUBoktpbBFlugIcE9Rzzw7FjVMhc+iIMcj3GAewZZ+mumrDWcC0BayJh7RCno9SwjgP-Q2WJjb4VNu6c2-1Lbx3NMdO251DDK1VrdNA7DdKJ3BF7biuFfY6UEl6TIKdYQpxDCjDhEcrQ2hjqMVSEAEa6H0MjT6QjipJ1KPIsR71F6KxodIlAOdxp50Xr+dBFcd7lHrs3HBrVOyFRphUIKzQgIYO3jXZxe83EJSShuQIlgfrIWSDAAAUhAHkajDABD7gPQWoCRblGqJSe8LQK54JnHtIcP9gCRKgHACAyEoCzEcdIZBbVFZ+JKWUipVSVgAHUWCV2qi0AAQnuBQcAADSXxam71cbg2B+DnaEJgMNYhpDbHCPlAAKwSWgWE8TzIoDREtBha0-bMIpBbYhYcjrKK4Q7HhTs+Gu0Ufojioj5D7JkHo0om1ZGOI4GcpR7IwZnSuUkh09z86GOMc88R5CPSpBQBfIeoxYTNMoK06APyIwR0EaC9GkKDmSL8FoGRo45Gx20Gi0GSlwXABgGpAl2itIgrsStBhqDgSlC2Wgax2tGUFzJu4xenjaYDnpmYRmr8WapSgKUrsXpYDAGwD-QgasgEC08WAgulRyqVWqrVYwfL-wTVJjBJZBjwQgG4HgUOLz7kenNVAL2CgaiWoZcoxAMqYBPKpbSpGVq3lmplfRB1Tq9Gg1dXgO0AptBaO9Ti15TCbUyvkYGslx1Q2wEpVGnRmLuWGJgIqZUzL9XY1KKmzlZDMm8vlvyoWgrgq8oSkAA
