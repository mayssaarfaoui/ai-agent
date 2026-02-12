package com.aimanager.agent.dto;

import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import com.aimanager.agent.services.taxonomies.TaxonomyElement;

public class CategoryDto {
	
	public static class CategoryElemDto{
		final Long id;
		final String name;
		final Integer level;
		
		public CategoryElemDto(TaxonomyElement te) {
			this(te.getId(), te.getName(), te.getLevel());
		}

		public CategoryElemDto(Long id, String name, Integer level) {
			super();
			this.id = id;
			this.name = name;
			this.level = level;
		}
		public Long getId() {
			return id;
		}
		public String getName() {
			return name;
		}
		public Integer getLevel() {
			return level;
		}
	}

	private final Long id;
	final String concatenatedText;
	private final List<CategoryElemDto> tuple;
	private final Long itemCount;

	public static CategoryDto of(TaxonomyElement te) {
		return te == null ? null : new CategoryDto(te);
	}
	
	private CategoryDto(TaxonomyElement te) {
		this.id = te.getId();
		this.tuple = te.getTaxonomy().stream().map(CategoryElemDto::new).collect(Collectors.toList());
		this.concatenatedText = StringUtils.join(te.getNameTaxonomy(), " / ");
		this.itemCount = te.getItemCount();
	}

	public Long getId() {
		return id;
	}
	public String getConcatenatedText() {
		return concatenatedText;
	}
	public List<CategoryElemDto> getTuple() {
		return tuple;
	}
	public Long getItemCount() {
		return itemCount;
	}
}
