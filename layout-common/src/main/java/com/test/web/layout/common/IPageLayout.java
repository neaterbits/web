package com.test.web.layout.common;

import java.util.function.BiConsumer;

import com.test.web.render.common.IDelayedRenderer;

public interface IPageLayout {

	void forEachLayerSortedOnZIndex(BiConsumer<Integer, IDelayedRenderer> consumer);
}
