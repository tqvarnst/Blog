package se.redhat.demo;

import java.util.List;
import java.util.logging.Logger;
import static java.util.logging.Level.SEVERE;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

@Named("blogManager")
@RequestScoped
public class BlogManager {

	@Inject
	private transient Logger logger;

	@PersistenceContext
	private EntityManager entityManager;

	@Inject
	private UserTransaction utx;

	Blog newBlog = new Blog();

	BlogPost newBlogPost = new BlogPost();

	@Inject
	@SelectedBookIdParam
	Long selectedBlogId;

	@SuppressWarnings("unchecked")
	public List<Blog> getBlogs() throws Exception {

		try {
			try {
				utx.begin();
				return entityManager.createQuery("select b from Blog b")
						.getResultList();
			} finally {
				utx.commit();
			}
		} catch (Exception e) {
			utx.rollback();
			throw e;
		}
	}

	private Blog getBlog(long id) throws Exception {
		try {
			try {
				utx.begin();
				return entityManager.find(Blog.class, id);
			} finally {
				utx.commit();
			}
		} catch (Exception e) {
			utx.rollback();
			throw e;
		}

	}

	public String addBlog() {
		try {
				logger.info("Trying to added a new blog " + newBlog);
				utx.begin();
				entityManager.persist(newBlog);
				logger.info("Added " + newBlog.toString());
				utx.commit();
		} catch (Exception e) {
			logger.log(SEVERE, "Error adding blog entry", e);
			try {
				utx.rollback();
			} catch (Exception e1) {
				logger.log(SEVERE,
						"Error rolling back transaction for adding blog", e1);
			}
			return "failure";
		}
		return "success";
	}

	public String deleteBlog() {
		try {
			logger.info("Trying to delete blog with id " + selectedBlogId);
			utx.begin();
			entityManager.remove(this.getBlog(selectedBlogId));
			logger.info("Deleted " + newBlog.toString());
			utx.commit();
		} catch (Exception e) {
			logger.log(SEVERE, "Error deleting blog", e);
			try {
				utx.rollback();
			} catch (Exception e1) {
				logger.log(SEVERE,
						"Error rolling back transaction for deleted blog", e1);
			}
			return "failure";
		}
		return "success";
	}

	public String addBlogPost() throws Exception {
		logger.info("Adding blog post with title "
				+ (newBlogPost != null ? newBlogPost.title : "---NOT SET----"));
		try {
			logger.info("Trying to added a new blog " + newBlog);
			newBlogPost.setBlog(getSelectedBlog());
			utx.begin();
			entityManager.persist(newBlogPost);
			logger.info("Added " + newBlogPost.toString());
			utx.commit();
		} catch (Exception e) {
			logger.log(SEVERE, "Error adding blog entry", e);
			try {
				utx.rollback();
			} catch (Exception e1) {
				logger.log(SEVERE,
						"Error rolling back transaction for adding blog", e1);
			}
			return "failure";
		}
		return "success";
	}

	public Long getSelectedBlogId() {
		return selectedBlogId;
	}

	public void setSelectedBlogId(Long selectedBlogId) {
		this.selectedBlogId = selectedBlogId;
	}

	public String viewBlog() {
		return "view-blog";
	}

	@Produces
	@RequestScoped
	@Named("selectedBlog")
	public Blog getSelectedBlog() throws Exception {
		if (selectedBlogId != -1) {
			Blog blog = this.getBlog(selectedBlogId);
			logger.info(String.format("Retrived Blog named %s for request",
					(blog != null ? blog.getName() : "null")));
			if (blog!=null)
				return blog;
		}
		logger.warning("Failed create a Blog from the request parameter \"selectedBlogId\". Are you sure that you included it in the request parameter?");
		return new Blog();
	}

	@Produces
	@RequestScoped
	@Named
	public Blog getNewBlog() {
		return newBlog;
	}

	@Produces
	@RequestScoped
	@Named
	public BlogPost getNewBlogPost() {
		return newBlogPost;
	}

}
