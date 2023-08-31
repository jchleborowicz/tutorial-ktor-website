<#-- @ftlvariable name="article" type="com.example.models.Article" -->
<#import "_layout.ftl" as layout />
<@layout.header>
    <div>
        <h3><a href="/articles/${article.id}">${article.title}</a></h3>
        <p>${article.body}</p>
    </div>
    <p><a href="/articles/${article.id}/edit">Edit article</a></p>
</@layout.header>
