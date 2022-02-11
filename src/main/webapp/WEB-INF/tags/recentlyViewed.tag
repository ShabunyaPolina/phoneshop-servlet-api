<%@ tag trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ attribute name="recentlyViewed" required="true" type="java.util.ArrayList" %>

<jsp:useBean id="recentlyViewed" type="java.util.ArrayList" scope="request"/>

<c:if test="${not empty recentlyViewed}">
      <h2>
          Recently viewed
      </h2>
      <c:forEach var="product" items="${recentlyViewed}">
          <figure>
              <img class="product-tile" src="${product.imageUrl}">
              <a href="${pageContext.servletContext.contextPath}/products/${product.id}">
                  <div>${product.description}</div>
              </a>
              <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
          </figure>
      </c:forEach>
  </c:if>
  <div class="clear"></div>
