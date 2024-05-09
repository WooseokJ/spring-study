<%@ page import="hello.servlet.domain.Member" %>
<%@ page import="hello.servlet.domain.MemberRepository" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // request, response 사용 가능
    MemberRepository memberRepository = MemberRepository.getInstance();

    String username = request.getParameter("username"); // 1.get 쿼리스트링, 2.post html form방식이든 꺼낼수있다.
    String ageString = request.getParameter("age"); // getParameter은 String 으로 꺼냄.

    // 문자 -> int 변경
    int age = Integer.parseInt(ageString);

    Member member = new Member(username, age);
    memberRepository.save(member);
%>


<html>
<head>
    <meta charset="UTF-8">
</head>
<body> 성공 <ul>
    <li>id=<%=member.getId()%></li>
    <li>username=<%=member.getUsername()%></li>
    <li>age=<%=member.getAge()%></li>
</ul>
<a href="/index.html">메인</a>
</body>
