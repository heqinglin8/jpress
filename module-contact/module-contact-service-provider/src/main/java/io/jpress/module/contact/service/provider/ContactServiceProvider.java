package io.jpress.module.contact.service.provider;

import io.jboot.aop.annotation.Bean;
import io.jboot.service.JbootServiceBase;
import io.jpress.module.contact.model.Contact;
import io.jpress.module.contact.service.ContactService;

@Bean
public class ContactServiceProvider extends JbootServiceBase<Contact> implements ContactService {

}