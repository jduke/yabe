package models;

import java.util.*;

import javax.persistence.*;

import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.*;

@Entity
public class Post extends Model {

	@Required
	public String title;
	
	@Required
	public Date postedAt;

	@Lob
	@Required
	@MaxSize(10000)
	public String content;

	@Required
	@ManyToOne
	public User author;

	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
	public List<Comment> comments;

	@ManyToMany(cascade = CascadeType.PERSIST)
	public Set<Tag> tags;

	public Post(User author, String title, String content) {
		this.comments = new ArrayList<Comment>();
		this.author = author;
		this.title = title;
		this.content = content;
		this.postedAt = new Date();
		this.tags = new TreeSet<Tag>();
	}

	public Comment addComment(String author, String message) {
		Comment comment = new Comment(this, author, message);

		this.comments.add(comment);
		this.save();

		return comment;
	}

	public Post previous() {
		return Post.find("postedAt < ? order by postedAt desc", postedAt)
				.first();
	}

	public Post next() {
		return Post.find("postedAt > ? order by postedAt asc", postedAt)
				.first();
	}

	public Post tagItWith(String name) {
		tags.add(Tag.findOrCreateByName(name));
		return this;
	}

	public static List<Post> findTaggedWith(String tag) {
		return Post
				.find(
						"select distinct p from Post p join p.tags as t where t.name = ?",
						tag).fetch();
	}

	public static List<Post> findTaggedWith(String... tags) {
		return Post
				.find(
						"select distinct p from Post p join p.tags as t "
								+ "where t.name in (:tags) group by p.id, p.author, p.title, "
								+ "p.content, p.postedAt having count(t.id) = :size")
				.bind("tags", tags).bind("size", tags.length).fetch();
	}
}