/*
 * This file is part of BDF
 * BDF,Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf2.rapido.component;

import java.util.Collection;

import com.bstek.bdf2.rapido.component.property.PropertySupport;
import com.bstek.bdf2.rapido.domain.ComponentInfo;
import com.bstek.bdf2.rapido.domain.ComponentProperty;
import com.bstek.bdf2.rapido.domain.Entity;

/**
 * 用于定义组件显示信息
 * @author jacky.gao@bstek.com
 * @since 2012-8-25
 */
public interface ISupport {
	/**
	 * 组件的显示名称
	 * @return 具体要显示的名称
	 */
	String getDisplayName();
	/**
	 * Dorado7中该组件类的完全限定名
	 * @return 类全名
	 */
	String getFullClassName();
	/**
	 * 组件显示图标
	 * @return 图标地址
	 */
	String getIcon();
	/**
	 * 对组件支持的某些属性进行特殊处理
	 * @return PropertySupport对象的集合
	 */
	Collection<PropertySupport> getPropertySupports();
	/**
	 * 当前组件下可以使用哪些子组件
	 * @return ISupport对象集合
	 */
	Collection<ISupport> getChildren();
	/**
	 * 根据选择的实体自动创建子组件
	 * @param entity 选择的实体对象
	 * @return 子组件集合
	 */
	Collection<ComponentInfo> createChildrenByEntity(Entity entity);
	/**
	 * 根据组件ID创建组件常用属性
	 * @param componentId 组件ID
	 * @return 组件属性集合
	 */
	Collection<ComponentProperty> createComponentPropertysByComponentId(String componentId);
	/**
	 * @return 是否支持实体
	 */
	boolean isSupportEntity();
	/**
	 * @return 是否支持布局
	 */
	boolean isSupportLayout();
	/**
	 * @return 是否支持动作
	 */
	boolean isSupportAction();
	/**
	 * @return 是否为容器类型组件
	 */
	boolean isContainer();
	/**
	 * @return 是否可以独立存在
	 */
	boolean isAlone();
}
