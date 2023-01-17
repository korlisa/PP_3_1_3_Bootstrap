package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import javax.validation.Valid;
import java.util.Collection;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;


    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
    }

//    /***
//     * Подготовить объект User для сохранения в базу
//     */
//    @GetMapping("/newUser")
//    public String createForm(Model model, @ModelAttribute("user") User user) {
//        model.addAttribute("listOfRoles", roleService.getRoles());
//        return "create";
//    }

    /***
     * Сохранить в базу
     */
    @PostMapping
    public String createUser(@RequestParam("listOfRoles") Collection<Role> roles,
                             @ModelAttribute("newUser")
                             @Valid User user, BindingResult bindingResult) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(roles);
        if(bindingResult.hasErrors()) {
            return "redirect:/admin";
        }
        userService.createOrUpdate(user);
        return "redirect:/admin";
    }
    /***
     * Получить всех пользователей
     */
    @GetMapping
    public String getAllUser(Model model, @AuthenticationPrincipal User user1) {
        model.addAttribute("users", userService.getUsersList());
        model.addAttribute("user1", user1);
        model.addAttribute("newUser", new User());
        model.addAttribute("listOfRoles", roleService.getRoles());
        return "index";
    }

    /***
     * Получить одного пользователя
     */
    @GetMapping("/{id}")
    public String getUserById(Model model, @PathVariable(name = "id") Long id) {
        model.addAttribute("user", userService.getUser(id));
        return "user";
    }
//    /***
//     * Подготовить изменения для объекта User
//     */
//    @GetMapping("/{id}/edit")
//    public String editForm(Model model, @PathVariable(name = "id") Long id) {
//        User user = userService.getUser(id);
//        model.addAttribute("user", user);
//        model.addAttribute("listRoles", roleService.getRoles());
//        return "edit";
//    }

    /***
     * Сохранить изменённого пользователя
     */
    @PatchMapping("/{id}")
    public String editUser(@RequestParam("listOfRoles") Collection<Role> roles, @ModelAttribute("editUser") User user,
                           @PathVariable("id") Long id) {
        user.setFirstName(user.getFirstName());
        user.setLastName(user.getLastName());
        user.setAge(user.getAge());
        user.setUsername(user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(roles);
        userService.createOrUpdate(user);
        return "redirect:/admin";
    }
    /***
     * Удалить пользователя
     */
    @DeleteMapping("/{id}")
    public String deleteUserById(@ModelAttribute("user") User user,
                                 @PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}