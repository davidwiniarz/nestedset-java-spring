package com.deyvid.spring;

import com.deyvid.spring.model.Node;
import com.deyvid.spring.service.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private NodeService service;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home() throws Exception {
        return "nodes";
    }

    @RequestMapping(value = "/nodes", method = RequestMethod.GET)
    public String nodes(Model model) throws Exception {
        List<Node> nodes = new ArrayList<Node>(service.findWholeTree());
        model.addAttribute("nodes", nodes);
        model.addAttribute("node", new Node());
        return "nodes_tree";
    }



}
