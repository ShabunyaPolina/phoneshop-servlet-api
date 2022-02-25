<%@ tag trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="name" required="true" %>
<%@ attribute name="label" required="true" %>
<%@ attribute name="order" required="true" type="com.es.phoneshop.model.order.Order"%>
<%@ attribute name="errors" required="true" type="java.util.Map" %>
<%@ attribute name="format" required="true" %>
<%@ attribute name="message" required="true" %>

<tr>
    <td class="list">
        ${label} <span style="color:red">*</span>:
    </td>
    <td class="list">
        <c:set var="error" value="${errors[name]}"/>
        <input name="${name}" pattern=${format} oninvalid="setCustomValidity('${message}')"
        onchange="try{setCustomValidity('')}catch(e){}"
        value="${not empty error ? param[name] : order[name]}"
        class="field"/>
        <c:if test="${not empty error}">
            <div class="error">
                ${error}
            </div>
        </c:if>
    </td>
</tr>
