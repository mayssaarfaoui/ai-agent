package com.aimanager.agent.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import com.aimanager.agent.nodes.NodeContext;
import com.aimanager.agent.services.taxonomies.TaxonomyConfig;
import com.aimanager.agent.services.taxonomies.TaxonomyElement;

import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("GOOGLE_TAXONOMY")
@Getter
@Setter
public class GoogleTaxonomyNode extends GraphNode {

    @Transient
        private List<TaxonomyElement> taxonomyElements;

    public GoogleTaxonomyNode() {
        super(NodeType.GOOGLE_TAXONOMY);
        this.taxonomyElements = new ArrayList<>();
    }

    @Override
    public GoogleTaxonomyNode clone() {
        GoogleTaxonomyNode clone = new GoogleTaxonomyNode();
        super.copyData(clone);
        clone.setTaxonomyElements(this.taxonomyElements);
        return clone;
    }

    @Override
    public void setup() {
        if(taxonomyElements == null || taxonomyElements.isEmpty()){
            taxonomyElements = TaxonomyConfig.getInstance().getMajorTaxonomies();
        }
    }

    @Override
    public void process(NodeContext context) {
        if(context == null || context.get("parentId") == null){
            taxonomyElements = TaxonomyConfig.getInstance().getMajorTaxonomies();
        }else{
            Long parentId = Long.parseLong((String) context.get("parentId"));
            taxonomyElements = TaxonomyConfig.getInstance().getSubTaxonomies(parentId);
        }
    }

    @Override
    public String getLabel() {
        return type.getName()+"_"+id+"_"+"Google Taxonomy";
    }
}
