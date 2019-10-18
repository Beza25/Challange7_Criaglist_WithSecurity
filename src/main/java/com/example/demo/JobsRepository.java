package com.example.demo;

import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface JobsRepository extends CrudRepository<Jobs, Long> {
    ArrayList<Jobs> findByTitleContainingIgnoreCase(String title);
}
