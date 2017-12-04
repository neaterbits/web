package com.test.web.render.common;

public interface IRenderer extends IRenderOperations {

	// We're done rendering, sync to display
	void sync();
	
	/*
	 * Render later, only works for async queue rendering, when we want add
	 * eg rendering of element background but we do not know complete size of element
	 * until after reaching end tag for element
	 */
	
	
	//int delayedRender();
	
	/*
	 * Render operations for later rendering, only works for async rendering queue
	 */
	
	//IRenderer renderDelayed(int id);
	
}
