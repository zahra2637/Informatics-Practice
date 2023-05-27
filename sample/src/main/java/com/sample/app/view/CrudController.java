package com.sample.app.view;
import com.sample.app.entity.Person;
import com.sample.app.entity.UserResponse;
import com.sample.app.service.PersonSearchResult;
import com.sample.app.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController()
@RequestMapping(value="api", produces = APPLICATION_JSON_VALUE)
public class CrudController extends BaseRestController{
    private static final Logger LOGGER = LoggerFactory.getLogger(CrudController.class);
    PersonService service;
    @Autowired
    public CrudController(PersonService service) {
        this.service = service;
    }
    //*****************************************************************************************
    //*****************************************************************************************

    @PostMapping(value = "create", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> create(@RequestBody @Validated Person person)
    {
        UserResponse response=new UserResponse();
        try {
        service.createOrUpdate(person);
        return new ResponseEntity<>(response.getResponseCode(), HttpStatus.OK);
        }
     catch (Exception e) {
         LOGGER.error(e.getMessage(), e);
         response.setResponseCode(UserResponse.RESPONSE_CODE_INTERNAL_ERROR);
         return new ResponseEntity<>(response.getResponseCode(), HttpStatus.INTERNAL_SERVER_ERROR);
     }

    }

    //*****************************************************************************************

    @DeleteMapping(value = "delete", produces =APPLICATION_JSON_VALUE)
    public ResponseEntity<String> delete(@RequestParam(name = "personId") Long id){
        UserResponse response=new UserResponse();
        try {
           service.delete(id);
            return new ResponseEntity<>(response.getResponseCode(), HttpStatus.OK);
                    }
        catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            response.setResponseCode(UserResponse.RESPONSE_CODE_INTERNAL_ERROR);
            return new ResponseEntity<>(response.getResponseCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //*****************************************************************************************

    @GetMapping(value = "get")
    public PersonSearchResult findAll(){
        try {
            List<Person> objects =
                    StreamSupport.stream(service.getAll().spliterator(), false)
                            .collect(Collectors.toList());
            return new PersonSearchResult(objects, 0, objects.size());
        }
        catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

}
