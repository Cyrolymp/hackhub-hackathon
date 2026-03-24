package hackathon.util;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings( "serial" )
@Data
@NoArgsConstructor
public class Paging implements Serializable {

	private int		pageNum		= 1;
	private int		pageSize	= 15;
	private String	search		= "";
	private int		totalPages;

	// setters

	public void setPageSize( int pageSize ) {
		this.pageSize = pageSize;
		pageNum = 1;
	}

	public void setSearch( String search ) {
		this.search = search;
		pageNum = 1;
	}

}
