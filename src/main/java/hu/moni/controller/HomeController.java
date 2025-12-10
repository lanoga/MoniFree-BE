package hu.moni.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.moni.model.dto.MenuItem;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class HomeController {

    private final ObjectMapper objectMapper;


    @RequestMapping(value = "/get-menus", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    public ResponseEntity<String> getMyMenus(SecurityContextHolderAwareRequestWrapper request) throws JsonProcessingException {
        String resultJson;
        // Oldalmenü összeállítása
        ArrayList<MenuItem> ret = new ArrayList<>();

            ret.add(new MenuItem(1L, "Dashboard", "/moni/dashboard", "fas fa-shield-alt"));
            ret.add(new MenuItem(2L, "Services", "/moni/services", "fas fa-shield-alt"));
            ret.add(new MenuItem(3L, "Logs", "/moni/logs", "fas fa-shield-alt"));
            ret.add(new MenuItem(4L, "Users", "/moni/user", "fas fa-shield-alt"));

            resultJson = objectMapper.writeValueAsString(ret);
            return new ResponseEntity<>(resultJson, HttpStatus.OK);
    }

}
