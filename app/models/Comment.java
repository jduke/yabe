package models;
 
import java.util.*;
import javax.persistence.*;
 
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.*;
 
@Entity
public class Comment extends Model {
 
	@Required
    public String author;
	@Required
    public Date postedAt;
     
    @Lob
    @Required
    @MaxSize(10000)
    public String content;
    
    @ManyToOne
    public Post post;
    
    public Comment(Post post, String author, String content) {
        this.post = post;
        this.author = author;
        this.content = content;
        this.postedAt = new Date();
    }
 
}