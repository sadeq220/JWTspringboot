package com.sadeqstore.demo.service;

import com.sadeqstore.demo.model.Product;
import com.sadeqstore.demo.repository.MyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    private MyRepository pRepository;
    @Autowired
    public ProductService(MyRepository pRepository){
        this.pRepository=pRepository;
    }
    public Integer deleteP(String name){
        if(pRepository.existsById(name))
        return pRepository.deleteByName(name);
        return 0;
    }
    public String updateP(String oldName, Product product){
       return pRepository.updateP(oldName,product.getName(),product.getCost()).toString();
    }
    public String addP(Product product){
        if(!pRepository.existsById(product.getName()))
        return pRepository.save(product).getName();
        return "product already exists!";
    }
}
