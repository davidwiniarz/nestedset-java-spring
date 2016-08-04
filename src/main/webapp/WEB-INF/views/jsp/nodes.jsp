<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<html>
<head>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="/resources/assets/js/jquery.js"></script>
    <script src="/resources/assets/js/bootstrap.js"></script>
    <title>Nodes</title>
</head>
<body>
<c:url var="nodesAction" value="/nodes"/>
<div id="nodes_page">
    <jsp:include page="${nodesAction}"/>
</div>
<div class="container" style="position: fixed;    top: 10px;    width: 76px;    left: 10px;">
    <div class="row">
        <button class="btn btn-danger" id="refresh">Refresh</button>
    </div>
</div>
</body>
<script>
    $('#refresh').click(function () {
        $('#nodes_page').load("${nodesAction}");
    })
</script>
</html>