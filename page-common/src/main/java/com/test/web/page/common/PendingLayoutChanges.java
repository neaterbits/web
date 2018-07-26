package com.test.web.page.common;


/**
 * Maintains a list of changes that have an impact on layout.
 * Eg. setting style.width changes layout while setting background-color does not.
 * 
 * Re-layout occurs at event loop idle or if a script or other tries to retrieve stale layout information 
 * 
 * Re-rendering to screen only occurs at event loop idle
 * 
 */

final class PendingLayoutChanges {

}

