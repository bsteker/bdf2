package com.bstek.bdf2.core.view.frame.main;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.bstek.bdf2.core.view.frame.IFrameShortcutActionRegister;

/**
 * @author Jacky.gao
 * @since 2013-2-6
 */
public abstract class AbstractFrameListener implements ApplicationContextAware{
	protected IFrameShortcutActionRegister[] registers;
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		Collection<IFrameShortcutActionRegister> result=applicationContext.getBeansOfType(IFrameShortcutActionRegister.class).values();
		registers=new IFrameShortcutActionRegister[result.size()];
		result.toArray(registers);
		Arrays.sort(registers,new ListenerComparator());
	}
}
class ListenerComparator implements Comparator<IFrameShortcutActionRegister>{
	public int compare(IFrameShortcutActionRegister first,
			IFrameShortcutActionRegister second) {
		if(first.order()>second.order()){
			return 1;
		}else if(first.order()<second.order()){
			return -1;
		}else{
			return 0;			
		}
	}
}
