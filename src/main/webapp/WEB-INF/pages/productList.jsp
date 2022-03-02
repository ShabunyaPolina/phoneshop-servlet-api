<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<jsp:useBean id="recentlyViewed" type="java.util.ArrayList" scope="request"/>
<tags:master pageTitle="Product List">
    <form style="margin:20px 0 20px 0 ;">
        <input name="query" value="${param.query}">
        <button>Search</button>
    </form>
     <c:if test="${not empty param.message}">
         <p class="success">
             ${param.message}
         </p>
     </c:if>
     <c:if test="${not empty param.error}">
         <p class="error">
            There was an error adding to cart
         </p>
     </c:if>
     <form>
     <button formaction="${pageContext.servletContext.contextPath}/search">
        Advanced search
    </button>
    </form>
    <p></p>
    <table>
        <thead>
            <tr>
                <td>Image</td>
                <td>
                Description
                    <span class="lowercase">
                        <tags:sortLink sort="DESCRIPTION" order="ASC"/>
                        <tags:sortLink sort="DESCRIPTION" order="DESC"/>
                    </span>
                </td>
                <td>
                    Quantity
                </td>
                <td>
                Price
                    <span class="lowercase">
                        <tags:sortLink sort="PRICE" order="ASC"/>
                        <tags:sortLink sort="PRICE" order="DESC"/>
                    </span>
                </td>
                <td>
                </td>
            </tr>
        </thead>
         <form method="post">
        <c:forEach var="product" items="${products}" varStatus="status">
            <tr>
                <td>
                    <img class="product-tile" src="${product.imageUrl}">
                </td>
                <td>
                    <a href="${pageContext.servletContext.contextPath}/products/${product.id}">
                        ${product.description}
                    </a>
                </td>

                <td>
                <input name="quantity" value="0" class="field" style="width:50px"/>
                <input type="hidden" name="productId" value="${product.id}" />
                <c:set var="id" value="${product.id}"/>
                <c:if test="${param.errorId eq id}">
                    <div class="error">
                        ${param.error}
                    </div>
                </c:if>
                </td>
                <td class="field">
                    <a href="${pageContext.servletContext.contextPath}/products/price-history/${product.id}">
                        <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
                    </a>
                </td>
                <td>
                    <button formaction="${pageContext.servletContext.contextPath}/products?id=${product.id}">
                        Add to cart
                    </button>
                </td>

            </tr>
        </c:forEach>
        </form>
    </table>
    <tags:recentlyViewed recentlyViewed="${recentlyViewed}"/>
</tags:master>
