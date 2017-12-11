package com.test.web.document.oo;

// Container in the meaning of this element can have sub-elements
abstract class OOContainerElement extends OOTagElement {

	// Linked-list of elements
	OODocumentElement head;
	OODocumentElement tail;
	
	// Cache number of elements
	private int numElements;
	
    OOContainerElement() {
    	this.head = null;
    	this.tail = null;
    	this.numElements = 0;
    }
	
	final void add(OODocumentElement element) {
		if (element == null) {
			throw new IllegalArgumentException("element == null");
		}
		
		if (element.last != null || element.next != null) {
			throw new IllegalStateException("element already in list");
		}
		
		if (head == null) {
			// initial element
			if (numElements != 0) {
				throw new IllegalStateException("numElements != 0");
			}
			
			head = tail = element;
		}
		else {
			tail.next = element;
			element.last = tail;
			tail = element;
		}
		
		element.parent = this;
		
		++ numElements;
	}
	
	final int getNumElements() {
		return numElements;
	}
}
