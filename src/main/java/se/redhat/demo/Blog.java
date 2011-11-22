package se.redhat.demo;

import javax.persistence.Entity;
import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.OneToMany;
import javax.persistence.Version;

@Entity
public class Blog implements Serializable {
	
	private static final long serialVersionUID = -6680906565754271095L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "blogId", updatable = false, nullable = false)
	private Long blogId = null;
	
	@Version
	@Column(name = "version")
	private int version = 0;
	
	private String name;
	
	@OneToMany(mappedBy="blog",fetch=FetchType.EAGER,cascade=CascadeType.REMOVE)
	private List<BlogPost> posts;

	public Long getBlogId() {
		return this.blogId;
	}

	public void setBlogId(final Long blogId) {
		this.blogId = blogId;
	}

	public int getVersion() {
		return this.version;
	}

	public void setVersion(final int version) {
		this.version = version;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public List<BlogPost> getPosts() {
		return posts;
	}

	public void setPosts(List<BlogPost> posts) {
		this.posts = posts;
	}
}