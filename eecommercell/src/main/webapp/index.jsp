<html>
<%@ page contentType="text/html;charset=gb2312"%>
<body>
<h2>Hello World!</h2>

springMvc上传文件<br>
<form name="form1" action="/manage/product/upload.do" method="post" enctype="multipart/form-data">
    <input type="file" name="upload_file"/>
    <input type="submit" value="springMvc上传文件"/>
</form>

富文本上传文件<br>
<form name="form1" action="/manage/product/richtext_img_upload.do" method="post" enctype="multipart/form-data">
    <input type="file" name="upload_file"/>
    <input type="submit" value="富文本上传文件"/>
</form>

</body>
</html>
