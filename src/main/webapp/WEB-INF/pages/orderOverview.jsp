<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<tags:master pageTitle="Order overview">
<jsp:useBean id="order" type="com.es.phoneshop.model.order.Order" scope="request"/>
<h1> Order overview </h1>
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
            <tags:orderOverviewRow name="firstName" label="First name" order="${order}"/>
            <tags:orderOverviewRow name="lastName" label="Last name" order="${order}"/>
            <tags:orderOverviewRow name="phone" label="Phone" order="${order}"/>
            <tags:orderOverviewRow name="deliveryDate" label="Delivery date" order="${order}"/>
            <tags:orderOverviewRow name="deliveryAddress" label="Delivery address" order="${order}"/>
            <tags:orderOverviewRow name="paymentMethod" label="Payment method" order="${paymentMethod}"/>
        </table>
</tags:master>
