<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/WEB-INF/views/layout/app.jsp">
	<c:param name="content">
	
		<c:choose>
			<c:when test="${report != null}"><%--もしreportが存在すれば以下を表示する --%>
				<h2>日報　編集ページ</h2>
				<form method="POST" action="<c:url value='/reports/update' />">
					<c:import url="_form.jsp" /><%--formパーツを表示 --%>
				</form>
			</c:when>
			
			<c:otherwise><%--reportがnullならば --%>
				<h2>お探しのデータは見つかりませんでした。</h2>
			</c:otherwise>
		</c:choose>

		<p><a href="<c:url value='/reports/index' />">一覧に戻る</a></p>
	</c:param>
</c:import>