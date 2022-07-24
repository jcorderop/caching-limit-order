package com.example.limitorder.cache.collections;

import lombok.ToString;

import java.util.*;

@ToString
public class NavigableSetWrapper<T> implements NavigableSet<T>{

    final NavigableSet<T> sortedElements;

    public NavigableSetWrapper(NavigableSet<T> sortedElements) {
        this.sortedElements = sortedElements;
    }

    @Override
    public T lower(T t) {
        return this.sortedElements.lower(t);
    }

    @Override
    public T floor(T t) {
        return this.sortedElements.floor(t);
    }

    @Override
    public T ceiling(T t) {
        return this.sortedElements.ceiling(t);
    }

    @Override
    public T higher(T t) {
        return this.sortedElements.higher(t);
    }

    @Deprecated
    @Override
    public T pollFirst() {
        throw new IllegalCallerException("Not Supported call...");
    }

    @Deprecated
    @Override
    public T pollLast() {
        throw new IllegalCallerException("Not Supported call...");
    }

    @Override
    public int size() {
        return this.sortedElements.size();
    }

    @Override
    public boolean isEmpty() {
        return this.sortedElements.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return this.sortedElements.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return this.sortedElements.iterator();
    }

    @Override
    public Object[] toArray() {
        return this.sortedElements.toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return null;
    }

    @Deprecated
    @Override
    public boolean add(T t) {
        throw new IllegalCallerException("Not Supported call...");
    }

    @Deprecated
    @Override
    public boolean remove(Object o) {
        throw new IllegalCallerException("Not Supported call...");
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return this.sortedElements.containsAll(c);
    }

    @Deprecated
    @Override
    public boolean addAll(Collection<? extends T> c) {
        throw new IllegalCallerException("Not Supported call...");
    }

    @Deprecated
    @Override
    public boolean retainAll(Collection<?> c) {
        throw new IllegalCallerException("Not Supported call...");
    }

    @Deprecated
    @Override
    public boolean removeAll(Collection<?> c) {
        throw new IllegalCallerException("Not Supported call...");
    }

    @Deprecated
    @Override
    public void clear() {
        throw new IllegalCallerException("Not Supported call...");
    }

    @Deprecated
    @Override
    public NavigableSet<T> descendingSet() {
        throw new IllegalCallerException("Not Supported call...");
    }

    @Deprecated
    @Override
    public Iterator<T> descendingIterator() {
        throw new IllegalCallerException("Not Supported call...");
    }

    @Deprecated
    @Override
    public NavigableSet<T> subSet(T fromElement, boolean fromInclusive, T toElement, boolean toInclusive) {
        throw new IllegalCallerException("Not Supported call...");
    }

    @Deprecated
    @Override
    public NavigableSet<T> headSet(T toElement, boolean inclusive) {
        throw new IllegalCallerException("Not Supported call...");
    }

    @Deprecated
    @Override
    public NavigableSet<T> tailSet(T fromElement, boolean inclusive) {
        throw new IllegalCallerException("Not Supported call...");
    }

    @Deprecated
    @Override
    public Comparator<? super T> comparator() {
        throw new IllegalCallerException("Not Supported call...");
    }

    @Deprecated
    @Override
    public SortedSet<T> subSet(T fromElement, T toElement) {
        throw new IllegalCallerException("Not Supported call...");
    }

    @Deprecated
    @Override
    public SortedSet<T> headSet(T toElement) {
        throw new IllegalCallerException("Not Supported call...");
    }

    @Deprecated
    @Override
    public SortedSet<T> tailSet(T fromElement) {
        throw new IllegalCallerException("Not Supported call...");
    }

    @Override
    public T first() {
        return this.sortedElements.first();
    }

    @Override
    public T last() {
        return this.sortedElements.last();
    }
}
