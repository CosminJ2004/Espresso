package com.example.demo.controller;

import com.example.demo.dto.VoteDto;
import com.example.demo.model.Vote;
import com.example.demo.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/votes")
public class VoteController {

    @Autowired
    private VoteService voteService;

    @PostMapping
    public Vote createVote(@RequestBody VoteDto voteDto) {
        return voteService.vote(voteDto);
    }

    @GetMapping
    public List<Vote> getAllVotes() {
        return voteService.getAllVotes();
    }


    @GetMapping("/{id}")
    public Vote getVote(@PathVariable Long id) {
        return voteService.getVote(id);
    }

    @DeleteMapping("/{id}")
    public void deleteVote(@PathVariable Long id) {
        voteService.deleteVote(id);
    }
}
