package annotation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Article {
	private int id;
	private ArticleDao dao;
	public Article(int id) {
		this.id = id;
	}
}
