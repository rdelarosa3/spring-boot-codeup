package com.codeup.demoproject.repos;

import com.codeup.demoproject.models.Post;
import com.codeup.demoproject.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    // Query methods
    Post findByDescription(String desc); // mysql> select * from ads where description = ?;
    List<Post> findAllByTitleIsLike(String term);
    List<Post> findAllByAuthor(User author);
    List<Post> findAllByTitleLikeOrDescriptionLike(String title,String desc);
    List<Post> findAllByTitleOrDescriptionIsLike(String term1,String term2);

}