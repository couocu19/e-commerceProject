<html>
<%@ page contentType="text/html;charset=gb2312"%>
<body>
<h2>Hello World!</h2>

springMvc�ϴ��ļ�<br>
<form name="form1" action="/manage/product/upload.do" method="post" enctype="multipart/form-data">
    <input type="file" name="upload_file"/>
    <input type="submit" value="springMvc�ϴ��ļ�"/>
</form>

���ı��ϴ��ļ�<br>
<form name="form1" action="/manage/product/richtext_img_upload.do" method="post" enctype="multipart/form-data">
    <input type="file" name="upload_file"/>
    <input type="submit" value="���ı��ϴ��ļ�"/>
</form>

</body>
</html>
