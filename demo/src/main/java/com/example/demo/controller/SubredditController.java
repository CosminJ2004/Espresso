//package com.example.demo.controller;
//
//import com.example.demo.dto.PostDto;
//import com.example.demo.dto.SubredditDto;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/subreddits")
//public class SubredditController {
//
//    // GET /subreddits
//    @GetMapping
//    public List<SubredditDto> getAllSubreddits() {
//    }
//
//    // GET /subreddits/:name
//    @GetMapping("/{id}")
//    public ResponseEntity<SubredditDto> getSubreddit(@PathVariable Long id) {
//
//    }
//
//    // POST /subreddits
//    @PostMapping
//    public ResponseEntity<SubredditDto> createSubreddit(@RequestBody SubredditDto subredditDto) {
//
//    }
//
//    // GET /subreddits/:name/posts
//    @GetMapping
//    public List<PostDto> getSubredditPosts() {
//
//    }
//
//    // PUT /subreddits/:name
//    @PutMapping
//    public ResponseEntity<Void> updateSubreddit(@RequestBody SubredditDto dto) {
//
//    }
//
//    // DELETE /subreddits/:name
//    @DeleteMapping
//    public ResponseEntity<Void> deleteSubreddit(@RequestBody SubredditDto dto) {
//
//    }
//
//}
//
