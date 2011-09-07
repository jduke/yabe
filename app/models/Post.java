package models;

import java.util.*;

import javax.persistence.*;

import play.db.jpa.*;

@Entity
public class Post extends Model {

	public String title;
	public Date postedAt;

	@Lob
	public String content;

	@ManyToOne
	public User author;

	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
	public List<Comment> comments;

	public Post(User author, String title, String content) {
		this.comments = new ArrayList<Comment>();
		this.author = author;
		this.title = title;
		this.content = content;
		this.postedAt = new Date();
	}

	public Comment addComment(String author, String message) {
		Comment comment = new Comment(this, author, message);

		this.comments.add(comment);
		this.save();

		return comment;
	}
	
	public Post previous(){
		return Post.find("postedAt < ? order by postedAt desc", postedAt).first();
	}
	
	public Post next(){
		return Post.find("postedAt > ? order by postedAt asc", postedAt).first();
	}

}