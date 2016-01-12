package com.cips.page;

public class PageContext {
	private static ThreadLocal<Pager> threadPage = new ThreadLocal<Pager>();  
	public static Pager getPager(){
		Pager p = threadPage.get();  
		return p;
	}
	
	public static void setPager(Pager pager){ 
		threadPage.set(pager);  
	}
}
