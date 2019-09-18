
# GitHub支持 Markdown 目录生成

**注意: 必须要以大写的 [TOC] 作为开头**
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

         
