package com.example.springweb;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class DataInitializer {

    @Autowired
    private TeamRepository teamRepository;

    @PostConstruct
    public void init() {
        List<Team> list = new ArrayList<>();

        Set<Player> players = new HashSet<>();
        players.add(new Player("Anton Shunin", "GK"));
        Team team = new Team("Moscow", "Dinamo", "Gladiator", players);
        list.add(team);

        players = new HashSet<>();
        players.add(new Player("Igor Akinfeev", "GK"));
        team = new Team("Moscow", "CSKA", "Horse", players);
        list.add(team);

        teamRepository.saveAll(list);
    }
}