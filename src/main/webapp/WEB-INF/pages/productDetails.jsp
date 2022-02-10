<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="product" type="com.es.phoneshop.model.product.Product" scope="request"/>
<jsp:useBean id="recentlyViewed" type="java.util.ArrayList" scope="request"/>
<tags:master pageTitle="Product Details">
  <h1>
      ${product.description}
  </h1>
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
  <form method="post">
      <table>
          <tr>
              <td>Image</td>
              <td class="field">
                  <img src="${product.imageUrl}">
              </td>
          </tr>
          <tr>
              <td>Code</td>
              <td class="field">
                  ${product.code}
              </td>
          </tr>
          <tr>
              <td>Stock</td>
              <td class="field">
                  ${product.stock}
              </td>
          </tr>
          <tr>
              <td>Price</td>
              <td class="field">
                  <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
              </td>
          </tr>
      </table>
      <input name="quantity" style="margin:20px 0 0 0 ;"class="field"
      value="${not empty error ? param.quantity : 1}">
      <button>Add to cart</button>
      <c:if test="${not empty param.error}">
          <div class="error">
              ${param.error}
          </div>
      </c:if>
  </form>
  <p>Cart: ${cart}</p>
 <tags:recentlyViewed recentlyViewed="${recentlyViewed}"/>
</tags:master>
