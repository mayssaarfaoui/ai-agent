package com.aimanager.agent.services.taxonomies;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

public class TaxonomyElement {

	private Long id;
    
	private int level;		// 0-major, 1-minor, ...

	private String name; 
	
	private TaxonomyElement parent;

	private List<TaxonomyElement> subTaxonomies = new LinkedList<>();

	private Long itemCount;
	
	public List<TaxonomyElement> getTaxonomy(){
		LinkedList<TaxonomyElement> l = new LinkedList<>();
		TaxonomyElement te = this;
		do {
			l.addFirst(te);
			te = te.getParent();
		}while(te != null);
		return l;
	}
	
	public List<String> getNameTaxonomy(){
		List<TaxonomyElement> tx = getTaxonomy();
		List<String> ntx = tx.stream().map(TaxonomyElement::getName).collect(Collectors.toList());
		return ntx;
	}


	public Long getItemCount() {
		return itemCount;
	}

	public void setItemCount(Long itemCount) {
		this.itemCount = itemCount;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getName() {
		return name;
	}

	public String getLowerCaseName() {
		return name.toLowerCase();
	}

	public void setName(String name) {
		this.name = name;
	}

	public TaxonomyElement getParent() {
		return parent;
	}

	public void setParent(TaxonomyElement parent) {
		this.parent = parent;
	}

	public List<TaxonomyElement> getSubTaxonomies() {
		return subTaxonomies;
	}
	public void setSubTaxonomies(List<TaxonomyElement> subTaxonomies) {
		this.subTaxonomies = subTaxonomies;
	}

	public void addSubTaxonomy(TaxonomyElement subTaxonomy) {
		if(this.subTaxonomies == null) {
			this.subTaxonomies = new LinkedList<>();
		}
		this.subTaxonomies.add(subTaxonomy);
	}

	
	public String getDisplayName() {
		return StringUtils.join(getNameTaxonomy(), " > ");
	}

}
