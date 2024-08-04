package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;
import ru.kata.spring.boot_security.demo.util.UserValidator;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller()
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;
    private final UserValidator userValidator;

    @Autowired
    public AdminController(UserService userService, RoleService roleService, UserValidator userValidator) {
        this.userService = userService;
        this.roleService = roleService;
        this.userValidator = userValidator;
    }


    @GetMapping
    public ModelAndView adminPage() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = (UserDetails) authentication.getPrincipal();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("info");
        return modelAndView;
    }

    @GetMapping("/users")
    public ModelAndView getUsersPage(@RequestParam(value = "id", required = false) Integer id) {
        List<User> users = new ArrayList<>();
        ModelAndView modelAndView = new ModelAndView();
        if (id == null) {
            users = userService.allUsers();
            modelAndView.addObject("users", users);
        } else {
            users.add(userService.getUserById(id));
            modelAndView.addObject("users", users);
        }
        modelAndView.setViewName("admin/users");
        return modelAndView;
    }

    @GetMapping("users/edit")
    public ModelAndView editUserPage(@RequestParam(value = "id", required = false) Integer id) {
        User user = userService.getUserById(id);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("admin/edit");
        return modelAndView;
    }

    @PostMapping("users/edit")
    public String editUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/edit";
        }
        User existingUser = userService.getUserById(user.getId());
        user.setRoles(existingUser.getRoles());
        userService.updateUser(user);
        return "redirect:/admin/users";
    }

    @GetMapping("users/add")
    public String addUserPage(@ModelAttribute("user") User user) {
        return "admin/add";
    }

    @PostMapping("users/add")
    public String addUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return "admin/add";
        }
        userService.saveUser(user);
        return "redirect:/admin/users";
    }


    @PostMapping("users/delete")
    public ModelAndView deleteUser(@RequestParam(value = "id", required = false) Integer id) {
        ModelAndView modelAndView = new ModelAndView();
        User user = userService.getUserById(id);
        userService.deleteUser(user);
        modelAndView.setViewName("redirect:/admin/users");
        return modelAndView;
    }

    @GetMapping("users/roles")
    public ModelAndView rolesPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/roles");
        modelAndView.addObject("roles", roleService.findAll());
        return modelAndView;
    }
}
