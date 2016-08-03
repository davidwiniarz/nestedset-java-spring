package com.deyvid.spring;

import com.deyvid.spring.model.Node;
import com.deyvid.spring.service.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class NodeController {

    @Autowired
    private NodeService service;

    @RequestMapping(value = "/node/add", method = RequestMethod.POST)
    public String addNode(@ModelAttribute("node") Node node, @RequestParam("currentNodeId") int id, @RequestParam("method") String method) throws Exception {

        Node parent = service.findOneById(id);
        if (method.equals("first")) {
            service.addChildAsFirst(parent, node.getName(), node.getValue());
        } else {
            service.addChildAsLast(parent, node.getName(), node.getValue());
        }

        return "redirect:/";
    }

    @RequestMapping(value = "/node/edit", method = RequestMethod.POST)
    public String editNode(@ModelAttribute("node") Node node) throws Exception {
        Node currentNode = service.findOneById(node.getId());
        currentNode = service.updateFields(currentNode, node);
        service.update(currentNode);
        return "redirect:/";
    }

    @RequestMapping(value = "/node/delete/{id}", method = RequestMethod.GET)
    public String deleteNode(@PathVariable int id) throws Exception {
        Node node = service.findOneById(id);
        service.deleteNode(node);
        return "redirect:/";
    }
}
