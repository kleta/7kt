package ru.sevenkt.db.services;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.sevenkt.db.entity.Parameter;
import ru.sevenkt.db.repositories.ParametersRepo;

@Service
public class DBService {
	@Autowired
	private ParametersRepo pr;
	
	@PostConstruct
	private void init(){
		pr.save(Parameter.builder().id((long)1).name("test").unit("test").build());
	}

}
