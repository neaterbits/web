package com.test.web.page.common;

/**
 * Maintains page update state, which elements are updated.
 * 
 * This represents the whole DOM tree but encoded as long[] buffers
 * where one long entry holds the state for a whole element.
 * 
 * This is for cache locality and very fast iteration
 * 
 * Each long holds
 * 
 * 24 bits next ref | 24 bits sub ref | 16 bits flags
 * 
 * We allocate this as a contigous array that we compact on idle together with the DOM model
 * that tracks the indices, ie. if elements have moved, we swap the indices by
 * allocating a new buffer and moving.
 * Ie we reallocate the new indices and then move them here as well to be in sync with the model.
 * 
 */



final class PageUpdateState {

    
    private long [] elements;
    
    private static long encode(long next, long sub, long flags) {
        return next << (24 + 16) | sub << 16 | flags;
    }
    
    private static long next(long encoded) {
        return encoded >> (24 + 16);
    }

    private static long sub(long encoded) {
        return (encoded >> 16) & ((1 << 24) - 1);
    }

    private static long flags(long encoded) {
        return encoded & ((1 << 16) - 1);
    }
    
    /**
     * Iterate model, looking for whether there are elements that require updates
     * 
     * @param startIdx start element to search at, usually the root element
     * @param elementIndicesToFind stack of indices, representing a path to the DOM (typically an element we want to see requires updates) 
     * 
     * @param flagToFind flag to match for updates
     * 
     * @return true if found an element affecting the current one
     */
    
    public void iterateUnto(int startIdx, int elementIndicesToFind[], int flagToFind) {
        
        final long elem = elements[startIdx];
        
        final int level = 0;

        if (elementIndicesToFind[0] != startIdx) {
            throw new IllegalArgumentException("Expected to be at root element");
        }

        // Make quick-test for root element instead of calling the recursive function to check for each element
        // rather check on flags in the iteration loop to skip a whole lot of calls
        
        // TODO continue here
        
        
    }
    

    private void iterateUntoRecursively(int level, int startIdx, int elementIndicesToFind[], int flagToFind) {
        
    }
    
    
}
