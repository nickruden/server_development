package com.example.springweb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TeamService {

    @Autowired
    private TeamRepository TeamRepository;

    public Team findById(Long id) {
        Optional<Team> teamOptional = TeamRepository.findById(id);
        if (teamOptional.isPresent()) {
            return teamOptional.get();
        } else {
            throw new RuntimeException("Team not found with id: " + id);
        }
    }
}