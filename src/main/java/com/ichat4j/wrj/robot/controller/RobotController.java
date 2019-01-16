package com.ichat4j.wrj.robot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/robot")
public class RobotController {

    @RequestMapping("/config")
    public ModelAndView listUser(Model model) {
        //model.addAttribute("users", userList);
        return new ModelAndView("/robot/index");
    }
}