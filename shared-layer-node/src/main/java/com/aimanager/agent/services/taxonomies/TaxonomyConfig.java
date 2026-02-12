package com.aimanager.agent.services.taxonomies;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TaxonomyConfig {
	
	public static final TaxonomyConfig INSTANCE = new TaxonomyConfig();

	private Map<String,Long> gtIds = new HashMap<>();
    private Map<Long,TaxonomyElement> teMap = new HashMap<>();
    private Map<Integer, ArrayList<TaxonomyElement>> teMapByLevel = new HashMap<>();

	private TaxonomyConfig(){
		try {
			gtIds = new HashMap<>();
			teMap = new HashMap<>();
			teMapByLevel = new HashMap<>();
			initTaxonomyTree();
		} catch (IOException | URISyntaxException e) {
			throw new RuntimeException("Error initializing taxonomy tree", e);
		}
	}

	public static TaxonomyConfig getInstance(){
		return INSTANCE;
	}

	

    /**
     * Get the taxonomy element
     * @param parentId
     * @return
     */
    private TaxonomyElement getTaxonomyElement(Long parentId){
        if(!teMap.containsKey(parentId)){
            throw new IllegalArgumentException("Parent taxonomy element not found");
        }
        return teMap.get(parentId);
    }

    /**
     * Get the sub taxonomies
     * @param parentId
     * @return
     */
    public List<TaxonomyElement> getSubTaxonomies(Long parentId){
        TaxonomyElement parent = getTaxonomyElement(parentId);
        return teMap.entrySet().stream().map(key-> key.getValue()).
				filter(te->te.getParent() != null && te.getParent().getId().equals(parentId)).
				collect(Collectors.toList());
    }

    /**
     * Get the major taxonomies
     * @return
     */
    public List<TaxonomyElement> getMajorTaxonomies(){
        return teMapByLevel.get(0);
    }



	class TENode{
		private TENode parent;
		private TaxonomyElement element;
		private Map<String,TENode> children;
		
		public TENode(TENode parent, String element) {
			this.parent = parent;
			this.element = new TaxonomyElement();
			this.element.setName(element);
			this.element.setLevel(parent == null ? 0 : parent.getElement().getLevel() + 1);
			this.element.setParent(parent == null ? null : parent.getElement());
			this.children = new TreeMap<>();
		}
		
		public void addChild(TENode node){
			this.children.put(node.getElement().getName(), node);
		}
		
		public TENode getChild(String name) {
			return this.children.getOrDefault(name, null);
		}
		
		public TaxonomyElement getElement() {
			return element;
		}
		
		public Map<String, TENode> getChildren() {
			return children;
		}
		
		public TENode getParent() {
			return parent;
		}

        public void saveElement(){
            element.setId(gtIds.get(element.getName()));
            if(!teMap.containsKey(element.getId())){
                teMap.put(element.getId(), element);
            }
            if(!teMapByLevel.containsKey(element.getLevel())){
                ArrayList<TaxonomyElement> list = new ArrayList<>();
                list.add(element);
                teMapByLevel.put(element.getLevel(), list);
            }else{
                teMapByLevel.get(element.getLevel()).add(element);
            }

        }
		
		public void persistChildren(){
			children.forEach((k,v)->{
				//set if for every a using the gtIds map
				v.getElement().setId(gtIds.get(v.getElement().getName()));
                v.saveElement();
				v.persistChildren();
			});
		}
	}


	@PostConstruct
	public void initTaxonomyTree() throws IOException, URISyntaxException {
		//5595 is the number of entries in the taxonomy tree
		if(teMap.size() != 5595) {
			//log.info("Taxonomy tree not loaded. Downloading the tree CSV ...");
			List<String[]> arrList = loadCSV();
			//log.info("Taxonomy tree CSV downloaded with {} lines. Loading the in-memory taxonomy tree ...", arrList.size());
			TENode root = loadTree(arrList);
			//log.info("Taxonomy tree loaded. Persisting to DB ...");
			root.persistChildren();
			//log.info("Taxonomy tree persisted. Total taxononmy elements in the DB: {}", teMap.size());
		}else {
			//log.info("Taxonomy tree was previously loaded with {} entries", teMap.size());
		}
		
	}

	private TENode loadTree(List<String[]> arr) {
		TENode root = new TENode(null, "root");
		TENode curr = root;
		for (String[] tuple : arr) {
			for (String te : tuple) {
				TENode child = curr.getChild(te);
				if(child == null) {
					child = new TENode(curr == root ? null : curr, te);
					curr.addChild(child);
				}
				curr = child;
			}
			curr = root;
		}
		return root;
	}

	private List<String[]> loadCSV() throws MalformedURLException, IOException {
		try(InputStream is = TaxonomyConfig.class.getClassLoader().getResourceAsStream("taxonomy-with-ids-2021-09-21.txt")){
			List<String> lines = IOUtils.readLines(is, Charset.defaultCharset());
			List<String[]> arrList = lines.stream()
				.map(StringUtils::trim)
				.filter(StringUtils::isNotBlank)
				.filter(s->!StringUtils.startsWith(s, "#"))
				.sorted()
				.map(this::csvToArr)
				.collect(Collectors.toList());
			return arrList;
		}
	}

	/**
	 * Extracts the number from the beginning of the line before the dash.
	 *
	 * @param line The input line containing the number and text.
	 * @return The extracted number as an integer or -1 if the format is invalid.
	 */
	public Long extractNumber(String line) {
		if (line == null || !line.contains(" - ")) {
			return -1L;
		}
		try {
			return Long.parseLong(line.split(" - ")[0].trim());
		} catch (NumberFormatException e) {
			System.err.println("Invalid number format in line: " + line);
			return -1L;
		}
	}

	/**
	 * Removes the number and the dash from the beginning of the line.
	 *
	 * @param line The input line containing the number and text.
	 * @return The cleaned line without the number and dash.
	 */
	public String removeNumberAndDash(String line) {
		if (line == null || !line.contains(" - ")) {
			return line;
		}
		return line.substring(line.indexOf(" - ") + 3).trim();
	}
	
	private String [] csvToArr(String line) {
		Long etid = extractNumber(line);
		String cleanedLine = removeNumberAndDash(line);
		String[] arr = StringUtils.split(cleanedLine, ">");
		for (int i = 0; i < arr.length; i++) {
			arr[i] = StringUtils.normalizeSpace(arr[i]);
		}
		String et= arr[arr.length - 1];
		gtIds.put(et,etid);
		return arr;
	}
	
	public static void main(String[] args) throws MalformedURLException, IOException {
		TaxonomyConfig tc = new TaxonomyConfig();
		List<String[]> loadCSV = tc.loadCSV();
		for (String[] arr : loadCSV) {
			System.out.println(Arrays.toString(arr));
		}
	}
}
