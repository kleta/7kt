package ru.sevenkt.db.services;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import ru.sevenkt.db.entities.Parameter;
import ru.sevenkt.db.repositories.ParametersRepo;

@Service
public class DBService implements IDBService{
	@Autowired
	private ParametersRepo pr;
	
	@PostConstruct
	private void init(){
		pr.save(Parameter.builder().id((long)1).name("test").unit("test").build());
	}

	@Override
	public void test() {
		// TODO Auto-generated method stub
		
	}

}
