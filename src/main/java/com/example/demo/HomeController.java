package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class HomeController {
    @Autowired
    JobsRepository jobsRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private UserService userService;

    @PostConstruct
    public void load() {
        System.out.println(roleRepository.findAll());

        if (!roleRepository.findAll().iterator().hasNext()){
            roleRepository.save(new Role("USER"));
            roleRepository.save(new Role("ADMIN"));
        }
        System.out.println(roleRepository.findAll());

    }

    @GetMapping("/register")
    public String showRegistrationPage(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/register")
    public String processRegistrationPage(@Valid
                                          @ModelAttribute("user") User user, BindingResult result,
                                          Model model) {
        model.addAttribute("user", user);
        if (result.hasErrors())
        {
            return "registration";
        }
        else
        {
            userService.saveUser(user);
            model.addAttribute("message", "User Account Created");
        }
        return "listJobs";
    }
    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    @Autowired
    UserRepository userRepository;

//    @RequestMapping("/")
//    public String homePage(){
//        return "listJobs";
//    }
    @RequestMapping("/")
    public String listJobs(Model model) {
        model.addAttribute("jobs", jobsRepository.findAll());
        if (userService.getUser() != null) {
            model.addAttribute("user_id", userService.getUser().getId());
        }
        return "listJobs";
    }
    @GetMapping ("/postJob")
    public String postJob(Model model){
        model.addAttribute("job", new Jobs());
        return "jobForm";
    }
    @PostMapping("/processJob")
    public String processJob(@ModelAttribute Jobs job,
                             //(name="postedDate")
                             @RequestParam String postedDate){
        String pattern = "yyyy-MM-dd";
        try{
            String formattedDate = postedDate;
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            Date realDate = format.parse(formattedDate);
            job.setPostedDate(realDate);
        }catch(java.text.ParseException e){
            e.printStackTrace();
        }

        //set the currently user to this field
       job.setUser(userService.getUser());
        jobsRepository.save(job);
        return "redirect:/";
    }



    @PostMapping("/processearch")
    public String searchResult(@RequestParam(name="search") String search, Model model){
        model.addAttribute("jobs", jobsRepository.findByTitleContainingIgnoreCase(search));
        return "searchlist";
    }


    @RequestMapping("/detail/{id}")
    public String showJob(@PathVariable("id") long idDetail, Model model){
        model.addAttribute("job", jobsRepository.findById(idDetail).get());
        return "detail";
    }
    @RequestMapping("/update/{id}")
    public String updateJob(@PathVariable("id") long id, Model model){
        model.addAttribute("job", jobsRepository.findById(id).get());
        return "jobForm";
    }
    @RequestMapping("/delete/{id}")
    public String deleteJob(@PathVariable ("id") long id){
        jobsRepository.deleteById(id);
        return "redirect:/";
    }


}
