package kr.gdu.sitemesh;

import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.config.ConfigurableSiteMeshFilter;

import jakarta.servlet.annotation.WebFilter;


@WebFilter("/*")
public class SiteMeshFilter extends ConfigurableSiteMeshFilter {

	@Override
	protected void applyCustomConfiguration(SiteMeshFilterBuilder builder) {
		builder.addDecoratorPath("/*", "layout/gdulayout.jsp")
		.addExcludedPath("/user/idSearch*")
		.addExcludedPath("/user/pwSearch")
		.addExcludedPath("/user/login")
		.addExcludedPath("/user/join")	
		.addExcludedPath("/ajax*"); //ajax의모든요청은 sitemesh제외
	}
}
