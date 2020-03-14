<%
	session.invalidate();
	response.sendRedirect(request.getContextPath()
			+ "/pages/main.jsf");
%>