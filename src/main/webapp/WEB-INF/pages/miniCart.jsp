<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:useBean id="cart" type="com.es.phoneshop.model.cart.Cart" scope="request"/>
<span class="cart">Cart: ${cart.totalQuantity} items
<fmt:formatNumber value="${cart.totalCoast}" type="currency"
                    currencySymbol="${cart.items[0].product.currency.symbol}"/>
                    </span>