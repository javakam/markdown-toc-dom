
# GitHub支持 Markdown 目录生成

参考项目: <br>
<https://github.com/YuraAAA/Markdown-Toc-Generator/> <br>
<https://github.com/zhang0peter/markdown2html>

## 效果图
![Image text](image/效果图.png)

## 使用方式
1.下载 [github-markdown-toc.jar](https://github-production-release-asset-2e65be.s3.amazonaws.com/209262321/81b10380-dadd-11e9-9256-2dc4e75bca9c?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAIWNJYAX4CSVEH53A%2F20190919%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20190919T050808Z&X-Amz-Expires=300&X-Amz-Signature=9e839da17fcee53d4fb1f2250f0ace131be742166c14f8c531d7070db230a528&X-Amz-SignedHeaders=host&actor_id=15343661&response-content-disposition=attachment%3B%20filename%3Dgithub-markdown-toc.jar&response-content-type=application%2Foctet-stream) ;
2.安装 Java 运行环境;
3.双击运行

## 可以用 **[TOC]** 或 **[toc]** 开头才会生成目录 , 不写也行...
```
[TOC]
# Heading **some bold** 
## Heading 1.1 _some italic
### Heading 1.1.1
### Heading 1.1.2  **_some bold italic_**
#### Heading 1.1.2.1  **_some bold italic_666**
##### Heading 1.1.2.1.1  **_some bold 2222**
``` 
> 上面代码生成效果:

<div class="toc">
<h1>目录</h1>
<ul>
<li><a href="#heading-some-bold">Heading <strong>some bold</strong></a>
<ul>
<li><a href="#heading-11--some-italic">Heading 1.1 _some italic</a>
<ul>
<li><a href="#heading-111">Heading 1.1.1</a></li>
<li><a href="#heading-112--some-bold-italic">Heading 1.1.2  <strong><em>some bold italic</em></strong></a>
<ul>
<li><a href="#heading-1121---some-bold-italic-666">Heading 1.1.2.1  <strong>_some bold italic_666</strong></a>
<ul>
<li><a href="#heading-11211---some-bold-2222">Heading 1.1.2.1.1  <strong>_some bold 2222</strong></a></li>
</ul>
</li>
</ul>
</li>
</ul>
</li>
</ul>
</li>
</ul>
</div>

         
