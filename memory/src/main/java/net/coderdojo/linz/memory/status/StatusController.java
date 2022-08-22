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
