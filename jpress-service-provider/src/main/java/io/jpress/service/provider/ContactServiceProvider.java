package io.jpress.service.provider;

import io.jboot.aop.annotation.Bean;
import io.jboot.service.JbootServiceBase;
import io.jpress.model.Contact;
import io.jpress.service.ContactService;

@Bean
public class ContactServiceProvider extends JbootServiceBase<Contact> implements ContactService {


    @Override
    public boolean doChangeStatus(long id, int status) {
        Contact contact = findById(id);
        contact.setStatus(status);
        return update(contact);
    }
}