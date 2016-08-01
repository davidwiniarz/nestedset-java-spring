<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ page session="false" %>

<div class="container">
    <c:choose>
        <c:when test="${!empty nodes}">


            <c:set var="depth" scope="page" value="-1"/>
            <c:forEach var="node" items="${nodes}">
                <c:choose>
                    <c:when test="${depth < node.depth}">
                        <ul>
                    </c:when>
                </c:choose>
                <c:choose>
                    <c:when test="${depth > node.depth}">
                        <c:forEach begin="1" end="${depth-node.depth}" var="val">
                            </ul>
                        </c:forEach>
                    </c:when>
                </c:choose>
                <li class="list-group-item" id="node${node.id}" data-depth="${node.depth}"
                    data-parent="${node.parent.id}">
                    <c:if test="${node.isLeaf()}"><span class="badge">
                            ${node.sumValue()}
                    </span> </c:if>
                    <a>
                            ${node.name}
                        <small> (${node.value})</small>
                        <c:choose>
                            <c:when test="${node.isLeaf()}">
                                <img src="/resources/assets/images/leaf.png"
                                     style="height:15px; vertical-align: text-bottom;"/>
                            </c:when>
                            <c:otherwise>
                                <c:choose>
                                    <c:when test="${node.isRoot()}">
                                        <img src="/resources/assets/images/root.gif"
                                             style="height:15px; vertical-align: text-bottom;"/>
                                    </c:when>
                                    <c:otherwise>
                                        <img src="/resources/assets/images/node.png"
                                             style="height:15px; vertical-align: text-bottom;"/>
                                    </c:otherwise>
                                </c:choose>
                            </c:otherwise>
                        </c:choose>

                    </a>

                    <p style="font-size:10px;">
                        <a data-toggle="modal" data-target="#add-modal${node.id}" id="id_add-modal${node.id}"
                           href="#">Add
                            child</a>
                        | <a data-toggle="modal" data-target="#edit-modal${node.id}" id="id_edit-modal${node.id}"
                             href="#">Edit</a>
                        <c:if test="${not node.isRoot()}">
                        | <a data-toggle="modal" data-target="#delete-modal${node.id}" id="id_delete-modal${node.id}"
                             href="#">Delete</a></p>
                    </c:if>

                </li>
                <c:set var="depth" scope="page" value="${node.depth}"/>
            </c:forEach>
            <c:forEach begin="1" end="${depth+1}" var="val">
                </ul>
            </c:forEach>

            <c:forEach var="node" items="${nodes}">
                <div class="modal fade" id="add-modal${node.id}" tabindex="-1" role="dialog"
                     aria-labelledby="myModalLabel">
                    <div class="modal-dialog" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                                        aria-hidden="true">&times;</span></button>
                                <h4 class="modal-title">Add child</h4>
                            </div>
                            <c:url var="addAction" value="/node/add"></c:url>
                            <form:form method="POST" action="${addAction}" commandName="node">
                                <div class="modal-body">
                                    <div class="form-group">
                                        <label for="method">Choose method:</label>
                                        <select class="form-control" id="method" name="method">
                                            <option value="first">Add child at First</option>
                                            <option value="last">Add child at Last</option>
                                        </select>
                                    </div>
                                    <div class="form-group">
                                        <form:label path="name" for="node_name">Name</form:label>
                                        <form:input path="name" class="form-control" id="new_node_name"
                                                    placeholder="Node's name"
                                                    required="required"/>
                                    </div>
                                    <div class="form-group">
                                        <form:label path="value" for="node_value">Value</form:label>
                                        <form:input path="value" class="form-control" id="new_node_value"
                                                    placeholder="Node's value"
                                                    required="required"/>
                                    </div>
                                    <input type="hidden" name="currentNodeId" value="${node.id}"/>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                                    <button type="submit" class="btn btn-primary">Save changes</button>
                                </div>
                            </form:form>
                        </div>
                    </div>
                </div>
                <div class="modal fade edit-from" id="edit-modal${node.id}" tabindex="-1" role="dialog"
                     aria-labelledby="myModalLabel">
                    <div class="modal-dialog" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                                        aria-hidden="true">&times;</span></button>
                                <h4 class="modal-title">Edit</h4>
                            </div>
                            <c:url var="editAction" value="/node/edit"></c:url>

                            <form:form method="POST" action="${editAction}" commandName="node" id="form-edit${node.id}">
                                <div class="modal-body">
                                    <p>Form is automatically saved.</p>

                                    <div class="modal-body">
                                        <div class="form-group">
                                            <form:label path="name" for="node_name">Name</form:label>
                                            <form:input path="name" class="form-control" id="node_name"
                                                        placeholder="Node's name"
                                                        required="required" value="${node.name}"/>
                                        </div>
                                        <div class="form-group">
                                            <form:label path="value" for="node_value">Value</form:label>
                                            <form:input path="value" class="form-control" id="node_value"
                                                        placeholder="Node's value"
                                                        required="required" value="${node.value}"/>
                                        </div>
                                        <form:hidden path="id" value="${node.id}"/>
                                    </div>
                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-default" data-dismiss="modal">Close
                                        </button>
                                    </div>
                                </div>
                            </form:form>
                        </div>
                    </div>
                </div>
                <div class="modal fade" id="delete-modal${node.id}" tabindex="-1" role="dialog"
                     aria-labelledby="myModalLabel">
                    <div class="modal-dialog" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                                        aria-hidden="true">&times;</span></button>
                                <h4 class="modal-title">Delete</h4>
                            </div>
                            <div class="modal-body">
                                Are you sure you want to delete ${node.name}? When node has children, they will be lost
                                too.
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-default" data-dismiss="modal">No</button>
                                <c:url var="deleteUrl" value="/node/delete/${node.id}"/>
                                <a href="${deleteUrl}" class="btn btn-primary">Yes</a>
                            </div>
                        </div>
                    </div>
                </div>

                <script>
                    $("#form-edit${node.id}").submit(function (event) {
                        event.preventDefault();
                        var action_url = "/node/edit";
                        var postData = $(this).serializeArray();
                        $.post(action_url, postData, function (data) {
                            $('#saved-info-modal').modal({
                                show: 'true'
                            });
                        })
                    });
                    $('#node_value,#node_name').on('focusout', function () {
                        $("#form-edit${node.id}").trigger('submit');
                    });
                </script>
            </c:forEach>
            <div class="modal fade" id="saved-info-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
                <div class="modal-dialog" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                                    aria-hidden="true">&times;</span></button>
                            <h4 class="modal-title">Success.</h4>
                        </div>
                        <div class="modal-body">
                            Data saved. Remember to hit the refresh button, to see changes.
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">OK</button>
                        </div>
                    </div>
                </div>
            </div>
        </c:when>
        <c:otherwise>
            Brak danych.
        </c:otherwise>
    </c:choose>
</div>
