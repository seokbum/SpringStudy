<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>사용자등록</title>
    <!-- Tailwind CSS CDN -->
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
    <!-- Google Fonts (Poppins) -->
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600&display=swap" rel="stylesheet">
    <style>
        body {
            font-family: 'Poppins', sans-serif;
            background: linear-gradient(135deg, #e0e7ff, #f3f4f6);
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
        }
        .error {
            color: #ef4444;
            font-size: 0.875rem;
            margin-top: 0.25rem;
        }
        input:focus {
            outline: none;
            ring: 2px solid #4f46e5;
        }
    </style>
</head>
<body>
    <div class="w-full max-w-md mx-auto bg-white p-8 rounded-lg shadow-lg">
        <h2 class="text-2xl font-semibold text-center text-indigo-600 mb-6">사용자 등록</h2>
        
        <form:form modelAttribute="user" method="post" action="join" cssClass="space-y-4">
            <!-- Global Errors -->
            <spring:hasBindErrors name="user">
                <div class="text-red-500 text-sm">
                    <c:forEach items="${errors.globalErrors}" var="error">
                        <spring:message code="${error.code}"/><br>
                    </c:forEach>
                </div>
            </spring:hasBindErrors>

            <table class="w-full">
                <tr>
                    <td class="py-2">
                        <label for="userid" class="block text-sm font-medium text-gray-700">아이디</label>
                        <form:input path="userid" cssClass="mt-1 w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:ring-indigo-500 focus:border-indigo-500"/>
                        <div class="error"><form:errors path="userid"/></div>
                    </td>
                </tr>
                <tr>
                    <td class="py-2">
                        <label for="password" class="block text-sm font-medium text-gray-700">비밀번호</label>
                        <form:password path="password" cssClass="mt-1 w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:ring-indigo-500 focus:border-indigo-500"/>
                        <div class="error"><form:errors path="password"/></div>
                    </td>
                </tr>
                <tr>
                    <td class="py-2">
                        <label for="username" class="block text-sm font-medium text-gray-700">이름</label>
                        <form:input path="username" cssClass="mt-1 w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:ring-indigo-500 focus:border-indigo-500"/>
                        <div class="error"><form:errors path="username"/></div>
                    </td>
                </tr>
                <tr>
                    <td class="py-2">
                        <label for="phoneno" class="block text-sm font-medium text-gray-700">전화번호</label>
                        <form:input path="phoneno" cssClass="mt-1 w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:ring-indigo-500 focus:border-indigo-500"/>
                    </td>
                </tr>
                <tr>
                    <td class="py-2">
                        <label for="postcode" class="block text-sm font-medium text-gray-700">우편번호</label>
                        <form:input path="postcode" cssClass="mt-1 w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:ring-indigo-500 focus:border-indigo-500"/>
                    </td>
                </tr>
                <tr>
                    <td class="py-2">
                        <label for="address" class="block text-sm font-medium text-gray-700">주소</label>
                        <form:input path="address" cssClass="mt-1 w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:ring-indigo-500 focus:border-indigo-500"/>
                    </td>
                </tr>
                <tr>
                    <td class="py-2">
                        <label for="email" class="block text-sm font-medium text-gray-700">이메일</label>
                        <form:input path="email" cssClass="mt-1 w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:ring-indigo-500 focus:border-indigo-500"/>
                        <div class="error"><form:errors path="email"/></div>
                    </td>
                </tr>
                <tr>
                    <td class="py-2">
                        <label for="birthday" class="block text-sm font-medium text-gray-700">생년월일</label>
                        <form:input path="birthday" cssClass="mt-1 w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:ring-indigo-500 focus:border-indigo-500"/>
                        <div class="error"><form:errors path="birthday"/></div>
                    </td>
                </tr>
                <tr>
                    <td class="py-2 text-center">
                        <input type="submit" value="회원가입" class="w-full px-4 py-2 bg-indigo-600 text-white font-medium rounded-md hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500"/>
                        <input type="reset" value="초기화" class="w-full mt-2 px-4 py-2 bg-gray-300 text-gray-700 font-medium rounded-md hover:bg-gray-400 focus:outline-none focus:ring-2 focus:ring-gray-500"/>
                    </td>
                </tr>
            </table>
        </form:form>
    </div>
</body>
</html>