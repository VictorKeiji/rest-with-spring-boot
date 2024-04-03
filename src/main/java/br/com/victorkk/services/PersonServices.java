package br.com.victorkk.services;

import br.com.victorkk.model.Person;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

@Service
public class PersonServices {

    private final AtomicLong counter = new AtomicLong();
    private Logger logger = Logger.getLogger(PersonServices.class.getName());

    public Person findById(String id) {

        logger.info("Finding one person...");
        Person person = new Person();
        person.setId(counter.incrementAndGet());
        person.setFirstName("Victor");
        person.setLastName("Keiji");
        person.setAddress("São Paulo - SP - Brasil");
        person.setGender("M");

        return person;
    }
}
