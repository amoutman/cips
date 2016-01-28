<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<div class="page">
					<c:choose>
						<c:when test="${pager.currentPage !=1 && pager.totalPage !=0 }">
							<a href="javascript:void(0)" onclick="pageClick(${pager.currentPage-1})">&lt;</a>  
						</c:when> 
						
						<c:otherwise >
							<a href="javascript:void(0)">&lt;</a>  
						</c:otherwise>
					</c:choose>
					<c:forEach items="${pager.pageNoDisp}" var="pNo">  
						<c:choose>  
							<c:when test="${pNo == 0}">  
								... 
							</c:when>  
							<c:when test="${pNo != pager.currentPage}">  
								<a href="javascript:void(0)" onclick="pageClick(${pNo})">${pNo}</a>
							</c:when>  
							<c:otherwise>  
								<a href="javascript:void(0)" class="active">${pNo}</a>
							</c:otherwise>  
						</c:choose>  
					</c:forEach>
					<c:choose>
					
						<c:when test="${pager.currentPage < pager.totalPage && pager.totalPage !=0 }">  
							<a href="javascript:void(0)" onclick="pageClick(${pager.currentPage+1})">&gt;</a> 
						</c:when> 
						
						<c:otherwise >
							<a href="javascript:void(0)">&gt;</a> 
						</c:otherwise> 
					</c:choose> 
    			</div>