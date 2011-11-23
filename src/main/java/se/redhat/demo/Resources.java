package se.redhat.demo;

import java.util.logging.Logger;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.faces.context.FacesContext;


public class Resources {
	
	public static final String BLOG_ID_REQUEST_PARAM_NAME = "selectedBlogId";

	// Expose an entity manager using the resource producer pattern
	// @SuppressWarnings("unused")
	// @PersistenceContext
	// @Produces
	// private EntityManager em;
	
//	@Inject
//	Logger logger;

	@Produces
	@SelectedBookIdParam
	Long getSelectedBookIdParam(InjectionPoint p) {
//		logger.info("Producing SelectedBookId from request param");
		FacesContext fc = FacesContext.getCurrentInstance();
//		logger.info("FacesContext is "+ fc);
		String strSelectedBookId = fc.getExternalContext().getRequestParameterMap().get(BLOG_ID_REQUEST_PARAM_NAME);
//		logger.info("strSelectedBookId = " + strSelectedBookId);
		try {
			return Long.parseLong(strSelectedBookId);
		} catch(Throwable t) {
			//Ignore, issues are resolved by returning -1L;
		}
//		logger.warning("Failed to parse strSelectedBookId");
		return -1L;
	}

	@Produces
	Logger getLogger(InjectionPoint ip) {
		String category = ip.getMember().getDeclaringClass().getName();
		return Logger.getLogger(category);
	}

}
;