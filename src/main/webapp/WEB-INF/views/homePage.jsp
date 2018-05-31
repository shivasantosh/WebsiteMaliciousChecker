<%-- 
    Document   : homePage
    Created on : 3 May, 2018, 10:36:31 AM
    Author     : santosh
--%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>HomePage</title>
        <%@include file="/WEB-INF/views/springTags.jsp"%>
        <script type="text/javascript">
            function loginSubmit() {
                if (!$("#domainName").val() === "") {
                    alert("Please Enter website URL.");
                    $("#domainName").focus();
                    return false;
                }
                return true;
            }
        </script>
    </head>
    <body>
        <h1>Malicious Website checker</h1>
        <form:form commandName="website" method="POST" action="/check-url.html">
            <form:input type="text" path="domainName" placeholder="Enter website URL"/>
            <input type="submit" value="Analyze" id="signIn" onclick="return loginSubmit();"/>
        </form:form>

    </body>
</html>
