package com.jsonservice.controller;


import com.jsonservice.DAO.MessageRepository;
import com.jsonservice.model.JMessage;
import com.jsonservice.parser.Parser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Controller
@ComponentScan("com.jsonservice.DAO")
@RequestMapping("/")
public class RestControll {

    static private Iterable<JMessage> all;

    @Autowired
    private Parser parser;

    @RequestMapping("file")
    public Model newfile(@RequestParam(value = "name") String name, Model model)  {
        model.addAttribute("htmlString",parser.parse(RestControll.getMessage(name)));
        return model;
    }




    @Autowired
    private MessageRepository messageRepository;

    static public JMessage getMessage(String name){
        for (JMessage jmessage: all) {
            if(jmessage.getName().equals(name)){
                return jmessage;
            }
        }
        return null;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView send(@RequestParam MultipartFile file) throws IOException {

        String message = new String(file.getBytes());
        String name = file.getOriginalFilename();
        messageRepository.save(new JMessage(message, name));
        return update();
    }

    @RequestMapping(method = RequestMethod.GET)
    public String get(Model model) {
        all = messageRepository.findAll();
        model.addAttribute("jmessages", all);
        return "index";
    }

    @RequestMapping("/update")
    public ModelAndView update() {
        all = messageRepository.findAll();
        return new ModelAndView("index","jmessages", all);
    }



}



