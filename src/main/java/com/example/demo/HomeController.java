package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class HomeController {
    @Autowired
    JobsRepository jobsRepository;
    @RequestMapping("/")
    public String homePage(){
        return "home";
    }
    @RequestMapping("/listJobs")
    public String jobsList(Model model){
        model.addAttribute("jobs",jobsRepository.findAll());
        return "listJobs";
    }
    @GetMapping("/postJob")
    public String postJob(Model model){
        model.addAttribute("job", new Jobs());
        return "jobForm";
    }
    @PostMapping("/processJob")
    public String processJob(@ModelAttribute Jobs job,
                             //(name="postedDate")
                             @RequestParam String postedDate){
        String pattern = "yyyy-MM-dd";
        System.out.println("before: " + postedDate);
        try{
            String formatedDate = postedDate;
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            Date realDate = format.parse(formatedDate);
            job.setPostedDate(realDate);
            System.out.println("after: " + realDate);
        }catch(java.text.ParseException e){
            e.printStackTrace();
        }
        jobsRepository.save(job);
        return "listJobs";
    }




    @PostMapping("/processearch")
    public String searchResult(@RequestParam(name="serach") String search, Model model){
        model.addAttribute("jobs", jobsRepository.findByTitleContainingIgnoreCase(search));
        return "searchlist";
    }


    @RequestMapping("/detail/{id}")
    public String showFligtInfo(@PathVariable("id") long idDetail, Model model){
        model.addAttribute("job", jobsRepository.findById(idDetail).get());
        return "show";
    }
    @RequestMapping("/update/{id}")
    public String updateFlight(@PathVariable("id") long id, Model model){
        model.addAttribute("job", jobsRepository.findById(id).get());
        return "jobForm";
    }
    @RequestMapping("/delete/{id}")
    public String deleteFlight(@PathVariable ("id") long id){
        jobsRepository.deleteById(id);
        return "redirect:/";
    }



}
