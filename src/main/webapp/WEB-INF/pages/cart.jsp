<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="cart" type="com.es.phoneshop.model.cart.Cart" scope="request"/>
<tags:master pageTitle="Cart">
    <h1> Cart </h1>
    <p>Cart: ${cart} Total quantity: ${cart.totalQuantity} </p>
    <c:if test="${not empty param.message}">
          <p class="success">
              ${param.message}
          </p>
      </c:if>
      <c:if test="${not empty errors}">
          <p class="error">
             There was an error adding to cart
          </p>
      </c:if>
    <form method="post" action="${pageContext.servletContext.contextPath}/cart">
        <table>
            <thead>
                <tr>
                    <td>Image</td>
                    <td>
                        Description
                    </td>
                    <td>
                        Quantity
                    </td>
                    <td>
                        Price
                    </td>
                    <td></td>
                </tr>
            </thead>
            <c:forEach var="item" items="${cart.items}" varStatus="status">
                <tr>
                    <td>
                        <img class="product-tile" src="${item.product.imageUrl}">
                    </td>
                    <td>
                        <a href="${pageContext.servletContext.contextPath}/products/${item.product.id}">
                            ${item.product.description}
                        </a>
                    </td>
                    <td>
                        <fmt:formatNumber value="${item.quantity}" var="quantity"/>
                        <c:set var="error" value="${errors[item.product.id]}"/>
                        <input name="quantity" value="${not empty error ? paramValues['quantity'][status.index] : item.quantity}"
                         class="field" style="width:50px"/>
                            <c:if test="${not empty error}">
                                <div class="error">
                                    ${error}
                                </div>
                            </c:if>
                        <input type="hidden" name="productId" value="${item.product.id}"/>
                    </td>
                    <td class="field">
                        <a href="${pageContext.servletContext.contextPath}/products/price-history/${item.product.id}">
                            <fmt:formatNumber value="${item.product.price}" type="currency"
                            currencySymbol="${item.product.currency.symbol}"/>
                        </a>
                    </td>
                    <td>
                        <button formaction="${pageContext.servletContext.contextPath}/cart/deleteCartItem/${item.product.id}">
                            Delete
                        </button>
                    </td>
                </tr>
            </c:forEach>
            <tr>
                <td></td>
                <td></td>
                <td>Total coast</td>
                <td>
                    <fmt:formatNumber value="${cart.totalCoast}" type="currency"
                    currencySymbol="${cart.items[0].product.currency.symbol}"/>
                </td>
            </tr>
        </table>
        <c:if test="${not empty cart.items}">
            <p>
                <button>Update</button>
            </p>
        </c:if>
    </form>
</tags:master>