<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<tags:master pageTitle="Product not found">
  <h1>
    Product with id ${pageContext.exception.productId} not found
  </h1>
</tags:master>
