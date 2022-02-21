<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<tags:master pageTitle="Checkout">
<jsp:useBean id="order" type="com.es.phoneshop.model.order.Order" scope="request"/>
<h1> Checkout </h1>
<c:if test="${not empty param.message}">
          <p class="success">
              ${param.message}
          </p>
      </c:if>
      <c:if test="${not empty errors}">
          <p class="error">
             There was an error while placing order
          </p>
      </c:if>
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
                </tr>
            </thead>
            <c:forEach var="item" items="${order.items}">
                <tr>
                    <td>
                        <img class="product-tile" src="${item.product.imageUrl}">
                    </td>
                    <td>
                        <a href="${pageContext.servletContext.contextPath}/products/${item.product.id}">
                            ${item.product.description}
                        </a>
                    </td>
                    <td class="field">
                        ${item.quantity}
                    </td>
                    <td class="field">
                        <a href="${pageContext.servletContext.contextPath}/products/price-history/${item.product.id}">
                            <fmt:formatNumber value="${item.product.price}" type="currency"
                            currencySymbol="${item.product.currency.symbol}"/>
                        </a>
                    </td>
                </tr>
            </c:forEach>
        </table>
        <p></p>
        <h3> Details: </h3>
        <table>
        <tr>
            <td>
                Total quantity:
            </td>
            <td class="field">
                ${cart.totalQuantity}
            </td>
        </tr>
        <tr>
            <td>
                Subtotal:
            </td>
            <td>
                <fmt:formatNumber value="${order.subtotal}" type="currency"
                currencySymbol="${order.currency.symbol}"/>
            </td>
        </tr>
        <tr>
            <td>
                Delivery coast:
            </td>
            <td class="field">
                <fmt:formatNumber value="${order.deliveryCoast}" type="currency"
                currencySymbol="${order.currency.symbol}"/>
            </td>
        </tr>
        <tr>
            <td>
                Total coast:
            </td>
            <td class="field">
                <fmt:formatNumber value="${order.totalCoast}" type="currency"
                currencySymbol="${order.currency.symbol}"/>
            </td>
        </tr>
    </table>
    <p></p>
    <h3> Order information: </h3>
    <form method="post" action="${pageContext.servletContext.contextPath}/checkout">
        <table>
            <tags:orderFormRow name="firstName" label="First name" order="${order}" errors="${errors}"/>
            <tags:orderFormRow name="lastName" label="Last name" order="${order}" errors="${errors}"/>
            <tags:orderFormRow name="phone" label="Phone" order="${order}" errors="${errors}"/>
            <tags:orderFormRow name="deliveryDate" label="Delivery date" order="${order}" errors="${errors}"/>
            <tags:orderFormRow name="deliveryAddress" label="Delivery address" order="${order}" errors="${errors}"/>
            <tr>
                <td class="list">
                    Payment method<span style="color:red">*</span>:
                </td>
                <td class="list">
                    <select name="paymentMethod">
                        <option></option>
                        <c:forEach var="paymentMethod" items="${paymentMethods}">
                        <option value="${paymentMethod}" ${param.paymentMethod eq paymentMethod ? 'selected' : '' }>
                        ${paymentMethod}</option>
                        </c:forEach>
                    </select>
                    <c:set var="error" value="${errors['paymentMethod']}"/>
                    <c:if test="${not empty error}">
                        <div class="error">
                            ${error}
                        </div>
                    </c:if>
                </td>
            </tr>
        </table>
        <p>
            <button> Place order </button>
        </p>
    </form>
</tags:master>
