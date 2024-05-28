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



[https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0wfQA0U7jqydAc45MzUyjDwEgIK1MAvpjCJTAFrOxclOX9g1AjYxNTs33zqotQyw9rfRtbO58HbE43FgpyOonKUCiMUyUAAFJForFKJEAI4+NRgACUh2KohOhVk8iUKnU5XsKDAAFUOrCbndsYTFMo1Kp8UYdOUyABRAAyXLg9RgdOAoxgADNvMMhR1MIziSyTqDcSpymgfAgEDiRCo2XLmaSYCBIXIUNTKLSOndZi83hxZj9tgztPL1GzjOUAJIAOW54UFwtG1v0ryW9s22xg3vqNWltDBOtOepJqnKRpQJoUPjAqQtQxFKCdRP1rNO7sjPq5ftjt3zs2AWdS9QgAGt0OXozB69nZc7i4rCvGUOUu42W+gtVQ8blToCLmUIlCkVBIqp1VhZ8D+0VqJcYNdLfnJuVVk8R03W2gj1N9hPKFvsuZygAmJxObr7vOjK8nqZnseXmBjxvdAOFMLxfH8AJoHYckYB5CBoiSAI0gyLJkHMNkjl3ao6iaVoDHUBI0HfAMUCDBYlgOLCQUKDddw-Gsv0+J4bSWXY+gBc5NyVbVDHKBAEKQNBYXgxDUXRWJsUHXVe2TMkKUzbNc0YsiYFY95CyZZM3Q5cheX5QU-wvcVJU7BsYAAdRYSsuWrO4YAAXjs-MYAUL1pDU4NbUczyKPeHsi206cB2VIczOzc9x2k4KziBXdRKElc10wOiQR4korgGA8mJ-PojPQdiqOKO8YofDBn1fd8ss-e5cvyy9-jMTgwO8PxAi8FA21E3xmGQ9JMkwMrmHS7DpH0+ouWaFoCNUIjunq28aNiudMvqwqUq4kqQt4-iEJ62F6qk0KZMCllyg4FBuEyRSc0OgKtIVUtdLGvkJpgAAxcIagAWXC1JLOs8JbPqnyFuimdNvnNUNQ2uK0u2jK9yKncoBi+B0PKmAXzfXoDhAlqIMCSEODg6EYAAcXzVk+tQwaMeGhHsPJrk8Jaex83mhtIrQRa2VS1auf-dbUsw0L+OhSnRlUWFNJdEtCjLbk+QFGAACoPq+372alicpwhuHykllAeWhWG5y3ajMu1tRxgqFx7caZHjlK+mKpx-prdUW37ZcR2mtAzxWsg7AfCgbBuHgY1MgplyaYGobRZR8ocIaKbrc5iL-3fa2vXzJ2tuW4EBczi92KeHPD0akX0vBQ0o5QI3YTgeujfEjEjt4k6HoNNMTUbivAz+7nZb7J7PQrKsB9UkGoxjT3dYTfWVpj0Zm-TAhEiShB10hi3ivnD38w9aRxhgABGJ8AGYABZ8-h9GcjAN2qut4-T4vm+8eawPCYCSxLoEskGAAApCAQkV6GACDoBAoBmx00fonRG1RKSs3TsEQWF53zh2AP-KAcAIACSgLMV+0g7580hsXUcpdmJzBgbg-BhDT65RIe-K+t8zbcQRrXAAVmAtAjdQFCVbigNE7cF6GETLJM6MBeFCPzLCbB9CCHQGIUfaQI8goK10pSKo0gFBvU9npQUijKAMOgD5ExeDlGwCsjZGAJCfIkPEVufmMBoaamriNA+ZCXaP2frjf2BM2oBC8Dg9G6ZYDAGwOHQg8REgpH6mhBBXjk4vRZpNVoxheZLyLkjDhBdBypm4HgGWzjJGnR7sUqAjI9AGFKUmR6WjyhpLep9H61YADcMhxq2TaVrKmXSWm9M1n9DpZScm7jVB0sZnjGbeOyacIabt-E9C-qBIAA
](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0wfQA0U7jqydAc45MzUyjDwEgIK1MAvpjCJTAFrOxclOX9g1AjYxNTs33zqotQyw9rfRtbO58HbE43FgpyOonKUCiMUyUAAFJForFKJEAI4+NRgACUh2KohOhVk8iUKnU5XsKDAAFUOrCbndsYTFMo1Kp8UYdOUyABRAAyXLg9RgdOAoxgADNvMMhR1MIziSyTqDcSpymgfAgEDiRCo2XLmaSYCBIXIUNTKLSOndZi83hxZj9tgztPL1GzjOUAJIAOW54UFwtG1v0ryW9s22xg3vqNWltDBOtOepJqnKRpQJoUPjAqQtQxFKCdRP1rNO7sjPq5ftjt3zs2AWdS9QgAGt0OXozB69nZc7i4rCvGUOUu42W+gtVQ8blToCLmUIlCkVBIqp1VhZ8D+0VqJcYNdLfnJuVVk8R03W2gj1N9hPKFvsuZygAmJxObr7vOjK8nqZnseXmBjxvdAOFMLxfH8AJoHYckYB5CBoiSAI0gyLJkHMNkjl3ao6iaVoDHUBI0HfAMUCDBYlgOLCQUKDddw-Gsv0+J4bSWXY+gBc5NyVbVDHKBAEKQNBYXgxDUXRWJsUHXVe2TMkKUzbNc0YsiYFY95CyZZM3Q5cheX5QU-wvcVJU7BsYAAdRYSsuWrO4YAAXjs-MYAUL1pDU4NbUczyKPeHsi206cB2VIdfJDfzgAQZgqi8pYYE2MAQFSNRMBgbdJwTGcuN3ck0EoDMG3PdBlPpNKziBO9goy8FDWNTJFJzUi6yK-9sXS6Tqro+cjPHDriiq04HwwZ9X3fAYDyYn8+l6y9-hgMwEAMGBYr8jgjAgNQ0AAcmYRLkvK7qt2oq5vigbwoBWD00CoKKkHW0iYGgcLbQ48rqPvdCRpgAAWJwAEZxsoC6rpuu6HsmwxnvU5YDhAsxODA7w-ECLwUDbUTfGYZD0kyTBhuYHiSnKCppH0+ouWaFoCNUIjulm28aIqudTtm9jOMqpnB3KW7kC0BqG1hWapNCmTApZeSwEa0razM7NirQTSXRLQoy25PkBTl0djIlCApRHSzrPCWzZp8hmkwVarua1hXMCi5hGoVmAUAAD2hDR+t4sWtIlowUG4AWlOF8qLddUtdLJvkKZgAAxcIagAWS1w2bJt-8zdai93tFrqcvnNUNUOvPjoG+cegOT2Smq+AvrAUa316OGlsMR3044TbWTQCBmFd6Ei85kudzLs6QYma7efurXzEIeJEhgSALzeyvBsKOAeRyOvfoBoHzugUGJ-Wkdp5AWekgX9A3vh+HPGRyDIXW9fYhgABxfNWRx1D8drzDS5J5+uTwi0ew+Z6aZ3HB9bKnNWZgLmqsDmc4f68R5uDQqQcYEiy9omWSvtyRS0Fs1NOF4lZ9nDpyfSmtTa631uZKyqdTZOXNtgsORNaoM3tq5GBzs3axA9jVBMBImEphgMgWIr9RiqFhO1GQgidJkI1oKAAVLHeOSdgHiOzpgyBLMX75kflgdKR0ra-z3H0NRahxgVBcFYxoFcMorxrhveu40zGqAsVYlwNiEagWvuBFGARsA+CgNgbg8B6qGDEYYD+eMCaIOJpUWoDQqZmNAfLf874zFenzFRUubJurQNSYvZiUwMmHn+JgQxIUkEwAPqgnMwdOoCPFgaXB0sCHB1DirdkciDKELbFQ5OtDja9KSAwmBAUfbMMqZlMKbDoocIKW2XuPCNHTO9srVMYSImwhKYGYZUiOmyPLL6QUOzVKmyjDGFxKypxaOBOUCJcB6pERXGufuCCib0VMfmD00hxgwH+k+AAzD9WxEChq1ycb0L5owfl-IBcCvYC1-bLVbsZduagYBdx7tw-RzNuIfLKP0YGe8x5gz5lPDAM8CBz3PpeUFOTqoE3KH9QGULiWXVJQfClDBT7z3-JfRGPjb6BEsP7ASyQYAACkIBCR0aKAIOgECgGbF-DesTsJVEpIA5JwQYHvmCcAUVUA4AQAElAWYZifnZKHrkvO+TtYXyKc8JVRqTVmr+dNS1vzyjwpBeU4uLCVTVJQYHOp6CJxTkaRMoRLT8GQxagsxW4zlaHPVj0yhpkDaDJNpw0Zibk0kMDTMsZ7DUWLJxbwhp0imlCIAFYyrQFsg1rrTXQAtd86Q+yZGkJgJSKo0gFDRxcXpQUzbKBuuek5R62aYBep8l665WVaJ2sxeqTUBiA0EquPSm1jKIUwBfA3cui0UWcPRZ3buXC+4boHkYoep12X73BtyqlRE+WLx3ccPdjit6sqJbvDlfRx7PqPpSk+1Kz78rhoKxGN8IKoygIamu6ZYDAGwME19c8oloTVVuyokcAGU1aMYRmtqoF7ngdxKZtUQDcDwJIiN-Dq3RtTHRqAjI9AGAYwcntBHo5x0TtWAA3DIcmtkBOqLfiJvj4mVFayE4xwwudyNqiEwpwxeHj1gtXvuw9kLj3wyAA)
