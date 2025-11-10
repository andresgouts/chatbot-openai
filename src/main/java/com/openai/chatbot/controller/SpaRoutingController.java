package com.openai.chatbot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller to handle SPA (Single Page Application) routing.
 * Forwards all non-API routes to index.html to support client-side routing.
 */
@Controller
public class SpaRoutingController {

    /**
     * Forward all non-API, non-static resource requests to index.html.
     * This allows the Next.js frontend to handle routing on the client side.
     *
     * Pattern explanation:
     * - Matches any path that doesn't contain a dot (excludes static files like .js, .css, .html)
     * - Doesn't match /api/** (handled by REST controllers)
     * - Doesn't match /actuator/** (handled by Spring Boot Actuator)
     */
    @GetMapping(value = {"/", "/{path:[^\\.]*}"})
    public String forward() {
        return "forward:/index.html";
    }
}
