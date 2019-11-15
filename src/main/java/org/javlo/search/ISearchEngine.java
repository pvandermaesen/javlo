package org.javlo.search;

import java.util.List;

import org.javlo.context.ContentContext;
import org.javlo.search.SearchResult.SearchElement;

public interface ISearchEngine {

	List<SearchElement> search(ContentContext ctx, String groupId, String searchStr, String sort, List<String> componentList) throws Exception;
	
	public void updateData(ContentContext ctx) throws Exception;
}
