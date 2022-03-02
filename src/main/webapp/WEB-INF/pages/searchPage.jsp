<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<tags:master pageTitle="Advanced search">
<h1>Advanced search page</h1>
<form>
    <c:if test="${not empty message}">
        <p class="success">
            ${message}
        </p>
    </c:if>
    <c:if test="${not empty errors}">
        <p class="error">
            There was an error
        </p>
    </c:if>
<table>
    <tr>
        <td class="list">
        Product code
        </td>
        <td class="list">
        <input name="productCode" class="field"/>
        </td>
    </tr>
    <tr>
        <td class="list">
            Min price
        </td>
        <td class="list">
            <c:set var="error" value="${errors['minPrice']}"/>
            <input name="minPrice" value="${not empty error ? param['minPrice'] : ''}" class="field"/>
            <c:if test="${not empty error}">
                <div class="error">
                    ${error}
                </div>
            </c:if>
        </td>
    </tr>
    <tr>
        <td class="list">
            Max price
        </td>
        <td class="list">
            <c:set var="error" value="${errors['maxPrice']}"/>
            <input name="maxPrice"  value="${not empty error ? param['maxPrice'] : ''}" class="field"/>
            <c:if test="${not empty error}">
                <div class="error">
                    ${error}
                </div>
            </c:if>
        </td>
    </tr>
    <tr>
        <td class="list">
            Min stock
        </td>
        <td class="list">
            <c:set var="error" value="${errors['minStock']}"/>
            <input name="minStock" value="${not empty error ? param['minStock'] : ''}" class="field"/>
            <c:if test="${not empty error}">
                <div class="error">
                    ${error}
                </div>
            </c:if>
        </td>
    </tr>
</table>
<button>
    Search
</button>
<p></p>
<table>
        <thead>
            <tr>
                <td>Image</td>
                <td>
                Description
                </td>
                <td>
                Price
                </td>
            </tr>
        </thead>
        <c:forEach var="product" items="${products}">
                    <tr>
                        <td>
                            <img class="product-tile" src="${product.imageUrl}">
                        </td>
                        <td>
                            <a href="${pageContext.servletContext.contextPath}/products/${product.id}">
                                ${product.description}
                            </a>
                        </td>
                        <td class="field">
                            <a href="${pageContext.servletContext.contextPath}/products/price-history/${product.id}">
                                <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
                            </a>
                        </td>
                    </tr>
                </c:forEach>
        </table>
</form>
</tags:master>