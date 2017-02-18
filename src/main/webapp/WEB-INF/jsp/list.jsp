<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<title>秒杀商品列表</title>
<%@include file="common/head.jsp"%>
<%@include file="common/tag.jsp"%>
</head>
<body>
	<div class="container">
		<div class="panel panel-default">
			<div class="panel-heading text-center">
				<h2>秒杀商品列表</h2>
				<div class="panel-body">
					<table class="table table-hover">
						<thead>
							<tr>
								<th>名称</th>
								<th>库存</th>
								<th>开始时间</th>
								<th>结束时间</th>
								<th>创建时间</th>
								<th>查看详情</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="seckill" items="${list}">
								<tr>
									<td>${seckill.name}</td>
									<td>${seckill.number}</td>

									<td><fmt:formatDate value="${seckill.startTime}"
											pattern="yyyy-MM-dd HH:mm:ss" /></td>
									<td><fmt:formatDate value="${seckill.endTime}"
											pattern="yyyy-MM-dd HH:mm:ss" /></td>
									<td><fmt:formatDate value="${seckill.createTime}"
											pattern="yyyy-MM-dd HH:mm:ss" /></td>
									<td><a class="btn btn-info"
										href="/seckill/${seckill.seckillId}/detail" target="_blank">Link</a></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</body>
</html>