package com.cips.page;

import java.util.ArrayList;
import java.util.List;

public class Pager {
	private int showCount;

	// 总的页数
	private int totalPage;
	// 查询的数据总条数
	private int totalResult;
	// 当前页
	private int currentPage;

	private List<Integer> pageNoDisp;

	// 从第几条开始获取数据
	@SuppressWarnings("unused")
	private int currentResult;

	public Pager() {
		this(1);
	}

	public Pager(int currentPage) {
		// 默认每页显示10条记录
		this(currentPage, 10);
	}

	public Pager(int currentPage, int showCount) {
		this.currentPage = currentPage;
		if (showCount > 0) {
			this.showCount = showCount;
		}
		// 错误处理
		if (this.currentPage < 1) {
			this.currentPage = 1;
		}
	}

	public int getTotalPage() {
		// 分页算法，计算总页数
		return this.totalPage;
	}

	public int getCurrentResult() {
		// 计算从第几条获取数据
		return (currentPage - 1) * showCount;
	}

	public int getTotalResult() {
		return totalResult;
	}

	public void setTotalResult(int totalResult) {
		this.totalResult = totalResult;
		refreshPage();
	}

	public int getShowCount() {
		return showCount;
	}

	public void setShowCount(int showCount) {
		this.showCount = showCount;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public void setCurrentResult(int currentResult) {
		this.currentResult = currentResult;
	}

	public List<Integer> getPageNoDisp() {
		return pageNoDisp;
	}

	public void setPageNoDisp(List<Integer> pageNoDisp) {
		this.pageNoDisp = pageNoDisp;
	}

	/**
	 * 总件数变化时，更新总页数并计算显示样式
	 */
	private void refreshPage() {
		// 总页数计算
		totalPage = totalResult % showCount == 0 ? totalResult / showCount : (totalResult / showCount + 1);
		// 防止超出最末页（浏览途中数据被删除的情况）
		if (currentPage > totalPage && totalPage != 0) {
			currentPage = totalPage;
		}
		pageNoDisp = computeDisplayStyleAndPage();
	}

	/**
	 * 计算页号显示样式 这里实现以下的分页样式("[]"代表当前页号)，可根据项目需求调整 [1],2,3,4,5,6,7,8..12,13
	 * 1,2..5,6,[7],8,9..12,13 1,2..6,7,8,9,10,11,12,[13]
	 */
	private List<Integer> computeDisplayStyleAndPage() {
		List<Integer> pageDisplays = new ArrayList<Integer>();
		if (totalPage <= 11) {
			for (int i = 1; i <= totalPage; i++) {
				pageDisplays.add(i);
			}
		} else if (currentPage < 7) {
			for (int i = 1; i <= 8; i++) {
				pageDisplays.add(i);
			}
			pageDisplays.add(0);// 0 表示 省略部分(下同)
			pageDisplays.add(totalPage - 1);
			pageDisplays.add(totalPage);
		} else if (currentPage > totalPage - 6) {
			pageDisplays.add(1);
			pageDisplays.add(2);
			pageDisplays.add(0);
			for (int i = totalPage - 7; i <= totalPage; i++) {
				pageDisplays.add(i);
			}
		} else {
			pageDisplays.add(1);
			pageDisplays.add(2);
			pageDisplays.add(0);
			for (int i = currentPage - 2; i <= currentPage + 2; i++) {
				pageDisplays.add(i);
			}
			pageDisplays.add(0);
			pageDisplays.add(totalPage - 1);
			pageDisplays.add(totalPage);
		}
		return pageDisplays;
	}

}
