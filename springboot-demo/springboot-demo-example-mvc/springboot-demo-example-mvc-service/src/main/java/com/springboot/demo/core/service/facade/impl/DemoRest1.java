package com.springboot.demo.core.service.facade.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.demo.core.service.facade.InterfaceDemoRest1;
import com.springboot.demo.core.service.impl.Demo1;

@Service("demoRest1")
public class DemoRest1 implements InterfaceDemoRest1 {

    @Autowired
    private Demo1 demo1;
}
