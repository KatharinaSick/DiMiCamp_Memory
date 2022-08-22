# DiMi Workshop: Memory

## Intro

* Show the game that we will code
* Explain the benefits of having the game logic on a server (multiple players on various clients, save high scores etc)
* Briefly talk about REST & Spring Boot (https://docs.google.com/presentation/d/14zxRD0DiGX21pDXrC-fBxWp4iToVGXaqKinmkbY_bSg/edit?usp=sharing) & why we are using it

## Init & Run

* Open empty folder in VS Code
* Install extension: [Spring Initializr Java Support](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-spring-initializr)
* Open command "terminal" with [Ctrl + Shift + p] and type "Spring"
* Select "Spring Initializr: Create a Gradle project...", enter the following options and generate the project into the currently opened folder
    - Spring Boot version: 2.7.3
    - Language: Java
    - Group id: any, e.g. "net.coderdojo.linz"
    - Artifact id: "memory"
    - Packaging type: Jar (doesn't matter)
    - Java version: 18
    - Dependencies: Spring Web
* Open `MemoryApplication` and click on the Play button in the top right to run the app

## Server Status

* Open frontend: https://react-ts-nhw22s.stackblitz.io/ - tiny url: https://tiny.one/dimi-memory
* Check Server status on the top - it should still state "Kein Server gefunden" (unfortunately I couldn't differentiate server not running from CORS)
* Quickly explain CORS and why the server is not reachable because of it
* Allow all CORS origins in the `MemoryApplication.java`
```java
@Bean
public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**").allowedOrigins("*");
        }
    };
}
```
* Now the status bar should state "/status nicht gefunden" -> implement this endpoint in a new controller
```java
// StatusController.java
package net.coderdojo.linz.memory.status;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatusController {
    
    @GetMapping("/status")
    public String getStatus() {
        return "OK";
    }

}
```
* Status bar should be green now