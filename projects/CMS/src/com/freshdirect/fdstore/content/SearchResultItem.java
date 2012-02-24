package com.freshdirect.fdstore.content;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SearchResultItem<N extends ContentNodeModel> implements Serializable {
	private static final long serialVersionUID = 5839350544651788541L;
	
	private N node;

	private Map<EnumSortingValue, Number> values;

	public SearchResultItem(N node) {
		super();
		this.node = node;
		this.values = new HashMap<EnumSortingValue, Number>();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((node == null) ? 0 : node.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("unchecked")
		SearchResultItem<N> other = (SearchResultItem<N>) obj;
		if (node == null) {
			if (other.node != null)
				return false;
		} else if (!node.equals(other.node))
			return false;
		return true;
	}

	public N getNode() {
		return node;
	}
	
	public N getModel() {
		return node;
	}

	public void replaceNode(N node) {
		this.node = node;
	}

	public Number getSortingValue(EnumSortingValue key) {
		Number n = values.get(key);
		return n == null ? key.getDefaultValue() : n;
	}

	public SearchResultItem<N> putSortingValue(EnumSortingValue key, Number value) {
		values.put(key, value);
		return this;
	}

	public SearchResultItem<N> removeSortingValue(EnumSortingValue key) {
		values.remove(key);
		return this;
	}

	public static <N extends ContentNodeModel> List<SearchResultItem<N>> fill(List<N> nodes, EnumSortingValue criterion,
			Number value) {
		List<SearchResultItem<N>> items = new ArrayList<SearchResultItem<N>>(nodes.size());
		for (N node : nodes) {
			SearchResultItem<N> item = new SearchResultItem<N>(node);
			item.putSortingValue(criterion, value);
			items.add(item);
		}
		return items;
	}

	public static <N extends ContentNodeModel> Set<SearchResultItem<N>> fill(Set<N> nodes, EnumSortingValue criterion,
			Number value) {
		Set<SearchResultItem<N>> items = new HashSet<SearchResultItem<N>>(nodes.size());
		for (N node : nodes) {
			SearchResultItem<N> item = new SearchResultItem<N>(node);
			item.putSortingValue(criterion, value);
			items.add(item);
		}
		return items;
	}
	
	public static <N extends ContentNodeModel> List<SearchResultItem<N>> wrap(List<N> nodes) {
		List<SearchResultItem<N>> items = new ArrayList<SearchResultItem<N>>(nodes.size());
		for (N node : nodes)
			items.add(new SearchResultItem<N>(node));
		return items;
	}

	public static <N extends ContentNodeModel> Set<SearchResultItem<N>> wrap(Set<N> nodes) {
		Set<SearchResultItem<N>> items = new HashSet<SearchResultItem<N>>(nodes.size());
		for (N node : nodes)
			items.add(new SearchResultItem<N>(node));
		return items;
	}

	public static <N extends ContentNodeModel> List<N> unwrap(List<SearchResultItem<N>> items) {
		List<N> nodes = new ArrayList<N>(items.size());
		for (SearchResultItem<N> item : items)
			nodes.add(item.getNode());
		return nodes;
	}
	
	public static <N extends ContentNodeModel> List<SearchResultItem<N>> emptyList() {
		return Collections.emptyList();
	}

	public static <N extends ContentNodeModel> Set<SearchResultItem<N>> emptySet() {
		return Collections.emptySet();
	}

	public static <N extends ContentNodeModel> Comparator<SearchResultItem<N>> wrap(final Comparator<N> comparator) {
		return new Comparator<SearchResultItem<N>>() {
			@Override
			public int compare(SearchResultItem<N> o1, SearchResultItem<N> o2) {
				return comparator.compare(o1.getModel(), o2.getModel());
			}
		};
	}

	public static <N extends ContentNodeModel> ComparatorChain<SearchResultItem<N>> wrap(final ComparatorChain<N> comparator) {
		return ComparatorChain.create(new Comparator<SearchResultItem<N>>() {
			@Override
			public int compare(SearchResultItem<N> o1, SearchResultItem<N> o2) {
				return comparator.compare(o1.getModel(), o2.getModel());
			}
		});
	}
}
