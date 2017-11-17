package com.freshdirect.storeapi.content;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FilteringSortingItem<N extends ContentNodeModel> implements Serializable {
	private static final long serialVersionUID = 5839350544651788541L;
	
	private N node;

	private Map<EnumSortingValue, Number> values;
	private Map<FilteringValue, Object> filteringValue;
	private Map<FilteringValue, Set<FilteringMenuItem>> menuValues;

	public FilteringSortingItem(N node) {
		super();
		this.node = node;
		this.values = new HashMap<EnumSortingValue, Number>();
		this.filteringValue = new HashMap<FilteringValue, Object>();
		this.menuValues = new HashMap<FilteringValue, Set<FilteringMenuItem>>();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((node == null) ? 0 : node.getContentKey().hashCode());
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
		FilteringSortingItem<N> other = (FilteringSortingItem<N>) obj;
		if (node == null) {
			if (other.node != null)
				return false;
		} else if (!node.getContentKey().equals(other.node.getContentKey()))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FilteringSortingItem [node=" + node + "]";
	}

	public N getNode() {
		return node;
	}
	
	public N getModel() {
		return node;
	}
	
	public void setNode(N node){
		this.node=node;
	}

	public void replaceNode(N node) {
		this.node = node;
	}

	public Number getSortingValue(EnumSortingValue key) {
		Number n = values.get(key);
		return n == null ? key.getDefaultValue() : n;
	}

	public FilteringSortingItem<N> putSortingValue(EnumSortingValue key, Number value) {
		values.put(key, value);
		return this;
	}

	public FilteringSortingItem<N> removeSortingValue(EnumSortingValue key) {
		values.remove(key);
		return this;
	}
	
	public Object getFilteringValue(FilteringValue key) {
		return filteringValue.get(key);
	}
	
	public FilteringSortingItem<N> putFilteringValue(FilteringValue key, Object value) {
		filteringValue.put(key, value);
		return this;
	}

	public FilteringSortingItem<N> removeFilteringValue(FilteringValue key) {
		filteringValue.remove(key);
		return this;
	}
	
	public Set<FilteringMenuItem> getMenuValue(FilteringValue key) {
		return menuValues.get(key);
	}
	
	public FilteringSortingItem<N> putMenuValue(FilteringValue key, Set<FilteringMenuItem> value) {
		menuValues.put(key, value);
		return this;
	}

	public FilteringSortingItem<N> removeMenuValue(FilteringValue key) {
		menuValues.remove(key);
		return this;
	}

	public static <N extends ContentNodeModel> List<FilteringSortingItem<N>> fill(List<N> nodes, EnumSortingValue criterion,
			Number value) {
		List<FilteringSortingItem<N>> items = new ArrayList<FilteringSortingItem<N>>(nodes.size());
		for (N node : nodes) {
			FilteringSortingItem<N> item = new FilteringSortingItem<N>(node);
			item.putSortingValue(criterion, value);
			items.add(item);
		}
		return items;
	}

	public static <N extends ContentNodeModel> Set<FilteringSortingItem<N>> fill(Set<N> nodes, EnumSortingValue criterion,
			Number value) {
		Set<FilteringSortingItem<N>> items = new HashSet<FilteringSortingItem<N>>(nodes.size());
		for (N node : nodes) {
			FilteringSortingItem<N> item = new FilteringSortingItem<N>(node);
			item.putSortingValue(criterion, value);
			items.add(item);
		}
		return items;
	}
	
	public static <N extends ContentNodeModel> List<FilteringSortingItem<N>> wrap(List<N> nodes) {
		List<FilteringSortingItem<N>> items = new ArrayList<FilteringSortingItem<N>>(nodes.size());
		for (N node : nodes)
			items.add(new FilteringSortingItem<N>(node));
		return items;
	}

	public static <N extends ContentNodeModel> Set<FilteringSortingItem<N>> wrap(Set<N> nodes) {
		Set<FilteringSortingItem<N>> items = new HashSet<FilteringSortingItem<N>>(nodes.size());
		for (N node : nodes)
			items.add(new FilteringSortingItem<N>(node));
		return items;
	}

	public static <N extends ContentNodeModel> List<N> unwrap(List<FilteringSortingItem<N>> items) {
		List<N> nodes = new ArrayList<N>(items.size());
		for (FilteringSortingItem<N> item : items)
			nodes.add(item.getNode());
		return nodes;
	}
	
	public static <N extends ContentNodeModel> List<FilteringSortingItem<N>> emptyList() {
		return Collections.emptyList();
	}

	public static <N extends ContentNodeModel> Set<FilteringSortingItem<N>> emptySet() {
		return Collections.emptySet();
	}

	public static <N extends ContentNodeModel> Comparator<FilteringSortingItem<N>> wrap(final Comparator<N> comparator) {
		return new Comparator<FilteringSortingItem<N>>() {
			@Override
			public int compare(FilteringSortingItem<N> o1, FilteringSortingItem<N> o2) {
				return comparator.compare(o1.getModel(), o2.getModel());
			}
		};
	}

	public static <N extends ContentNodeModel> ComparatorChain<FilteringSortingItem<N>> wrap(final ComparatorChain<N> comparator) {
		return ComparatorChain.create(new Comparator<FilteringSortingItem<N>>() {
			@Override
			public int compare(FilteringSortingItem<N> o1, FilteringSortingItem<N> o2) {
				return comparator.compare(o1.getModel(), o2.getModel());
			}
		});
	}
}
